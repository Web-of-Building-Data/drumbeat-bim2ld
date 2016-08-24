package fi.aalto.cs.drumbeat.data.edm.schema;

public abstract class DrbCollectionTypeInfo extends DrbTypeInfo {

	public DrbCollectionTypeInfo(DrbSchema schema, String name) {
		super(schema, name);
	}
	
	@Override
	public boolean isCollectionType() {
		return true;
	}

}
