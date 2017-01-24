package fi.aalto.cs.drumbeat.data.ifc.parsers;

import fi.aalto.cs.drumbeat.data.bem.parsers.BemUnsupportedDataTypeException;
import fi.aalto.cs.drumbeat.data.step.parsers.ExpressSchemaParser;

public class IfcSchemaParser extends ExpressSchemaParser {
	
	@Override
	public IfcSchemaBuilder getSchemaBuilder(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {		
		internalCheckFileType(fileType, checkFileType);
		return new IfcSchemaBuilder(fileType);
	}
	
}
