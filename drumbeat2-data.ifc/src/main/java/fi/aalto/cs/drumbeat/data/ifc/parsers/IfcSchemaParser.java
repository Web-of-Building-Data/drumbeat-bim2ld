package fi.aalto.cs.drumbeat.data.ifc.parsers;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbSchema;
import fi.aalto.cs.drumbeat.data.ifc.schema.IfcSchema;
import fi.aalto.cs.drumbeat.data.step.parsers.ExpressSchemaParser;

public class IfcSchemaParser extends ExpressSchemaParser {
	
	@Override
	public DrbSchema createDefaultSchema(String fileType) {
		return new IfcSchema();
	}
	
}
