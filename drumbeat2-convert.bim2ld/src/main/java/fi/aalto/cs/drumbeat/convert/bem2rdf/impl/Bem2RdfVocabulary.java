package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import fi.aalto.cs.drumbeat.data.bem.schema.BemCollectionKindEnum;

public class Bem2RdfVocabulary {
	
	public static class BuiltInOntology {
		
		public static final String Array = "Array";		
		public static final String Bag = "Bag";		
		public static final String Collection = "Collection";
		public static final String Defined = "Defined";		
		public static final String EmptyList = "EmptyList";		
		public static final String Entity = "Entity";		
//		public static final String EntityProperty = "EntityProperty";
		public static final String Enum = "Enum";		
//		public static final String InverseEntityProperty = "InverseEntityProperty";
		public static final String List = "List";		
		public static final String Select = "Select";		
		public static final String Set = "Set";		
		public static final String Slot = "Slot";
		public static final String SpecialValue = "SpecialValue";

		public static final String endIndex = "endIndex";
		public static final String index = "index";
//		public static final String isOrdered = "isOrdered";
		public static final String item = "item";
		public static final String itemType = "itemType";
		public static final String hasBinary = "hasBinary";
//		public static final String hasBoolean = "hasBoolean";
		public static final String hasContent = "hasContent";
		public static final String hasInteger = "hasInteger";
		public static final String hasLogical = "hasLogical";
		public static final String hasNext = "hasNext";		
		public static final String hasNumber = "hasNumber";
		public static final String hasReal = "hasReal";
		public static final String hasSetItem = "hasSetItem";
		public static final String hasString = "hasString";
		public static final String hasValue = "hasValue";
		public static final String next = "next";
		public static final String previous = "previous";
//		public static final String propertyIndex = "propertyIndex";
		public static final String slot = "slot";
		public static final String size = "size";
		public static final String startIndex = "startIndex";
//		public static final String value = "value";
		
		public static final String TRUE = "TRUE";
		public static final String FALSE = "FALSE";
		
		public static String getCollectionClass(BemCollectionKindEnum collectionKind) {
			if (collectionKind == BemCollectionKindEnum.List) {
				return List;				
			} else if (collectionKind == BemCollectionKindEnum.Set) {
				return Set;				
			} else if (collectionKind == BemCollectionKindEnum.Array) {
				return Array;				
			} else {
				return Bag;				
			}	
			
		}		
	}
	
	public static class Dataset {
		
		public static final String NAMED_NODE_ENTITY_URI_FORMAT = "%s";
		public static final String BLANK_NODE_ENTITY_URI_FORMAT = "BLANK_%s";
		
	}

}
