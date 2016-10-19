/**
 * 
 */
package fi.aalto.cs.drumbeat.data.bedm.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nam Vu
 *
 */
public class DrbEnumerationTypeInfo extends DrbSimpleNonCollectionTypeInfo {

	private List<String> values;

	/**
	 * Creates a new enumeration type.
	 * 
	 * @param schema
	 * @param name
	 */
	public DrbEnumerationTypeInfo(DrbSchema schema, String name, List<String> values) {
		super(schema, name);
		this.values = new ArrayList<>(values);
	}
	
	/**
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}
	
	

}
