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
public class BemLogicalTypeInfo extends BemSimpleTypeInfo {

	private List<BemLogicalEnum> values;

	/**
	 * Creates a new enumeration type.
	 * 
	 * @param schema
	 * @param typeName
	 */
	public BemLogicalTypeInfo(BemSchema schema, String typeName) {
		super(schema, typeName);
	}
	
	public BemLogicalTypeInfo(BemSchema schema, String typeName, Collection<BemLogicalEnum> values) {
		super(schema, typeName);
		setValues(values);
	}

	/**
	 * Gets the enumeration values
	 * @return enumeration values
	 */
	public List<BemLogicalEnum> getValues() {
		return values;
	}
	
	/**
	 * Sets enumeration values
	 * @param values
	 */
	public void setValues(Collection<BemLogicalEnum> values) {
		this.values = new ArrayList<>(values);
	}

	@Override
	public BemValueKindEnum getValueKind() {
		return BemValueKindEnum.LOGICAL;
	}
	
	

}
