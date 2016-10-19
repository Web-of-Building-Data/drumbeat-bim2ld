package fi.aalto.cs.drumbeat.data.bedm.model;

import java.util.List;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbEntityTypeInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbInverseLinkInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbOutgoingLinkInfo;


public class DrbLink extends DrbAttribute { // implements IRdfTriple {
	
	private static final long serialVersionUID = 1L;

	protected DrbEntity source;
	protected DrbInverseLinkInfo inverseLinkInfo;
	private boolean useInverseLink;
	
	public DrbLink(DrbOutgoingLinkInfo linkInfo, int attributeIndex, DrbEntity source, DrbEntity destination) {
		super(linkInfo, attributeIndex, destination);
		this.source = source;
	}
	
	public DrbLink(DrbOutgoingLinkInfo linkInfo, int attributeIndex, DrbEntity source, DrbEntityCollection destinations) {
		super(linkInfo, attributeIndex, destinations);
		this.source = source;
	}
	
	public DrbEntity getSource() {
		return source;
	}
	
	public DrbEntity getSingleDestination() {
		return (DrbEntity)getValue(); 
	}
	
	public List<DrbEntityBase> getDestinations() {
		return super.getSingleValues();
	}
	
	public DrbOutgoingLinkInfo getLinkInfo() {
		return (DrbOutgoingLinkInfo)getAttributeInfo();
	}
	
	public DrbInverseLinkInfo getInverseLinkInfo() {
		return inverseLinkInfo;
	}

	public void setInverseLinkInfo(DrbInverseLinkInfo inverseLinkInfo) {
		this.inverseLinkInfo = inverseLinkInfo;
	}

	@Override
	public boolean isLiteralType() {
		return false;
	}
	
	/**
	 * @return the useInverseLink
	 */
	public boolean isUseInverseLink() {
		return useInverseLink;
	}

	/**
	 * @param useInverseLink the useInverseLink to set
	 */
	public void setUseInverseLink(boolean useInverseLink) {
		this.useInverseLink = useInverseLink;
	}

//	@Override
//	public IRdfNode getRdfSubject() {
//		return source;
//	}

}
