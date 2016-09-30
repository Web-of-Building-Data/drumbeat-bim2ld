package fi.aalto.cs.drumbeat.data.step.parsers;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbCollectionKindEnum;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;

class StepInternalParser {
	
	/**
	 * Parses collection kind strings: "LIST", "ARRAY", "SET", "BAG" 
	 * @param collectionKind
	 * @return the corresponding collection kind, or null if collectionKind param is not one of "LIST", "ARRAY", "SET", "BAG" 
	 */	
	static DrbCollectionKindEnum parseCollectionKind(String collectionKind) {
		switch(collectionKind.toUpperCase()) {
		case StepVocabulary.ExpressFormat.LIST:
			return DrbCollectionKindEnum.List;
		case StepVocabulary.ExpressFormat.ARRAY:
			return DrbCollectionKindEnum.Array;
		case StepVocabulary.ExpressFormat.SET:
			return DrbCollectionKindEnum.Set;
		case StepVocabulary.ExpressFormat.BAG:
			return DrbCollectionKindEnum.Bag;
		}
//		throw new DrbParserException("Unsupported collection kind: " + collectionKind);
		return null;
	}

}
