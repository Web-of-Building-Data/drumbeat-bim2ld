package fi.aalto.cs.drumbeat.data.step.parsers;

import fi.aalto.cs.drumbeat.data.bem.parsers.BemDatasetBuilder;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.step.dataset.StepDataset;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

public class StepDatasetBuilder extends BemDatasetBuilder {	
	
	public StepDatasetBuilder(BemSchema schema, String fileType) {
		super(schema, fileType);
	}
	
	@Override
	public ExpressSchema getSchema() {
		return (ExpressSchema)super.getSchema();
	}

	@Override
	public StepDataset createDataset() {
		return new StepDataset(getSchema());
	}
	
}
