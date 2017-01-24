package fi.aalto.cs.drumbeat.data.bem.parsers;

import java.io.InputStream;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public abstract class BemDatasetParser {
	
	public abstract Collection<String> getSupportedFileTypes();
	
	public abstract BemDataset parse(BemSchema schema, InputStream in, String fileType, boolean checkFileType) throws BemException;
	
	public abstract BemDatasetBuilder getDatasetBuilder(BemSchema schema, String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException;	
	
	public boolean checkFileType(String fileType) {
		// case-sensitive checking (like in UNIX)
		return getSupportedFileTypes().contains(fileType);		
	}
	
	protected void internalCheckFileType(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {
		if (checkFileType && !checkFileType(fileType)) {
			throw new BemUnsupportedDataTypeException("Invalid file type: " + fileType);
		}
	}
	
}
