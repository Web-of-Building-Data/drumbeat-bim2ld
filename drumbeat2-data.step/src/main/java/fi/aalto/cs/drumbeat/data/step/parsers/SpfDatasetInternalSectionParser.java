package fi.aalto.cs.drumbeat.data.step.parsers;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.StrMatcher;
import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.common.string.RegexUtils;
import fi.aalto.cs.drumbeat.common.string.StrBuilderWrapper;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.data.bem.BemEntityNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.BemTypeNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.dataset.*;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemFormatException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemParserException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

class SpfDatasetInternalSectionParser {
	
	private class StepTemporaryCollectionValueWrapper extends BemValue {
		
		private List<BemValue> values;

		public StepTemporaryCollectionValueWrapper(List<BemValue> values) {
			this.values = values;
		}
		
		public List<BemValue> getValues() {
			return values;
		}
	}
	
	
	
	private static final Logger logger = Logger.getLogger(StepDatasetParser.class);	

	private StepLineReader reader;
	private StepDatasetBuilder builder; 
	private ExpressSchema schema;

	private Map<String, BemEntity> entityMap = new HashMap<>(); 	// map of entities indexed by line numbers
	
	
	/**
	 * Reads line by line and creates new entities
	 * @throws IOException
	 * @throws BemParserException
	 * @throws BemEntityNotFoundException 
	 * @throws BemTypeNotFoundException 
	 * @throws BemNotFoundException 
	 */
	public List<BemEntity> parseEntities(StepLineReader reader, StepDatasetBuilder builder, ExpressSchema schema, boolean isHeaderSection, boolean ignoreUnknownTypes) throws IOException, BemParserException, BemTypeNotFoundException, BemEntityNotFoundException {		
		
		this.reader = reader;
		this.builder = builder; 
		this.schema = schema;
		
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
			} catch (BemTypeNotFoundException e) {
				if (ignoreUnknownTypes) {
					continue;
				} else {
					throw e;
				}
			}
			entity.setTypeInfo(entityTypeInfo);
			
			entityAttributesString = entityAttributesString.substring(indexOfOpeningBracket + 1,
					entityAttributesString.length() - 1);
			
			List<BemAttributeInfo> attributeInfos = entityTypeInfo.getAttributeInfos(true);
			BemAttributeInfo[] attributeInfoArray = new BemAttributeInfo[attributeInfos.size()];
			attributeInfos.toArray(attributeInfoArray);
					
			//
			// parse attribute string to get attribute values
			//
			List<BemValue> attributeValues = parseAttributeValues(new StrBuilderWrapper(entityAttributesString), entity, false, null, attributeInfoArray);

			assignEntityAttributeValues(entity, attributeInfos, attributeValues);
			
			//
			// add entity to the model
			//
			entities.add(entity);
						
		}
		
		if (!isHeaderSection) {
			bindInverseLinks(entities);
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
	 * @throws BemEntityNotFoundException 
	 * @throws BemParserException 
	 * @throws BemNotFoundException
	 * @throws BemValueTypeConflictException 
	 */
	private List<BemValue> parseAttributeValues(StrBuilderWrapper attributeStrBuilderWrapper, BemEntity entity,
			boolean areCollectionItems, BemTypeInfo fixedValueTypeInfo, BemAttributeInfo... entityAttributeInfos) throws BemTypeNotFoundException, BemEntityNotFoundException, BemParserException {

		logger.debug(String.format("Parsing entity '%s'", entity));

		List<BemValue> attributeValues = new ArrayList<>();
		
		for (int attributeIndex = 0; !attributeStrBuilderWrapper.trimLeft().isEmpty(); ++attributeIndex) {
			
			BemTypeInfo valueTypeInfo;

			BemAttributeInfo attributeInfo;
			if (!areCollectionItems) {
				assert(attributeIndex < entityAttributeInfos.length) :
					String.format("attributeIndex=%d, entityAttributeInfos.size=%s, attributeStrBuilderWrapper='%s'",
							attributeIndex,
							entityAttributeInfos,
							attributeStrBuilderWrapper);
				attributeInfo = entityAttributeInfos[attributeIndex];
				
				valueTypeInfo = fixedValueTypeInfo != null ? fixedValueTypeInfo : attributeInfo.getValueTypeInfo();
			} else {
				assert(fixedValueTypeInfo != null);
				valueTypeInfo = fixedValueTypeInfo;
				attributeInfo = entityAttributeInfos[0];
			}
			
			if (valueTypeInfo instanceof BemCollectionTypeInfo) {
				valueTypeInfo = ((BemCollectionTypeInfo)valueTypeInfo).getItemTypeInfo();
			}

			switch (attributeStrBuilderWrapper.charAt(0)) {

			case StepVocabulary.SpfFormat.LINE_NUMBER_SYMBOL: // Entity
				attributeStrBuilderWrapper.skip(1);
				//Long remoteLineNumber = attributeStrBuilderWrapper.getLong();
				String remoteLineNumber = Long.toString(attributeStrBuilderWrapper.getLong());
				BemEntity remoteEntity = getEntity(remoteLineNumber);
				if (remoteEntity == null) {
					throw new BemEntityNotFoundException("Entity not found: #" + remoteLineNumber);
				}
				attributeValues.add(remoteEntity);
				break;

			case StepVocabulary.SpfFormat.STRING_VALUE_SYMBOL:
				String s = attributeStrBuilderWrapper.getStringBetweenSingleQuotes();
//				if (!attributeValueTypes.contains(StepTypeEnum.GUID)) {
					attributeValues.add(builder.createPrimitiveValue(s, BemValueKindEnum.STRING));
//					break;
//				} else {
//					attributeValues.add(new StepGuidValue(s));
//					break;
//				}
				break;

			case StepVocabulary.SpfFormat.ENUMERATION_VALUE_SYMBOL:
				
				BemValue value = null;

				s = attributeStrBuilderWrapper.getStringBetweenSimilarCharacters(StepVocabulary.SpfFormat.ENUMERATION_VALUE_SYMBOL);
				
				if (valueTypeInfo.getValueKind() == BemValueKindEnum.ENUM) {
					value = new BemEnumerationValue(s);						
				} else if (valueTypeInfo.getValueKind() == BemValueKindEnum.LOGICAL) {
					switch (s) {
					case StepVocabulary.StepValues.T:
					case StepVocabulary.StepValues.TRUE:
						value = builder.createLogicalValue(BemLogicalEnum.TRUE);
						break;
					case StepVocabulary.StepValues.F:
					case StepVocabulary.StepValues.FALSE:
						value = builder.createLogicalValue(BemLogicalEnum.FALSE);
						break;
					case StepVocabulary.StepValues.U:
					case StepVocabulary.StepValues.UNKNOWN:
						value = builder.createLogicalValue(BemLogicalEnum.UNKNOWN);
						break;
					}
				} else {
					throw new BemFormatException(reader.getCurrentLineNumber(), "Expected enum type or logical type");
				}
				
				if (value != null) {
					attributeValues.add(value);
				} else {
					throw new BemFormatException(reader.getCurrentLineNumber(), "Unknown enum/logical value: " + value);
				}
				break;

			case StepVocabulary.SpfFormat.NULL_SYMBOL: // $
				attributeValues.add(BemSpecialValue.NULL);
				attributeStrBuilderWrapper.skip(1);
				break;

			case StepVocabulary.SpfFormat.ANY_SYMBOL: // *
				attributeValues.add(BemSpecialValue.ANY);
				attributeStrBuilderWrapper.skip(1);
				break;

			case StringUtils.OPENING_ROUND_BRACKET_CHAR: // List or Set

				String stringBetweenBrackets = attributeStrBuilderWrapper.getStringBetweenRoundBrackets();

				StrBuilderWrapper sbWrapper = new StrBuilderWrapper(stringBetweenBrackets);
				
				List<BemValue> values = parseAttributeValues(sbWrapper, null, true, valueTypeInfo, attributeInfo);
				attributeValues.add(new StepTemporaryCollectionValueWrapper(values));
				break;

			default:

				if (Character.isAlphabetic(attributeStrBuilderWrapper.charAt(0))) {
					
					// 
					// Parsing SELECT type value, e.g. IfcLength(130.5)
					//
					assert(valueTypeInfo instanceof BemSelectTypeInfo);
					
					// 
					// parsing sub entity
					//
					String subEntityTypeInfoName = attributeStrBuilderWrapper.getIdentifierName();
					BemTypeInfo subNonEntityTypeInfo = schema.getTypeInfo(subEntityTypeInfoName);
					assert(!(subNonEntityTypeInfo instanceof BemSelectTypeInfo));
					
					s = attributeStrBuilderWrapper.getStringBetweenRoundBrackets();
					
					assert (s != null);

					values = parseAttributeValues(new StrBuilderWrapper(s), null, false, subNonEntityTypeInfo, attributeInfo);
					
					if (values.size() != 1) {
						throw new BemParserException("Expect only 1 argument: " + entity + ":" + values.toString());
					}
					
					value = values.get(0);
					
					if (!(value instanceof BemSimpleValue)) {
						throw new BemParserException(
								String.format(
										"Expected a BemSimpleValue: %s (class: %s, typeInfo: %s)",
										values.get(0),
										values.get(0).getClass(),
										subNonEntityTypeInfo));						
					}
					
					attributeValues.add(builder.createTypedSimpleValue((BemSimpleValue)value, subNonEntityTypeInfo));
//					attributeValues.add((BemPrimitiveValue)values.get(0));
				} else {
					
					//
					// parsing number or datetime
					//
					assert(!(valueTypeInfo instanceof BemSelectTypeInfo));

					BemValueKindEnum valueKind = valueTypeInfo.getValueKind();
					assert(BemValueKindEnum.PRIMITIVE.contains(valueKind)) : valueTypeInfo.getValueKind();
					
					Object primitiveValue;
					if (valueKind == BemValueKindEnum.INTEGER) {
						primitiveValue = attributeStrBuilderWrapper.getLong();
					} else if (valueKind == BemValueKindEnum.REAL || valueKind == BemValueKindEnum.NUMBER) {
						primitiveValue = attributeStrBuilderWrapper.getDouble();
					} else if (valueKind == BemValueKindEnum.DATETIME) {
						long timeStamp = attributeStrBuilderWrapper.getLong();
						primitiveValue = Calendar.getInstance();
						((Calendar)primitiveValue).setTimeInMillis(timeStamp * 1000);
					} else {
						throw new BemFormatException(reader.getCurrentLineNumber(), "Invalid attributeValueType: " + valueKind);
					}
					
					attributeValues.add(builder.createPrimitiveValue(primitiveValue, valueKind));						
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
	private void assignEntityAttributeValues(BemEntity entity, List<BemAttributeInfo> attributeInfos, List<BemValue> attributeValues) throws BemParserException {
		if (attributeValues.size() != attributeInfos.size()) {
			throw new BemParserException(String.format("Type %s: Expected %d attributes, but %d were found: %s, %s",
					entity.getTypeInfo().getName(), attributeInfos.size(), attributeValues.size(), attributeInfos, attributeValues));						
		}

		for (int attributeIndex = 0; attributeIndex < attributeValues.size(); ++attributeIndex) {
			
			BemAttributeInfo attributeInfo = attributeInfos.get(attributeIndex);
			BemTypeInfo attributeValueTypeInfo = attributeInfo.getValueTypeInfo();
			BemValue attributeValue = attributeValues.get(attributeIndex);
			
			if (attributeValue instanceof StepTemporaryCollectionValueWrapper) {
				
				if ((attributeValueTypeInfo instanceof BemCollectionTypeInfo) && 
						((BemCollectionTypeInfo)attributeValueTypeInfo).isSorted())
				{
					// sorted collection
					BemCollectionValue<BemValue> valueCollection = new BemCollectionValue<>();						
					for (BemValue value : ((StepTemporaryCollectionValueWrapper)attributeValue).getValues()) {
						valueCollection.add(value);
					}
					entity.getAttributeMap().add(attributeInfo, valueCollection);
				} else {
					// unsorted collection
					for (BemValue value : ((StepTemporaryCollectionValueWrapper)attributeValue).getValues()) {
						entity.getAttributeMap().add(attributeInfo, value);
					}
				}
				
			} else {
				
				// single attribute				
				entity.getAttributeMap().add(attributeInfo, attributeValue);					
				
			}
			
		}
				
	}
	
	
	private void bindInverseLinks(List<BemEntity> entities) {
		for (BemEntity sourceEntity : entities) {
			for (Entry<BemAttributeInfo, BemValue> entry : sourceEntity.getAttributeMap().entries()) {
				BemAttributeInfo attributeInfo = entry.getKey();				
				BemValue attributeValue = entry.getValue();
				if (attributeValue instanceof BemEntity) {
					List<BemInverseAttributeInfo> possibleInverseAttributeInfos = attributeInfo.getPossibleInverseAttributeInfos();
					if (possibleInverseAttributeInfos != null && !possibleInverseAttributeInfos.isEmpty()) {					
						BemEntity destinationEntity = (BemEntity)attributeValue;
						for (BemInverseAttributeInfo inverseAttributeInto : possibleInverseAttributeInfos) {
							if (destinationEntity.isInstanceOf(inverseAttributeInto.getDestinationEntityTypeInfo())) {
								destinationEntity.getIncomingAttributeMap().add(inverseAttributeInto, sourceEntity);
								break;
							}
						}
					}
				}
			}
		}
	}

}
