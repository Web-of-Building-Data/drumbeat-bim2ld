package fi.aalto.cs.drumbeat.data.edm.schema;

public class DrbPrimitiveTypeInfo extends DrbSimpleTypeInfo {

	private final DrbTypeEnum valueType;
	
	public DrbPrimitiveTypeInfo(DrbSchema schema, String name, DrbTypeEnum valueType) {
		super(schema, name);
		this.valueType = valueType;
	}
	
	public DrbTypeEnum getValueType() {
		return valueType;
	}

}
