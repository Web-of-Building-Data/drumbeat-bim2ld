/**
 * 
 */
package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nam Vu
 *
 */
public class BemEnumerationTypeInfo extends BemSimpleTypeInfo {

	private List<String> values;

	/**
	 * Creates a new enumeration type.
	 * 
	 * @param schema
	 * @param typeName
	 */
	public BemEnumerationTypeInfo(BemSchema schema, String typeName) {
		super(schema, typeName);
	}
	
	public BemEnumerationTypeInfo(BemSchema schema, String typeName, Collection<String> values) {
		super(schema, typeName);
		setValues(values);
	}

	/**
	 * Gets the enumeration values
	 * @return enumeration values
	 */
	public List<String> getValues() {
		return values;
	}
	
	/**
	 * Sets enumeration values
	 * @param values
	 */
	public void setValues(Collection<String> values) {
		this.values = new ArrayList<>(values);
	}
	
	

}
