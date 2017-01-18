package fi.aalto.cs.drumbeat.data.bem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fi.aalto.cs.drumbeat.data.bem.schema.BemAttributeInfo;


public abstract class BemAttribute implements Comparable<BemAttribute>, Serializable { // IRdfLink {
	
	private static final long serialVersionUID = 1L;
	
	private final BemAttributeInfo attributeInfo;
	private final int index;
	private final BemValue value;
	
	public BemAttribute(BemAttributeInfo attributeInfo, int attributeIndex, BemValue value) {
		this.attributeInfo = attributeInfo;
		this.index = attributeIndex;
		this.value = value;
	}

	public BemAttributeInfo getAttributeInfo() {
		return attributeInfo;
	}

	public int getIndex() {
		return index;
	}
	
	public BemValue getValue() {
		return value;	
	}	

	@Override
	public int compareTo(BemAttribute o) {
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
	public <T extends BemNonCollectionValue> List<T> getSingleValues() {
		if (value instanceof BemNonCollectionValue) {
			List<T> singleValues = new ArrayList<>(1);
			singleValues.add((T)value);
			return singleValues;
		} else {
			return ((BemCollectionValue<T>)value).getSingleValues();
		}
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof BemAttribute)
				&& this.getAttributeInfo().equals(((BemAttribute)other).getAttributeInfo())
				&& this.getValue().equals(((BemAttribute)other).getValue());
	}	

	@Override
	public String toString() {
		return value.toString();		
	}

	public abstract boolean isLiteralType();	
}
