package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fi.aalto.cs.drumbeat.data.bem.BemTypeAlreadyExistsException;
import fi.aalto.cs.drumbeat.data.bem.BemTypeNotFoundException;

/**
 * A data schema, i.e. a container for data types. 
 * 
 * @author Nam Vu
 *
 */
public class BemSchema {
	
	private String name;
	private String language;
	private BemSchema header;
	private final Map<String, BemTypeInfo> allTypeInfoDictionary;
	private final Map<String, BemEntityTypeInfo> entityTypeInfoDictionary;
	
	public BemSchema() {
		allTypeInfoDictionary = new HashMap<>();
		entityTypeInfoDictionary = new HashMap<>();
	}
	
	public BemSchema(String name, String language) {
		this();
		this.name = name;
		this.language = language;
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
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
		
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
	public BemTypeInfo getTypeInfo(String typeName) throws BemTypeNotFoundException {
		String upperTypeName = typeName.toUpperCase();
		BemTypeInfo result = allTypeInfoDictionary.get(upperTypeName);
		if (result == null) {
			throw new BemTypeNotFoundException(String.format("Type not found: '%s'", typeName));
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
	public BemEntityTypeInfo getEntityTypeInfo(String typeName) throws BemTypeNotFoundException {
		String upperTypeName = typeName.toUpperCase();
		BemEntityTypeInfo result = entityTypeInfoDictionary.get(upperTypeName);
		if (result == null) {
			throw new BemTypeNotFoundException(String.format("Entity type not found: '%s'", typeName));
		}
		return result;
	}
	
	public void addTypeInfo(BemTypeInfo typeInfo) throws BemTypeAlreadyExistsException {
		String upperCaseTypeName = typeInfo.getName().toUpperCase();
		if (allTypeInfoDictionary.containsKey(upperCaseTypeName)) {
			throw new BemTypeAlreadyExistsException(typeInfo.getName());
		}
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
