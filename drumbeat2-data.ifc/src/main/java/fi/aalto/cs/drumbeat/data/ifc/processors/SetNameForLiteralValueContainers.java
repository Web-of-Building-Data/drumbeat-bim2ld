//package fi.aalto.cs.drumbeat.data.ifc.processors;
//
//import java.util.Properties;
//import java.util.regex.Matcher;
//
//import fi.aalto.cs.drumbeat.data.bem.dataset.*;
//import fi.aalto.cs.drumbeat.data.bem.processors.*;
//import fi.aalto.cs.drumbeat.data.bem.schema.*;
//
//
//
///**
// * Processor that sets (and encodes) entity names for all literal-value-containers. Naming is based on their literal values.
// * 
// * 		<processor name="SetNameForLiteralValueContainers" enabled="true">
//			<class>fi.aalto.cs.drumbeat.ifc.util.grounding.SetNameForLiteralValueContainers</class>
//			<params>
//				<param name="propertyPattern" value="$Property.Name$:'$Property.Value$';" />
//				<param name="entityNamePattern" value="$Entity.Type${$propertyPattern$}" />
//				<param name="encoderType" value="MD5" />
//			</params>
//		</processor>
//
// * 
// * @author vuhoan1
// *
// */
//class SetNameForLiteralValueContainers extends BemDatasetProcessor {
//	
//	public static final String PARAM_PROPERTY_PATTERN = "propertyPattern";
//	public static final String FORMAT_VARIABLE_TYPE = Matcher.quoteReplacement("$Entity.Type$");
//	public static final String FORMAT_VARIABLE_PROPERTY = Matcher.quoteReplacement(String.format("$%s$", PARAM_PROPERTY_PATTERN));
//	public static final String FORMAT_VARIABLE_PROPERTY_NAME = Matcher.quoteReplacement("$Property.Name$");
//	public static final String FORMAT_VARIABLE_PROPERTY_VALUE = Matcher.quoteReplacement("$Property.Value$");
//	
//	private String entityNamePattern;
//	private String propertyPattern;	
//	
//	public SetNameForLiteralValueContainers(BemDatasetProcessorManager processor,
//			Properties properties) {
//		super(processor, properties);
//	}	
//	
//	@Override
//	protected void initialize() throws BemDatasetProcessorException {
//		entityNamePattern = getProperties().getProperty(PARAM_ENTITY_NAME_PATTERN)
//				.replaceAll(FORMAT_VARIABLE_TYPE, Matcher.quoteReplacement("%1$s"))
//				.replaceAll(FORMAT_VARIABLE_PROPERTY, Matcher.quoteReplacement("%2$s"));
//		
//		propertyPattern = getProperties().getProperty(PARAM_PROPERTY_PATTERN)
//				.replaceAll(FORMAT_VARIABLE_PROPERTY_NAME, Matcher.quoteReplacement("%1$s"))
//				.replaceAll(FORMAT_VARIABLE_PROPERTY_VALUE, Matcher.quoteReplacement("%2$s"));		
//	}
//
//	@Override
//	public boolean process(BemEntity entity) throws BemDatasetProcessorException {
//		
//		return internalProcess(entity, false);
//		
//	}
//	
//	private boolean internalProcess(BemEntity entity, boolean isAttributeOfSharedBlankNode) throws BemDatasetProcessorException {
//		
//		if (entity.hasName()) {
//			return true;
//		}
//		
//		if (entity.isLiteralValueContainer()) {
//			
//			StringBuilder propertyStringBuilder = new StringBuilder();
//			
//			for (IfcLiteralAttribute literalAttribute : entity.getLiteralAttributes()) {
//				propertyStringBuilder.append(String.format(propertyPattern, literalAttribute.getAttributeInfo().getName(), literalAttribute));
//			}
//
//			BemEntityTypeInfo entityTypeInfo = entity.getTypeInfo();			
//			String rawName = String.format(entityNamePattern, entityTypeInfo.getName(), propertyStringBuilder);			
//			trySetEntityName(entity, rawName);
//			
//			addEffectedEntityInfoForDebugging(entity);
//			
//			return true;
//			
//		}
//		
//		return false;		
//	}
//	
//	
//	
//	@Override
//	public InputTypeEnum getInputType() {
//		return InputTypeEnum.UngroundedEntity;
//	}
//
//	@Override
//	protected boolean checkNameDuplication() {
//		return true;
//	}
//
//	@Override
//	protected boolean allowNameDuplication() {
//		return true;
//	}	
//
//}
