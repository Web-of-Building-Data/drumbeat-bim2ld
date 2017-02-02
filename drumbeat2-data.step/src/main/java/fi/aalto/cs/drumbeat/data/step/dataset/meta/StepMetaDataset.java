package fi.aalto.cs.drumbeat.data.step.dataset.meta;

import fi.aalto.cs.drumbeat.data.bem.BemEntityNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.BemTypeNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat.Header.*;

public class StepMetaDataset extends BemDataset {
	
	public StepMetaDataset(BemSchema stepSchema) {
		super(stepSchema);
	}
	
	public BemEntity getFileDescriptionEntity() throws BemEntityNotFoundException, BemTypeNotFoundException {
		return super.getAnyEntityByType(FileDescription.TYPE_NAME);		
	}

	public BemEntity getFileNameEntity() throws BemEntityNotFoundException, BemTypeNotFoundException {
		return super.getAnyEntityByType(FileName.TYPE_NAME);		
	}

	public BemEntity getFileSchemaEntity() throws BemEntityNotFoundException, BemTypeNotFoundException {
		return super.getAnyEntityByType(FileSchema.TYPE_NAME);		
	}


}
