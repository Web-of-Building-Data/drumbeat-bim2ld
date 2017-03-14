package fi.aalto.cs.drumbeat.data.step.parsers;

/**
 * This class is for parsing IFC schema from an in stream.
 * 
 * The IFC syntax format is based on this doc:
 * http://iaiweb.lbl.gov/Resources/IFC_Releases/IFC_Release_2.0/BETA_Docs_for_Review/IFC_R2_SpecDevGuide_Beta_d2.PDF
 * (page A-27).
 * 
 * This parser includes only minimum syntax checking and ignores many insignificant keywords
 * such as SUPERTYPE OF, DERIVE, WHERE, INVERSE, etc.
 * 
 *  
 * @author Nam Vu Hoang
 * 
 * History:
 * 20120217 - Created
 */

import fi.aalto.cs.drumbeat.common.string.RegexUtils;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.data.bem.BemAttributeNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.BemTypeAlreadyExistsException;
import fi.aalto.cs.drumbeat.data.bem.BemTypeNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemFormatException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemParserException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.ExpressFormat;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 
 * Parser of IFC schema from in stream   
 * 
 * @author vuhoan1
 *
 */

class ExpressSchemaInternalParser {
	
//	/**
//	 * Cache for EXPRESS schema of STFF header section
//	 */
//	private static BemSchema stffExpressSchema;
	
	/**
	 * The in reader, reads line by line, wraps original in stream inside
	 */
	private StepLineReader lineReader;
	
	/**
	 * The output schema
	 */
	private ExpressSchemaBuilder builder;
	private ExpressSchema schema;
	private Map<String, BemCollectionTypeInfo> derivedCollectionTypes;
	
	/**
	 * Creates a new parser. For internal use.
	 * 
	 * @param in
	 */	
	ExpressSchemaInternalParser(ExpressSchemaBuilder builder, InputStream in) {
		this.builder = builder;
		this.schema = builder.createSchema();
		lineReader = new StepLineReader(in);
		derivedCollectionTypes = new HashMap<>();
	}

	/**
	 * Parses an IFC schema from an in stream. This is the main entry of the parser. 
	 * 
	 * @param in
	 * @return {@link BemSchema}
	 * @throws BemException
	 */
	ExpressSchema parse() throws BemException {		
		try {
			String statement = lineReader.getNextStatement();
			String tokens[] = RegexUtils.split2(statement, RegexUtils.WHITE_SPACE);
			
			if (tokens.length != 2 || !tokens[0].equals(StepVocabulary.ExpressFormat.SCHEMA)) {
				throw new BemFormatException(lineReader.getCurrentLineNumber(), "Invalid schema");			
			}
			
			//
			// get schema version
			//
			String version = tokens[1].trim();
			schema.setName(version);
			
			List<ExpressEntityTypeInfoTextWrapper> entityTypeInfoTextWrappers = new LinkedList<ExpressEntityTypeInfoTextWrapper>();
			List<ExpressNonEntityTypeInfoTextWrapper> nonEntityTypeInfoTextWrappers = new LinkedList<ExpressNonEntityTypeInfoTextWrapper>();
			
			
			//
			// read all type definition headers and put new types into the schema
			//
			for (;;) {
				statement = lineReader.getNextStatement();				
				if (statement == null) {
					throw new BemFormatException(lineReader.getCurrentLineNumber(), String.format("Expected '%s'", StepVocabulary.ExpressFormat.END_SCHEMA));
				}
					
				tokens = RegexUtils.split2(statement, RegexUtils.WHITE_SPACE);
				
				if (tokens[0].equals(StepVocabulary.ExpressFormat.TYPE)) {
					
					// parse an non-entity type info
					ExpressNonEntityTypeInfoTextWrapper nonEntityTypeInfoTextWrapper = parseNonEntityTypeHeader(tokens);
					nonEntityTypeInfoTextWrappers.add(nonEntityTypeInfoTextWrapper);
					schema.addTypeInfo(nonEntityTypeInfoTextWrapper.getTypeInfo());
					
				} else if (tokens[0].equals(StepVocabulary.ExpressFormat.ENTITY)) {
					
					// get the entity type body and put into the cache for LATER BINDING
					ExpressEntityTypeInfoTextWrapper entityTypeInfoTextWrapper =  parseEntityTypeInfoText(tokens);
					entityTypeInfoTextWrappers.add(entityTypeInfoTextWrapper);						
					schema.addTypeInfo(entityTypeInfoTextWrapper.getEntityTypeInfo());
					
				} else if (tokens[0].equals(StepVocabulary.ExpressFormat.END_SCHEMA)) {
					break;
				}
					
			}
			
			//
			// parse all non-entity types
			//
			for (ExpressNonEntityTypeInfoTextWrapper typeWrapper : nonEntityTypeInfoTextWrappers) {
				parseNonEntityTypeBody(typeWrapper.getTypeInfo(), typeWrapper.getTypeBodyStatements());
			}
			
			
//				//
//				// bind all types
//				//
//				for (BemNonEntityTypeInfo typeInfo : schema.getNonEntityTypeInfos()) {
//					if (typeInfo instanceof IBemLateBindingTypeInfo) {
//						((IBemLateBindingTypeInfo)typeInfo).bindTypeInfo(schema);
//					}				
//				}
			
			//
			// bind entity types with their supertypes
			// bind entity types' attributes with the attribute types
			//
			for (ExpressEntityTypeInfoTextWrapper entityTypeInfoTextWrapper : entityTypeInfoTextWrappers) {
				bindEntitySuperTypeAndAttributes(entityTypeInfoTextWrapper);
			}
			
			//
			// bind entity types' inverse links and unique keys
			// 
			for (ExpressEntityTypeInfoTextWrapper entityTypeInfoTextWrapper : entityTypeInfoTextWrappers) {
				bindEntityInverseLinks(entityTypeInfoTextWrapper);
				bindEntityUniqueKeys(entityTypeInfoTextWrapper);
			}
			
			for (BemEntityTypeInfo entityTypeInfo : schema.getEntityTypeInfos()) {
				setEntityAttributeIndexes(entityTypeInfo);				
			}


			//
			// return schema
			//
			return schema;
				
		} catch (BemException e) {
			throw e;
		} catch (Exception e) {
			throw new BemParserException(e);
		}
	}
	
	/**
	 * Parses non-entity type definition header
	 * @param tokens
	 * @return type wrapper which includes the type itself and the type body statements
	 * @throws IOException
	 * @throws BemNotFoundException
	 * @throws BemParserException
	 */
	private ExpressNonEntityTypeInfoTextWrapper parseNonEntityTypeHeader(String[] tokens) throws IOException, BemTypeNotFoundException, BemParserException {
		
		assert(tokens[0].equals(ExpressFormat.TYPE));
		tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.WHITE_SPACE);			
		
		String typeName = parseAndFormatTypeName(tokens[0]);
		
		tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.EQUAL);			
		String typeInfoString = tokens[1].trim();
		
		tokens = RegexUtils.split2(typeInfoString, RegexUtils.WHITE_SPACE);
		
		BemCollectionKindEnum collectionKind = parseCollectionKind(tokens[0]);
		BemTypeInfo typeInfo;
		
		if (collectionKind != null) {
			typeInfo = builder.createCollectionTypeInfo(schema, typeName, false);
		} else if (tokens[0].equals(StepVocabulary.ExpressFormat.SELECT)) {
			typeInfo = builder.createSelectTypeInfo(schema, typeName);
		} else if (tokens[0].equals(StepVocabulary.ExpressFormat.ENUMERATION)) {
			typeInfo = builder.createEnumerationTypeInfo(schema, typeName);
		} else {			
			typeInfo = builder.createDefinedTypeInfo(schema, typeName);
		}
		
		ExpressNonEntityTypeInfoTextWrapper typeWrapper = new ExpressNonEntityTypeInfoTextWrapper(typeInfo, typeInfoString);					
		
		for (;;) {

			String statement = lineReader.getNextStatement();
			
			if (statement != null) {
				
				if (statement.equals(StepVocabulary.ExpressFormat.END_TYPE)) {
					return typeWrapper;
				} else {
					typeWrapper.addTypeBodyStatement(statement);
				}
				
			} else {
				throw new BemFormatException(lineReader.getCurrentLineNumber(), String.format("Expected '%s'", StepVocabulary.ExpressFormat.END_TYPE));
			}
		}
	}
	
	/**
	 * Parses a type string to get a type
	 * @param typeInfoString The type info string to parse
	 * @param typeName The name of external type
	 * @return
	 * @throws IOException
	 * @throws BemParserException 
	 * @throws BemNotFoundException 
	 */
	private BemTypeInfo parseNonEntityTypeBody(BemTypeInfo typeInfo, String... statements) throws IOException, BemParserException, BemTypeNotFoundException {
		
		String[] tokens = RegexUtils.split2(statements[0], RegexUtils.WHITE_SPACE);
		
		if (typeInfo instanceof BemCollectionTypeInfo) {			

			BemCollectionKindEnum collectionKind = parseCollectionKind(tokens[0]);
			((BemCollectionTypeInfo)typeInfo).setCollectionKind(collectionKind);

			boolean isArray = tokens[0].equals(StepVocabulary.ExpressFormat.ARRAY);
			
			tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.OF);
			
			BemCardinality cardinality = parseCardinality(tokens[0], isArray);
			((BemCollectionTypeInfo)typeInfo).setCardinality(cardinality);
			
			tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.UNIQUE);			
			boolean itemsAreUnique = tokens.length == 2;
			
			String typeInfoString = itemsAreUnique ? tokens[1].trim() : tokens[0].trim();
			
			String itemTypeInfoName = parseAndFormatTypeName(typeInfoString);
			
			tokens = RegexUtils.split2(itemTypeInfoName, RegexUtils.WHITE_SPACE);
			
			if (isCollectionTypeHeader(tokens[0])) {				
				BemTypeInfo itemTypeInfo = builder.createCollectionTypeInfo(schema, itemTypeInfoName, true);				
				parseNonEntityTypeBody(itemTypeInfo, typeInfoString);
				((BemCollectionTypeInfo)typeInfo).setItemTypeInfo(itemTypeInfo);				
			} else {
				BemTypeInfo itemTypeInfo = schema.getTypeInfo(itemTypeInfoName);
				((BemCollectionTypeInfo)typeInfo).setItemTypeInfo(itemTypeInfo);				
			}
			
			return typeInfo;	
			
		} else if (typeInfo instanceof BemSelectTypeInfo) {
			
			tokens = StringUtils.getStringBetweenBrackets(tokens[1].trim());
			tokens = RegexUtils.splitAll(tokens[0], StringUtils.COMMA);
			for (int i = 0; i < tokens.length; ++i) {
				String itemTypeInfoName = parseAndFormatTypeName(tokens[i].trim());
				BemTypeInfo itemTypeInfo = schema.getTypeInfo(itemTypeInfoName);
				((BemSelectTypeInfo)typeInfo).addItemTypeInfo(itemTypeInfo);
			}
			
			return typeInfo;
			
		} else if (tokens[0].equals(StepVocabulary.ExpressFormat.ENUMERATION)) {

			//
			// create a enumeration type
			//
			tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.OF);
			tokens = StringUtils.getStringBetweenBrackets(tokens[1].trim());
			tokens = RegexUtils.splitAll(tokens[0], StringUtils.COMMA);
			
			List<String> values = new ArrayList<String>();
			for (String value : tokens) {
				values.add(value.trim());
			}
			
			((BemEnumerationTypeInfo)typeInfo).setValues(values);
			return typeInfo;
			
		} else {			
			
			String internalTypeInfoName = parseAndFormatTypeName(tokens[0]);
			BemTypeInfo internalTypeInfo = schema.getTypeInfo(internalTypeInfoName);
			((BemDefinedTypeInfo)typeInfo).setWrappedTypeInfo(internalTypeInfo);
			return typeInfo;
				
		}			
			
	}
	
	/**
	 * Parses collection cardinality in format [X:Y], where X and Y can be a number or sign '?'
	 * @param s
	 * @param isArrayIndex
	 * @return
	 */
	private BemCardinality parseCardinality(String s, boolean isArrayIndex) {
		
		s = s.trim();
		
		if (s.isEmpty()) {
			return null;
		}
		
		String[] cardinalityTokens = 
				RegexUtils.split2(s.replaceAll("[\\[\\]\\s]", ""), StringUtils.COLON);
		
		int min = cardinalityTokens[0].equals(StepVocabulary.ExpressFormat.UNBOUNDED) ?
				BemCardinality.UNBOUNDED : Integer.parseInt(cardinalityTokens[0]); 
		
		int max = cardinalityTokens[1].equals(StepVocabulary.ExpressFormat.UNBOUNDED) ?
				BemCardinality.UNBOUNDED : Integer.parseInt(cardinalityTokens[1]);
		
		return builder.createCardinality(min, max, isArrayIndex);
			
	}

	/**
	 * Subsections of section ENTITY
	 *
	 */
	public enum EntityFormatSection {
		ATTRIBUTES,
		DERIVE,
		INVERSE,
		UNIQUE,
		WHERE,		
	}
	
	/**
	 * Parses tokens to get an entity info
	 * @param tokens
	 * @return
	 * @throws BemFormatException
	 * @throws IOException
	 * @throws BemNotFoundException 
	 */
	private ExpressEntityTypeInfoTextWrapper parseEntityTypeInfoText(String[] tokens) throws BemFormatException, IOException {
		
		if (tokens.length != 2) {
			throw new BemFormatException(lineReader.getCurrentLineNumber(), "Invalid format");		
		}
			
		// get entity type name
		tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.WHITE_SPACE);		
		
		String entityTypeName = tokens[0];
		BemEntityTypeInfo entityTypeInfo;
		try {
			entityTypeInfo = schema.getEntityTypeInfo(entityTypeName);
		} catch (BemTypeNotFoundException e) {
			entityTypeInfo = builder.createEntityTypeInfo(schema, entityTypeName);
		}
		ExpressEntityTypeInfoTextWrapper entityTypeInfoTextWrapper = new ExpressEntityTypeInfoTextWrapper(entityTypeInfo);			
		
		if (tokens.length == 2) {
			
			// 
			// check if it's an abstract class
			//
			tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.ABSTRACT);				
			int tokenIndex;				
			if (tokens.length == 2) {
				entityTypeInfo.setAbstractSuperType(true);
				tokenIndex = 1;
			} else {
				tokenIndex = 0;
			}
			
			// 
			// get super entity type name
			//
			tokens = RegexUtils.split2(tokens[tokenIndex], StepVocabulary.ExpressFormat.SUBTYPE);
			
			if (tokens.length == 2) {
				tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.OF);				
				tokens = StringUtils.getStringBetweenBrackets(tokens[1].trim());
				
				String superTypeName = parseAndFormatTypeName(tokens[0].trim());
				entityTypeInfoTextWrapper.setSuperTypeName(superTypeName);
			}
		}			
		
		EntityFormatSection currentSection = EntityFormatSection.ATTRIBUTES;
		List<String> listOfStatements = entityTypeInfoTextWrapper.getAttributeStatements();
		
		for (;;) {
			
			String statement = lineReader.getNextStatement();				
			
			if (statement == null) {
				throw new BemFormatException(lineReader.getCurrentLineNumber(), String.format("Expected '%s'", StepVocabulary.ExpressFormat.END_ENTITY));
			}
				
			tokens = RegexUtils.split2(statement, RegexUtils.WHITE_SPACE);
			
			if (tokens[0].equals(StepVocabulary.ExpressFormat.END_ENTITY)) {						
				return entityTypeInfoTextWrapper;
			} else if (currentSection.compareTo(EntityFormatSection.WHERE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.WHERE)) {
				currentSection = EntityFormatSection.WHERE;
				listOfStatements = null;
			} else if (currentSection.compareTo(EntityFormatSection.UNIQUE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.UNIQUE)) {
				currentSection = EntityFormatSection.UNIQUE;						
				listOfStatements = entityTypeInfoTextWrapper.getUniqueKeysStatements();
			} else if (currentSection.compareTo(EntityFormatSection.INVERSE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.INVERSE)) {
				currentSection = EntityFormatSection.INVERSE;						
				statement = tokens[1].trim();
				listOfStatements = entityTypeInfoTextWrapper.getInverseLinkStatements();
			} else if (currentSection.compareTo(EntityFormatSection.DERIVE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.DERIVE)) {
				currentSection = EntityFormatSection.DERIVE;						
				listOfStatements = null;
			}
			
			if (listOfStatements != null) {
				listOfStatements.add(statement);						
			}
				
		} // for
	}
	
	private BemCollectionTypeInfo parseCollectionType(BemCollectionKindEnum collectionKind, String typeInfoString, boolean isDerivedType)
			throws BemParserException, BemTypeNotFoundException, BemTypeAlreadyExistsException
	{
		
		// read collection cardinality
		String[] tokens = RegexUtils.split2(typeInfoString, StepVocabulary.ExpressFormat.OF);				
		BemCardinality collectionCardinality = parseCardinality(tokens[0], collectionKind == BemCollectionKindEnum.Array);				
		tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.UNIQUE);				
		
		boolean collectionItemsAreUnique = tokens.length == 2;		
		typeInfoString = collectionItemsAreUnique ? tokens[1].trim(): tokens[0].trim();		
		
		tokens = RegexUtils.split2(typeInfoString, RegexUtils.WHITE_SPACE);
		
		if (tokens.length == 1) {
			
			//
			// create or get the collection type
			//
			String collectionItemTypeInfoName = parseAndFormatTypeName(typeInfoString);				
			String collectionTypeInfoName = BemCollectionTypeInfo.formatCollectionTypeName(collectionKind, collectionItemTypeInfoName, collectionCardinality);
			try {
				return (BemCollectionTypeInfo)schema.getTypeInfo(collectionTypeInfoName);
			} catch (BemTypeNotFoundException e) {					
			
				// create collection type (with cardinality)
				BemCollectionTypeInfo collectionTypeInfo = builder.createCollectionTypeInfo(schema, collectionTypeInfoName, isDerivedType);
				collectionTypeInfo.setCollectionKind(collectionKind);
				
				BemTypeInfo collectionItemTypeInfo = schema.getTypeInfo(collectionItemTypeInfoName);
				collectionTypeInfo.setItemTypeInfo(collectionItemTypeInfo);
				collectionTypeInfo.setCardinality(collectionCardinality);
				
				if (!isDerivedType) {
					assert(collectionKind.isSorted()) : collectionTypeInfo;
					schema.addTypeInfo(collectionTypeInfo);
				} else {
					derivedCollectionTypes.put(collectionTypeInfoName, collectionTypeInfo);
				}
				
				return collectionTypeInfo;
			}
			
		} else {
			
			BemCollectionKindEnum collectionKind2 = parseCollectionKind(tokens[0]);			
			if (collectionKind == null) {
				throw new BemFormatException(lineReader.getCurrentLineNumber(), String.format("Expected one of %s", BemCollectionKindEnum.values().toString()));				
			}
			
			// case SET/LIST/ARRAY/BAG OF LIST OF ... 

			BemCollectionTypeInfo collectionItemTypeInfo = parseCollectionType(collectionKind2, tokens[1], isDerivedType);
			
			// create or get the super collection type (without cardinality)
			String collectionTypeInfoName = BemCollectionTypeInfo.formatCollectionTypeName(collectionKind, collectionItemTypeInfo.getName(), collectionCardinality);
			
			BemCollectionTypeInfo collectionTypeInfo = null;
			
			try {
				collectionTypeInfo = (BemCollectionTypeInfo) schema.getTypeInfo(collectionTypeInfoName);
			} catch (BemTypeNotFoundException e) {
				
				if (isDerivedType) {
					collectionTypeInfo = derivedCollectionTypes.get(collectionTypeInfoName);
				}
				
				if (collectionTypeInfo == null) {				
					// create collection type (with cardinality)
					collectionTypeInfo = builder.createCollectionTypeInfo(schema, collectionTypeInfoName, isDerivedType);
					collectionTypeInfo.setCollectionKind(collectionKind2);
					collectionTypeInfo.setItemTypeInfo(collectionItemTypeInfo);
					collectionTypeInfo.setCardinality(collectionCardinality);
					//collectionTypeInfo.bindTypeInfo(schema);
				}
				
				if (!isDerivedType) {
					schema.addTypeInfo(collectionTypeInfo);
				} else {
					derivedCollectionTypes.put(collectionTypeInfoName, collectionTypeInfo);					
				}
			}
			
			return collectionTypeInfo;
		}
			
	}
	
	private void bindEntitySuperTypeAndAttributes(ExpressEntityTypeInfoTextWrapper entityTypeInfoTextWrapper)
			throws BemParserException, BemTypeNotFoundException, BemTypeAlreadyExistsException, IOException
	{		
				
		BemEntityTypeInfo entityTypeInfo = entityTypeInfoTextWrapper.getEntityTypeInfo();
		
		String superTypeInfoName = entityTypeInfoTextWrapper.getSuperTypeName();
		if (superTypeInfoName != null) {
			BemEntityTypeInfo superTypeInfo = schema.getEntityTypeInfo(superTypeInfoName); 
			entityTypeInfo.setSuperTypeInfo(superTypeInfo);
		}

		for (String statement : entityTypeInfoTextWrapper.getAttributeStatements()) {
			
			String[] tokens = RegexUtils.split2(statement, StringUtils.COLON);						
			String attributeName = parseAndFormatAttributeName(tokens[0].trim());

			tokens = RegexUtils.split2(tokens[1].trim(), StepVocabulary.ExpressFormat.OPTIONAL);						
			boolean isOptional;
			if (tokens.length == 1) {
				isOptional = false;
				tokens = RegexUtils.split2(tokens[0].trim(), RegexUtils.WHITE_SPACE);
			} else {
				isOptional = true;
				tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.WHITE_SPACE);
			}
			
			BemTypeInfo attributeValueTypeInfo;
			
			BemCollectionKindEnum collectionKind = parseCollectionKind(tokens[0]); 
			
			if (collectionKind != null) {				
				attributeValueTypeInfo = parseCollectionType(collectionKind, tokens[1], true);				
			} else {
				String attributeValueTypeInfoName = parseAndFormatTypeName(tokens[0]);
				attributeValueTypeInfo = schema.getTypeInfo(attributeValueTypeInfoName);
			}
			
			BemAttributeInfo attributeInfo;
			if (attributeValueTypeInfo instanceof BemEntityTypeInfo ||
					attributeValueTypeInfo instanceof BemSelectTypeInfo ||
					attributeValueTypeInfo instanceof BemCollectionTypeInfo) {
				attributeInfo = new BemAttributeInfo(entityTypeInfo, attributeName, attributeValueTypeInfo);
			} else {
				assert (attributeValueTypeInfo instanceof BemDefinedTypeInfo ||
						attributeValueTypeInfo instanceof BemEnumerationTypeInfo ||
						attributeValueTypeInfo instanceof BemLogicalTypeInfo ||
						attributeValueTypeInfo instanceof BemPrimitiveTypeInfo) :
					attributeValueTypeInfo.getClass();
				
//				if (attributeValueTypeInfo instanceof BemLiteralTypeInfo) {
//				attributeValueTypeInfo = schema.getEquivalentDefinedType((BemLiteralTypeInfo)attributeValueTypeInfo);
//			}

// TODO: Convert IfcTimeStamp (=Long) to DateTime.
//				if (attributeName.equals(StepVocabulary.TypeNames.IFC_TIME_STAMP)) {
//					attributeValueTypeInfo = schema.IFC_TIME_STAMP;
//				}
				attributeInfo = new BemAttributeInfo(entityTypeInfo, attributeName, attributeValueTypeInfo);				
			}
			attributeInfo.setOptional(isOptional);
			attributeInfo.setFunctional(true);
			
			entityTypeInfo.addAttributeInfo(attributeInfo);			
		}			

	}
	
	public static boolean isCollectionTypeHeader(String typeHeader) {
		return typeHeader.equals(StepVocabulary.ExpressFormat.SET) ||
				typeHeader.equals(StepVocabulary.ExpressFormat.LIST) ||
				typeHeader.equals(StepVocabulary.ExpressFormat.ARRAY) ||
				typeHeader.equals(StepVocabulary.ExpressFormat.BAG);
	}
	
	private void setEntityAttributeIndexes(BemEntityTypeInfo entityTypeInfo) {
		int attributeCount = 0;
		
		if (entityTypeInfo.getSuperTypeInfo() != null) {
			attributeCount = entityTypeInfo.getSuperTypeInfo().getAttributeInfos(true).size();
		}
		
		for (BemAttributeInfo attributeInfo : entityTypeInfo.getAttributeInfos(false)) {
			attributeInfo.setAttributeIndex(attributeCount++);
		}			
	}
	
	private void bindEntityInverseLinks(ExpressEntityTypeInfoTextWrapper entityTypeInfoTextWrapper) throws BemParserException, BemTypeNotFoundException, BemAttributeNotFoundException {

		BemEntityTypeInfo entityTypeInfo = entityTypeInfoTextWrapper.getEntityTypeInfo();
		
		for (String statement : entityTypeInfoTextWrapper.getInverseLinkStatements()) {
			
			String[] tokens = RegexUtils.split2(statement, StringUtils.COLON);						
			String attributeName = parseAndFormatAttributeName(tokens[0].trim());

			tokens = RegexUtils.split2(tokens[1].trim(), StepVocabulary.ExpressFormat.SET);
			
			BemCardinality cardinality;  
			
			if (tokens.length == 1) {
								
				cardinality = builder.createCardinality(BemCardinality.ONE, BemCardinality.ONE, false);
				tokens = RegexUtils.split2(tokens[0].trim(), RegexUtils.WHITE_SPACE);
				
			} else {
				
				tokens = RegexUtils.split2(tokens[1].trim(), StepVocabulary.ExpressFormat.OF);
				cardinality = parseCardinality(tokens[0], false);
				tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.WHITE_SPACE);
				
			}
			
			String sourceEntityTypeInfoName = tokens[0];

			tokens = RegexUtils.split2(tokens[1].trim(), StepVocabulary.ExpressFormat.FOR);
			String outgoingLinkName = parseAndFormatAttributeName(tokens[1].trim());
			
			BemEntityTypeInfo sourceEntityTypeInfo = schema.getEntityTypeInfo(sourceEntityTypeInfoName);

			assert sourceEntityTypeInfo.getAttributeInfo(outgoingLinkName) instanceof BemAttributeInfo : outgoingLinkName;
			BemAttributeInfo outgoingAttributeInfo = (BemAttributeInfo)sourceEntityTypeInfo.getAttributeInfo(outgoingLinkName);
			
			BemInverseAttributeInfo inverseAttributeInfo =
					new BemInverseAttributeInfo(entityTypeInfo, attributeName, sourceEntityTypeInfo, outgoingAttributeInfo);
			inverseAttributeInfo.setCardinality(cardinality);
			if (cardinality.isSingle()) {
				inverseAttributeInfo.setFunctional(true);
				outgoingAttributeInfo.setInverseFunctional(true);
			}
			inverseAttributeInfo.setInverseFunctional(outgoingAttributeInfo.isFunctional());
			entityTypeInfo.addInverseAttributeInfo(inverseAttributeInfo);
		}
	}
	
	private void bindEntityUniqueKeys(ExpressEntityTypeInfoTextWrapper entityTypeInfoTextWrapper) throws BemParserException, BemTypeNotFoundException, BemAttributeNotFoundException {

		BemEntityTypeInfo entityTypeInfo = entityTypeInfoTextWrapper.getEntityTypeInfo();
		
		for (String statement : entityTypeInfoTextWrapper.getUniqueKeysStatements()) {
			
			String[] tokens = RegexUtils.split2(statement, StringUtils.COLON);						
//			String uniqueKeyName = tokens[0].trim();
			
			BemUniqueKeyInfo uniqueKeyInfo = new BemUniqueKeyInfo();

			while (tokens.length > 1) {
				tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.COMMA);
				String attributeName = parseAndFormatAttributeName(tokens[0].trim());
				BemAttributeInfo attributeInfo = entityTypeInfo.getAttributeInfo(attributeName); 
				uniqueKeyInfo.addAttributeInfo(attributeInfo);
			}
			
			entityTypeInfo.addUniqueKey(uniqueKeyInfo);
			
			if (uniqueKeyInfo.size() == 1) {
				uniqueKeyInfo.getFirstAttributeInfo().setInverseFunctional(true); 
			}
		}
	}
	
//	public BemSchema getStepSchema() throws BemParserException {
//		if (stffExpressSchema == null) {
//			stffExpressSchema = parse(new ByteArrayInputStream(StepVocabulary.SpfFormat.Header.SCHEMA_STRING.getBytes()));
//		}
//		return stffExpressSchema;
//	}
	
	/**
	 * Returns type name before opening brackets (if any);
	 * @param typeName
	 * @return
	 */
	private static String parseAndFormatTypeName(String typeName) {
		int indexOfOpeningBracket = typeName.indexOf(StringUtils.OPENING_ROUND_BRACKET_CHAR);
		if (indexOfOpeningBracket >= 0) {
			typeName = typeName.substring(0, indexOfOpeningBracket);
		}
		return typeName.trim();
		
//		return RegexUtils.removeNonSafeUrlSymbols(typeName);		
	}

	private static String parseAndFormatAttributeName(String attributeName) {
		return attributeName.substring(0, 1).toLowerCase() + attributeName.substring(1);
	}
	
	/**
	 * Parses collection kind strings: "LIST", "ARRAY", "SET", "BAG" 
	 * @param collectionKind
	 * @return the corresponding collection kind, or null if collectionKind param is not one of "LIST", "ARRAY", "SET", "BAG" 
	 */	
	private static BemCollectionKindEnum parseCollectionKind(String collectionKind) {
		switch(collectionKind.toUpperCase()) {
		case StepVocabulary.ExpressFormat.LIST:
			return BemCollectionKindEnum.List;
		case StepVocabulary.ExpressFormat.ARRAY:
			return BemCollectionKindEnum.Array;
		case StepVocabulary.ExpressFormat.SET:
			return BemCollectionKindEnum.Set;
		case StepVocabulary.ExpressFormat.BAG:
			return BemCollectionKindEnum.Bag;
		}
//		throw new BemParserException("Unsupported collection kind: " + collectionKind);
		return null;
	}
	
	
}
