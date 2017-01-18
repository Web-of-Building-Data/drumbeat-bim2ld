package fi.aalto.cs.drumbeat.data.bem.schema;

public enum BemCollectionKindEnum {
	
	List,
	Array,
	Set,
	Bag;
	
	public boolean isSorted() {
		return this == List || this == Array; 
	}
	
	public boolean allowsDuplicatedItems() {
		return this != Set;
	}
	
}
