package fi.aalto.cs.drumbeat.data.bedm.schema;


/**
 * An entity attribute, can have not more than one value which can be a collection value.
 * An attribute value can be a literal value or an entity.
 * 
 * @author Nam Vu
 *
 */
public class DrbAttributeInfo implements Comparable<DrbAttributeInfo>{

	private DrbEntityTypeInfo entityTypeInfo;
	private String name;
	private int attributeIndex;
	private DrbTypeInfo attributeTypeInfo;
	private boolean isOptional;
	private boolean isFunctional;
	private boolean isInverseFunctional;

	public DrbAttributeInfo(DrbEntityTypeInfo entityTypeInfo, String name, DrbTypeInfo attributeTypeInfo) {
		this.entityTypeInfo = entityTypeInfo;
		this.name = name;
		this.attributeTypeInfo = attributeTypeInfo;
	}

	public DrbEntityTypeInfo getEntityTypeInfo() {
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

	public DrbTypeInfo getAttributeTypeInfo() {
		return attributeTypeInfo;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public boolean isCollection() {
		return getAttributeTypeInfo() instanceof DrbCollectionTypeInfo;
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

	@Override
	public String toString() {
		return name; // String.format("%s.%s", entityTypeInfo.getName(), name);
	}

	public boolean equals(DrbAttributeInfo o) {
		return name.equals(o.name);
	}

	@Override
	public int compareTo(DrbAttributeInfo o) {
		int compare = name.compareTo(o.name);
		if (compare == 0) {
			compare = entityTypeInfo.compareTo(o.entityTypeInfo);
		}
		return compare;
	}

}
