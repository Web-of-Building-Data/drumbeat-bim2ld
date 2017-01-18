package fi.aalto.cs.drumbeat.data.ifc.parsers;

import fi.aalto.cs.drumbeat.data.bem.parsers.BemUnsupportedDataTypeException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.ifc.schema.IfcSchema;
import fi.aalto.cs.drumbeat.data.step.parsers.ExpressSchemaParser;

public class IfcSchemaParser extends ExpressSchemaParser {
	
	@Override
	public BemSchema createDefaultSchema(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {		
		internalCheckFileType(fileType, checkFileType);
		return new IfcSchema();
	}
	
}
