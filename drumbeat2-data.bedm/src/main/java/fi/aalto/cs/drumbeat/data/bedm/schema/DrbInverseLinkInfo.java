package fi.aalto.cs.drumbeat.data.bedm.schema;

/**
 * An entity's inverse link is a link to another entity. An inverse link is inverse to some outgoing link of that entity.  
 * 
 * @author Nam Vu
 *
 */
public class DrbInverseLinkInfo extends DrbAttributeInfo {

	private DrbOutgoingLinkInfo outgoingLinkInfo;
	private DrbCardinality cardinality;

	public DrbInverseLinkInfo(DrbEntityTypeInfo entityTypeInfo, String name,
			DrbEntityTypeInfo linkSourceEntityTypeInfo,
			DrbOutgoingLinkInfo outgoingLinkInfo) {
		super(entityTypeInfo, name, linkSourceEntityTypeInfo);
		this.outgoingLinkInfo = outgoingLinkInfo;
		outgoingLinkInfo.addInverseLinkInfo(this);
	}

	public DrbEntityTypeInfo getSourceEntityTypeInfo() {
		return (DrbEntityTypeInfo) getAttributeTypeInfo();
	}

	public DrbOutgoingLinkInfo getOutgoingLinkInfo() {
		return outgoingLinkInfo;
	}

	public DrbEntityTypeInfo getDestinationEntityTypeInfo() {
		return getEntityTypeInfo();
	}

	public boolean isInverseTo(DrbOutgoingLinkInfo outgoingLinkInfo) {
		return this.outgoingLinkInfo.equals(outgoingLinkInfo);
	}

	public DrbCardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(DrbCardinality cardinality) {
		this.cardinality = cardinality;
	}

}
