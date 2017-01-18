package fi.aalto.cs.drumbeat.data.bem.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public abstract class BemModel {

	private BemSchema schema;
	private List<BemEntity> entities;

	public BemModel(BemSchema schema) {
		this.schema = schema;
		entities = new ArrayList<>(getInitialEntitySize());
	}
	
	protected int getInitialEntitySize() {
		return 10;
	}
	
	public BemSchema getSchema() {
		return schema;
	}
	
	public List<BemEntity> getEntities() {
		return entities;
	}
	
	public List<BemEntity> getEntitiesByType(BemEntityTypeInfo entityType) {
		List<BemEntity> selectedEntities = new ArrayList<>();		
		for (BemEntity entity : entities) {
			if (entity.isInstanceOf(entityType)) {
				selectedEntities.add(entity);
			}
		}
		return selectedEntities;
	}
	
	public List<BemEntity> getEntitiesByType(String entityTypeName) throws DrbNotFoundException {
		BemEntityTypeInfo entityType = getSchema().getEntityTypeInfo(entityTypeName);
		return getEntitiesByType(entityType);
	}
	
	public BemEntity getFirstEntityByType(BemEntityTypeInfo entityType) throws DrbNotFoundException {
		for (BemEntity entity : entities) {
			if (entity.isInstanceOf(entityType)) {
				return entity;
			}
		}
		throw new DrbNotFoundException("Entity with type " + entityType + " not found");
	}
	
	public BemEntity getFirstEntityByType(String entityTypeName) throws DrbNotFoundException {		
		BemEntityTypeInfo entityType = getSchema().getEntityTypeInfo(entityTypeName);
		return getFirstEntityByType(entityType);
	}

	public void addEntities(Collection<BemEntity> entities) {
		this.entities.addAll(entities);	
	}

	public void addEntity(BemEntity entity) {
		entities.add(entity);	
	}
	
	public boolean removeEntity(BemEntity entity) {		
		return entities.remove(entity);		
	}

}
