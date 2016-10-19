package fi.aalto.cs.drumbeat.data.bedm.model;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbAttributeInfo;

public class DrbLiteralAttribute extends DrbAttribute {
	
	private static final long serialVersionUID = 1L;

	public DrbLiteralAttribute(DrbAttributeInfo attributeInfo, int attributeIndex, DrbValue value) {
		super(attributeInfo, attributeIndex, value);
	}

	@Override
	public boolean isLiteralType() {
		return true;
	}	

}
