package fi.aalto.cs.drumbeat.data.bedm.schema;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

/**
 * A data type which is super to another data type.
 * 
 * @author Nam Vu
 *
 */
public class DrbDefinedTypeInfo extends DrbTypeInfo {
	
	private DrbTypeInfo superTypeInfo;
	private String superTypeInfoName;

	public DrbDefinedTypeInfo(DrbSchema schema, String name, DrbTypeInfo superTypeInfo) {
		super(schema, name);
		this.superTypeInfo = superTypeInfo;
	}

	public DrbDefinedTypeInfo(DrbSchema schema, String name, String superTypeInfoName) {
		super(schema, name);
		this.superTypeInfoName = superTypeInfoName;
	}

	@Override
	public boolean isCollectionType() {
		return superTypeInfo.isCollectionType();
	}
	
	public DrbTypeInfo getSuperTypeInfo() throws DrbNotFoundException {
		if (superTypeInfo == null) {
			return getSchema().getTypeInfo(superTypeInfoName);
		}
		return superTypeInfo;
	}

}
