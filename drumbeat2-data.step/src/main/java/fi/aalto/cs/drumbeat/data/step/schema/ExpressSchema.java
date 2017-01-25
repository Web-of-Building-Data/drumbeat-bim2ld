package fi.aalto.cs.drumbeat.data.step.schema;

import java.util.Arrays;

import fi.aalto.cs.drumbeat.data.bem.schema.BemEnumerationTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemPrimitiveTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemValueKindEnum;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.StepTypes;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.StepValues;

public class ExpressSchema extends BemSchema {

	/**
	 * Predefined literal types
	 */
	public final BemPrimitiveTypeInfo BINARY = new BemPrimitiveTypeInfo(this, StepTypes.BINARY, BemValueKindEnum.BINARY);
	public final BemPrimitiveTypeInfo DATETIME = new BemPrimitiveTypeInfo(this, StepTypes.DATETIME, BemValueKindEnum.DATETIME);
	public final BemPrimitiveTypeInfo INTEGER = new BemPrimitiveTypeInfo(this, StepTypes.INTEGER, BemValueKindEnum.INTEGER);
	public final BemPrimitiveTypeInfo NUMBER = new BemPrimitiveTypeInfo(this, StepTypes.NUMBER, BemValueKindEnum.NUMBER);
	public final BemPrimitiveTypeInfo REAL = new BemPrimitiveTypeInfo(this, StepTypes.REAL, BemValueKindEnum.REAL);
	public final BemPrimitiveTypeInfo STRING = new BemPrimitiveTypeInfo(this, StepTypes.STRING, BemValueKindEnum.STRING);
	
	public final BemEnumerationTypeInfo BOOLEAN = new ExpressLogicalTypeInfo(this, StepTypes.BOOLEAN, Arrays.asList(StepValues.TRUE, StepValues.FALSE));	
	public final BemEnumerationTypeInfo LOGICAL = new ExpressLogicalTypeInfo(this, StepTypes.LOGICAL, Arrays.asList(StepValues.TRUE, StepValues.FALSE, StepValues.UNKNOWN));
	
	public ExpressSchema() {
		addTypeInfo(BINARY);
//		addTypeInfo(BINARY32);
		addTypeInfo(DATETIME);
//		addTypeInfo(GUID);
		addTypeInfo(INTEGER);
		addTypeInfo(NUMBER);
		addTypeInfo(REAL);
		addTypeInfo(STRING);
//		addTypeInfo(STRING255);
		
		addTypeInfo(BOOLEAN); // true or false
		addTypeInfo(LOGICAL); // true, false or null	
	}

}
