package fi.aalto.cs.drumbeat.data.ifc.schema;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemSpecialValue;
//import fi.aalto.cs.drumbeat.data.bem.schema.*;
//import fi.aalto.cs.drumbeat.data.ifc.IfcVocabulary.IfcTypes;
//import fi.aalto.cs.drumbeat.data.step.StepVocabulary.StepTypes;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;


/**
 * Represents an IFC-EXPRESS schema.
 * 
 * @author vuhoan1
 *
 */
public class IfcSchema extends ExpressSchema {
	
	/**
	 * Initializes a new {@link IfcSchema}.
	 * 
	 * @param schemaVersion - the version of the schema.
	 */
	public IfcSchema() {
		this(null);		
	}
	
	public IfcSchema(String name) {
		super(name);
		addSpecialValue(BemSpecialValue.ANY);
		addSpecialValue(BemSpecialValue.NULL);
	}
	
}
