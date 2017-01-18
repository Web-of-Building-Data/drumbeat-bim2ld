package fi.aalto.cs.drumbeat.data.bem.parsers;

import java.io.InputStream;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public abstract class BemSchemaParser {
	
	public abstract Collection<String> getSupportedFileTypes();
	
	public abstract BemSchema parse(InputStream in, String fileType, boolean checkFileType) throws BemException;
	
	public abstract BemSchema createDefaultSchema(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException;	
	
	public boolean checkFileType(String fileType) {
		// case-sensitive checking
		return getSupportedFileTypes().contains(fileType);
		
//		for (String supportedFileType : getSupportedFileTypes()) {
//			if (supportedFileType.equalsIgnoreCase(fileType)) {
//				return true;
//			}			
//		}
//		return false;
	}
	
	protected void internalCheckFileType(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {
		if (checkFileType && !checkFileType(fileType)) {
			throw new BemUnsupportedDataTypeException("Invalid file type: " + fileType);
		}
	}
	
}
