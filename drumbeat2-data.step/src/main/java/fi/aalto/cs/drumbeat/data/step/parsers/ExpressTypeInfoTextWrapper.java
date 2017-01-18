package fi.aalto.cs.drumbeat.data.step.parsers;

import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;


class ExpressTypeInfoTextWrapper {
	
	private BemTypeInfo typeInfo;
	private String typeBodyStatement;

	public ExpressTypeInfoTextWrapper(BemTypeInfo typeInfo) {
		this.typeInfo = typeInfo;
	}
	
	public BemTypeInfo getTypeInfo() {
		return typeInfo;
	}
	
	public String getTypeBodyStatement() {
		return typeBodyStatement;
	}
	
	public void setTypeBodyStatement(String typeBodyStatement) {
		this.typeBodyStatement = typeBodyStatement;
	}


}
