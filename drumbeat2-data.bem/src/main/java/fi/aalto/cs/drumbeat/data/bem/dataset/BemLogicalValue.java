package fi.aalto.cs.drumbeat.data.bem.dataset;

import fi.aalto.cs.drumbeat.data.bem.schema.BemLogicalEnum;

public class BemLogicalValue extends BemSimpleValue {

	public BemLogicalValue(BemLogicalEnum value) {
		super(value);
	}
	
	@Override
	public BemLogicalEnum getValue() {
		return (BemLogicalEnum)super.getValue();
	}
	
}
