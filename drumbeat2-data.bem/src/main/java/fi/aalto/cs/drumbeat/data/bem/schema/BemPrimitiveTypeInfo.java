package fi.aalto.cs.drumbeat.data.bem.schema;

public class BemPrimitiveTypeInfo extends BemSimpleTypeInfo {

	private final BemPrimitiveKindEnum valueType;
	
	public BemPrimitiveTypeInfo(BemSchema schema, String name, BemPrimitiveKindEnum valueType) {
		super(schema, name);
		this.valueType = valueType;
	}
	
	public BemPrimitiveKindEnum getValueType() {
		return valueType;
	}

}
