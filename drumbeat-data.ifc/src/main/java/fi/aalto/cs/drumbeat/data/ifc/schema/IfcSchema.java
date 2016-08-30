package fi.aalto.cs.drumbeat.data.ifc.schema;

import java.util.*;

import fi.aalto.cs.drumbeat.data.edm.schema.*;
import fi.aalto.cs.drumbeat.data.ifc.IfcVocabulary.IfcTypes;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.StepTypes;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.StepValues;


/**
 * Represents an IFC-EXPRESS schema.
 * 
 * @author vuhoan1
 *
 */
public class IfcSchema extends DrbSchema {
	
	/**
	 * Predefined schema entity types
	 *
	 */
	public final DrbEntityTypeInfo IFC_ELEMENT = new DrbEntityTypeInfo(this, IfcTypes.IFC_ELEMENT);
	public final DrbEntityTypeInfo IFC_OBJECT = new DrbEntityTypeInfo(this, IfcTypes.IFC_OBJECT);
	public final DrbEntityTypeInfo IFC_OBJECT_DEFINITION = new DrbEntityTypeInfo(this, IfcTypes.IFC_OBJECT_DEFINITION);
	public final DrbEntityTypeInfo IFC_PRODUCT = new DrbEntityTypeInfo(this, IfcTypes.IFC_PRODUCT);
	public final DrbEntityTypeInfo IFC_PROJECT = new DrbEntityTypeInfo(this, IfcTypes.IFC_PROJECT);
	public final DrbEntityTypeInfo IFC_PROPERTY_DEFINITION = new DrbEntityTypeInfo(this, IfcTypes.IFC_PROPERTY_DEFINITION);
	public final DrbEntityTypeInfo IFC_RELATIONSHIP = new DrbEntityTypeInfo(this, IfcTypes.IFC_RELATIONSHIP);
	public final DrbEntityTypeInfo IFC_ROOT = new DrbEntityTypeInfo(this, IfcTypes.IFC_ROOT);	
	public final DrbEntityTypeInfo IFC_SPACIAL_STRUCTURAL_ELEMENT = new DrbEntityTypeInfo(this, IfcTypes.IFC_SPACIAL_STRUCTURAL_ELEMENT);
	
	/**
	 * Predefined literal types
	 */
	public final DrbPrimitiveTypeInfo BINARY = new DrbPrimitiveTypeInfo(this, StepTypes.BINARY, DrbTypeEnum.BINARY);
	public final DrbPrimitiveTypeInfo DATETIME = new DrbPrimitiveTypeInfo(this, StepTypes.DATETIME, DrbTypeEnum.DATETIME);
	public final DrbPrimitiveTypeInfo INTEGER = new DrbPrimitiveTypeInfo(this, StepTypes.INTEGER, DrbTypeEnum.INTEGER);
	public final DrbPrimitiveTypeInfo NUMBER = new DrbPrimitiveTypeInfo(this, StepTypes.NUMBER, DrbTypeEnum.NUMBER);
	public final DrbPrimitiveTypeInfo REAL = new DrbPrimitiveTypeInfo(this, StepTypes.REAL, DrbTypeEnum.REAL);
	public final DrbPrimitiveTypeInfo STRING = new DrbPrimitiveTypeInfo(this, StepTypes.STRING, DrbTypeEnum.STRING);
	
	public final DrbDefinedTypeInfo IFC_BOOLEAN = new DrbDefinedTypeInfo(this, IfcTypes.IFC_BOOLEAN, StepTypes.BOOLEAN);
	public final DrbDefinedTypeInfo IFC_INTEGER = new DrbDefinedTypeInfo(this, IfcTypes.IFC_INTEGER, StepTypes.INTEGER);
	public final DrbDefinedTypeInfo IFC_LOGICAL = new DrbDefinedTypeInfo(this, IfcTypes.IFC_LOGICAL, StepTypes.LOGICAL);
	public final DrbDefinedTypeInfo IFC_REAL = new DrbDefinedTypeInfo(this, IfcTypes.IFC_REAL, StepTypes.REAL);
	public final DrbDefinedTypeInfo IFC_TIME_STAMP = new DrbDefinedTypeInfo(this, IfcTypes.IFC_TIME_STAMP, StepTypes.DATETIME);
	public final DrbDefinedTypeInfo IFC_TEXT = new DrbDefinedTypeInfo(this, IfcTypes.IFC_TEXT, StepTypes.STRING);
	
	public final DrbEnumerationTypeInfo BOOLEAN = new DrbEnumerationTypeInfo(this, StepTypes.BOOLEAN, Arrays.asList(StepValues.TRUE, StepValues.FALSE));
	public final DrbEnumerationTypeInfo LOGICAL = new DrbEnumerationTypeInfo(this, StepTypes.LOGICAL, Arrays.asList(StepValues.TRUE, StepValues.FALSE, StepValues.UNKNOWN));
	
	
	
	/**
	 * Initializes a new {@link IfcSchema}.
	 * 
	 * @param schemaVersion - the version of the schema.
	 */
	public IfcSchema(String schemaVersion) {
		super(schemaVersion);

		//
		// add some predefined types
		//
		addEntityTypeInfo(IFC_ELEMENT);
		addEntityTypeInfo(IFC_OBJECT);
		addEntityTypeInfo(IFC_OBJECT_DEFINITION);
		addEntityTypeInfo(IFC_PRODUCT);
		addEntityTypeInfo(IFC_PROJECT);
		addEntityTypeInfo(IFC_PROPERTY_DEFINITION);
		addEntityTypeInfo(IFC_RELATIONSHIP);
		addEntityTypeInfo(IFC_ROOT);
		addEntityTypeInfo(IFC_SPACIAL_STRUCTURAL_ELEMENT);
		
		addNonEntityTypeInfo(BINARY);
//		addNonEntityTypeInfo(BINARY32);
		addNonEntityTypeInfo(DATETIME);
//		addNonEntityTypeInfo(GUID);
		addNonEntityTypeInfo(INTEGER);
		addNonEntityTypeInfo(NUMBER);
		addNonEntityTypeInfo(REAL);
		addNonEntityTypeInfo(STRING);
//		addNonEntityTypeInfo(STRING255);
		
		addNonEntityTypeInfo(IFC_BOOLEAN);
		addNonEntityTypeInfo(IFC_INTEGER);
		addNonEntityTypeInfo(IFC_LOGICAL);
		addNonEntityTypeInfo(IFC_REAL);
		addNonEntityTypeInfo(IFC_TEXT);
		addNonEntityTypeInfo(IFC_TIME_STAMP);
		
		addNonEntityTypeInfo(BOOLEAN); // true or false
		addNonEntityTypeInfo(LOGICAL); // true, false or null
	}
	
}
