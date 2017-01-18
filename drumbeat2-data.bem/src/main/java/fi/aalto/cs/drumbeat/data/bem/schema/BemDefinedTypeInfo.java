package fi.aalto.cs.drumbeat.data.bem.schema;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

/**
 * A data type which is super to another data type.
 * 
 * @author Nam Vu
 *
 */
public class BemDefinedTypeInfo extends BemTypeInfo {
	
	private BemTypeInfo wrappedTypeInfo;

	public BemDefinedTypeInfo(BemSchema schema, String name) {
		super(schema, name);
	}

	
	public BemTypeInfo getWrappedTypeInfo() throws DrbNotFoundException {
		return wrappedTypeInfo;
	}
	
	public void setWrappedTypeInfo(BemTypeInfo wrappedTypedInfo) {
		this.wrappedTypeInfo = wrappedTypedInfo;		
	}

}
