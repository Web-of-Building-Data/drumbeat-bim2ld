package fi.aalto.cs.drumbeat.data.bedm.model;

import java.io.Serializable;
import java.util.*;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbAttributeInfo;


public class DrbUniqueKeyValue implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SortedMap<DrbAttributeInfo, DrbValue> valueMap;
	
	public DrbUniqueKeyValue() {
		valueMap = new TreeMap<>();
	}
	
	public SortedMap<DrbAttributeInfo, DrbValue> getValueMap() {
		return valueMap;
	}
	
	public void addValue(DrbAttributeInfo attributeInfo, DrbValue value) {
		valueMap.put(attributeInfo, value);
	}
	
	public int size() {
		return valueMap.size();
	}
	
	public boolean isEmpty() {
		return valueMap.isEmpty();
	}

}
