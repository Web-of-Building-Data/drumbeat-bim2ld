package fi.aalto.cs.drumbeat.data.step.parsers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemSchemaParser;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemUnsupportedDataTypeException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.ExpressFormat;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

public class ExpressSchemaParser extends BemSchemaParser {
	
	public static final Collection<String> SUPPORTED_FILE_TYPES = Arrays.asList(ExpressFormat.FILE_EXTENSION_EXP); 

	@Override
	public Collection<String> getSupportedFileTypes() {
		return SUPPORTED_FILE_TYPES;
	}

	@Override
	public BemSchema parse(InputStream in, String fileType, boolean checkFileType) throws BemException {
		BemSchema shema = createDefaultSchema(fileType, checkFileType);
		return new ExpressSchemaInternalParser(shema, in, fileType).parse();
	}

	@Override
	public BemSchema createDefaultSchema(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {
		internalCheckFileType(fileType, checkFileType);
		return new ExpressSchema();
	}

}
