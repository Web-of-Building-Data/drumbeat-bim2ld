package fi.aalto.cs.drumbeat.data.bem.schema;

public abstract class BemNonCollectionTypeInfo extends BemTypeInfo {

	public BemNonCollectionTypeInfo(BemSchema schema, String name) {
		super(schema, name);
	}
	
	@Override
	public boolean isCollectionType() {
		return false;
	}

}
