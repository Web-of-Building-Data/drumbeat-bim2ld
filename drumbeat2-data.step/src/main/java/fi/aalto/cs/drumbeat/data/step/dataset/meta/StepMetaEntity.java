package fi.aalto.cs.drumbeat.data.step.dataset.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemAttribute;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemCollectionValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;

public abstract class StepMetaEntity {

	private BemEntity entity;
	private Map<String, BemValue> values;
	
	public StepMetaEntity(BemEntity entity) {
		this.entity = entity;
		values = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	protected <T> List<T> getValues(String attributeName) {
		try {
			List<BemAttribute> attributes = entity.getAttributes(attributeName);
			if (attributes != null) {
				return attributes.stream().map(x -> (T)x.getValue()).collect(Collectors.toList());
			}
			
		} catch (BemNotFoundException e) {
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getAttributeValue(String attributeName) {
		
		return null;
		
		
//		IfcLiteralValue value = (IfcLiteralValue)values.get(attributeName);
//
//		if (value == null) {
//			value = (IfcLiteralValue)entity.getLiteralAttributes().selectFirstByName(attributeName)
//					.getValue();
//			values.put(attributeName, value);
//		}
//		return (T)value.getValue();
	}
	

}
