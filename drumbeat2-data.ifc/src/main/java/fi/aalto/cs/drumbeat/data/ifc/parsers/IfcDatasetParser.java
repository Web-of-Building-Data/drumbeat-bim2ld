package fi.aalto.cs.drumbeat.data.ifc.parsers;

import java.util.Arrays;
import java.util.Collection;

import fi.aalto.cs.drumbeat.data.ifc.IfcVocabulary.IfcFormat;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat;
import fi.aalto.cs.drumbeat.data.step.parsers.StepDatasetParser;

public class IfcDatasetParser extends StepDatasetParser {
	
	public static final Collection<String> SUPPORTED_FILE_TYPES = Arrays.asList(SpfFormat.FILE_EXTENSION_STP, IfcFormat.FILE_EXTENSION_IFC); 

	@Override
	public Collection<String> getSupportedFileTypes() {
		return SUPPORTED_FILE_TYPES;
	}
	
}
