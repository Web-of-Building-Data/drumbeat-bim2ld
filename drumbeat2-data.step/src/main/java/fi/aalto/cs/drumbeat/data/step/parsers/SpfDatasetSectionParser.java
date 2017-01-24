package fi.aalto.cs.drumbeat.data.step.parsers;

import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.text.StrMatcher;
import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.common.string.RegexUtils;
import fi.aalto.cs.drumbeat.common.string.StrBuilderWrapper;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.data.bem.dataset.*;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemFormatException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemParserException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;
import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

class SpfDatasetSectionParser {
	
	private class StepTemporaryCollectionValueWrapper extends BemValue {
		
		private static final long serialVersionUID = 1L;

		private List<BemValue> values;

		public StepTemporaryCollectionValueWrapper(List<BemValue> values) {
			this.values = values;
		}
		
		public List<BemValue> getValues() {
			return values;
		}

		@Override
		public String toString() {
			return null;
		}
		
		@Override
		public boolean equals(Object other) {
			return false;
		}

	}
	
	
	
	private static final Logger logger = Logger.getLogger(StepDatasetParser.class);	

	private ExpressSchema schema;
	private StepLineReader reader;

	private Map<String, BemEntity> entityMap = new HashMap<>(); 	// map of entities indexed by line numbers
	
	
	/**
	 * Reads line by line and creates new entities
	 * @throws IOException
	 * @throws DrbNotFoundException
	 * @throws BemParserException
	 */
	public List<BemEntity> parseEntities(StepLineReader reader, ExpressSchema schema, boolean isHeaderSection, boolean ignoreUnknownTypes) throws IOException, DrbNotFoundException, BemParserException {		
		
		this.schema = schema;
		this.reader = reader;
		
		List<BemEntity> entities = new ArrayList<>();
		
		
		String statement;
		String[] tokens;
		//
		// getting entity headers
		//
		while ((statement = reader.getNextStatement()) != null) {
			
			BemEntity entity;
			String entityAttributesString;
			
			if (!isHeaderSection) {
				tokens = RegexUtils.split2(statement, StepVocabulary.SpfFormat.LINE_NUMBER);
				if (tokens.length != 2) {
					if (tokens.length == 1 && tokens[0].equalsIgnoreCase(StepVocabulary.SpfFormat.ENDSEC)) {
						break;
					}
					throw new BemParserException("Invalid statement: '" + statement + "'");
//					continue;
				}
			
				tokens = RegexUtils.split2(tokens[1], StepVocabulary.SpfFormat.EQUAL);
			
				//
				// create entity
				//
				//long lineNumber = Long.parseLong(tokens[0].trim());
				String lineNumber = tokens[0].trim();
				entity = getEntity(lineNumber);
				entityAttributesString = tokens[1].trim();
				
			} else {
				// header entities have no line numbers, they have different types 
				entity = new BemEntity("");
				entityAttributesString = statement;
			}
		
			//
			// set entity type
			//
			int indexOfOpeningBracket = entityAttributesString.indexOf(StringUtils.OPENING_ROUND_BRACKET);		
			String entityTypeInfoName = entityAttributesString.substring(0, indexOfOpeningBracket).trim();
			
			BemEntityTypeInfo entityTypeInfo;
			
			try {			
				entityTypeInfo = schema.getEntityTypeInfo(entityTypeInfoName);
			} catch (DrbNotFoundException e) {
				if (ignoreUnknownTypes) {
					continue;
				} else {
					throw e;
				}
			}
			entity.setTypeInfo(entityTypeInfo);
			
			entityAttributesString = entityAttributesString.substring(indexOfOpeningBracket + 1,
					entityAttributesString.length() - 1);
			
			List<BemAttributeInfo> attributeInfos = entityTypeInfo.getInheritedAttributeInfos();

			//
			// parse attribute string to get attribute values
			//
			List<BemValue> attributeValues = parseAttributeValues(new StrBuilderWrapper(entityAttributesString), entity, attributeInfos, null, null);

			setEntityAttributeValues(entity, attributeInfos, attributeValues);
			
			//
			// add entity to the model
			//
			entities.add(entity);
			
						
		}
		
		if (!isHeaderSection) {
			for (BemEntity entity : entities) {
				entity.bindInverseLinks();
			}
		}
		
		return entities;
	}

	/**
	 * Gets an entity from the map by its line number, or creates a new entity if it doesn't exist 
	 * @param lineNumber
	 * @return
	 */
	private BemEntity getEntity(String lineNumber) {
		BemEntity entity = entityMap.get(lineNumber);
		if (entity == null) {
			entity = new BemEntity(null, lineNumber);
			entityMap.put(lineNumber, entity);
		}
		return entity;
	}

	/**
	 * Parses an entity's attribute string to get attribute values
	 * 
	 * @param attributeStringBuilder
	 * @param attributeValueType
	 * @return a single attribute value or list of attribute values
	 * @throws BemFormatException
	 * @throws DrbNotFoundException
	 * @throws BemValueTypeConflictException 
	 */
	private List<BemValue> parseAttributeValues(StrBuilderWrapper attributeStrBuilderWrapper, BemEntity entity,
			List<BemAttributeInfo> entityAttributeInfos, BemTypeInfo commonAttributeTypeInfo, EnumSet<BemTypeEnum> commonValueTypes) throws BemFormatException, DrbNotFoundException {

		logger.debug(String.format("Parsing entity '%s'", entity));			

		List<BemValue> attributeValues = new ArrayList<>();
		
		for (int attributeIndex = 0; !attributeStrBuilderWrapper.trimLeft().isEmpty(); ++attributeIndex) {

			EnumSet<BemTypeEnum> attributeValueTypes;
			BemAttributeInfo attributeInfo;
			BemTypeInfo attributeTypeInfo;
			if (commonValueTypes == null) {
				assert(attributeIndex < entityAttributeInfos.size()) :
					String.format("attributeIndex=%d, entityAttributeInfos.size=%s, attributeStrBuilderWrapper='%s'",
							attributeIndex,
							entityAttributeInfos,
							attributeStrBuilderWrapper);
				attributeInfo = entityAttributeInfos.get(attributeIndex);
				attributeTypeInfo = attributeInfo.getValueTypeInfo();
				attributeValueTypes = attributeTypeInfo.getValueTypes();				
			} else {
				assert(commonAttributeTypeInfo != null);
				attributeInfo = entityAttributeInfos.get(0);
				attributeTypeInfo = commonAttributeTypeInfo;
				attributeValueTypes = commonValueTypes;
			}
			
			if (attributeTypeInfo instanceof BemCollectionTypeInfo) {
				attributeTypeInfo = ((BemCollectionTypeInfo)attributeTypeInfo).getItemTypeInfo();
			}

			switch (attributeStrBuilderWrapper.charAt(0)) {

			case StepVocabulary.SpfFormat.LINE_NUMBER_SYMBOL: // Entity
				attributeStrBuilderWrapper.skip(1);
				//Long remoteLineNumber = attributeStrBuilderWrapper.getLong();
				String remoteLineNumber = Long.toString(attributeStrBuilderWrapper.getLong());
				BemEntity remoteEntity = getEntity(remoteLineNumber);
				if (remoteEntity == null) {
					throw new DrbNotFoundException("Entity not found: #" + remoteLineNumber);
				}
				attributeValues.add(remoteEntity);
				break;

			case StepVocabulary.SpfFormat.STRING_VALUE_SYMBOL:
				String s = attributeStrBuilderWrapper.getStringBetweenSingleQuotes();
				assert attributeValueTypes.size() == 1 : "Expect attributeValueTypes.size() == 1"; 
//				if (!attributeValueTypes.contains(StepTypeEnum.GUID)) {
					attributeValues.add(new BemPrimitiveValue(s, attributeTypeInfo, BemTypeEnum.STRING));
//					break;
//				} else {
//					attributeValues.add(new StepGuidValue(s));
//					break;
//				}
				break;

			case StepVocabulary.SpfFormat.ENUMERATION_VALUE_SYMBOL:

				s = attributeStrBuilderWrapper.getStringBetweenSimilarCharacters(StepVocabulary.SpfFormat.ENUMERATION_VALUE_SYMBOL);

				assert attributeValueTypes.size() == 1 : "Expect attributeValueTypes.size() == 1"; 
				if (!attributeValueTypes.contains(BemTypeEnum.LOGICAL)) {
					attributeValues.add(new BemPrimitiveValue(s, attributeTypeInfo, BemTypeEnum.ENUM));
				} else {
					switch (s) {
					case "T":
					case "TRUE":
						attributeValues.add(new BemPrimitiveValue(LogicalEnum.TRUE, attributeTypeInfo, BemTypeEnum.LOGICAL));
						break;
					case "F":
					case "FALSE":
						attributeValues.add(new BemPrimitiveValue(LogicalEnum.FALSE, attributeTypeInfo, BemTypeEnum.LOGICAL));
						break;
					default:
						attributeValues.add(new BemPrimitiveValue(LogicalEnum.UNKNOWN, attributeTypeInfo, BemTypeEnum.LOGICAL));
						break;

					}
				}
				break;

			case StepVocabulary.SpfFormat.NULL_SYMBOL: // $
				attributeValues.add(BemValue.NULL);
				attributeStrBuilderWrapper.skip(1);
				break;

			case StepVocabulary.SpfFormat.ANY_SYMBOL: // *
				attributeValues.add(BemValue.ANY);
				attributeStrBuilderWrapper.skip(1);
				break;

			case StringUtils.OPENING_ROUND_BRACKET_CHAR: // List or Set

				String stringBetweenBrackets = attributeStrBuilderWrapper.getStringBetweenRoundBrackets();

				StrBuilderWrapper sbWrapper = new StrBuilderWrapper(stringBetweenBrackets);
				
				List<BemAttributeInfo> attributeInfos = new ArrayList<>(1);
				attributeInfos.add(attributeInfo);

				List<BemValue> values = parseAttributeValues(sbWrapper, null, attributeInfos, attributeTypeInfo, attributeValueTypes);
				attributeValues.add(new StepTemporaryCollectionValueWrapper(values));
				break;

			default:

				if (Character.isAlphabetic(attributeStrBuilderWrapper.charAt(0))) {

					// 
					// parsing sub entity
					//
					String subEntityTypeInfoName = attributeStrBuilderWrapper.getIdentifierName();
					DrbNonEntityTypeInfo subNonEntityTypeInfo = schema.getNonEntityTypeInfo(subEntityTypeInfoName);
					attributeValueTypes = subNonEntityTypeInfo.getValueTypes();
					s = attributeStrBuilderWrapper.getStringBetweenRoundBrackets();
					
					assert (s != null);

					attributeInfos = new ArrayList<>(1);
					attributeInfos.add(attributeInfo);

					values = parseAttributeValues(new StrBuilderWrapper(s), null, attributeInfos, subNonEntityTypeInfo, attributeValueTypes);
					assert values.size() == 1 : "Expect only 1 argument: " + entity + ":" + values.toString();
					attributeValues.add(new StepShortEntity(subNonEntityTypeInfo, (BemPrimitiveValue)values.get(0)));
//					attributeValues.add((BemPrimitiveValue)values.get(0));
				} else {
					
					//
					// parsing number or datetime
					//
					assert attributeValueTypes.size() == 1 : "Expect attributeValueTypes.size() == 1";
					BemTypeEnum attributeValueType = (BemTypeEnum)attributeValueTypes.iterator().next();
					Object value;
					if (attributeValueType == BemTypeEnum.INTEGER) {
						value = attributeStrBuilderWrapper.getLong();
					} else if (attributeValueType == BemTypeEnum.REAL || attributeValueType == BemTypeEnum.NUMBER) {
						value = attributeStrBuilderWrapper.getDouble();
					} else if (attributeValueType == BemTypeEnum.DATETIME) {
						long timeStamp = attributeStrBuilderWrapper.getLong();
						value = Calendar.getInstance();
						((Calendar)value).setTimeInMillis(timeStamp * 1000);
					} else {
						throw new BemFormatException(reader.getCurrentLineNumber(), "Invalid attributeValueType: " + attributeValueType);
					}
					
					attributeValues.add(new BemPrimitiveValue(value, (DrbNonEntityTypeInfo)attributeTypeInfo, attributeValueType));						
				}

				break;
			}
			
			attributeStrBuilderWrapper.trimLeft();
			attributeStrBuilderWrapper.getFirstMatch(StrMatcher.commaMatcher());
		}

		return attributeValues;
	}
	
	/**
	 * Set entity attribute values
	 * @param entity
	 * @param attributeInfos
	 * @param attributeValues
	 * @throws BemParserException
	 */
	private void setEntityAttributeValues(BemEntity entity, List<BemAttributeInfo> attributeInfos, List<BemValue> attributeValues) throws BemParserException {
		try {
			
			if (attributeValues.size() == attributeInfos.size()) {
				boolean isLiteralValueContainer = true;
				for (int attributeIndex = 0; attributeIndex < attributeValues.size(); ++attributeIndex) {
					
					BemAttributeInfo attributeInfo = attributeInfos.get(attributeIndex);
					BemValue attributeValue = attributeValues.get(attributeIndex);
					
					Boolean isLiteralValue = attributeValue.isLiteralType();
					
					if (isLiteralValue != null) {
						isLiteralValueContainer = isLiteralValueContainer && isLiteralValue == Boolean.TRUE;							
						if (isLiteralValue) {
						
							if (attributeValue instanceof StepTemporaryCollectionValueWrapper) {
								
								if (attributeInfo.isCollection()) {
									
									BemPrimitiveValueCollection values = new BemPrimitiveValueCollection();										
									for (BemValue value : ((StepTemporaryCollectionValueWrapper)attributeValue).getValues()) {
										values.add((BemPrimitiveValue)value);
									}
									
									entity.addLiteralAttribute(new StepLiteralAttribute(attributeInfo, attributeIndex, values));
									
								} else {										
									
									for (BemValue value : ((StepTemporaryCollectionValueWrapper)attributeValue).getValues()) {
										entity.addLiteralAttribute(new StepLiteralAttribute(attributeInfo, attributeIndex, value));
									}										
									
								}
								
							} else {
								
								assert(attributeValue instanceof BemPrimitiveValue) :
									String.format("Object is not a literal value, line number: %s, attributeInfo: %s, attribute value: %s, value type: %s",
											entity.getLocalId(), attributeInfo.getName(), attributeValue, attributeInfo.getValueTypeInfo().getValueTypes()); 
								entity.addLiteralAttribute(new StepLiteralAttribute(attributeInfo, attributeIndex, attributeValue));
								
							}
							
						} else { // attributeInfo instanceof BemAttributeInfo								
							
							if (attributeValue instanceof StepTemporaryCollectionValueWrapper) {
								
								if (attributeInfo.isCollection()) {
									
									BemEntityCollection destinations = new BemEntityCollection();										
									for (BemValue destination : ((StepTemporaryCollectionValueWrapper)attributeValue).getValues()) {
										if (!(destination instanceof BemEntityBase)) {
											throw new BemParserException(
													String.format("Entity attribute %s.%s has non-entity value: %s (%s)", 
															entity,
															attributeInfo,
															destination.getClass(),
															destination));
										}
										destinations.add((BemEntityBase)destination);
									}
									
									entity.addOutgoingLink(new StepLink((BemAttributeInfo)attributeInfo, attributeIndex, entity, destinations));
									
								} else {										
									
									for (BemValue destination : ((StepTemporaryCollectionValueWrapper)attributeValue).getValues()) {
										StepLink link = new StepLink((BemAttributeInfo)attributeInfo, attributeIndex, entity, (BemEntityBase)destination);
										entity.addOutgoingLink(link);
									}										
									
								}
								
							} else {
								
								assert (attributeValue instanceof BemEntityBase) :
									String.format("Object is not an entity, line number: %s, attributeInfo: %s, attribute value: %s, value type: %s",
											entity.getLocalId(), attributeInfo.getName(), attributeValue, attributeInfo.getValueTypeInfo().getValueTypes());
								StepLink link = new StepLink((BemAttributeInfo)attributeInfo, attributeIndex, entity, (BemEntityBase)attributeValue);
								entity.addOutgoingLink(link);
								
							}
						}
					}
					
				}						
				entity.setLiteralValueContainer(isLiteralValueContainer);
				
			} else {
				throw new BemParserException(String.format("Type %s: Expected %d attributes, but %d were found: %s, %s",
						entity.getTypeInfo().getName(), attributeInfos.size(), attributeValues.size(), attributeInfos, attributeValues));						
			}

		} catch (Exception e) {
			throw new BemParserException(String.format("Error parsing entity %s (line %s): %s", entity.toString(), entity.getLocalId(),
					e.getMessage()), e);
		}
	}	

}
