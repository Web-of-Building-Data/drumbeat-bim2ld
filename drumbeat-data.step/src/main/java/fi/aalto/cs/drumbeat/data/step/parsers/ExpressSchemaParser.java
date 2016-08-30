package fi.aalto.cs.drumbeat.data.step.parsers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import fi.aalto.cs.drumbeat.data.edm.parsers.DrbSchemaParser;
import fi.aalto.cs.drumbeat.data.edm.schema.DrbSchema;

public class ExpressSchemaParser extends DrbSchemaParser {
	
	public static final List<String> SUPPORTED_FILE_TYPES = Arrays.asList("exp"); 

	public ExpressSchemaParser() {
	}

	@Override
	public List<String> getSupportedFileTypes() {
		return SUPPORTED_FILE_TYPES;
	}

	@Override
	public boolean checkFileType(String fileType) {
		
	}

	@Override
	public DrbSchema parse(InputStream in, String fileType) {
		return null;
	}

}
