package fi.aalto.cs.drumbeat.data.bem.parsers;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public class BemDatasetBuilder {
	
	private BemSchema schema;
	private String fileType;
	
	public BemDatasetBuilder(BemSchema schema, String fileType) {
		this.schema = schema;
		this.fileType = fileType;
	}
	
	public BemSchema getSchema() {
		return schema;
	}
	
	public void setSchema(BemSchema schema) {
		this.schema = schema;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public BemDataset createDataset() {
		return new BemDataset(schema);
	}
	
}
