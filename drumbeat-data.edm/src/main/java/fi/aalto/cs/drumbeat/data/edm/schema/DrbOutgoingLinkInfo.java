package fi.aalto.cs.drumbeat.data.edm.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity's outgoing link is an attribute the value of which is another entity or a list of entities.
 * Each outgoing link may have different inverse links (from different entity types).
 * 
 * @author Nam Vu
 *
 */
public class DrbOutgoingLinkInfo extends DrbAttributeInfo {

	private List<DrbInverseLinkInfo> inverseLinkInfos;	

	public DrbOutgoingLinkInfo(DrbEntityTypeInfo sourceEntityTypeInfo, String name, DrbTypeInfo destinationTypeInfo) {
		super(sourceEntityTypeInfo, name, destinationTypeInfo);
	}
	
	public DrbEntityTypeInfo getSourceTypeInfo() {
		return getEntityTypeInfo();
	}
	
	public DrbTypeInfo getDestinationTypeInfo() {
		return getAttributeTypeInfo();
	}	
	
	public List<DrbInverseLinkInfo> getInverseLinkInfos() {
		return inverseLinkInfos;
	}

	public void addInverseLinkInfo(DrbInverseLinkInfo inverseLinkInfo) {
		if (inverseLinkInfos == null) {
			inverseLinkInfos = new ArrayList<>();
		}
		inverseLinkInfos.add(inverseLinkInfo);
	}
}
