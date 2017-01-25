package fi.aalto.cs.drumbeat.data.bem.schema;

public class BemPrimitiveTypeInfo extends BemSimpleTypeInfo {

	private final BemValueKindEnum valueKind;
	
	public BemPrimitiveTypeInfo(BemSchema schema, String name, BemValueKindEnum valueKind) {
		super(schema, name);
		this.valueKind = valueKind;
	}
	
	@Override
	public BemValueKindEnum getValueKind() {
		return valueKind;
	}

}
