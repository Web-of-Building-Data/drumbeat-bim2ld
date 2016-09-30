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
import fi.aalto.cs.drumbeat.data.bedm.parsers.DrbFormatException;
import fi.aalto.cs.drumbeat.data.bedm.parsers.DrbParserException;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbSchema;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;
import fi.aalto.cs.drumbeat.data.bedm.schema.*;


/**
 * 
 * Parser of IFC schema from in stream   
 * 
 * @author vuhoan1
 *
 */

class ExpressSchemaInternalParser extends StepInternalParser {
	
//	/**
//	 * Cache for EXPRESS schema of STFF header section
//	 */
//	private static DrbSchema stffExpressSchema;
	
	/**
	 * The in reader, reads line by line, wraps original in stream inside
	 */
	private StepLineReader lineReader;
	
	/**
	 * The output schema
	 */
	private DrbSchema schema;
	
	private List<ExpressEntityTypeInfoText> entityTypeInfoTexts = new ArrayList<ExpressEntityTypeInfoText>();
	
	/**
	 * Creates a new parser. For internal use.
	 * 
	 * @param in
	 */	
	ExpressSchemaInternalParser(DrbSchema schema, InputStream in, String fileType) {
		this.schema = schema;
		lineReader = new StepLineReader(in);
	}

	/**
	 * Parses an IFC schema from an in stream. This is the main entry of the parser. 
	 * 
	 * @param in
	 * @return {@link DrbSchema}
	 * @throws DrbParserException
	 */
	DrbSchema parse() throws DrbParserException {		
		try {
			String statement = lineReader.getNextStatement();
			String tokens[] = RegexUtils.split2(statement, RegexUtils.WHITE_SPACE);
			
			if (tokens.length != 2 || !tokens[0].equals(StepVocabulary.ExpressFormat.SCHEMA)) {
				throw new DrbFormatException(lineReader.getCurrentLineNumber(), "Invalid schema");			
			}
			
			//
			// get schema version
			//
			String version = tokens[1].trim();
			schema.setName(version);
			
			//
			// read all type definitions,
			// parse, create and put new types into the schema
			//
			for (;;) {
				statement = lineReader.getNextStatement();				
				if (statement == null) {
					throw new DrbFormatException(lineReader.getCurrentLineNumber(), String.format("Expected '%s'", StepVocabulary.ExpressFormat.END_SCHEMA));
				}
					
				tokens = RegexUtils.split2(statement, RegexUtils.WHITE_SPACE);
				
				if (tokens[0].equals(StepVocabulary.ExpressFormat.TYPE)) {
					
					// parse an non-entity type info
					DrbTypeInfo nonEntityTypeInfo = parseNonEntityTypeInfo(tokens);
					schema.addNonEntityTypeInfo(nonEntityTypeInfo);
					
				} else if (tokens[0].equals(StepVocabulary.ExpressFormat.ENTITY)) {
					
					// get the entity type body and put into the cache for LATER BINDING
					ExpressEntityTypeInfoText entityTypeInfoText =  parseEntityTypeInfoText(tokens);
					entityTypeInfoTexts.add(entityTypeInfoText);						
					schema.addEntityTypeInfo(entityTypeInfoText.getEntityTypeInfo());
					
				} else if (tokens[0].equals(StepVocabulary.ExpressFormat.END_SCHEMA)) {
					break;
				}
					
			}
			
//				//
//				// bind all types
//				//
//				for (DrbNonEntityTypeInfo typeInfo : schema.getNonEntityTypeInfos()) {
//					if (typeInfo instanceof IDrbLateBindingTypeInfo) {
//						((IDrbLateBindingTypeInfo)typeInfo).bindTypeInfo(schema);
//					}				
//				}
			
			//
			// bind entity types with their supertypes
			// bind entity types' attributes with the attribute types
			//
			for (ExpressEntityTypeInfoText entityTypeInfoText : entityTypeInfoTexts) {
				bindEntitySuperTypeAndAttributes(entityTypeInfoText);
			}
			
			//
			// bind entity types' inverse links and unique keys
			// 
			for (ExpressEntityTypeInfoText entityTypeInfoText : entityTypeInfoTexts) {
				bindEntityInverseLinks(entityTypeInfoText);
				bindEntityUniqueKeys(entityTypeInfoText);
			}
			
			for (DrbEntityTypeInfo entityTypeInfo : schema.getEntityTypeInfos()) {
				setEntityAttributeIndexes(entityTypeInfo);				
			}


			//
			// return schema
			//
			return schema;
				
		} catch (DrbParserException e) {
			throw e;
		} catch (Exception e) {
			throw new DrbParserException(e);
		} finally {
			entityTypeInfoTexts = null;			
		}
	}
	
	private DrbTypeInfo parseNonEntityTypeInfo(String[] tokens) throws IOException, DrbNotFoundException, DrbParserException {
		
		tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.WHITE_SPACE);			
		
		String typeName = parseAndFormatTypeName(tokens[0]);
		
		tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.EQUAL);			
		String typeInfoString = tokens[1].trim();
		
		DrbTypeInfo typeInfo = parseNonEntityTypeInfoBody(typeInfoString, typeName);
		
		for (;;) {

			String statement = lineReader.getNextStatement();
			
			if (statement != null) {
				
				if (statement.equals(StepVocabulary.ExpressFormat.END_TYPE)) {

					return typeInfo;
				}
				
			} else {
				throw new DrbFormatException(lineReader.getCurrentLineNumber(), String.format("Expected '%s'", StepVocabulary.ExpressFormat.END_TYPE));
			}
		}		
	}
	
	/**
	 * Parses a type string to get a type
	 * @param typeInfoString The type info string to parse
	 * @param typeName The name of external type
	 * @return
	 * @throws IOException
	 * @throws DrbParserException 
	 */
	private DrbTypeInfo parseNonEntityTypeInfoBody(String typeInfoString, String typeName) throws IOException, DrbParserException {		
		String[] tokens = RegexUtils.split2(typeInfoString, RegexUtils.WHITE_SPACE);
		
		DrbCollectionKindEnum collectionKind = parseCollectionKind(tokens[0]);
		
		if (collectionKind != null) {
			//
			// create a collection type
			//
			boolean isArray = tokens[0].equals(StepVocabulary.ExpressFormat.ARRAY);
			
			tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.OF);
			
			DrbCardinality cardinality = parseCardinality(tokens[0], isArray);
			
			tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.UNIQUE);			
			boolean itemsAreUnique = tokens.length == 2;
			
			typeInfoString = itemsAreUnique ? tokens[1].trim() : tokens[0].trim();
			
			String itemTypeInfoName = parseAndFormatTypeName(typeInfoString);
			
			tokens = RegexUtils.split2(itemTypeInfoName, RegexUtils.WHITE_SPACE);
			
			DrbCollectionTypeInfo typeInfo;
			if (isCollectionTypeHeader(tokens[0])) {
				DrbTypeInfo itemTypeInfo = parseNonEntityTypeInfoBody(typeInfoString, itemTypeInfoName); 
				typeInfo = new DrbCollectionTypeInfo(schema, typeName, collectionKind, itemTypeInfo);
			} else {
				typeInfo = new DrbCollectionTypeInfo(schema, typeName, collectionKind, itemTypeInfoName);				
			}
			
			typeInfo.setCardinality(cardinality);
			return typeInfo;		
			
		} else if (tokens[0].equals(StepVocabulary.ExpressFormat.SELECT)) {
			
			//
			// create a select type
			//
			tokens = StringUtils.getStringBetweenBrackets(tokens[1].trim());
			tokens = RegexUtils.splitAll(tokens[0], StringUtils.COMMA);
			List<String> selectTypeInfoNames = new ArrayList<String>();
			for (int i = 0; i < tokens.length; ++i) {
				selectTypeInfoNames.add(parseAndFormatTypeName(tokens[i].trim()));
			}
			
			DrbSelectTypeInfo typeInfo = new DrbSelectTypeInfo(schema, typeName, selectTypeInfoNames);			
			
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
			
			DrbEnumerationTypeInfo typeInfo = new DrbEnumerationTypeInfo(schema, typeName, values);
			return typeInfo;
			
		} else {			
			
			String internalTypeInfoName = parseAndFormatTypeName(tokens[0]);
			
			assert(typeName != null);
			
			try {
				return schema.getNonEntityTypeInfo(typeName);
			} catch (DrbNotFoundException e) {			
				return new DrbDefinedTypeInfo(schema, typeName, internalTypeInfoName);
			}			
				
		}			
			
	}
	
	private static DrbCardinality parseCardinality(String s, boolean isArrayIndex) {
		
		s = s.trim();
		
		if (s.isEmpty()) {
			return null;
		}
		
		String[] cardinalityTokens = 
				RegexUtils.split2(s.replaceAll("[\\[\\]\\s]", ""), StringUtils.COLON);
		
		int min = cardinalityTokens[0].equals(StepVocabulary.ExpressFormat.UNBOUNDED) ?
				DrbCardinality.UNBOUNDED : Integer.parseInt(cardinalityTokens[0]); 
		
		int max = cardinalityTokens[1].equals(StepVocabulary.ExpressFormat.UNBOUNDED) ?
				DrbCardinality.UNBOUNDED : Integer.parseInt(cardinalityTokens[1]);
		
		return new DrbCardinality(min, max, isArrayIndex);
			
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
	 * @throws DrbFormatException
	 * @throws IOException
	 * @throws DrbNotFoundException 
	 */
	private ExpressEntityTypeInfoText parseEntityTypeInfoText(String[] tokens) throws DrbFormatException, IOException {
		
		if (tokens.length != 2) {
			throw new DrbFormatException(lineReader.getCurrentLineNumber(), "Invalid format");		
		}
			
		// get entity type name
		tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.WHITE_SPACE);		
		
		String entityTypeName = tokens[0];
		DrbEntityTypeInfo entityTypeInfo;
		try {
			entityTypeInfo = schema.getEntityTypeInfo(entityTypeName);
		} catch (DrbNotFoundException e) {
			entityTypeInfo = new DrbEntityTypeInfo(schema, entityTypeName);
		}
		ExpressEntityTypeInfoText entityTypeInfoText = new ExpressEntityTypeInfoText(entityTypeInfo);			
		
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
				entityTypeInfoText.setSuperTypeName(superTypeName);
			}
		}			
		
		EntityFormatSection currentSection = EntityFormatSection.ATTRIBUTES;
		List<String> listOfStatements = entityTypeInfoText.getAttributeStatements();
		
		for (;;) {
			
			String statement = lineReader.getNextStatement();				
			
			if (statement == null) {
				throw new DrbFormatException(lineReader.getCurrentLineNumber(), String.format("Expected '%s'", StepVocabulary.ExpressFormat.END_ENTITY));
			}
				
			tokens = RegexUtils.split2(statement, RegexUtils.WHITE_SPACE);
			
			if (tokens[0].equals(StepVocabulary.ExpressFormat.END_ENTITY)) {						
				return entityTypeInfoText;
			} else if (currentSection.compareTo(EntityFormatSection.WHERE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.WHERE)) {
				currentSection = EntityFormatSection.WHERE;
				listOfStatements = null;
			} else if (currentSection.compareTo(EntityFormatSection.UNIQUE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.UNIQUE)) {
				currentSection = EntityFormatSection.UNIQUE;						
				listOfStatements = entityTypeInfoText.getUniqueKeysStatements();
			} else if (currentSection.compareTo(EntityFormatSection.INVERSE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.INVERSE)) {
				currentSection = EntityFormatSection.INVERSE;						
				statement = tokens[1].trim();
				listOfStatements = entityTypeInfoText.getInverseLinkStatements();
			} else if (currentSection.compareTo(EntityFormatSection.DERIVE) <= 0 && tokens[0].equals(StepVocabulary.ExpressFormat.DERIVE)) {
				currentSection = EntityFormatSection.DERIVE;						
				listOfStatements = null;
			}
			
			if (listOfStatements != null) {
				listOfStatements.add(statement);						
			}
				
		} // for
	}
	
	private DrbCollectionTypeInfo parseCollectionType(DrbCollectionKindEnum collectionKind, String typeInfoString) throws DrbParserException {
		
		// read collection cardinality
		String[] tokens = RegexUtils.split2(typeInfoString, StepVocabulary.ExpressFormat.OF);				
		DrbCardinality collectionCardinality = parseCardinality(tokens[0], collectionKind == DrbCollectionKindEnum.Array);				
		tokens = RegexUtils.split2(tokens[1], StepVocabulary.ExpressFormat.UNIQUE);				
		
		boolean collectionItemsAreUnique = tokens.length == 2;		
		typeInfoString = collectionItemsAreUnique ? tokens[1].trim(): tokens[0].trim();		
		
		tokens = RegexUtils.split2(typeInfoString, RegexUtils.WHITE_SPACE);
		
		if (tokens.length == 1) {
			
			//
			// create or get the collection type
			//
			String collectionItemTypeInfoName = parseAndFormatTypeName(typeInfoString);				
			String collectionTypeInfoName = DrbCollectionTypeInfo.formatCollectionTypeName(collectionKind, collectionItemTypeInfoName, collectionCardinality);
			try {
				return (DrbCollectionTypeInfo)schema.getTypeInfo(collectionTypeInfoName);
			} catch (DrbNotFoundException e) {					
			
				// create collection type (with cardinality)
				DrbCollectionTypeInfo collectionTypeInfo = new DrbCollectionTypeInfo(schema, collectionTypeInfoName, collectionKind, collectionItemTypeInfoName);
				collectionTypeInfo.setCardinality(collectionCardinality);
				schema.addNonEntityTypeInfo(collectionTypeInfo);
				
				return collectionTypeInfo;
			}
			
		} else {
			
			DrbCollectionKindEnum collectionKind2 = parseCollectionKind(tokens[0]);			
			if (collectionKind == null) {
				throw new DrbFormatException(lineReader.getCurrentLineNumber(), String.format("Expected one of %s", DrbCollectionKindEnum.values().toString()));				
			}
			
			// case SET/LIST/ARRAY/BAG OF LIST OF ... 

			DrbCollectionTypeInfo collectionItemTypeInfo = parseCollectionType(collectionKind2, tokens[1]);
			
			// create or get the super collection type (without cardinality)
			String collectionTypeInfoName = DrbCollectionTypeInfo.formatCollectionTypeName(collectionKind, collectionItemTypeInfo.getName(), collectionCardinality);
			
			// create collection type (with cardinality)
			DrbCollectionTypeInfo collectionTypeInfo = new DrbCollectionTypeInfo(schema, collectionTypeInfoName, collectionKind2, collectionItemTypeInfo);
			collectionTypeInfo.setCardinality(collectionCardinality);
			//collectionTypeInfo.bindTypeInfo(schema);
			schema.addNonEntityTypeInfo(collectionTypeInfo);
			
			return collectionTypeInfo;
			
		}
			
	}
	
	private void bindEntitySuperTypeAndAttributes(ExpressEntityTypeInfoText entityTypeInfoText) throws DrbNotFoundException, IOException, DrbParserException {		
				
		DrbEntityTypeInfo entityTypeInfo = entityTypeInfoText.getEntityTypeInfo();
		
		String superTypeInfoName = entityTypeInfoText.getSuperTypeName();
		if (superTypeInfoName != null) {
			DrbEntityTypeInfo superTypeInfo = schema.getEntityTypeInfo(superTypeInfoName); 
			entityTypeInfo.setSuperTypeInfo(superTypeInfo);
		}

		for (String statement : entityTypeInfoText.getAttributeStatements()) {
			
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
			
			DrbTypeInfo attributeTypeInfo;
			
			DrbCollectionKindEnum collectionKind = parseCollectionKind(tokens[0]); 
			
			if (collectionKind != null) {				
				attributeTypeInfo = parseCollectionType(collectionKind, tokens[1]);				
			} else {
				String attributeTypeInfoName = parseAndFormatTypeName(tokens[0]);
				attributeTypeInfo = schema.getTypeInfo(attributeTypeInfoName);
			}
			
			DrbAttributeInfo attributeInfo;
			if (attributeTypeInfo instanceof DrbEntityTypeInfo ||
					attributeTypeInfo instanceof DrbSelectTypeInfo ||
					attributeTypeInfo instanceof DrbCollectionTypeInfo) {
				attributeInfo = new DrbOutgoingLinkInfo(entityTypeInfo, attributeName, attributeTypeInfo);
			} else {
				assert (attributeTypeInfo instanceof DrbDefinedTypeInfo ||
						attributeTypeInfo instanceof DrbEnumerationTypeInfo ||
						attributeTypeInfo instanceof DrbPrimitiveTypeInfo) :
					attributeTypeInfo.getClass();
				
//				if (attributeTypeInfo instanceof DrbLiteralTypeInfo) {
//				attributeTypeInfo = schema.getEquivalentDefinedType((DrbLiteralTypeInfo)attributeTypeInfo);
//			}

// TODO: Convert IfcTimeStamp (=Long) to DateTime.
//				if (attributeName.equals(StepVocabulary.TypeNames.IFC_TIME_STAMP)) {
//					attributeTypeInfo = schema.IFC_TIME_STAMP;
//				}
				attributeInfo = new DrbAttributeInfo(entityTypeInfo, attributeName, attributeTypeInfo);				
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
	
	private void setEntityAttributeIndexes(DrbEntityTypeInfo entityTypeInfo) {
		int attributeCount = 0;
		
		if (entityTypeInfo.getSuperTypeInfo() != null) {
			attributeCount = entityTypeInfo.getSuperTypeInfo().getInheritedAttributeInfos().size();
		}
		
		for (DrbAttributeInfo attributeInfo : entityTypeInfo.getAttributeInfos()) {
			attributeInfo.setAttributeIndex(attributeCount++);
		}			
	}
	
	private void bindEntityInverseLinks(ExpressEntityTypeInfoText entityTypeInfoText) throws DrbParserException, DrbNotFoundException {

		DrbEntityTypeInfo entityTypeInfo = entityTypeInfoText.getEntityTypeInfo();
		
		for (String statement : entityTypeInfoText.getInverseLinkStatements()) {
			
			String[] tokens = RegexUtils.split2(statement, StringUtils.COLON);						
			String attributeName = parseAndFormatAttributeName(tokens[0].trim());

			tokens = RegexUtils.split2(tokens[1].trim(), StepVocabulary.ExpressFormat.SET);
			
			DrbCardinality cardinality;  
			
			if (tokens.length == 1) {
								
				cardinality = new DrbCardinality(DrbCardinality.ONE, DrbCardinality.ONE, false);
				tokens = RegexUtils.split2(tokens[0].trim(), RegexUtils.WHITE_SPACE);
				
			} else {
				
				tokens = RegexUtils.split2(tokens[1].trim(), StepVocabulary.ExpressFormat.OF);
				cardinality = parseCardinality(tokens[0], false);
				tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.WHITE_SPACE);
				
			}
			
			String sourceEntityTypeInfoName = tokens[0];

			tokens = RegexUtils.split2(tokens[1].trim(), StepVocabulary.ExpressFormat.FOR);
			String outgoingLinkName = parseAndFormatAttributeName(tokens[1].trim());
			
			DrbEntityTypeInfo sourceEntityTypeInfo = schema.getEntityTypeInfo(sourceEntityTypeInfoName);

			assert sourceEntityTypeInfo.getAttributeInfo(outgoingLinkName) instanceof DrbOutgoingLinkInfo : outgoingLinkName;
			DrbOutgoingLinkInfo outgoingLinkInfo = (DrbOutgoingLinkInfo)sourceEntityTypeInfo.getAttributeInfo(outgoingLinkName);
			
			DrbInverseLinkInfo inverseLinkInfo =
					new DrbInverseLinkInfo(entityTypeInfo, attributeName, sourceEntityTypeInfo, outgoingLinkInfo);
			inverseLinkInfo.setCardinality(cardinality);
			if (cardinality.isSingle()) {
				inverseLinkInfo.setFunctional(true);
				outgoingLinkInfo.setInverseFunctional(true);
			}
			inverseLinkInfo.setInverseFunctional(outgoingLinkInfo.isFunctional());
			entityTypeInfo.addInverseLinkInfo(inverseLinkInfo);
		}
	}
	
	private void bindEntityUniqueKeys(ExpressEntityTypeInfoText entityTypeInfoText) throws DrbParserException, DrbNotFoundException {

		DrbEntityTypeInfo entityTypeInfo = entityTypeInfoText.getEntityTypeInfo();
		
		for (String statement : entityTypeInfoText.getUniqueKeysStatements()) {
			
			String[] tokens = RegexUtils.split2(statement, StringUtils.COLON);						
//			String uniqueKeyName = tokens[0].trim();
			
			DrbUniqueKeyInfo uniqueKeyInfo = new DrbUniqueKeyInfo();

			while (tokens.length > 1) {
				tokens = RegexUtils.split2(tokens[1].trim(), RegexUtils.COMMA);
				String attributeName = parseAndFormatAttributeName(tokens[0].trim());
				DrbAttributeInfo attributeInfo = entityTypeInfo.getAttributeInfo(attributeName); 
				uniqueKeyInfo.addAttributeInfo(attributeInfo);
			}
			
			entityTypeInfo.addUniqueKey(uniqueKeyInfo);
			
			if (uniqueKeyInfo.size() == 1) {
				uniqueKeyInfo.getFirstAttributeInfo().setInverseFunctional(true); 
			}
		}
	}
	
//	public DrbSchema getStepSchema() throws DrbParserException {
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
	
}
