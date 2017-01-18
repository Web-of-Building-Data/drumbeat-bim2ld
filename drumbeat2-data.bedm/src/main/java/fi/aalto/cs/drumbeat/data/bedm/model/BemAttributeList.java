package fi.aalto.cs.drumbeat.data.bem.model;

import java.util.ArrayList;
import java.util.List;
//import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fi.aalto.cs.drumbeat.common.collections.SortedList;
import fi.aalto.cs.drumbeat.data.bem.schema.BemAttributeInfo;


public class BemAttributeList extends SortedList<BemAttribute> {

	/**
	 * Needed for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Selects attribute by a specified {@link BemAttributeInfo} and {@link BemValue}.
	 * @param attributeInfo
	 * @param value
	 * @return
	 */
	public BemAttribute select(BemAttributeInfo attributeInfo, BemValue value) {
		
		for (BemAttribute attribute : this) {
			if (attribute.getAttributeInfo().equals(attributeInfo) && attribute.getValue().equals(value)) {
				return attribute;
			}
		}
		
		return null;
	}
	

	/**
	 * Selects attributes by a specified {@link BemAttributeInfo}.
	 * @param attributeInfo
	 * @return
	 */
	public BemAttribute selectFirst(BemAttributeInfo attributeInfo) {
		
		for (BemAttribute attribute : this) {
			if (attribute.getAttributeInfo().equals(attributeInfo)) {
				return attribute;
			}
		}
		
		return null;
	}	
	
	/**
	 * Selects attributes by a specified {@link BemAttributeInfo}.
	 * @param attributeInfo
	 * @return
	 */
	public BemAttribute selectFirstByName(String attributeName) {
		
		for (BemAttribute attribute : this) {
			if (attribute.getAttributeInfo().getName().equalsIgnoreCase(attributeName)) {
				return attribute;
			}
		}
		
		return null;
	}	

	
	/**
	 * Selects attributes by a specified {@link BemAttributeInfo}.
	 * @param attributeInfo
	 * @return
	 */
	public List<BemAttribute> selectAllByName(String attributeName) {		
		return super.stream()
				.filter(attribute -> attribute.getAttributeInfo().getName().equalsIgnoreCase(attributeName))
				.collect(Collectors.toList());		
	}	

	/**
	 * Selects attributes by a specified {@link BemAttributeInfo}.
	 * @param attributeInfo
	 * @return
	 */
	public List<BemAttribute> selectAll(BemAttributeInfo attributeInfo) {
		
//		if (!attributeInfo.isMultiple()) {
			
			//
			// single attribute
			//			
			BemAttribute attribute = selectFirst(attributeInfo);
			if (attribute != null) {
				List<BemAttribute> attributes = new ArrayList<BemAttribute>(); 
				attributes.add(attribute);
				return attributes;				
			} else {
				return null;
			}
			
//		} else {
//			
//			//
//			// multiple attribute (set of attributes)
//			//			
//			for (ListIterator<BemAttribute> it = this.listIterator(); it.hasNext();) {
//				
//				//
//				// find the first appropriate attribute
//				//
//				BemAttribute attribute = it.next();
//				if (attribute.getAttributeInfo().equals(attributeInfo)) {
//					List<BemAttribute> attributes = new ArrayList<BemAttribute>(); 			
//					attributes.add(attribute);
//					
//					//
//					// find all other appropriate attributes (they should stand in a row)
//					//
//					while (it.hasNext()) {
//						attribute = it.next();
//						if (attribute.getAttributeInfo().equals(attributeInfo)) {
//							attributes.add(attribute);
//						} else {
//							break;
//						}
//					}
//					
//					return attributes;
//				}
//			}
//			
//			return null;
//		}		
	}	
	
	/**
	 * Selects attribute by a specified {@link BemAttributeInfo} and {@link BemValue}.
	 * @param attributeInfo
	 * @param value
	 * @return
	 */
	public BemAttribute remove(BemAttributeInfo attributeInfo, BemValue value) {
		
		for (BemAttribute attribute : this) {
			if (attribute.getAttributeInfo().equals(attributeInfo) && attribute.getValue().equals(value)) {
				remove(attribute);
				return attribute;
			}
		}
		
		return null;
	}
	
	
	public Map<BemAttributeInfo, BemAttributeList> toMap() {
		Map<BemAttributeInfo, BemAttributeList> map = new TreeMap<>();
		for (BemAttribute attribute : this) {
			BemAttributeInfo attributeInfo = attribute.getAttributeInfo();
			BemAttributeList list = map.get(attributeInfo);
			if (list == null) {
				list = new BemAttributeList();
				map.put(attributeInfo, list);				
			}
			list.add(attribute);
		}
		return map;
	}

}
