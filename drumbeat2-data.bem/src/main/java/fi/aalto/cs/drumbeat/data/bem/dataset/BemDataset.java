package fi.aalto.cs.drumbeat.data.bem.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.aalto.cs.drumbeat.data.bem.BemEntityNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.BemTypeNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class BemDataset {

	private BemSchema schema;
	private List<BemEntity> entities;

	public BemDataset(BemSchema schema) {
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
	
	public List<BemEntity> getAllEntitiesByType(BemEntityTypeInfo entityType) {
		List<BemEntity> selectedEntities = new ArrayList<>();		
		for (BemEntity entity : entities) {
			if (entity.isInstanceOf(entityType)) {
				selectedEntities.add(entity);
			}
		}
		return selectedEntities;
	}
	
	public List<BemEntity> getEntitiesByType(String entityTypeName) throws BemEntityNotFoundException, BemTypeNotFoundException {
		BemEntityTypeInfo entityType = getSchema().getEntityTypeInfo(entityTypeName);
		return getAllEntitiesByType(entityType);
	}
	
	public BemEntity getFirstEntityByType(BemEntityTypeInfo entityType) throws BemEntityNotFoundException {
		for (BemEntity entity : entities) {
			if (entity.isInstanceOf(entityType)) {
				return entity;
			}
		}
		throw new BemEntityNotFoundException("Entity with type " + entityType + " not found");
	}
	
	public BemEntity getAnyEntityByType(String entityTypeName) throws BemEntityNotFoundException, BemTypeNotFoundException {		
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
