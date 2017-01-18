package fi.aalto.cs.drumbeat.data.bem.dataset;

import java.io.Serializable;
import java.util.*;

import fi.aalto.cs.drumbeat.data.bem.schema.BemAttributeInfo;


public class BemUniqueKeyValue implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SortedMap<BemAttributeInfo, BemValue> valueMap;
	
	public BemUniqueKeyValue() {
		valueMap = new TreeMap<>();
	}
	
	public SortedMap<BemAttributeInfo, BemValue> getValueMap() {
		return valueMap;
	}
	
	public void addValue(BemAttributeInfo attributeInfo, BemValue value) {
		valueMap.put(attributeInfo, value);
	}
	
	public int size() {
		return valueMap.size();
	}
	
	public boolean isEmpty() {
		return valueMap.isEmpty();
	}

}
