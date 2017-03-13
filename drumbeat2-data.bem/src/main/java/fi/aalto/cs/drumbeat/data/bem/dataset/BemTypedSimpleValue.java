package fi.aalto.cs.drumbeat.data.bem.dataset;

import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;

public class BemTypedSimpleValue extends BemComplexValue {
	
	private final BemSimpleValue value;
	private final BemTypeInfo type;
	
	public BemTypedSimpleValue(BemSimpleValue value, BemTypeInfo type) {
		assert(type.getValueKind().isSimple());
		
		this.type = type;
		this.value = value;
	}
	
	public BemSimpleValue getValue() {
		return value;
	}
	
	public BemTypeInfo getType() {
		return type;
	}	
	
	
	
}
