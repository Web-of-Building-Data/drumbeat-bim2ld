package fi.aalto.cs.drumbeat.data.bem.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import fi.aalto.cs.drumbeat.data.bem.BemEntityNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.BemTypeNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class BemDataset {

	private BemSchema schema;
	private List<BemEntity> entities;
	private String name;
	private String language;

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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
		
	}
	
	public List<BemEntity> getAllEntities() {
		return entities;
	}
	
	public List<BemEntity> getAllEntities(String subSetId) {
		if (subSetId == null) {
			return entities;
		}
		return getAllEntitiesByType(subSetId, (BemEntityTypeInfo)null);
	}

	public List<BemEntity> getAllEntitiesByType(BemEntityTypeInfo entityType) {
		return getAllEntitiesByType(null, entityType);
	}
	
	public List<BemEntity> getAllEntitiesByType(String subSetId, BemEntityTypeInfo entityType) {
		
		return entities
				.stream()
				.filter(entity -> 
					(subSetId == null || entity.getSubSetId().equals(subSetId)) &&
					(entityType == null || entity.isInstanceOf(entityType)))
				.collect(Collectors.toList());
		
	}

	public List<BemEntity> getAllEntitiesByType(String entityTypeName) throws BemEntityNotFoundException, BemTypeNotFoundException {
		return getAllEntitiesByType(null, entityTypeName);
	}
	
	public List<BemEntity> getAllEntitiesByType(String subSetId, String entityTypeName) throws BemEntityNotFoundException, BemTypeNotFoundException {
		BemEntityTypeInfo entityType = getSchema().getEntityTypeInfo(entityTypeName);
		return getAllEntitiesByType(subSetId, entityType);
	}	
	
	public BemEntity getAnyEntityByType(BemEntityTypeInfo entityType) throws BemEntityNotFoundException {
		for (BemEntity entity : entities) {
			if (entity.isInstanceOf(entityType)) {
				return entity;
			}
		}
		throw new BemEntityNotFoundException("Entity with type " + entityType + " not found");
	}
	
	public BemEntity getAnyEntityByType(String entityTypeName) throws BemEntityNotFoundException, BemTypeNotFoundException {		
		BemEntityTypeInfo entityType = getSchema().getEntityTypeInfo(entityTypeName);
		return getAnyEntityByType(entityType);
	}
	
	public BemEntity getAnyEntityByTypeAndName(BemEntityTypeInfo entityType, String entityName) throws BemEntityNotFoundException {
		if (entityName == null) {
			throw new NullPointerException("entityName");
		}
		
		for (BemEntity entity : entities) {
			if (entity.isInstanceOf(entityType) && entityName.equals(entity.getName())) {
				return entity;
			}
		}
		throw new BemEntityNotFoundException("Entity with type '" + entityType + "' and name '" + name + "' not found");
	}

	public BemEntity getEntityLocalId(String localId) throws BemEntityNotFoundException {
		if (localId == null) {
			throw new NullPointerException("localId");
		}

		for (BemEntity entity : entities) {
			if (localId.equals(entity.getLocalId())) {
				return entity;
			}
		}
		throw new BemEntityNotFoundException("Entity with localId '" + localId + "' not found");
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

