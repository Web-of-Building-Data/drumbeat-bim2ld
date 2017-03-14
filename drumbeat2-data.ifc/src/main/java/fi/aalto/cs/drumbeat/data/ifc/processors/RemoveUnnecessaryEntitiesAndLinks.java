//package fi.aalto.cs.drumbeat.data.ifc.processors;
//
//import java.util.*;
//
//import org.apache.log4j.Logger;
//
//import fi.aalto.cs.drumbeat.common.string.StringUtils;
//import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
//import fi.aalto.cs.drumbeat.data.bem.dataset.*;
//import fi.aalto.cs.drumbeat.data.bem.processors.*;
//import fi.aalto.cs.drumbeat.data.bem.schema.*;
//
//
///**
// * Processor that removes all unnecessary entities and links.
// * 
// *  Sample syntax:
// *  
// *		<processor name="RemoveUnnecessaryEntitiesAndLinks" enabled="true">
// *			<class>fi.aalto.cs.drumbeat.ifc.processing.grounding.RemoveUnnecessaryEntitiesAndLinks</class>
// *			<params>
// *				<param name="entityTypeNames" value="type1, type2, ..." />
// *			</params>
// *		</processor>
// *  
// * @author vuhoan1
// *
// */
//public class RemoveUnnecessaryEntitiesAndLinks extends BemDatasetProcessor {
//	
//	private static final Logger logger = Logger.getLogger(RemoveUnnecessaryEntitiesAndLinks.class);	
//
//	private static final String PARAM_ENTITY_TYPE_NAMES = "entityTypeNames";
////	private static final String PARAM_RECURSIVE = "recursive";
//	
//	private List<BemEntityTypeInfo> entityTypeInfos;
//	private List<BemEntity> entitiesToRemove;
//	private int numberOfRemovedEntities;
//	private int numberOfRemovedLinks;
////	private boolean recursive;
//
//	public RemoveUnnecessaryEntitiesAndLinks(BemDatasetProcessorManager processorManager, Properties properties) {
//		super(processorManager, properties);
//	}
//
//	@Override
//	protected void initialize() throws BemDatasetProcessorException {
//		BemSchema schema = getProcessorManager().getSchema();
//		entityTypeInfos = new ArrayList<>();
//		String entityTypeNamesString = getProperties().getProperty(PARAM_ENTITY_TYPE_NAMES);
//		if (entityTypeNamesString != null) {
//			String[] tokens = entityTypeNamesString.split(StringUtils.COMMA);
//			try {
//				for (String token : tokens) {
//					String entityTypeName = token.trim();
//					logger.trace("Removing entity type: '" + entityTypeName + "'");
//					entityTypeInfos.add(schema.getEntityTypeInfo(entityTypeName));
//				}
//			} catch (BemNotFoundException e) {
//				throw new BemDatasetProcessorException(e.getMessage(), e);
//			}
//		} else {
//			throw new BemDatasetProcessorException(String.format("Parameter %s is undefined", PARAM_ENTITY_TYPE_NAMES));
//		}
//		
////		String recursiveString = getProperties().getProperty(PARAM_RECURSIVE);
////		BooleanParam recursiveParam = new BooleanParam();
////		recursiveParam.setStringValue(recursiveString);
////		recursive = recursiveParam.getValue();
//	}
//
//	@Override
//	public InputTypeEnum getInputType() {
//		return InputTypeEnum.UngroundedEntity;
//	}
//
//	@Override
//	protected boolean process(BemEntity entity) throws BemDatasetProcessorException {
//		for (BemEntityTypeInfo entityTypeInfo : entityTypeInfos) {
//			if (entity.isInstanceOf(entityTypeInfo)) {
//				logger.trace("Removing entity " + entity);
//				entitiesToRemove.add(entity);				
////				if (recursive) {
////					removeOutgoingLinks(entity);
////				}
//				return true;
//			}
//		}
//		return false;
//	}
//	
////	private void removeOutgoingLinks(BemEntity entity) {
////		for (IfcLink link : entity.getOutgoingLinks()) {
////			for (BemEntityBase linkedEntity : link.getDestinations()) {
////				if (linkedEntity instanceof BemEntity) {
////					entitiesToRemove.add((BemEntity)entity);
////					removeOutgoingLinks(entity);
////				}
////			}
////		}
////	}
//	
////	private void removeIncomingLinks(BemEntity entity) {
////		for (IfcLink link : entity.getIncomingLinks()) {
////			link.getDestinations().remove(entity);
////			if (link.get)
////		}
////	}
//
//	@Override
//	protected boolean checkNameDuplication() {
//		return false;
//	}
//
//	@Override
//	protected boolean allowNameDuplication() {
//		return false;
//	}
//	
//	public void preProcess() {
//		entitiesToRemove = new ArrayList<>();
//		super.preProcess();
//	}
//
//	@Override
//	public void postProcess() {
//		numberOfRemovedEntities = 0;
//		numberOfRemovedLinks = 0;
//
//		BemDataset dataset = getProcessorManager().getAnalyzer().getModel();
//		
//		for (BemEntity entity : entitiesToRemove) {
//			removeEntity(dataset, entity, true);
//		}
//		
//		logger.debug(String.format("\tTotal number of removed entities: %,d", numberOfRemovedEntities));
//		logger.debug(String.format("\tTotal number of removed links: %,d", numberOfRemovedLinks));
//		
//		super.postProcess();
//		
//	}
//	
//	private void removeEntity(BemDataset dataset, BemEntity entity, boolean forceRemovingIncomingLinks) {
////		logger.trace("Removing entity " + entity);
//		
//		if (forceRemovingIncomingLinks) {
//			
//			for (IfcLink incomingLink : entity.getIncomingLinks()) {
//				
//				BemEntity source = incomingLink.getSource();
//				IfcLinkInfo linkInfo = incomingLink.getLinkInfo();				
//				source.getOutgoingLinks().remove(linkInfo, entity);
//				
//				++numberOfRemovedLinks;
//				
//			}
//
//		} else {
//			assert (entity.getIncomingLinks().isEmpty()) : "Expected: entity.getIncomingLinks().isEmpty()";
//		}		
//	
//		for (IfcLink outgoingLink : entity.getOutgoingLinks()) {
//			
//			for (BemEntityBase d : outgoingLink.getDestinations()) {				
//				if (d instanceof BemEntity) {
//					BemEntity destination = (BemEntity)d;
//					List<IfcLink> incomingLinks = destination.getIncomingLinks();
//					for (int i = 0; i < incomingLinks.size(); ++i) {
//						IfcLink incomingLink = incomingLinks.get(i);
//						if (incomingLink.getSource().equals(entity) && incomingLink.getLinkInfo().equals(outgoingLink.getLinkInfo())) {
//							incomingLinks.remove(i);
//							++numberOfRemovedLinks;
//							break;
//						}
//					}
//					
//					if (incomingLinks.isEmpty()) {
//						removeEntity(dataset, destination, false);
//					}
//				}
//			}
//			
//		}			
//		
//		dataset.removeEntity(entity);
//		
//		++numberOfRemovedEntities;
//		
//		addEffectedEntityInfoForDebugging(entity);		
//	}
//
//}
