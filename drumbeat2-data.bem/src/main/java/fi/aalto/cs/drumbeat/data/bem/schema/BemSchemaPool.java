package fi.aalto.cs.drumbeat.data.bem.schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Container of {@link BemSchema}s.
 * 
 * @author Nam Vu
 *
 */
@SuppressWarnings("serial")
public class BemSchemaPool implements Serializable {
	
	private static Map<String, BemSchema> schemas = new HashMap<>();
	
	/**
	 * Gets the map of schemas (schemaVersion --> schema) 
	 * @return
	 */
	public static Map<String, BemSchema> getSchemas() {
		return schemas;
	}
	
	/**
	 * Gets the {@link BemSchema} with the specified name.
	 * 
	 * @param name - the name of the schema 
	 * @return the {@link BemSchema} with the specified name, or <code>null</code> if the name is not found.
	 */
	public static BemSchema getSchema(String name) {
		return schemas.get(name);
	}
	
	
	/**
	 * Add a new {@link BemSchema} using its default name string.
	 * 
	 * @param schema - the schema to be added.
	 */
	public static void add(BemSchema schema) {
		add(schema.getName(), schema);
	}
	

	/**
	 * Add a new {@link BemSchema} using the specified name string.
	 * 
	 * @param name - the name to be used as key of the schema.
	 * @param schema - the schema to be added.
	 */
	public static void add(String name, BemSchema schema) {
		schemas.put(name, schema);
	}
	
	public static void addAll(Collection<BemSchema> schemas) {
		for (BemSchema schema : schemas) {
			BemSchemaPool.schemas.put(schema.getName(), schema);
		}
	}
	
	
	/**
	 * Gets number of schemas
	 * @return
	 */
	public static int size() {
		return schemas.size();
	}
}
