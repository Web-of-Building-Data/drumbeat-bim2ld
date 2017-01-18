package fi.aalto.cs.drumbeat.data.bem.schema;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

/**
 * A data type which is super to another data type.
 * 
 * @author Nam Vu
 *
 */
public class BemDefinedTypeInfo extends BemTypeInfo {
	
	private BemTypeInfo superTypeInfo;
	private String superTypeInfoName;

	public BemDefinedTypeInfo(BemSchema schema, String name, BemTypeInfo superTypeInfo) {
		super(schema, name);
		this.superTypeInfo = superTypeInfo;
	}

	public BemDefinedTypeInfo(BemSchema schema, String name, String superTypeInfoName) {
		super(schema, name);
		this.superTypeInfoName = superTypeInfoName;
	}

	@Override
	public boolean isCollectionType() {
		return superTypeInfo.isCollectionType();
	}
	
	public BemTypeInfo getSuperTypeInfo() throws DrbNotFoundException {
		if (superTypeInfo == null) {
			return getSchema().getTypeInfo(superTypeInfoName);
		}
		return superTypeInfo;
	}

}
