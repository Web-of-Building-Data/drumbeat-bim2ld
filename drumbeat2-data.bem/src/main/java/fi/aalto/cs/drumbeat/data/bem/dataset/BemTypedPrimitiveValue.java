package fi.aalto.cs.drumbeat.data.bem.dataset;

import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;

public class BemTypedPrimitiveValue extends BemComplexValue {
	
	private final BemPrimitiveValue value;
	private final BemTypeInfo type;
	
	public BemTypedPrimitiveValue(BemPrimitiveValue value, BemTypeInfo type) {
		this.type = type;
		this.value = value;
	}
	
	public BemPrimitiveValue getValue() {
		return value;
	}
	
	public BemTypeInfo getType() {
		return type;
	}	
	
	
	
}
