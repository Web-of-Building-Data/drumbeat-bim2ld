package fi.aalto.cs.drumbeat.data.step.parsers;

import java.util.ArrayList;

import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;


class ExpressNonEntityTypeInfoTextWrapper {
	
	private BemTypeInfo typeInfo;
	private ArrayList<String> typeBodyStatements;

	public ExpressNonEntityTypeInfoTextWrapper(BemTypeInfo typeInfo, String typeInfoString) {
		this.typeInfo = typeInfo;
		typeBodyStatements = new ArrayList<>();
		typeBodyStatements.add(typeInfoString);
	}
	
	public BemTypeInfo getTypeInfo() {
		return typeInfo;
	}
	
	public String[] getTypeBodyStatements() {
		String[] result = new String[typeBodyStatements.size()];
		typeBodyStatements.toArray(result);
		return result;
	}
	
	public void addTypeBodyStatement(String statement) {
		typeBodyStatements.add(statement);
	}


}
