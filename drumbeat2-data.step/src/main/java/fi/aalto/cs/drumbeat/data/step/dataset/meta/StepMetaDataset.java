package fi.aalto.cs.drumbeat.data.step.dataset.meta;

import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;

public class StepMetaDataset extends BemDataset {
	
	private StepMetaEntity_FileDescription fileDescription;
	private StepMetaEntity_FileName fileName;
	private StepMetaEntity_FileSchema fileSchema;

	public StepMetaDataset(BemSchema stepSchema) {
		super(stepSchema);
	}

	public StepMetaEntity_FileDescription getFileDescription() throws BemNotFoundException {
		if (fileDescription == null) {
			BemEntity entity = super.getFirstEntityByType(StepVocabulary.SpfFormat.Header.FileDescription.TYPE_NAME);
			fileDescription = new StepMetaEntity_FileDescription(entity);
		}
		return fileDescription;
	}

	public StepMetaEntity_FileName getFileName() throws BemNotFoundException {
		if (fileName == null) {
			BemEntity entity = super.getFirstEntityByType(StepVocabulary.SpfFormat.Header.FileName.TYPE_NAME);
			fileName = new StepMetaEntity_FileName(entity);
		}
		return fileName;
	}

	public StepMetaEntity_FileSchema getFileSchema() throws BemNotFoundException {
		if (fileSchema == null) {
			BemEntity entity = super.getFirstEntityByType(StepVocabulary.SpfFormat.Header.FileSchema.TYPE_NAME);
			fileSchema = new StepMetaEntity_FileSchema(entity);
		}
		return fileSchema;
	}
	
	

}
