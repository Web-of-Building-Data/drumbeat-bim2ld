package fi.aalto.cs.drumbeat.data.step.schema;

import java.util.Arrays;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbEnumerationTypeInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbPrimitiveTypeInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbSchema;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbTypeEnum;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.StepTypes;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.StepValues;

public class ExpressSchema extends DrbSchema {

	/**
	 * Predefined literal types
	 */
	public final DrbPrimitiveTypeInfo BINARY = new DrbPrimitiveTypeInfo(this, StepTypes.BINARY, DrbTypeEnum.BINARY);
	public final DrbPrimitiveTypeInfo DATETIME = new DrbPrimitiveTypeInfo(this, StepTypes.DATETIME, DrbTypeEnum.DATETIME);
	public final DrbPrimitiveTypeInfo INTEGER = new DrbPrimitiveTypeInfo(this, StepTypes.INTEGER, DrbTypeEnum.INTEGER);
	public final DrbPrimitiveTypeInfo NUMBER = new DrbPrimitiveTypeInfo(this, StepTypes.NUMBER, DrbTypeEnum.NUMBER);
	public final DrbPrimitiveTypeInfo REAL = new DrbPrimitiveTypeInfo(this, StepTypes.REAL, DrbTypeEnum.REAL);
	public final DrbPrimitiveTypeInfo STRING = new DrbPrimitiveTypeInfo(this, StepTypes.STRING, DrbTypeEnum.STRING);
	
	public final DrbEnumerationTypeInfo BOOLEAN = new DrbEnumerationTypeInfo(this, StepTypes.BOOLEAN, Arrays.asList(StepValues.TRUE, StepValues.FALSE));
	public final DrbEnumerationTypeInfo LOGICAL = new DrbEnumerationTypeInfo(this, StepTypes.LOGICAL, Arrays.asList(StepValues.TRUE, StepValues.FALSE, StepValues.UNKNOWN));
	
	public ExpressSchema() {
		addNonEntityTypeInfo(BINARY);
//		addNonEntityTypeInfo(BINARY32);
		addNonEntityTypeInfo(DATETIME);
//		addNonEntityTypeInfo(GUID);
		addNonEntityTypeInfo(INTEGER);
		addNonEntityTypeInfo(NUMBER);
		addNonEntityTypeInfo(REAL);
		addNonEntityTypeInfo(STRING);
//		addNonEntityTypeInfo(STRING255);
		
		addNonEntityTypeInfo(BOOLEAN); // true or false
		addNonEntityTypeInfo(LOGICAL); // true, false or null	
	}

}
