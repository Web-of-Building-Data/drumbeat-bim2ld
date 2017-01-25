package fi.aalto.cs.drumbeat.data.step.dataset.meta;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import fi.aalto.cs.drumbeat.common.converters.CalendarConverter;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat;

public class StepMetaEntity_FileName extends StepMetaEntity {
	
	public StepMetaEntity_FileName(BemEntity entity) {
		super(entity);
	}
	
	public String getName() {
		return getAttributeValue(SpfFormat.Header.FileName.NAME);
	}

	public Calendar getTimeStamp() {
		try {
			return CalendarConverter.xsdDateTimeToCalendar(getAttributeValue(SpfFormat.Header.FileName.TIME_STAMP));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} 
	}

	public List<String> getAuthors() {
		return getAttributeValues(SpfFormat.Header.FileName.AUTHOR);
	}
	
	public List<String> getOrganizations() {
		return getAttributeValues(SpfFormat.Header.FileName.ORGANIZATION);
	}
	
	public String getPreprocessorVersion() {
		return getAttributeValue(SpfFormat.Header.FileName.PREPROCESSOR_VERSION);
	}

	public String getOriginatingSystem() {
		return getAttributeValue(SpfFormat.Header.FileName.ORIGINATING_SYSTEM);
	}

	public String getAuthorization() {
		return getAttributeValue(SpfFormat.Header.FileName.AUTHORIZATION);
	}

}
