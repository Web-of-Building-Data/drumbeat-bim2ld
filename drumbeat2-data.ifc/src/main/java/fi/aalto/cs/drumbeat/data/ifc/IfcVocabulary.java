package fi.aalto.cs.drumbeat.data.ifc;

import fi.aalto.cs.drumbeat.common.io.FileManager;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;

public class IfcVocabulary extends StepVocabulary {
	
	
	public static class IfcTypes {
		
		public static final String IFC_ELEMENT = "IfcElement";
		public static final String IFC_OBJECT = "IfcObject";
		public static final String IFC_OBJECT_DEFINITION = "IfcObjectDefinition";
		public static final String IFC_OWNER_HISTORY = "IfcOwnerHistory";
		public static final String IFC_PRODUCT = "IfcProduct";
		public static final String IFC_PROJECT = "IfcProject";
		public static final String IFC_ROOT = "IfcRoot";
		public static final String IFC_SPACIAL_STRUCTURAL_ELEMENT = "IfcSpacialStructuralElement";
		public static final String IFC_RELATIONSHIP = "IfcRelationship";
		public static final String IFC_PROPERTY_DEFINITION = "IfcPropertyDefinition";
		
		public static final String IFC_BOOLEAN = "IfcBoolean";
		public static final String IFC_INTEGER = "IfcInteger";
		public static final String IFC_LOGICAL = "IfcLogical";
		public static final String IFC_REAL = "IfcReal";
		public static final String IFC_TEXT = "IfcText";
		public static final String IFC_TIME_STAMP = "IfcTimeStamp";	
		
	}
	
	public static class IfcAttributes {	
		public static final String GLOBAL_ID = "globalId";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String OWNER_HISTORY = "ownerHistory";
		public static final String LONG_NAME = "longName";
	}
	
	public static class IfcFormat {
		public static final String FILE_EXTENSION_IFC = "ifc";
		public static final String FILE_EXTENSION_IFC_ZIP = "ifczip";
		
	}
	
	public static class IfcXmlFormat {
		public static final String FILE_EXTENSION_IFC_XML = "ifcxml";
		public static final String FILE_EXTENSION_IFX = "ifx";
		public static final String FILE_EXTENSION_XML = FileManager.FILE_EXTENSION_XML;
	}
	
	
}
