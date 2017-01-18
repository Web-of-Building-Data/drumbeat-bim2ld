package fi.aalto.cs.drumbeat.data.bem.schema;

public class BemPrimitiveTypeInfo extends BemSimpleNonCollectionTypeInfo {

	private final BemTypeEnum valueType;
	
	public BemPrimitiveTypeInfo(BemSchema schema, String name, BemTypeEnum valueType) {
		super(schema, name);
		this.valueType = valueType;
	}
	
	public BemTypeEnum getValueType() {
		return valueType;
	}

}
