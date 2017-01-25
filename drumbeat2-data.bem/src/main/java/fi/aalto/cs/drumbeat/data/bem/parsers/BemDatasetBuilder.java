package fi.aalto.cs.drumbeat.data.bem.parsers;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public class BemDatasetBuilder {
	
	private String fileType;
	
	public BemDatasetBuilder(String fileType) {
		this.fileType = fileType;
	}
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public BemDataset createDataset(BemSchema schema) {
		return new BemDataset(schema);
	}
	
}
