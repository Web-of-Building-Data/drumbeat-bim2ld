package fi.aalto.cs.drumbeat.data.step.parsers;

import fi.aalto.cs.drumbeat.data.bem.schema.BemCollectionKindEnum;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary;

class StepInternalParser {
	
	/**
	 * Parses collection kind strings: "LIST", "ARRAY", "SET", "BAG" 
	 * @param collectionKind
	 * @return the corresponding collection kind, or null if collectionKind param is not one of "LIST", "ARRAY", "SET", "BAG" 
	 */	
	static BemCollectionKindEnum parseCollectionKind(String collectionKind) {
		switch(collectionKind.toUpperCase()) {
		case StepVocabulary.ExpressFormat.LIST:
			return BemCollectionKindEnum.List;
		case StepVocabulary.ExpressFormat.ARRAY:
			return BemCollectionKindEnum.Array;
		case StepVocabulary.ExpressFormat.SET:
			return BemCollectionKindEnum.Set;
		case StepVocabulary.ExpressFormat.BAG:
			return BemCollectionKindEnum.Bag;
		}
//		throw new BemParserException("Unsupported collection kind: " + collectionKind);
		return null;
	}

}
