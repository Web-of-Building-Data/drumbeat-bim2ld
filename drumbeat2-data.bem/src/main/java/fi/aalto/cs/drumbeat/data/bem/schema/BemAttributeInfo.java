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
	private BemTypeInfo valueTypeInfo;
	private BemCardinality cardinality;	
	
	private boolean isOptional;
	private boolean isFunctional;
	private boolean isInverseFunctional;
	private List<BemInverseAttributeInfo> possibleInverseAttributeInfos;	

	public BemAttributeInfo(BemEntityTypeInfo entityTypeInfo, String name, BemTypeInfo valueTypeInfo) {
		this.entityTypeInfo = entityTypeInfo;
		this.name = name;
		this.valueTypeInfo = valueTypeInfo;
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

	public BemTypeInfo getValueTypeInfo() {
		return valueTypeInfo;
	}

	public BemCardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(BemCardinality cardinality) {
		this.cardinality = cardinality;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
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
	
	public List<BemInverseAttributeInfo> getPossibleInverseAttributeInfos() {
		return possibleInverseAttributeInfos;
	}

	public void addPossibleInverseAttributeInfo(BemInverseAttributeInfo inverseAttributeInfo) {
		if (possibleInverseAttributeInfos == null) {
			possibleInverseAttributeInfos = new ArrayList<>();
		}
		possibleInverseAttributeInfos.add(inverseAttributeInfo);
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
