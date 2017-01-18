package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity attribute, can have not more than one value which can be a collection value.
 * An attribute value can be a literal value or an entity.
 * 
 * @author Nam Vu
 *
 */
public class BemAttributeInfo implements Comparable<BemAttributeInfo>{

	private BemEntityTypeInfo entityTypeInfo;
	private String name;
	private int attributeIndex;
	private BemTypeInfo attributeTypeInfo;
	private boolean isOptional;
	private boolean isFunctional;
	private boolean isInverseFunctional;
	private List<BemInverseAttributeInfo> inverseAttributeInfos;	

	public BemAttributeInfo(BemEntityTypeInfo entityTypeInfo, String name, BemTypeInfo attributeTypeInfo) {
		this.entityTypeInfo = entityTypeInfo;
		this.name = name;
		this.attributeTypeInfo = attributeTypeInfo;
	}

	public BemEntityTypeInfo getEntityTypeInfo() {
		return entityTypeInfo;
	}

	public String getName() {
		return name;
	}

	public String getLongName() {
		return String.format("%s_%s", name, entityTypeInfo);
	}

	public int getAttributeIndex() {
		return attributeIndex;
	}

	public void setAttributeIndex(int attributeIndex) {
		this.attributeIndex = attributeIndex;
	}

	public BemTypeInfo getAttributeTypeInfo() {
		return attributeTypeInfo;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public boolean isCollection() {
		return getAttributeTypeInfo() instanceof BemCollectionTypeInfo;
	}

	/**
	 * @return the isFunctional
	 */
	public boolean isFunctional() {
		return isFunctional;
	}

	/**
	 * @param isFunctional
	 *            the isFunctional to set
	 */
	public void setFunctional(boolean isFunctional) {
		this.isFunctional = isFunctional;
	}

	/**
	 * @return the isInverseFunctional
	 */
	public boolean isInverseFunctional() {
		return isInverseFunctional;
	}

	/**
	 * @param isInverseFunctional
	 *            the isInverseFunctional to set
	 */
	public void setInverseFunctional(boolean isInverseFunctional) {
		this.isInverseFunctional = isInverseFunctional;
	}
	
	public List<BemInverseAttributeInfo> getInverseAttributeInfos() {
		return inverseAttributeInfos;
	}

	public void addInverseAttributeInfo(BemInverseAttributeInfo inverseAttributeInfo) {
		if (inverseAttributeInfos == null) {
			inverseAttributeInfos = new ArrayList<>();
		}
		inverseAttributeInfos.add(inverseAttributeInfo);
	}	

	@Override
	public String toString() {
		return name; // String.format("%s.%s", entityTypeInfo.getName(), name);
	}

	public boolean equals(BemAttributeInfo o) {
		return name.equals(o.name);
	}

	@Override
	public int compareTo(BemAttributeInfo o) {
		int compare = name.compareTo(o.name);
		if (compare == 0) {
			compare = entityTypeInfo.compareTo(o.entityTypeInfo);
		}
		return compare;
	}

}
