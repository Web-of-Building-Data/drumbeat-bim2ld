//package fi.aalto.cs.drumbeat.data.ifc.processors;
//
//import java.util.List;
//import java.util.Properties;
//
//import org.apache.log4j.Logger;
//
//import fi.aalto.cs.drumbeat.data.bem.processors.*;
//import fi.aalto.cs.drumbeat.data.bem.schema.*;
//import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
//import fi.aalto.cs.drumbeat.data.bem.dataset.*;
//
//
//
///**
// * Replace outgoing links by their inverse ones.
// * 
// *  Sample syntax:
// *  
// *		<processor name="ReplaceByInverseLinks" enabled="true">
// *			<class>fi.aalto.cs.drumbeat.ifc.util.grounding.ReplaceByInverseLinks</class>
// *			<params>
// *				<param name="inverseLinkNames" value="type1.link1, type2.link2, ..." />
// *			</params>
// *		</processor>
// *  
// * @author vuhoan1
// *
// */
//public class ReplacePropertiesWithValueGlobalId extends BemDatasetProcessor {
//
//	private static final Logger logger = Logger.getLogger(ReplacePropertiesWithValueGlobalId.class);	
//
//	private static final String IFCPROPERTY_TYPE = "IfcPropertySingleValue";
//	private static final String IFCPROPERTY_NAME_ATTRIBUTE = "name";
//	private static final String IFCPROPERTY_NOMINALVALUE_ATTRIBUTE = "nominalValue";
//	
//	private BemEntityTypeInfo entityTypeInfo;
//	private BemAttributeInfo nameAttributeInfo;
//	private BemAttributeInfo valueAttributeInfo;	
//	
//	public ReplacePropertiesWithValueGlobalId(BemDatasetProcessorManager processorManager, Properties properties) {
//		super(processorManager, properties);
//	}
//
//	@Override
//	public InputTypeEnum getInputType() {
//		return InputTypeEnum.UngroundedEntity;
//	}
//
//	@Override
//	protected void initialize() throws BemDatasetProcessorException {
//
//		BemSchema schema = getProcessorManager().getAnalyzer().getModel().getSchema();		
//		
//		try {
//			entityTypeInfo = schema.getEntityTypeInfo(IFCPROPERTY_TYPE);
//			
//			final List<BemAttributeInfo> inheritedAttributeInfos = entityTypeInfo.getInheritedAttributeInfos();
//			
//			logger.debug("Type: " + entityTypeInfo.getName());
//
//			for (BemAttributeInfo attributeInfo : inheritedAttributeInfos) {
//				String attributeName = attributeInfo.getName();
//				
//				logger.debug(((attributeInfo instanceof IfcLinkInfo) ?
//						"   Link: " : "   Attribute: ") + attributeName);
//				if (attributeName.equals(IFCPROPERTY_NAME_ATTRIBUTE)) {
//					nameAttributeInfo = attributeInfo;
//				} else if (attributeName.equals(IFCPROPERTY_NOMINALVALUE_ATTRIBUTE)) {
//					valueAttributeInfo = attributeInfo;
//				}
//			}
//			
//			if (nameAttributeInfo == null) {
//				throw new BemNotFoundException(IFCPROPERTY_NAME_ATTRIBUTE);
//			}
//			
//			if (valueAttributeInfo == null) {
//				throw new BemNotFoundException(IFCPROPERTY_NOMINALVALUE_ATTRIBUTE);
//			}
//
//		} catch (BemNotFoundException e) {
//			throw new BemDatasetProcessorException("Couldn't find property type or attribute: " + e.getMessage());
//		}
//	}
//
//	@Override
//	protected boolean process(BemEntity entity) throws BemDatasetProcessorException {
//		
//		if (entity.isInstanceOf(entityTypeInfo)) {
//			logger.debug("Testing object " + entity);			
//			
//			IfcLiteralAttribute nameAttribute = entity.getLiteralAttributes().getFirst();
//			if (!nameAttribute.getAttributeInfo().equals(nameAttributeInfo)) {
//				throw new BemDatasetProcessorException("Expected attribute: " + nameAttributeInfo.getName() +
//						"\tActual attribute: " + nameAttribute.getAttributeInfo().getName());
//			}
//			
//			if (nameAttribute.getValue().toString().toUpperCase().endsWith("GUID")) {
//				logger.debug("   name: " + nameAttribute.getValue().toString());
//				
//				if (!entity.getOutgoingLinks().isEmpty()) {
//					IfcLink oldLink = entity.getOutgoingLinks().getFirst();
//					if (!oldLink.getAttributeInfo().equals(valueAttributeInfo)) {
//						throw new BemDatasetProcessorException("Expected link: " + valueAttributeInfo.getName());
//					}		
//					
//					IfcLiteralValue value = (IfcLiteralValue)oldLink.getValue();
//					String oldGuid = (String)value.getValue();
//					String newGuid = GuidCompressor.uncompressGuidString(oldGuid);
//					
//					value.setValue(newGuid);
//					
////					IfcShortEntity newValue = new IfcShortEntity(oldValue.getTypeInfo(),
////							new IfcLiteralValue(newGuid, IfcTypeEnum.STRING));
////					
////					IfcLink newLink = new IfcLink(oldLink.getLinkInfo(), oldLink.getIndex(), oldLink.getSource(), newValue);
////					
////					entity.getOutgoingLinks().set(0, newLink);
//					
//					logger.debug("Replaced");
//				}
//			}
//			
//		}
//		
//		
//
//		
//		return false;
//		
//	}
//
//	@Override
//	boolean checkNameDuplication() {
//		return false;
//	}
//
//	@Override
//	boolean allowNameDuplication() {
//		return false;
//	}
//
//}
