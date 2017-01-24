package fi.aalto.cs.drumbeat.data.step.parsers;

import fi.aalto.cs.drumbeat.data.bem.parsers.BemSchemaBuilder;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

public class ExpressSchemaBuilder extends BemSchemaBuilder {
	
	public ExpressSchemaBuilder(String fileType) {
		super(fileType);
	}

	@Override
	public ExpressSchema createSchema() {
		return new ExpressSchema();
	}
	

}
