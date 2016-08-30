package fi.aalto.cs.drumbeat.data.edm.parsers;

import java.io.InputStream;
import java.util.List;

import fi.aalto.cs.drumbeat.data.edm.schema.DrbSchema;

public abstract class DrbSchemaParser {
	
	public abstract String[] getSupportedFileTypes();
	
	public abstract DrbSchema parse(InputStream in, String fileType);
	
	
	public boolean checkFileType(String fileType) {
		String[] getSupportedFileTypes;
	}
	
	
	
}
