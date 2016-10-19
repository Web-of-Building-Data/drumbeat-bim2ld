package fi.aalto.cs.drumbeat.data.bedm.model;

import java.util.*;

import fi.aalto.cs.drumbeat.common.collections.IteratorEqualChecker;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.data.bedm.schema.*;


public class DrbEntity extends DrbNonCollectionValue implements Comparable<DrbEntity> {

	private static final long serialVersionUID = 1L;

	private DrbEntityTypeInfo typeInfo;
	private String localId;
	private String name;
	private String rawName;
	private DrbAttributeList<DrbLiteralAttribute> literalAttributes = new DrbAttributeList<>();
	private DrbAttributeList<DrbLink> outgoingLinks = new DrbAttributeList<>();
	private DrbAttributeList<DrbLink> incomingLinks = new DrbAttributeList<>();
	private List<DrbUniqueKeyValue> uniqueKeyValues;
	private DrbEntity parent;
	private boolean isSharedBlankNode;
	private boolean isLiteralValueContainer;
	private DrbEntity sameAsEntity;
	private String debugMessage;
	
	public DrbEntity(String localId) {
		this.localId = localId;
	}

	public DrbEntity(DrbEntityTypeInfo typeInfo, String localId) {
		this(localId);
		this.typeInfo = typeInfo;
	}

	public DrbEntityTypeInfo getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(DrbEntityTypeInfo typeInfo) {
		this.typeInfo = typeInfo;
	}


	public String getLocalId() {
		return localId;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean hasName() {
		return name != null;
	}

	/**
	 * @return the rawName
	 */
	public String getRawName() {
		return rawName != null ? rawName : name;
	}

	/**
	 * @param rawName the rawName to set
	 */
	public void setRawName(String rawName) {
		this.rawName = rawName;
	}

//	public void setAttributes(List<DrbAttribute> attributes) {
////		List<DrbAttributeInfo> inheritedAttributeInfos = typeInfo.getInheritedAttributeInfos();
//		for (int i = 0; i < attributes.size(); ++i) {
//			DrbAttribute attribute = attributes.get(i);
//			if (attribute instanceof DrbLink) {
//				addOutgoingLink((DrbLink) attribute);
//			} else {
//				assert attribute instanceof DrbLiteralAttribute;
//				addLiteralAttribute((DrbLiteralAttribute) attribute);
//			}
//		}
//	}

//	
//	public DrbAttribute getAttribute(DrbAttributeInfo attributeInfo) {
//		DrbAttribute attribute = literalAttributes.get(attributeInfo);
//		if (attribute == null) {
//			attribute = outgoingLinks.get(attributeInfo);
//		}
//		return attribute;
//	}

	public DrbAttributeList<DrbLiteralAttribute> getLiteralAttributes() {
		return literalAttributes;
	}
	
	public void addLiteralAttribute(DrbLiteralAttribute literalAttribute) {
		literalAttributes.add(literalAttribute);
	}	

//	public List<DrbLiteralAttribute> getLiteralAttributes(DrbAttributeInfo attributeInfo) {
//		return literalAttributes.selectAll(attributeInfo);
//	}

	public DrbAttributeList<DrbLink> getOutgoingLinks() {
		return outgoingLinks;
	}	

	public void addOutgoingLink(DrbLink link) {
		
		assert(link != null);

		outgoingLinks.add(link);
	}
	
	public DrbLink getOutgoingLink(DrbOutgoingLinkInfo linkInfo) {
		for (DrbLink link : outgoingLinks) {
			if (link.getLinkInfo().equals(linkInfo)) {
				return link;
			}
		}
		return null;
	}
	
	public void bindInverseLinks() {
		
		for (DrbLink link : outgoingLinks) {		
			//
			// bind link with inverse link (if any)
			//
			DrbOutgoingLinkInfo linkInfo = link.getLinkInfo();

			// Bind inverse outgoingLinks with direct outgoingLinks
			for (DrbEntity destination : link.getDestinations()) {

				if (destination instanceof DrbEntity) {
					List<DrbInverseLinkInfo> inverseLinkInfos = linkInfo
							.getInverseLinkInfos();

					DrbInverseLinkInfo inverseLinkInfo = findInverseLinkInfo(
							(DrbEntity) destination, inverseLinkInfos);
					if (inverseLinkInfo != null) {
						link.setInverseLinkInfo(inverseLinkInfo);
					}

					((DrbEntity) destination).addIncomingLink(link);
				}

			}
		}
		
	}
	
	private void addIncomingLink(DrbLink link) {
		incomingLinks.add(link); 
	}
	
	private DrbInverseLinkInfo findInverseLinkInfo(DrbEntity destination, List<DrbInverseLinkInfo> inverseLinkInfos) {

		if (inverseLinkInfos != null) {
			if (inverseLinkInfos.size() == 1) {
				DrbInverseLinkInfo inverseLinkInfo = inverseLinkInfos.get(0);
				DrbEntityTypeInfo destionationEntityTypeInfo = inverseLinkInfo.getDestinationEntityTypeInfo();
				if (destination.isInstanceOf(destionationEntityTypeInfo)) {
					return inverseLinkInfo;
				}
			} else {
				for (DrbInverseLinkInfo inverseLinkInfo : inverseLinkInfos) {
					DrbEntityTypeInfo destionationEntityTypeInfo = inverseLinkInfo.getDestinationEntityTypeInfo();
					if (destination.isInstanceOf(destionationEntityTypeInfo)) {
						return inverseLinkInfo;
					}
				}
			}
		}
		return null;
	}
	
	public DrbAttributeList<DrbLink> getIncomingLinks() {
		return incomingLinks;
	}
	
	@Override
	public int compareTo(DrbEntity o) {
		int compare = localId.compareTo(o.localId);
//		int compare = Long.compare(localId, o.localId);
		if (compare == 0) {
			compare = typeInfo.compareTo(o.typeInfo);
		}
		return compare;
	}

	public boolean isInstanceOf(DrbEntityTypeInfo typeInfo) {
		assert(this.typeInfo != null) : "typeInfo is null";
		return this.typeInfo.isTypeOf(typeInfo);
	}
	
	@Override
	public Boolean isLiteralType() {
		return Boolean.FALSE;
	}
	
	public List<DrbUniqueKeyValue> getUniqueKeyValues() {
		if (uniqueKeyValues == null) {
			uniqueKeyValues = getUniqueKeyValues(typeInfo.getUniqueKeyInfos());
		}
		return uniqueKeyValues;
	}
	
	public List<DrbUniqueKeyValue> getUniqueKeyValues(List<DrbUniqueKeyInfo> uniqueKeyInfos) {		
		List<DrbUniqueKeyValue> uniqueKeyValues = new ArrayList<>();
		if (uniqueKeyInfos != null) { 
			for (DrbUniqueKeyInfo uniqueKeyInfo : uniqueKeyInfos) {
				uniqueKeyValues.add(getUniqueKeyValue(uniqueKeyInfo));
			}
		}
		return uniqueKeyValues;
	}
	
	public DrbUniqueKeyValue getUniqueKeyValue(DrbUniqueKeyInfo uniqueKeyInfo) {
		DrbUniqueKeyValue uniqueKeyValue = new DrbUniqueKeyValue();
		for (DrbAttributeInfo attributeInfo : uniqueKeyInfo.getAttributeInfos()) {
			DrbLiteralAttribute attribute = literalAttributes.selectFirst(attributeInfo);
			if (attribute != null) {
				uniqueKeyValue.addValue(attributeInfo, attribute.getValue());
			} else {
				assert(attributeInfo.isOptional());
//				logger.warn(String.format("Unique literal attribute not found: %s.%s", getTypeInfo(), attributeInfo));
				uniqueKeyValue.addValue(attributeInfo, DrbValue.NULL);				
			}
		}
		return uniqueKeyValue;
	}
	
	public DrbEntity getParent() {
		return parent;
	}

	public void setParent(DrbEntity parent) {
		this.parent = parent;
	}

	public boolean isSharedBlankNode() {
		return isSharedBlankNode;
	}
	
	public void setSharedBlankNode(boolean isSharedBlankNode) {
		this.isSharedBlankNode = isSharedBlankNode;
	}

	/**
	 * @return the isLiteralValueContainer
	 */
	public boolean isLiteralValueContainer() {
		return isLiteralValueContainer;
	}

	/**
	 * @param isLiteralValueContainer the isLiteralValueContainer to set
	 */
	public void setLiteralValueContainer(boolean isLiteralValueContainer) {
		this.isLiteralValueContainer = isLiteralValueContainer;
	}
	
	/**
	 * @return the isDuplicated
	 */
	public boolean isDuplicated() {
		return sameAsEntity != null;
	}
	
	public void setSameAs(DrbEntity other) {
		this.sameAsEntity = other;
		
		for (DrbLink incomingLink : incomingLinks) {
			List<DrbEntityBase> destinations = incomingLink.getDestinations();
			int index = destinations.indexOf(this);
			assert(index >= 0) : "Expected: index >= 0";
			destinations.remove(index);
			destinations.add(index, other);
			other.addIncomingLink(incomingLink);
		}
		
	}
	
	public DrbEntity getSameAsEntity() {
		return sameAsEntity;
	}
	
	public void appendDebugMessage(String s) {
		if (debugMessage != null) {
			debugMessage = debugMessage + StringUtils.SEMICOLON + s;
		} else {
			debugMessage = s;
		}
	}
	
	public String getDebugMessage() {
		return debugMessage;
	}

	/**
	 * Checks if two entities have the same attribute values
	 * @param other
	 * @return
	 */
	public boolean isIdenticalTo(DrbEntityBase other1) {
		if (!(other1 instanceof DrbEntity)) {
			return false;
		}
		
		DrbEntity other = (DrbEntity)other1;
		
		//
		// compare types
		//		
		if (!typeInfo.equals(other.typeInfo)) {
			return false;
		}
		
		//
		// compare literal attributes
		//
		if (!IteratorEqualChecker.areEqual(literalAttributes, other.literalAttributes)) {
			return false;
		}
		
		//
		// compare outgoing links
		//
		Iterator<DrbLink> outgoingLinkIterator1 = outgoingLinks.iterator();
		Iterator<DrbLink> outgoingLinkIterator2 = other.outgoingLinks.iterator();
		
		while (outgoingLinkIterator1.hasNext()) {
			if (!outgoingLinkIterator2.hasNext()) {
				return false;
			}
			
			Iterator<DrbEntityBase> destinations1 = outgoingLinkIterator1.next().getDestinations().iterator();
			Iterator<DrbEntityBase> destinations2 = outgoingLinkIterator2.next().getDestinations().iterator();
			
			while (destinations1.hasNext()) {
				if (!destinations2.hasNext()) {
					return false;
				}
				
				DrbEntityBase destination1 = destinations1.next();
				DrbEntityBase destination2 = destinations2.next();				
				
				if (!destination1.equals(destination2) && !destination1.isIdenticalTo(destination2)) {
					return false;
				}				
			}
			
			if (destinations2.hasNext()) {
				return false;
			}			
			
		}
		
		if (outgoingLinkIterator2.hasNext()) {
			return false;
		}

		
		return true;
	}

	@Override
	public String toString() {
//		assert (typeInfo != null) : "Undefined entity type, localId=" + localId;
		return DrbValueFormatter.formatEntityName(typeInfo, localId);
	}

	@Override
	public boolean equals(Object o) {
		return ((DrbEntity)o).localId == localId;
	}
	
	@Override
	public int hashCode() {		
		return localId.hashCode();
	}

//	@Override
//	public List<? extends IRdfTriple> getIncomingRdfTriples() {
//		return incomingLinks;
//	}
//	
//	@Override
//	public IRdfNode getRdfClass() {
//		return typeInfo;
//	}
	
	
}
