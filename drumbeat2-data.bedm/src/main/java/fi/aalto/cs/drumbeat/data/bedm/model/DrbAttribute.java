package fi.aalto.cs.drumbeat.data.bedm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbAttributeInfo;


public abstract class DrbAttribute implements Comparable<DrbAttribute>, Serializable { // IRdfLink {
	
	private static final long serialVersionUID = 1L;
	
	private final DrbAttributeInfo attributeInfo;
	private final int index;
	private final DrbValue value;
	
	public DrbAttribute(DrbAttributeInfo attributeInfo, int attributeIndex, DrbValue value) {
		this.attributeInfo = attributeInfo;
		this.index = attributeIndex;
		this.value = value;
	}

	public DrbAttributeInfo getAttributeInfo() {
		return attributeInfo;
	}

	public int getIndex() {
		return index;
	}
	
	public DrbValue getValue() {
		return value;
	}	

	@Override
	public int compareTo(DrbAttribute o) {
		if (this == o) {
			return 0;
		} else {
			return attributeInfo.compareTo(o.attributeInfo);
		}
	}
	
//	@Override
//	public IRdfPredicate getRdfPredicate() {
//		return attributeInfo;
//	}
//
//	@Override
//	public IRdfNode getRdfObject() {
//		return value;
//	}
	
	@SuppressWarnings("unchecked")
	public <T extends DrbSingleValue> List<T> getSingleValues() {
		if (value instanceof DrbSingleValue) {
			List<T> singleValues = new ArrayList<>(1);
			singleValues.add((T)value);
			return singleValues;
		} else {
			return ((DrbCollectionValue<T>)value).getSingleValues();
		}
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof DrbAttribute)
				&& this.getAttributeInfo().equals(((DrbAttribute)other).getAttributeInfo())
				&& this.getValue().equals(((DrbAttribute)other).getValue());
	}	

	@Override
	public String toString() {
		return value.toString();		
	}

	public abstract boolean isLiteralType();	
}
