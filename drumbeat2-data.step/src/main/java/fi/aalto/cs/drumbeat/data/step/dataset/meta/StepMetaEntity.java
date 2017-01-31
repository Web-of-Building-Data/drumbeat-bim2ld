package fi.aalto.cs.drumbeat.data.step.dataset.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemAttribute;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemCollectionValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;

public abstract class StepMetaEntity {

	private BemEntity entity;
	
	public StepMetaEntity(BemEntity entity) {
		this.entity = entity;
	}
	
	@SuppressWarnings({ "unchecked", "serial" })
	protected <T> List<T> getAttributeValues(String attributeName) {
		BemAttribute attribute = entity.getAttributeList().selectFirstByName(attributeName);
		if (attribute == null) {
			return null;
		}
		BemValue value = attribute.getValue();
		if (value instanceof BemCollectionValue<?>) {
			return ((BemCollectionValue<BemPrimitiveValue>)value).getSingleValues().stream().map(x -> (T)x.getValue()).collect(Collectors.toList());
		} else {
			return new ArrayList<T>() {{
				add((T)((BemPrimitiveValue)value).getValue());
			}};
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getAttributeValue(String attributeName) {
		BemAttribute attribute = entity.getAttributeList().selectFirstByName(attributeName);
		if (attribute == null) {
			return null;
		}
		
		BemValue value = attribute.getValue();
		if (value instanceof BemCollectionValue<?>) {
			return (T)((BemCollectionValue<BemPrimitiveValue>)value).getSingleValues().get(0).getValue();
		} else {
			return (T)((BemPrimitiveValue)value).getValue();
		}
	}
	

}
