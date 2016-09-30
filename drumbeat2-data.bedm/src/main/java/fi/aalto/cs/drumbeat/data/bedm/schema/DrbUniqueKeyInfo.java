package fi.aalto.cs.drumbeat.data.bedm.schema;

import java.io.Serializable;
import java.util.*;

public class DrbUniqueKeyInfo implements Comparable<DrbUniqueKeyInfo>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private SortedMap<String, DrbAttributeInfo> attributeInfoMap;
	
	public DrbUniqueKeyInfo() {
		attributeInfoMap = new TreeMap<>();
	}

	public SortedMap<String, DrbAttributeInfo> getAttributeInfoMap() {
		return attributeInfoMap;
	}
	
	public Collection<DrbAttributeInfo> getAttributeInfos() {
		return attributeInfoMap.values();
	}
	
	public void addAttributeInfo(DrbAttributeInfo attributeInfo) {
		attributeInfoMap.put(attributeInfo.getName(), attributeInfo);
	}
	
	public DrbAttributeInfo getAttributeInfo(String attributeName) {
		return attributeInfoMap.get(attributeName);
	}
	
	public DrbAttributeInfo getFirstAttributeInfo() {
		return attributeInfoMap.get(attributeInfoMap.firstKey());
	}

	public int size() {
		return attributeInfoMap.size();
	}

	@Override
	public int compareTo(DrbUniqueKeyInfo o) {
		int result = Integer.compare(attributeInfoMap.size(), o.attributeInfoMap.size());
		
		if (result == 0) {
			result = attributeInfoMap.firstKey().compareTo(o.attributeInfoMap.firstKey());
			assert(result != 0) : "Expected: attributeInfoMap.firstKey().compareTo(o.attributeInfoMap.firstKey()) != 0";
		}
		
		return result;
	}
	
}
