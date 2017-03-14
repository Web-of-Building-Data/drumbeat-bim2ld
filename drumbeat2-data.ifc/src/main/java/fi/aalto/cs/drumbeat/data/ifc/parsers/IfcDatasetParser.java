package fi.aalto.cs.drumbeat.data.ifc.parsers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.data.ifc.IfcVocabulary.IfcFormat;
import fi.aalto.cs.drumbeat.data.ifc.processors.SetNameByGlobalId;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat;
import fi.aalto.cs.drumbeat.data.step.parsers.StepDatasetParser;

public class IfcDatasetParser extends StepDatasetParser {
	
	public static final Collection<String> SUPPORTED_FILE_TYPES = Arrays.asList(SpfFormat.FILE_EXTENSION_STP, IfcFormat.FILE_EXTENSION_IFC);
	
	@SuppressWarnings("serial")
	public IfcDatasetParser() {
		getProcessors().add(
				new Pair<>(
						SetNameByGlobalId.class,
						new Properties() {{
							put(SetNameByGlobalId.PARAM_ENTITY_NAME_PATTERN, "GUID_${Entity.LongGuid}");
						}})
		);
		
	}

	@Override
	public Collection<String> getSupportedFileTypes() {
		return SUPPORTED_FILE_TYPES;
	}
	
}
