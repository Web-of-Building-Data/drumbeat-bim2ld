package fi.aalto.cs.drumbeat.data.bem.parsers;

import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

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
	
}
