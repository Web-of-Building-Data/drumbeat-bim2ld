package fi.aalto.cs.drumbeat.data.edm.schema;

public enum DrbCollectionKindEnum {
	
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
