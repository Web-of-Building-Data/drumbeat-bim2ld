package fi.aalto.cs.drumbeat.data.bem.dataset;

import fi.aalto.cs.drumbeat.data.bem.schema.BemSimpleTypeInfo;

public class BemTypedSimpleValue extends BemComplexValue {
	
	private final Object value;
	private final BemSimpleTypeInfo type;
	
	public BemTypedSimpleValue(Object value, BemSimpleTypeInfo type) {
		this.type = type;
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public BemSimpleTypeInfo getType() {
		return type;
	}	
	
	
	
}
