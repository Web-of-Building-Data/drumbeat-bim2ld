package fi.aalto.cs.drumbeat.data.bem.dataset;

import fi.aalto.cs.drumbeat.data.bem.schema.BemPrimitiveKindEnum;

public class BemPrimitiveValue extends BemSimpleValue {
	
	private final BemPrimitiveKindEnum valueKind;

	public BemPrimitiveValue(Object value, BemPrimitiveKindEnum valueKind) {
		super(value);
		this.valueKind = valueKind;
	}
	
	public BemPrimitiveKindEnum getValueKind() {
		return valueKind;
	}

}
