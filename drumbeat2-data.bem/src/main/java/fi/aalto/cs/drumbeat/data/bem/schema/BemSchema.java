package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;

/**
 * A data schema, i.e. a container for data types. 
 * 
 * @author Nam Vu
 *
 */
public class BemSchema {
	
	private String name;
	private BemSchema header;
	private Map<String, BemTypeInfo> allTypeInfoDictionary;
	private Map<String, BemEntityTypeInfo> entityTypeInfoDictionary;
	
	
	public BemSchema() {
		allTypeInfoDictionary = new HashMap<>();
		entityTypeInfoDictionary = new HashMap<>();
	}
	
	/**
	 * Gets the schema's name
	 * 
	 * @return the schema name
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the header schema.
	 * @return
	 */
	public BemSchema getHeader() {
		return header;
	}
	
	public void setHeader(BemSchema header) {
		this.header = header;
	}
	
	
	public Collection<BemTypeInfo> getAllTypeInfos() {
		return allTypeInfoDictionary.values();
	}
	
	public Collection<BemEntityTypeInfo> getEntityTypeInfos() {	
		return entityTypeInfoDictionary.values();
	}
	
	
	/**
	 * Gets a type (entity or non-entity) by its name 
	 * 
	 * @param typeName the type name
	 * @return an instance of {@link BemTypeInfo} with the specified name
	 * @throws BemNotFoundException
	 */
	public BemTypeInfo getTypeInfo(String typeName) throws BemNotFoundException {
		String upperTypeName = typeName.toUpperCase();
		BemTypeInfo result = allTypeInfoDictionary.get(upperTypeName);
		if (result == null) {
			throw new BemNotFoundException(String.format("Type not found: '%s'", typeName));
		}
		return result;
	}

	/**
	 * Gets an entity type by its name
	 * 
	 * @param typeName the type name
	 * @return an instance of {@link BemTypeInfo} with the specified name
	 * @throws BemNotFoundException
	 */
	public BemEntityTypeInfo getEntityTypeInfo(String typeName) throws BemNotFoundException {
		String upperTypeName = typeName.toUpperCase();
		BemEntityTypeInfo result = entityTypeInfoDictionary.get(upperTypeName);
		if (result == null) {
			throw new BemNotFoundException(String.format("Entity type not found: '%s'", typeName));
		}
		return result;
	}
	
	public void addTypeInfo(BemTypeInfo typeInfo) {
		String upperCaseTypeName = typeInfo.getName().toUpperCase();
		allTypeInfoDictionary.put(upperCaseTypeName, typeInfo);
		if (typeInfo instanceof BemEntityTypeInfo) {
			entityTypeInfoDictionary.put(upperCaseTypeName, (BemEntityTypeInfo)typeInfo);			
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
