package fi.aalto.cs.drumbeat.data.edm.schema;

import java.util.*;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

public class DrbEntityTypeInfo extends DrbComplexTypeInfo {
	
	private DrbEntityTypeInfo superTypeInfo;
	private List<DrbEntityTypeInfo> subTypeInfos;
	private List<DrbAttributeInfo> attributeInfos = new ArrayList<>();
	private List<DrbInverseLinkInfo> inverseLinkInfos = new ArrayList<>();
	private List<DrbAttributeInfo> inheritedAttributeInfos;
	private List<DrbInverseLinkInfo> inheritedInverseLinkInfos;
	private List<DrbUniqueKeyInfo> uniqueKeyInfos;
	private boolean isAbstractSuperType;

	public DrbEntityTypeInfo(DrbSchema schema, String name) {
		super(schema, name);
	}
	
	public DrbEntityTypeInfo getSuperTypeInfo() {
		return superTypeInfo;
	}
	
	/**
	 * Sets the super entity type
	 * Note: This method should be called not more than once for every entity type
	 * @param superTypeInfo
	 */
	public void setSuperTypeInfo(DrbEntityTypeInfo superTypeInfo) {
		assert(this.superTypeInfo == null);
		this.superTypeInfo = superTypeInfo;
		
		if (superTypeInfo.subTypeInfos == null) {
			superTypeInfo.subTypeInfos = new ArrayList<>();
		}
		superTypeInfo.subTypeInfos.add(this);
	}
	
	public List<DrbEntityTypeInfo> getSubTypeInfos() {
		return subTypeInfos;
	}
	
	/**
	 * Gets the list of attributeInfos
	 * @return
	 */
	public List<DrbAttributeInfo> getAttributeInfos() {
		return attributeInfos;
	}
	
	public void addAttributeInfo(DrbAttributeInfo attributeInfo) {
		attributeInfos.add(attributeInfo);		
	}
	
	
	/**
	 * Gets list of all attributes including inherited ones from the super type. 
	 * @return List of all attributes
	 */
	public List<DrbAttributeInfo> getInheritedAttributeInfos() {
		if (inheritedAttributeInfos == null) {
			inheritedAttributeInfos = new ArrayList<>();
			if (superTypeInfo != null) {
				inheritedAttributeInfos.addAll(superTypeInfo.getInheritedAttributeInfos());
			}
			inheritedAttributeInfos.addAll(attributeInfos);
		}
		return inheritedAttributeInfos;
	}
	
	/**
	 * Gets list of all attributes including inherited ones from the super type. 
	 * @return List of all attributes
	 */
	public List<DrbInverseLinkInfo> getInheritedInverseLinkInfos() {
		if (inheritedInverseLinkInfos == null) {
			inheritedInverseLinkInfos = new ArrayList<>();
			if (superTypeInfo != null) {
				inheritedInverseLinkInfos.addAll(superTypeInfo.getInheritedInverseLinkInfos());
			}
			inheritedInverseLinkInfos.addAll(inverseLinkInfos);
		}
		return inheritedInverseLinkInfos;
	}

	/**
	 * @return the isAbstractSuperType
	 */
	public boolean isAbstractSuperType() {
		return isAbstractSuperType;
	}

	/**
	 * @param isAbstractSuperType the isAbstractSuperType to set
	 */
	public void setAbstractSuperType(boolean isAbstractSuperType) {
		this.isAbstractSuperType = isAbstractSuperType;
	}

	/**
	 * Checks if the current type is a subtype of another type
	 * @param typeInfo
	 * @return True if the current type is a subtype of typeInfo, False otherwise 
	 */
	public boolean isSubtypeOf(DrbEntityTypeInfo typeInfo) {
		return superTypeInfo != null && (superTypeInfo.equals(typeInfo) || superTypeInfo.isSubtypeOf(typeInfo));
	}
	
	/**
	 * Checks if the current type is the same as or a subtype of another type
	 * @param typeInfo
	 * @return True if the current type is the same or a subtype of typeInfo, False otherwise 
	 */
	public boolean isTypeOf(DrbEntityTypeInfo typeInfo) {
		return this.equals(typeInfo) || isSubtypeOf(typeInfo);
	}
	
	public DrbAttributeInfo getAttributeInfo(String name) throws DrbNotFoundException {
		return getAttributeInfo(name, false);
	}

	public DrbAttributeInfo getAttributeInfo(String name, boolean includeInverseLinks) throws DrbNotFoundException {
		for (DrbAttributeInfo attributeInfo : attributeInfos) {
			if (attributeInfo.getName().equalsIgnoreCase(name)) {
				return attributeInfo;
			}
		}
		
		if (superTypeInfo != null) {
			try {
				return superTypeInfo.getAttributeInfo(name, includeInverseLinks);
			} catch (DrbNotFoundException e) {				
			}
		}
		
		if (includeInverseLinks) {
			for (DrbInverseLinkInfo inverseLinkInfo : inverseLinkInfos) {
				if (inverseLinkInfo.getName().equalsIgnoreCase(name)) {
					return inverseLinkInfo;
				}
			}			
		}
		
		throw new DrbNotFoundException(String.format("Entity type '%s' has no such attribute: '%s'", toString(), name));
	}

	public List<DrbInverseLinkInfo> getInverseLinkInfos() {
		return inverseLinkInfos;
	}
	
	public void addInverseLinkInfo(DrbInverseLinkInfo inverseLinkInfo) {
		inverseLinkInfos.add(inverseLinkInfo);		
	}
	
	public DrbInverseLinkInfo getInverseLinkInfo(DrbOutgoingLinkInfo linkInfo) {
		for (DrbInverseLinkInfo inverseLinkInfo : inverseLinkInfos) {
			if (inverseLinkInfo.isInverseTo(linkInfo))
			return inverseLinkInfo;
		}
		return null;
	}	
	
	
	public List<DrbUniqueKeyInfo> getUniqueKeyInfos() {
		return uniqueKeyInfos;		
	}

	public void addUniqueKey(DrbUniqueKeyInfo uniqueKeyInfo) {
		if (uniqueKeyInfos == null) {
			uniqueKeyInfos = new ArrayList<>();
		}
		uniqueKeyInfos.add(uniqueKeyInfo);
	}

}
