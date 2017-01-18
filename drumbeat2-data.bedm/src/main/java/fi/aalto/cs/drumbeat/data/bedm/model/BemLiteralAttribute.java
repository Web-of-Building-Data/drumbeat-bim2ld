package fi.aalto.cs.drumbeat.data.bem.model;

import fi.aalto.cs.drumbeat.data.bem.schema.BemAttributeInfo;

public class BemLiteralAttribute extends BemAttribute {
	
	private static final long serialVersionUID = 1L;

	public BemLiteralAttribute(BemAttributeInfo attributeInfo, int attributeIndex, BemValue value) {
		super(attributeInfo, attributeIndex, value);
	}

	@Override
	public boolean isLiteralType() {
		return true;
	}	

}
