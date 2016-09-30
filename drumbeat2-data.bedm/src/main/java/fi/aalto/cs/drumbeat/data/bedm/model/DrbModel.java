package fi.aalto.cs.drumbeat.data.bedm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;
import fi.aalto.cs.drumbeat.data.bedm.schema.*;

public abstract class DrbModel {

	private DrbSchema schema;
	private List<DrbEntity> entities;

	public DrbModel(DrbSchema schema) {
		this.schema = schema;
		entities = new ArrayList<>(getInitialEntitySize());
	}
	
	protected int getInitialEntitySize() {
		return 10;
	}
	
	public DrbSchema getSchema() {
		return schema;
	}
	
	public List<DrbEntity> getEntities() {
		return entities;
	}
	
	public List<DrbEntity> getEntitiesByType(DrbEntityTypeInfo entityType) {
		List<DrbEntity> selectedEntities = new ArrayList<>();		
		for (DrbEntity entity : entities) {
			if (entity.isInstanceOf(entityType)) {
				selectedEntities.add(entity);
			}
		}
		return selectedEntities;
	}
	
	public List<DrbEntity> getEntitiesByType(String entityTypeName) throws DrbNotFoundException {
		DrbEntityTypeInfo entityType = getSchema().getEntityTypeInfo(entityTypeName);
		return getEntitiesByType(entityType);
	}
	
	public DrbEntity getFirstEntityByType(DrbEntityTypeInfo entityType) throws DrbNotFoundException {
		for (DrbEntity entity : entities) {
			if (entity.isInstanceOf(entityType)) {
				return entity;
			}
		}
		throw new DrbNotFoundException("Entity with type " + entityType + " not found");
	}
	
	public DrbEntity getFirstEntityByType(String entityTypeName) throws DrbNotFoundException {		
		DrbEntityTypeInfo entityType = getSchema().getEntityTypeInfo(entityTypeName);
		return getFirstEntityByType(entityType);
	}

	public void addEntities(Collection<DrbEntity> entities) {
		this.entities.addAll(entities);	
	}

	public void addEntity(DrbEntity entity) {
		entities.add(entity);	
	}
	
	public boolean removeEntity(DrbEntity entity) {		
		return entities.remove(entity);		
	}

}
