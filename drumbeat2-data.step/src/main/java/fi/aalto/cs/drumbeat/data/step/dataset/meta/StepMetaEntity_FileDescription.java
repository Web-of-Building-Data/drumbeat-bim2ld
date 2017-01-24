package fi.aalto.cs.drumbeat.data.step.dataset.meta;

import java.util.List;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat;

public class StepMetaEntity_FileDescription extends StepMetaEntity {
	
	public StepMetaEntity_FileDescription(BemEntity entity) {
		super(entity);
	}
	
	public List<String> getDescriptions() {
		return getValues(SpfFormat.Header.FileDescription.DESCRIPTION);
	}
	
	public String getImplementationLevel() {
		return getAttributeValue(SpfFormat.Header.FileDescription.IMPLEMENTATION_LEVEL);
	}
	
	
}
