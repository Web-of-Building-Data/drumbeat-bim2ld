package fi.aalto.cs.drumbeat.data.bem.parsers;

import java.io.InputStream;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public abstract class BemSchemaParser {
	
	public abstract Collection<String> getSupportedFileTypes();
	
	public abstract BemSchema parse(InputStream in, String fileType, boolean checkFileType) throws BemException;
	
	public abstract BemSchemaBuilder getSchemaBuilder(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException;	
	
	public boolean checkFileType(String fileType) {
		// case-sensitive checking (like in UNIX)
		return getSupportedFileTypes().contains(fileType.trim());		
	}
	
	protected void internalCheckFileType(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {
		if (checkFileType && !checkFileType(fileType)) {
			throw new BemUnsupportedDataTypeException("Invalid file type: " + fileType);
		}
	}
	
}
