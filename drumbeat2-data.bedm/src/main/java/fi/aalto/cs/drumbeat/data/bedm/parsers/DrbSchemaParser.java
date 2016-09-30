package fi.aalto.cs.drumbeat.data.bedm.parsers;

import java.io.InputStream;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbSchema;

public abstract class DrbSchemaParser {
	
	public abstract Collection<String> getSupportedFileTypes();
	
	public abstract DrbSchema parse(InputStream in, String fileType) throws DrbParserException;
	
	public abstract DrbSchema createDefaultSchema(String fileType) throws DrbUnsupportedDataTypeException;	
	
	public boolean checkFileType(String fileType) {
		return getSupportedFileTypes().contains(fileType);
	}
	
}
