package fi.aalto.cs.drumbeat.data.step.dataset;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.step.dataset.meta.StepMetaDataset;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

public class StepDataset extends BemDataset {
	
	private StepMetaDataset metaDataset;

	public StepDataset(ExpressSchema schema) {
		super(schema);
	}

	public StepMetaDataset getMetaDataset() {
		return metaDataset;
	}
	
	public void setMetaDataset(StepMetaDataset metaDataset) {
		this.metaDataset = metaDataset;
	}

}
