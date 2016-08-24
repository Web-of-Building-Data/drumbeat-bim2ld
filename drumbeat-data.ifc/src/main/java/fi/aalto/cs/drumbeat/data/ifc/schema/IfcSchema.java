package fi.aalto.cs.drumbeat.data.ifc.schema;

import java.util.*;

import fi.aalto.cs.drumbeat.data.edm.schema.*;
import fi.aalto.cs.drumbeat.data.ifc.IfcVocabulary;
import fi.aalto.cs.drumbeat.data.ifc.IfcVocabulary.LogicalValues;


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
	public final DrbEntityTypeInfo IFC_ELEMENT = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_ELEMENT);
	public final DrbEntityTypeInfo IFC_OBJECT = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_OBJECT);
	public final DrbEntityTypeInfo IFC_OBJECT_DEFINITION = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_OBJECT_DEFINITION);
	public final DrbEntityTypeInfo IFC_PRODUCT = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_PRODUCT);
	public final DrbEntityTypeInfo IFC_PROJECT = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_PROJECT);
	public final DrbEntityTypeInfo IFC_PROPERTY_DEFINITION = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_PROPERTY_DEFINITION);
	public final DrbEntityTypeInfo IFC_RELATIONSHIP = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_RELATIONSHIP);
	public final DrbEntityTypeInfo IFC_ROOT = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_ROOT);	
	public final DrbEntityTypeInfo IFC_SPACIAL_STRUCTURAL_ELEMENT = new DrbEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_SPACIAL_STRUCTURAL_ELEMENT);
	
	/**
	 * Predefined literal types
	 */
	public final DrbPrimitiveTypeInfo BINARY = new DrbPrimitiveTypeInfo(this, IfcVocabulary.TypeNames.BINARY, DrbTypeEnum.BINARY);
	public final DrbPrimitiveTypeInfo DATETIME = new DrbPrimitiveTypeInfo(this, IfcVocabulary.TypeNames.DATETIME, DrbTypeEnum.DATETIME);
	public final DrbPrimitiveTypeInfo INTEGER = new DrbPrimitiveTypeInfo(this, IfcVocabulary.TypeNames.INTEGER, DrbTypeEnum.INTEGER);
	public final DrbPrimitiveTypeInfo NUMBER = new DrbPrimitiveTypeInfo(this, IfcVocabulary.TypeNames.NUMBER, DrbTypeEnum.NUMBER);
	public final DrbPrimitiveTypeInfo REAL = new DrbPrimitiveTypeInfo(this, IfcVocabulary.TypeNames.REAL, DrbTypeEnum.REAL);
	public final DrbPrimitiveTypeInfo STRING = new DrbPrimitiveTypeInfo(this, IfcVocabulary.TypeNames.STRING, DrbTypeEnum.STRING);
	
	public final DrbReferenceTypeInfo IFC_BOOLEAN = new DrbReferenceTypeInfo(this, IfcVocabulary.TypeNames.IFC_BOOLEAN, IfcVocabulary.TypeNames.BOOLEAN);
	public final DrbReferenceTypeInfo IFC_INTEGER = new DrbReferenceTypeInfo(this, IfcVocabulary.TypeNames.IFC_INTEGER, IfcVocabulary.TypeNames.INTEGER);
	public final DrbReferenceTypeInfo IFC_LOGICAL = new DrbReferenceTypeInfo(this, IfcVocabulary.TypeNames.IFC_LOGICAL, IfcVocabulary.TypeNames.LOGICAL);
	public final DrbReferenceTypeInfo IFC_REAL = new DrbReferenceTypeInfo(this, IfcVocabulary.TypeNames.IFC_REAL, IfcVocabulary.TypeNames.REAL);
	public final DrbReferenceTypeInfo IFC_TIME_STAMP = new DrbReferenceTypeInfo(this, IfcVocabulary.TypeNames.IFC_TIME_STAMP, IfcVocabulary.TypeNames.DATETIME);
	public final DrbReferenceTypeInfo IFC_TEXT = new DrbReferenceTypeInfo(this, IfcVocabulary.TypeNames.IFC_TEXT, IfcVocabulary.TypeNames.STRING);
	
	public final DrbEnumerationTypeInfo BOOLEAN = new DrbEnumerationTypeInfo(this, IfcVocabulary.TypeNames.BOOLEAN, Arrays.asList(LogicalValues.TRUE, LogicalValues.FALSE));
	public final DrbEnumerationTypeInfo LOGICAL = new DrbEnumerationTypeInfo(this, IfcVocabulary.TypeNames.LOGICAL, Arrays.asList(LogicalValues.TRUE, LogicalValues.FALSE, LogicalValues.UNKNOWN));
	
	
	
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
