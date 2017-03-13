package fi.aalto.cs.drumbeat.data.bem.dataset;

import fi.aalto.cs.drumbeat.data.bem.schema.BemValueKindEnum;

public class BemPrimitiveValue extends BemSimpleValue {
	
	private final BemValueKindEnum valueKind;

	public BemPrimitiveValue(Object value, BemValueKindEnum valueKind) {
		super(value);
		
		assert(valueKind.isPrimitive()) : "ValueKind must be primitive: " + valueKind;
		this.valueKind = valueKind;
	}
	
	public BemValueKindEnum getValueKind() {
		return valueKind;
	}

}
