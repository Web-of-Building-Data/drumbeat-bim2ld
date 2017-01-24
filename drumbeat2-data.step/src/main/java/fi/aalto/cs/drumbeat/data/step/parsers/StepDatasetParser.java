package fi.aalto.cs.drumbeat.data.step.parsers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemDatasetParser;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemUnsupportedDataTypeException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat;

public class StepDatasetParser extends BemDatasetParser {
	
	public static final Collection<String> SUPPORTED_FILE_TYPES = Arrays.asList(SpfFormat.FILE_EXTENSION_STP); 

	@Override
	public Collection<String> getSupportedFileTypes() {
		return SUPPORTED_FILE_TYPES;
	}

	@Override
	public BemDataset parse(BemSchema schema, InputStream in, String fileType, boolean checkFileType) throws BemException {
		StepDatasetBuilder builder = getDatasetBuilder(schema, fileType, checkFileType);
		switch (fileType) {
		case SpfFormat.FILE_EXTENSION_STP:
			return new SpfDatasetInternalParser(builder, in).parse();			
		}
		throw new BemUnsupportedDataTypeException("Invalid file type: " + fileType);
	}

	@Override
	public StepDatasetBuilder getDatasetBuilder(BemSchema schema, String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {
		internalCheckFileType(fileType, checkFileType);
		return new StepDatasetBuilder(schema, fileType);
	}

}
