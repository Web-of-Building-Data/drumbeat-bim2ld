package fi.aalto.cs.drumbeat.ifc.data;

import java.util.Arrays;

import fi.aalto.cs.drumbeat.ifc.data.schema.*;

public class IfcSchema extends DrbSchema {

	/**
	 * Predefined schema entity types
	 *
	 */
	public final IfcEntityTypeInfo IFC_ELEMENT = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_ELEMENT);
	public final IfcEntityTypeInfo IFC_OBJECT = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_OBJECT);
	public final IfcEntityTypeInfo IFC_OBJECT_DEFINITION = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_OBJECT_DEFINITION);
	public final IfcEntityTypeInfo IFC_PRODUCT = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_PRODUCT);
	public final IfcEntityTypeInfo IFC_PROJECT = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_PROJECT);
	public final IfcEntityTypeInfo IFC_PROPERTY_DEFINITION = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_PROPERTY_DEFINITION);
	public final IfcEntityTypeInfo IFC_RELATIONSHIP = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_RELATIONSHIP);
	public final IfcEntityTypeInfo IFC_ROOT = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_ROOT);	
	public final IfcEntityTypeInfo IFC_SPACIAL_STRUCTURAL_ELEMENT = new IfcEntityTypeInfo(this, IfcVocabulary.TypeNames.IFC_SPACIAL_STRUCTURAL_ELEMENT);
	
	/**
	 * Predefined literal types
	 */
	public final DrbLiteralTypeInfo BINARY = new DrbLiteralTypeInfo(this, IfcVocabulary.TypeNames.BINARY, DrbTypeEnum.BINARY);
//	public final IfcLiteralTypeInfo BINARY32 = new IfcLiteralTypeInfo(this, IfcVocabulary.TypeNames.BINARY32, IfcTypeEnum.INTEGER);
//	public final IfcLiteralTypeInfo BOOLEAN = new IfcLiteralTypeInfo(this, IfcVocabulary.TypeNames.BOOLEAN, IfcTypeEnum.LOGICAL); // true or false
	public final DrbLiteralTypeInfo DATETIME = new DrbLiteralTypeInfo(this, IfcVocabulary.TypeNames.DATETIME, DrbTypeEnum.DATETIME);
//	public final IfcLiteralTypeInfo GUID = new IfcLiteralTypeInfo(this, IfcVocabulary.TypeNames.GUID, IfcTypeEnum.GUID);
	public final DrbLiteralTypeInfo INTEGER = new DrbLiteralTypeInfo(this, IfcVocabulary.TypeNames.INTEGER, DrbTypeEnum.INTEGER);
//	public final IfcLiteralTypeInfo LOGICAL = new IfcLiteralTypeInfo(this, IfcVocabulary.TypeNames.LOGICAL, IfcTypeEnum.LOGICAL); // true, false or null
	public final DrbLiteralTypeInfo NUMBER = new DrbLiteralTypeInfo(this, IfcVocabulary.TypeNames.NUMBER, DrbTypeEnum.NUMBER);
	public final DrbLiteralTypeInfo REAL = new DrbLiteralTypeInfo(this, IfcVocabulary.TypeNames.REAL, DrbTypeEnum.REAL);
	public final DrbLiteralTypeInfo STRING = new DrbLiteralTypeInfo(this, IfcVocabulary.TypeNames.STRING, DrbTypeEnum.STRING);
//	public final IfcLiteralTypeInfo STRING255 = new IfcLiteralTypeInfo(this, IfcVocabulary.TypeNames.STRING255, IfcTypeEnum.STRING);
	
	public final IfcDefinedTypeInfo IFC_BOOLEAN = new IfcDefinedTypeInfo(this, IfcVocabulary.TypeNames.IFC_BOOLEAN, IfcVocabulary.TypeNames.BOOLEAN);
	public final IfcDefinedTypeInfo IFC_INTEGER = new IfcDefinedTypeInfo(this, IfcVocabulary.TypeNames.IFC_INTEGER, IfcVocabulary.TypeNames.INTEGER);
	public final IfcDefinedTypeInfo IFC_LOGICAL = new IfcDefinedTypeInfo(this, IfcVocabulary.TypeNames.IFC_LOGICAL, IfcVocabulary.TypeNames.LOGICAL);
	public final IfcDefinedTypeInfo IFC_REAL = new IfcDefinedTypeInfo(this, IfcVocabulary.TypeNames.IFC_REAL, IfcVocabulary.TypeNames.REAL);
	public final IfcDefinedTypeInfo IFC_TIME_STAMP = new IfcDefinedTypeInfo(this, IfcVocabulary.TypeNames.IFC_TIME_STAMP, IfcVocabulary.TypeNames.DATETIME);
	public final IfcDefinedTypeInfo IFC_TEXT = new IfcDefinedTypeInfo(this, IfcVocabulary.TypeNames.IFC_TEXT, IfcVocabulary.TypeNames.STRING);
	
	public final IfcLogicalTypeInfo BOOLEAN = new IfcLogicalTypeInfo(this, IfcVocabulary.TypeNames.BOOLEAN, Arrays.asList(LogicalEnum.TRUE, LogicalEnum.FALSE));
	public final IfcLogicalTypeInfo LOGICAL = new IfcLogicalTypeInfo(this, IfcVocabulary.TypeNames.LOGICAL, Arrays.asList(LogicalEnum.TRUE, LogicalEnum.FALSE, LogicalEnum.UNKNOWN));
	
	
	public IfcSchema(String schemaVersion) {
		super(schemaVersion);
		

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
