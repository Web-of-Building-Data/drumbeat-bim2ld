package fi.aalto.cs.drumbeat.data.step.schema;

import java.util.Arrays;

import fi.aalto.cs.drumbeat.data.bem.BemTypeAlreadyExistsException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemLogicalEnum;
import fi.aalto.cs.drumbeat.data.bem.schema.BemLogicalTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemPrimitiveTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemValueKindEnum;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;

public class ExpressSchema extends BemSchema {

	/**
	 * Predefined literal types
	 */
	public final BemPrimitiveTypeInfo BINARY = new BemPrimitiveTypeInfo(this, StepVocabulary.StepTypes.BINARY, BemValueKindEnum.BINARY);
	public final BemPrimitiveTypeInfo DATETIME = new BemPrimitiveTypeInfo(this, StepVocabulary.StepTypes.DATETIME, BemValueKindEnum.DATETIME);
	public final BemPrimitiveTypeInfo INTEGER = new BemPrimitiveTypeInfo(this, StepVocabulary.StepTypes.INTEGER, BemValueKindEnum.INTEGER);
	public final BemPrimitiveTypeInfo NUMBER = new BemPrimitiveTypeInfo(this, StepVocabulary.StepTypes.NUMBER, BemValueKindEnum.NUMBER);
	public final BemPrimitiveTypeInfo REAL = new BemPrimitiveTypeInfo(this, StepVocabulary.StepTypes.REAL, BemValueKindEnum.REAL);
	public final BemPrimitiveTypeInfo STRING = new BemPrimitiveTypeInfo(this, StepVocabulary.StepTypes.STRING, BemValueKindEnum.STRING);
	
//	public final BemPrimitiveTypeInfo BOOLEAN = new BemPrimitiveTypeInfo(this, StepVocabulary.StepTypes.BOOLEAN, BemValueKindEnum.BOOLEAN);	
	public final BemLogicalTypeInfo BOOLEAN = new BemLogicalTypeInfo(
			this,
			StepVocabulary.StepTypes.BOOLEAN,
			Arrays.asList(BemLogicalEnum.FALSE, BemLogicalEnum.TRUE));
	
	
	public final BemLogicalTypeInfo LOGICAL = new BemLogicalTypeInfo(
			this,
			StepVocabulary.StepTypes.LOGICAL,
			Arrays.asList(BemLogicalEnum.FALSE, BemLogicalEnum.TRUE, BemLogicalEnum.UNKNOWN));
	
	public ExpressSchema() {
		this(null);
	}
	
	public ExpressSchema(String name) {
		super(name);
		
		setLanguage(StepVocabulary.ExpressFormat.EXPRESS);
		
		BINARY.setBuiltInType(true);
		DATETIME.setBuiltInType(true);
		INTEGER.setBuiltInType(true);
		NUMBER.setBuiltInType(true);
		REAL.setBuiltInType(true);
		STRING.setBuiltInType(true);		
		BOOLEAN.setBuiltInType(true);
		LOGICAL.setBuiltInType(true);
		
		try {
			addTypeInfo(BINARY);
			addTypeInfo(DATETIME);
			addTypeInfo(INTEGER);
			addTypeInfo(NUMBER);
			addTypeInfo(REAL);
			addTypeInfo(STRING);		
			addTypeInfo(BOOLEAN); // true or false
			addTypeInfo(LOGICAL); // true, false or null
		} catch (BemTypeAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
	}
	

}
