package fi.aalto.cs.drumbeat.data.ifc.parsers;

import fi.aalto.cs.drumbeat.data.ifc.schema.IfcSchema;
import fi.aalto.cs.drumbeat.data.step.parsers.ExpressSchemaBuilder;

public class IfcSchemaBuilder extends ExpressSchemaBuilder {
	
	public IfcSchemaBuilder(String fileType) {
		super(fileType);
	}

	@Override
	public IfcSchema createSchema() {
		return new IfcSchema();
	}
	

}
