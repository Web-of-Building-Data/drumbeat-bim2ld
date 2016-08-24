package fi.aalto.cs.drumbeat.data.edm.schema;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

/**
 * A data type which is referred to another data type.
 * 
 * @author Nam Vu
 *
 */
public class DrbReferenceTypeInfo extends DrbTypeInfo {
	
	private DrbTypeInfo referredTypeInfo;
	private String referredTypeInfoName;

	public DrbReferenceTypeInfo(DrbSchema schema, String name, DrbTypeInfo referredTypeInfo) {
		super(schema, name);
		this.referredTypeInfo = referredTypeInfo;
	}

	public DrbReferenceTypeInfo(DrbSchema schema, String name, String referredTypeInfoName) {
		super(schema, name);
		this.referredTypeInfoName = referredTypeInfoName;
	}

	@Override
	public boolean isCollectionType() {
		return referredTypeInfo.isCollectionType();
	}
	
	public DrbTypeInfo getReferredTypeInfo() throws DrbNotFoundException {
		if (referredTypeInfo == null) {
			return getSchema().getTypeInfo(referredTypeInfoName);
		}
		return referredTypeInfo;
	}

}
