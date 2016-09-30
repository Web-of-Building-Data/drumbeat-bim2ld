package fi.aalto.cs.drumbeat.data.step.parsers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bedm.parsers.DrbParserException;
import fi.aalto.cs.drumbeat.data.bedm.parsers.DrbSchemaParser;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbSchema;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

public class ExpressSchemaParser extends DrbSchemaParser {
	
	public static final Collection<String> SUPPORTED_FILE_TYPES = Arrays.asList("exp"); 

	@Override
	public Collection<String> getSupportedFileTypes() {
		return SUPPORTED_FILE_TYPES;
	}

	@Override
	public DrbSchema parse(InputStream in, String fileType) throws DrbParserException {
		DrbSchema shema = createDefaultSchema(fileType);
		return new ExpressSchemaInternalParser(shema, in, fileType).parse();
	}

	@Override
	public DrbSchema createDefaultSchema(String fileType) {
		return new ExpressSchema();
	}

}
