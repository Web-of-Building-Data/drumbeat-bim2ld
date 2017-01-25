package fi.aalto.cs.drumbeat.data.step.parsers;

import fi.aalto.cs.drumbeat.data.bem.dataset.*;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemDatasetBuilder;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSimpleTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemValueKindEnum;
import fi.aalto.cs.drumbeat.data.step.dataset.StepDataset;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

public class StepDatasetBuilder extends BemDatasetBuilder {	
	
	public StepDatasetBuilder(String fileType) {
		super(fileType);
	}
	
	@Override
	public StepDataset createDataset(BemSchema schema) {
		return new StepDataset((ExpressSchema)schema);
	}

	public BemPrimitiveValue createPrimitiveValue(Object value, BemValueKindEnum valueKind) {
		return new BemPrimitiveValue(value, valueKind);
	}

	public BemTypedPrimitiveValue createTypedSimpleValue(BemPrimitiveValue value, BemTypeInfo type) {
		return new BemTypedPrimitiveValue(value, type);
	}
	
}
