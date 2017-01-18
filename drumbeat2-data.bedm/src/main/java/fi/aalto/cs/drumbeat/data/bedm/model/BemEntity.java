package fi.aalto.cs.drumbeat.data.bem.model;

import java.util.*;

import org.apache.commons.lang3.NotImplementedException;

import fi.aalto.cs.drumbeat.common.collections.IteratorEqualChecker;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.data.bem.schema.*;


public class BemEntity extends BemNonCollectionValue implements Comparable<BemEntity> {

	private BemEntityTypeInfo typeInfo;
	private String localId;
	private String name;
	private String rawName;
	private BemAttributeList attributes = new BemAttributeList();
	private BemAttributeList incomingLinks = new BemAttributeList();
//	private BemAttributeList<BemInverseAttribute> inverseAttributes = new BemAttributeList<>();
	private List<BemUniqueKeyValue> uniqueKeyValues;
	private BemEntity parent;
	private boolean isSharedBlankNode;
	private boolean isLiteralValueContainer;
	private BemEntity sameAsEntity;
	private String debugMessage;
	
	public BemEntity(String localId) {
		this.localId = localId;
	}

	public BemEntity(BemEntityTypeInfo typeInfo, String localId) {
		this(localId);
		this.typeInfo = typeInfo;
	}

	public BemEntityTypeInfo getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(BemEntityTypeInfo typeInfo) {
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

//	public void setAttributes(List<BemAttribute> attributes) {
////		List<BemAttributeInfo> inheritedAttributeInfos = typeInfo.getInheritedAttributeInfos();
//		for (int i = 0; i < attributes.size(); ++i) {
//			BemAttribute attribute = attributes.get(i);
//			if (attribute instanceof BemLink) {
//				addOutgoingLink((BemLink) attribute);
//			} else {
//				assert attribute instanceof BemAttribute;
//				addAttribute((BemAttribute) attribute);
//			}
//		}
//	}

//	
//	public BemAttribute getAttribute(BemAttributeInfo attributeInfo) {
//		BemAttribute attribute = attributes.get(attributeInfo);
//		if (attribute == null) {
//			attribute = outgoingLinks.get(attributeInfo);
//		}
//		return attribute;
//	}

	public BemAttributeList getAttributes() {
		return attributes;
	}
	
	public void addAttribute(BemAttribute literalAttribute) {
		attributes.add(literalAttribute);
	}	

	public List<BemAttribute> getAttributes(BemAttributeInfo attributeInfo) {
		return attributes.selectAll(attributeInfo);
	}


	public void bindInverseLinks() {
		
		throw new NotImplementedException("Not implemented after change");
		
//		for (BemAttribute attribute : attributes) {		
//			//
//			// bind link with inverse link (if any)
//			//
//			BemAttributeInfo attributeInfo = attribute.getAttributeInfo();
//			
//			// Bind inverse outgoingLinks with direct outgoingLinks
//			for (BemEntity destination : link.getDestinations()) {
//
//				if (destination instanceof BemEntity) {
//					List<BemInverseAttributeInfo> inverseAttributeInfos = attributeInfo
//							.getInverseAttributeInfos();
//
//					BemInverseAttributeInfo inverseAttributeInfo = findInverseAttributeInfo(
//							(BemEntity) destination, inverseAttributeInfos);
//					if (inverseAttributeInfo != null) {
//						link.setInverseAttributeInfo(inverseAttributeInfo);
//					}
//
//					((BemEntity) destination).addIncomingLink(link);
//				}
//
//			}
//		}
		
	}
	
	private void addIncomingLink(BemAttribute link) {
		incomingLinks.add(link); 
	}
	
	private BemInverseAttributeInfo findInverseAttributeInfo(BemEntity destination, List<BemInverseAttributeInfo> inverseAttributeInfos) {

		if (inverseAttributeInfos != null) {
			if (inverseAttributeInfos.size() == 1) {
				BemInverseAttributeInfo inverseAttributeInfo = inverseAttributeInfos.get(0);
				BemEntityTypeInfo destionationEntityTypeInfo = inverseAttributeInfo.getDestinationEntityTypeInfo();
				if (destination.isInstanceOf(destionationEntityTypeInfo)) {
					return inverseAttributeInfo;
				}
			} else {
				for (BemInverseAttributeInfo inverseAttributeInfo : inverseAttributeInfos) {
					BemEntityTypeInfo destionationEntityTypeInfo = inverseAttributeInfo.getDestinationEntityTypeInfo();
					if (destination.isInstanceOf(destionationEntityTypeInfo)) {
						return inverseAttributeInfo;
					}
				}
			}
		}
		return null;
	}
	
	public BemAttributeList getIncomingLinks() {
		return incomingLinks;
	}
	
	@Override
	public int compareTo(BemEntity o) {
		int compare = localId.compareTo(o.localId);
//		int compare = Long.compare(localId, o.localId);
		if (compare == 0) {
			compare = typeInfo.compareTo(o.typeInfo);
		}
		return compare;
	}

	public boolean isInstanceOf(BemEntityTypeInfo typeInfo) {
		assert(this.typeInfo != null) : "typeInfo is null";
		return this.typeInfo.isTypeOf(typeInfo);
	}
	
	@Override
	public Boolean isLiteralType() {
		return Boolean.FALSE;
	}
	
	public List<BemUniqueKeyValue> getUniqueKeyValues() {
		if (uniqueKeyValues == null) {
			uniqueKeyValues = getUniqueKeyValues(typeInfo.getUniqueKeyInfos());
		}
		return uniqueKeyValues;
	}
	
	public List<BemUniqueKeyValue> getUniqueKeyValues(List<BemUniqueKeyInfo> uniqueKeyInfos) {		
		List<BemUniqueKeyValue> uniqueKeyValues = new ArrayList<>();
		if (uniqueKeyInfos != null) { 
			for (BemUniqueKeyInfo uniqueKeyInfo : uniqueKeyInfos) {
				uniqueKeyValues.add(getUniqueKeyValue(uniqueKeyInfo));
			}
		}
		return uniqueKeyValues;
	}
	
	public BemUniqueKeyValue getUniqueKeyValue(BemUniqueKeyInfo uniqueKeyInfo) {
		BemUniqueKeyValue uniqueKeyValue = new BemUniqueKeyValue();
		for (BemAttributeInfo attributeInfo : uniqueKeyInfo.getAttributeInfos()) {
			BemAttribute attribute = attributes.selectFirst(attributeInfo);
			if (attribute != null) {
				uniqueKeyValue.addValue(attributeInfo, attribute.getValue());
			} else {
				assert(attributeInfo.isOptional());
//				logger.warn(String.format("Unique literal attribute not found: %s.%s", getTypeInfo(), attributeInfo));
				uniqueKeyValue.addValue(attributeInfo, BemValue.NULL);				
			}
		}
		return uniqueKeyValue;
	}
	
	public BemEntity getParent() {
		return parent;
	}

	public void setParent(BemEntity parent) {
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
	
	public void setSameAs(BemEntity other) {
		throw new NotImplementedException("");
		
//		this.sameAsEntity = other;
//		
//		for (BemAttribute incomingLink : incomingLinks) {
//			List<BemEntity> destinations = incomingLink.getDestinations();
//			int index = destinations.indexOf(this);
//			assert(index >= 0) : "Expected: index >= 0";
//			destinations.remove(index);
//			destinations.add(index, other);
//			other.addIncomingLink(incomingLink);
//		}
		
	}
	
	public BemEntity getSameAsEntity() {
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
	public boolean isIdenticalTo(BemEntity other1) {
		
		throw new NotImplementedException("Not implemented after change");
		
//		BemEntity other = (BemEntity)other1;
//		
//		//
//		// compare types
//		//		
//		if (!typeInfo.equals(other.typeInfo)) {
//			return false;
//		}
//		
//		//
//		// compare literal attributes
//		//
//		if (!IteratorEqualChecker.areEqual(attributes, other.attributes)) {
//			return false;
//		}
//		
//		//
//		// compare outgoing links
//		//
//		Iterator<BemLink> outgoingLinkIterator1 = outgoingLinks.iterator();
//		Iterator<BemLink> outgoingLinkIterator2 = other.outgoingLinks.iterator();
//		
//		while (outgoingLinkIterator1.hasNext()) {
//			if (!outgoingLinkIterator2.hasNext()) {
//				return false;
//			}
//			
//			Iterator<BemEntityBase> destinations1 = outgoingLinkIterator1.next().getDestinations().iterator();
//			Iterator<BemEntityBase> destinations2 = outgoingLinkIterator2.next().getDestinations().iterator();
//			
//			while (destinations1.hasNext()) {
//				if (!destinations2.hasNext()) {
//					return false;
//				}
//				
//				BemEntityBase destination1 = destinations1.next();
//				BemEntityBase destination2 = destinations2.next();				
//				
//				if (!destination1.equals(destination2) && !destination1.isIdenticalTo(destination2)) {
//					return false;
//				}				
//			}
//			
//			if (destinations2.hasNext()) {
//				return false;
//			}			
//			
//		}
//		
//		if (outgoingLinkIterator2.hasNext()) {
//			return false;
//		}
//
//		
//		return true;
	}

	@Override
	public String toString() {
//		assert (typeInfo != null) : "Undefined entity type, localId=" + localId;
		return BemValueFormatter.formatEntityName(typeInfo, localId);
	}

	@Override
	public boolean equals(Object o) {
		return ((BemEntity)o).localId == localId;
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
