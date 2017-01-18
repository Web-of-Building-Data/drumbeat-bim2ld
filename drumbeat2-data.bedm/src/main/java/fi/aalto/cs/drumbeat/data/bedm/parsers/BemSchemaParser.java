package fi.aalto.cs.drumbeat.data.bem.parsers;

import java.io.InputStream;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public abstract class BemSchemaParser {
	
	public abstract Collection<String> getSupportedFileTypes();
	
	public abstract BemSchema parse(InputStream in, String fileType) throws BemParserException;
	
	public abstract BemSchema createDefaultSchema(String fileType) throws BemUnsupportedDataTypeException;	
	
	public boolean checkFileType(String fileType) {
		return getSupportedFileTypes().contains(fileType);
	}
	
}
