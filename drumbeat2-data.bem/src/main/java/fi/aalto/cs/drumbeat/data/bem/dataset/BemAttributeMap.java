package fi.aalto.cs.drumbeat.data.bem.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.*;

import fi.aalto.cs.drumbeat.data.bem.BemAttributeNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.BemValueTypeException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemAttributeInfo;

public class BemAttributeMap {
	
	private final BemEntity entity;
	private final boolean isInverseAttributeMap;
	private final ListMultimap<BemAttributeInfo, BemValue> map;

	public BemAttributeMap(BemEntity entity, boolean isInverseAttributeMap) {
		this.entity = entity;
		this.isInverseAttributeMap = isInverseAttributeMap;
		map = LinkedListMultimap.create();
	}
	
	public BemEntity getEntity() {
		return entity;
	}
	
	public Map<BemAttributeInfo, Collection<BemValue>> asMap() {
		return map.asMap();
	}
	
	public Collection<Entry<BemAttributeInfo, BemValue>> entries() {
		return map.entries();
	}
	
	public void clear() {
		map.clear();
	}

	public void add(BemAttributeInfo attributeInfo, BemValue value) {
		map.put(attributeInfo, value);
	}

	public List<BemValue> get(BemAttributeInfo attributeInfo) {
		return map.get(attributeInfo);
	}	

	public List<BemValue> getAll(String attributeName) throws BemAttributeNotFoundException {
		BemAttributeInfo attributeInfo = isInverseAttributeMap ?
				entity.getTypeInfo().getInverseAttributeInfo(attributeName) : entity.getTypeInfo().getAttributeInfo(attributeName);				
		return get(attributeInfo);
	}
	
	public BemValue getAny(BemAttributeInfo attributeInfo) {
		List<BemValue> values = map.get(attributeInfo);  
		return !values.isEmpty() ? values.get(0) : null;
	}	
	
	/**
	 * Gets first available attribute value by name 
	 * @param attributeName
	 * @return the first attribute value if found, otherwise null 
	 * @throws BemNotFoundException if attribute name is not found
	 */
	public BemValue getAny(String attributeName) throws BemAttributeNotFoundException {
		BemAttributeInfo attributeInfo = isInverseAttributeMap ?
				entity.getTypeInfo().getInverseAttributeInfo(attributeName) : entity.getTypeInfo().getAttributeInfo(attributeName);
		return getAny(attributeInfo);
	}	

	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public int size() {
		return map.size();
	}

	public Collection<BemValue> getAll() {
		return map.values();
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getAllPrimitiveValues(String attributeName) throws BemValueTypeException, BemAttributeNotFoundException {
		
		List<BemValue> values = getAll(attributeName);
		if (values != null) {
			
			if (values.size() == 1 && values.get(0) instanceof BemCollectionValue<?>) {				
				values = ((BemCollectionValue<BemValue>)values.get(0)).getSingleValues();				
			}
				
			List<T> primitiveValues = new ArrayList<>(values.size());
			for (BemValue value : values) {
				if (value instanceof BemPrimitiveValue) {
					primitiveValues.add((T)((BemPrimitiveValue)value).getValue());
				} else {
					throw new BemValueTypeException("Primitive value expected: " + value);
				}
			}
			return primitiveValues;
			
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAnyPrimitiveValue(String attributeName) {
		try {
			BemValue value = entity.getAttributeMap().getAny(attributeName);
			if (value instanceof BemCollectionValue<?>) {
				return (T)((BemCollectionValue<BemPrimitiveValue>)value).getSingleValues().get(0).getValue();
			} else {
				return (T)((BemPrimitiveValue)value).getValue();
			}
		} catch (BemAttributeNotFoundException e) {
			return null;
		}
	}	
	

}
