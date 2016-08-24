package fi.aalto.cs.drumbeat.data.edm.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

/**
 * A data schema, i.e. a container for data types. 
 * 
 * @author Nam Vu
 *
 */
public class DrbSchema {
	
	private final String name;
	private Map<String, DrbTypeInfo> allTypeInfoDictionary = new HashMap<String, DrbTypeInfo>();
	private Map<String, DrbTypeInfo> nonEntityTypeInfoDictionary = new HashMap<>();
	private Map<String, DrbEntityTypeInfo> entityTypeInfoDictionary = new HashMap<>();
	
	
	
	/**
	 * Creates a DrbSchema using the specified name
	 * 
	 * @param name the schema's name
	 */
	public DrbSchema(String name) {
		this.name = name;
	}

	/**
	 * Gets the schema's name
	 * 
	 * @return the schema name
	 */
	public String getName() {
		return name;
	}
	
	
	public Collection<DrbTypeInfo> getAllTypeInfos() {
		return allTypeInfoDictionary.values();
	}
	
	public Collection<DrbTypeInfo> getNonEntityTypeInfos() {
		return nonEntityTypeInfoDictionary.values();
	}

	public Collection<DrbEntityTypeInfo> getEntityTypeInfos() {	
		return entityTypeInfoDictionary.values();
	}
	
	
	/**
	 * Gets a type (entity or non-entity) by its name 
	 * 
	 * @param typeName the type name
	 * @return an instance of {@link DrbTypeInfo} with the specified name
	 * @throws DrbNotFoundException
	 */
	public DrbTypeInfo getTypeInfo(String typeName) throws DrbNotFoundException {
		String upperTypeName = typeName.toUpperCase();
		DrbTypeInfo result = allTypeInfoDictionary.get(upperTypeName);
		if (result == null) {
			throw new DrbNotFoundException(String.format("Type not found: '%s'", typeName));
		}
		return result;
	}

	/**
	 * Gets an entity type by its name
	 * 
	 * @param typeName the type name
	 * @return an instance of {@link DrbTypeInfo} with the specified name
	 * @throws DrbNotFoundException
	 */
	public DrbEntityTypeInfo getEntityTypeInfo(String typeName) throws DrbNotFoundException {
		String upperTypeName = typeName.toUpperCase();
		DrbEntityTypeInfo result = entityTypeInfoDictionary.get(upperTypeName);
		if (result == null) {
			throw new DrbNotFoundException(String.format("Entity type not found: '%s'", typeName));
		}
		return result;
	}
	
	/**
	 * Gets a non-entity type by its name
	 * 
	 * @param typeName the type name
	 * @return an instance of {@link DrbTypeInfo} with the specified name
	 * @throws DrbNotFoundException
	 */
	public DrbTypeInfo getNonEntityTypeInfo(String typeName) throws DrbNotFoundException {
		String upperCaseTypeName = typeName.toUpperCase();
		DrbTypeInfo result = nonEntityTypeInfoDictionary.get(upperCaseTypeName);
		if (result == null) {
			throw new DrbNotFoundException(String.format("Non-entity type not found: '%s'", typeName));
		}
		return result;
	}
	
	public void addNonEntityTypeInfo(DrbTypeInfo typeInfo) {
		String upperCaseTypeName = typeInfo.getName().toUpperCase();
		allTypeInfoDictionary.put(upperCaseTypeName, typeInfo);
		nonEntityTypeInfoDictionary.put(upperCaseTypeName, typeInfo);
	}
	
	public void addEntityTypeInfo(DrbEntityTypeInfo typeInfo) {
		String upperCaseTypeName = typeInfo.getName().toUpperCase();
		allTypeInfoDictionary.put(upperCaseTypeName, typeInfo);
		entityTypeInfoDictionary.put(upperCaseTypeName, typeInfo);
	}

}
