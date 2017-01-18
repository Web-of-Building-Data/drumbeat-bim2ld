package fi.aalto.cs.drumbeat.data.bem.schema;

import java.io.Serializable;
import java.util.*;

public class BemUniqueKeyInfo implements Comparable<BemUniqueKeyInfo>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private SortedMap<String, BemAttributeInfo> attributeInfoMap;
	
	public BemUniqueKeyInfo() {
		attributeInfoMap = new TreeMap<>();
	}

	public SortedMap<String, BemAttributeInfo> getAttributeInfoMap() {
		return attributeInfoMap;
	}
	
	public Collection<BemAttributeInfo> getAttributeInfos() {
		return attributeInfoMap.values();
	}
	
	public void addAttributeInfo(BemAttributeInfo attributeInfo) {
		attributeInfoMap.put(attributeInfo.getName(), attributeInfo);
	}
	
	public BemAttributeInfo getAttributeInfo(String attributeName) {
		return attributeInfoMap.get(attributeName);
	}
	
	public BemAttributeInfo getFirstAttributeInfo() {
		return attributeInfoMap.get(attributeInfoMap.firstKey());
	}

	public int size() {
		return attributeInfoMap.size();
	}

	@Override
	public int compareTo(BemUniqueKeyInfo o) {
		int result = Integer.compare(attributeInfoMap.size(), o.attributeInfoMap.size());
		
		if (result == 0) {
			result = attributeInfoMap.firstKey().compareTo(o.attributeInfoMap.firstKey());
			assert(result != 0) : "Expected: attributeInfoMap.firstKey().compareTo(o.attributeInfoMap.firstKey()) != 0";
		}
		
		return result;
	}
	
}
