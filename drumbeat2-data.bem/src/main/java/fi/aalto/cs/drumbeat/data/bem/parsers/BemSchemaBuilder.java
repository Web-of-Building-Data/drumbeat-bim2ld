package fi.aalto.cs.drumbeat.data.bem.parsers;

import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class BemSchemaBuilder {
	
	private String fileType;
	
	public BemSchemaBuilder(String fileType) {
		this.fileType = fileType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public BemSchema createSchema() {
		return new BemSchema();
	}
	
	public BemEntityTypeInfo createEntityTypeInfo(BemSchema schema, String typeName) {
		return new BemEntityTypeInfo(schema, typeName);
	}

	public BemCollectionTypeInfo createCollectionTypeInfo(BemSchema schema, String typeName, boolean isDerivedType) {
		BemCollectionTypeInfo typeInfo = new BemCollectionTypeInfo(schema, typeName);
		typeInfo.setDerivedType(isDerivedType);
		return typeInfo;
	}
	
	public BemSelectTypeInfo createSelectTypeInfo(BemSchema schema, String typeName) {
		return new BemSelectTypeInfo(schema, typeName);
	}
	
	public BemEnumerationTypeInfo createEnumerationTypeInfo(BemSchema schema, String typeName) {
		return new BemEnumerationTypeInfo(schema, typeName);
	}
	
	public BemDefinedTypeInfo createDefinedTypeInfo(BemSchema schema, String typeName) {
		return new BemDefinedTypeInfo(schema, typeName);
	}
	
	public BemCardinality createCardinality(int left, int right, boolean isArrayIndex) {
		return new BemCardinality(left, right, isArrayIndex);
	}
	

}
