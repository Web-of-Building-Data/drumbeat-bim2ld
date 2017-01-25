package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.*;

import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;

public class BemEntityTypeInfo extends BemComplexTypeInfo {
	
	private BemEntityTypeInfo superTypeInfo;
	private List<BemEntityTypeInfo> subTypeInfos;
	private List<BemAttributeInfo> attributeInfos = new ArrayList<>();
	private List<BemInverseAttributeInfo> inverseAttributeInfos = new ArrayList<>();
	private List<BemAttributeInfo> inheritedAttributeInfos;
	private List<BemInverseAttributeInfo> inheritedInverseAttributeInfos;
	private List<BemUniqueKeyInfo> uniqueKeyInfos;
	private boolean isAbstractSuperType;

	public BemEntityTypeInfo(BemSchema schema, String name) {
		super(schema, name);
	}
	
	@Override
	public BemValueKindEnum getValueKind() {
		return BemValueKindEnum.COLLECTION;		
	}	
	
	public BemEntityTypeInfo getSuperTypeInfo() {
		return superTypeInfo;
	}
	
	/**
	 * Sets the super entity type
	 * Note: This method should be called not more than once for every entity type
	 * @param superTypeInfo
	 */
	public void setSuperTypeInfo(BemEntityTypeInfo superTypeInfo) {		
		assert(this.superTypeInfo == null) : 
			String.format("Super type of %s is already set as %s", getName(), this.superTypeInfo);
		
		this.superTypeInfo = superTypeInfo;
		
		if (superTypeInfo.subTypeInfos == null) {
			superTypeInfo.subTypeInfos = new ArrayList<>();
		}
		superTypeInfo.subTypeInfos.add(this);
	}
	
	public List<BemEntityTypeInfo> getSubTypeInfos() {
		return subTypeInfos;
	}
	
	/**
	 * Gets the list of attributeInfos
	 * @return
	 */
	public List<BemAttributeInfo> getAttributeInfos() {
		return attributeInfos;
	}
	
	public void addAttributeInfo(BemAttributeInfo attributeInfo) {
		attributeInfos.add(attributeInfo);		
	}
	
	
	/**
	 * Gets list of all attributes including inherited ones from the super type. 
	 * @return List of all attributes
	 */
	public List<BemAttributeInfo> getInheritedAttributeInfos() {
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
	public List<BemInverseAttributeInfo> getInheritedInverseAttributeInfos() {
		if (inheritedInverseAttributeInfos == null) {
			inheritedInverseAttributeInfos = new ArrayList<>();
			if (superTypeInfo != null) {
				inheritedInverseAttributeInfos.addAll(superTypeInfo.getInheritedInverseAttributeInfos());
			}
			inheritedInverseAttributeInfos.addAll(inverseAttributeInfos);
		}
		return inheritedInverseAttributeInfos;
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
	public boolean isSubtypeOf(BemEntityTypeInfo typeInfo) {
		return superTypeInfo != null && (superTypeInfo.equals(typeInfo) || superTypeInfo.isSubtypeOf(typeInfo));
	}
	
	/**
	 * Checks if the current type is the same as or a subtype of another type
	 * @param typeInfo
	 * @return True if the current type is the same or a subtype of typeInfo, False otherwise 
	 */
	public boolean isTypeOf(BemEntityTypeInfo typeInfo) {
		return this.equals(typeInfo) || isSubtypeOf(typeInfo);
	}
	
	public BemAttributeInfo getAttributeInfo(String name) throws BemNotFoundException {
		return getAttributeInfo(name, false);
	}

	public BemAttributeInfo getAttributeInfo(String name, boolean includeInverseLinks) throws BemNotFoundException {
		for (BemAttributeInfo attributeInfo : attributeInfos) {
			if (attributeInfo.getName().equalsIgnoreCase(name)) {
				return attributeInfo;
			}
		}
		
		if (superTypeInfo != null) {
			try {
				return superTypeInfo.getAttributeInfo(name, includeInverseLinks);
			} catch (BemNotFoundException e) {				
			}
		}
		
		if (includeInverseLinks) {
			for (BemInverseAttributeInfo inverseAttributeInfo : inverseAttributeInfos) {
				if (inverseAttributeInfo.getName().equalsIgnoreCase(name)) {
					return inverseAttributeInfo;
				}
			}			
		}
		
		throw new BemNotFoundException(String.format("Entity type '%s' has no such attribute: '%s'", toString(), name));
	}

	public List<BemInverseAttributeInfo> getInverseAttributeInfos() {
		return inverseAttributeInfos;
	}
	
	public void addInverseAttributeInfo(BemInverseAttributeInfo inverseAttributeInfo) {
		inverseAttributeInfos.add(inverseAttributeInfo);		
	}
	
	public BemInverseAttributeInfo getInverseAttributeInfo(BemAttributeInfo attributeInfo) {
		for (BemInverseAttributeInfo inverseAttributeInfo : inverseAttributeInfos) {
			if (inverseAttributeInfo.isInverseTo(attributeInfo))
			return inverseAttributeInfo;
		}
		return null;
	}	
	
	
	public List<BemUniqueKeyInfo> getUniqueKeyInfos() {
		return uniqueKeyInfos;		
	}

	public void addUniqueKey(BemUniqueKeyInfo uniqueKeyInfo) {
		if (uniqueKeyInfos == null) {
			uniqueKeyInfos = new ArrayList<>();
		}
		uniqueKeyInfos.add(uniqueKeyInfo);
	}

//	@Override
//	public boolean isCollectionType() {
//		return false;
//	}

}
