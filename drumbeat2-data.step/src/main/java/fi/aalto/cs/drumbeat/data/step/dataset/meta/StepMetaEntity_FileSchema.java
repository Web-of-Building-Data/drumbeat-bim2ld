package fi.aalto.cs.drumbeat.data.step.dataset.meta;

import java.util.List;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat;

public class StepMetaEntity_FileSchema extends StepMetaEntity {
	
	public StepMetaEntity_FileSchema(BemEntity entity) {
		super(entity);
	}

	public List<String> getSchemas() {
		return getAttributeValues(SpfFormat.Header.FileSchema.SCHEMA_IDENTIFIERS);
	}	
}
