package fi.aalto.cs.drumbeat.ifc.processing.grounding;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import fi.hut.cs.drumbeat.ifc.processing.IfcAnalyserException;
import fi.hut.cs.drumbeat.ifc.processing.grounding.IfcGroundingMainProcessor;
import fi.hut.cs.drumbeat.ifc.processing.grounding.IfcGroundingProcessor;
import fi.hut.cs.drumbeat.ifc.processing.grounding.InputTypeEnum;
import fi.hut.cs.drumbeat.ifc.common.IfcNotFoundException;
import fi.hut.cs.drumbeat.ifc.data.model.IfcAttributeList;
import fi.hut.cs.drumbeat.ifc.data.model.IfcEntity;
import fi.hut.cs.drumbeat.ifc.data.model.IfcLink;
import fi.hut.cs.drumbeat.ifc.data.schema.IfcEntityTypeInfo;
import fi.hut.cs.drumbeat.ifc.data.schema.IfcInverseAttributeInfo;
import fi.hut.cs.drumbeat.ifc.data.schema.IfcAttributeInfo;
import fi.hut.cs.drumbeat.ifc.data.schema.IfcSchema;


/**
 * Replace outgoing links by their inverse ones.
 * 
 *  Sample syntax:
 *  
 *		<processor name="ReplaceByInverseLinks" enabled="true">
 *			<class>fi.hut.cs.drumbeat.ifc.util.grounding.ReplaceByInverseLinks</class>
 *			<params>
 *				<param name="inverseLinkNames" value="type1.link1, type2.link2, ..." />
 *			</params>
 *		</processor>
 *  
 * @author vuhoan1
 *
 */
public class ReplaceByInverseLinks extends IfcGroundingProcessor {

	private static final String PARAM_INVERSE_LINK_NAMES = "inverseLinkNames";
	
	private Map<IfcEntityTypeInfo, IfcInverseAttributeInfo> inverseAttributeInfoMap;
	
	public ReplaceByInverseLinks(IfcGroundingMainProcessor mainProcessor, Properties properties) {
		super(mainProcessor, properties);
	}

	@Override
	public InputTypeEnum getInputType() {
		return InputTypeEnum.Any;
	}

	@Override
	void initialize() throws IfcAnalyserException {

		inverseAttributeInfoMap = new TreeMap<>();
		
		String inverseLinkNameString = getProperties().getProperty(PARAM_INVERSE_LINK_NAMES);
		
		if (inverseLinkNameString == null) {
			throw new IfcAnalyserException(String.format("Param %s is undefined", PARAM_INVERSE_LINK_NAMES));
		}
		
		IfcSchema schema = getMainProcessor().getAnalyzer().getModel().getSchema();

		for (String subInverseLinkNameString : inverseLinkNameString.split(",")) {			
			String[] tokens = subInverseLinkNameString.split("\\.");
			IfcEntityTypeInfo entityTypeInfo;
			try {
				entityTypeInfo = schema.getEntityTypeInfo(tokens[0].trim());
			} catch (IfcNotFoundException e) {
				throw new IfcAnalyserException(String.format("Entity type not found: '%s'", tokens[0]));
			}
			
			String inverseLinkName = tokens[1];
			boolean found = false;
			for (IfcInverseAttributeInfo inverseAttributeInfo : entityTypeInfo.getInheritedInverseAttributeInfos()) {
				if (inverseAttributeInfo.getName().equalsIgnoreCase(inverseLinkName)) {
					inverseAttributeInfoMap.put(entityTypeInfo, inverseAttributeInfo);
					found = true;
					continue;
				}
			}
			
			if (!found) {
				throw new IfcAnalyserException(String.format("Inverse link not found: '%s'", subInverseLinkNameString));				
			}
		}
		
	}

	@Override
	boolean process(IfcEntity entity) throws IfcAnalyserException {
		
		for (IfcEntityTypeInfo entityTypeInfo : inverseAttributeInfoMap.keySet()) {
			if (entity.isInstanceOf(entityTypeInfo)) {
				IfcInverseAttributeInfo inverseAttributeInfo = inverseAttributeInfoMap.get(entityTypeInfo); 
				IfcAttributeInfo outgoingAttributeInfo = inverseAttributeInfo.getOutgoingAttributeInfo();
				List<IfcLink> incomingLinks = entity.getIncomingLinks(); 
				for (int i = 0; i < incomingLinks.size(); ++i) {
					IfcLink incomingLink = incomingLinks.get(i);
					if (incomingLink.getAttributeInfo().equals(outgoingAttributeInfo)) {
						IfcAttributeList<IfcLink> outgoingLinksOfSource = incomingLink.getSource().getOutgoingLinks();
						List<IfcLink> links = outgoingLinksOfSource.selectAll(outgoingAttributeInfo);
						if (links.size() == 1) {
							links.get(0).setUseInverseLink(true);
							return true;
						} else {
							throw new IfcAnalyserException(String.format("Incoming link %s is a multiple link", entityTypeInfo.getName(), outgoingAttributeInfo.getName()));
						}						
					}
				}
				
//				if (!inverseAttributeInfo.isOptional()) {
//					throw new IfcAnalyserException(String.format("Incoming link %s.%s-->%s is not found for entity %s",
//							incomingAttributeInfo.getEntityTypeInfo().getName(), incomingAttributeInfo.getName(), entityTypeInfo.getName(), entity));
//				}
			}
		}
		
		
		return false;
	}

	@Override
	boolean checkNameDuplication() {
		return false;
	}

	@Override
	boolean allowNameDuplication() {
		return false;
	}

}
