package fi.aalto.cs.drumbeat.data.bem.model;

import fi.aalto.cs.drumbeat.data.bem.schema.BemSelectTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeEnum;
import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;

public class BemLiteralValue extends BemNonCollectionValue {
	
	private final BemTypeInfo typeInfo;
	private final BemTypeEnum type;
	private Object value;
	
	public BemLiteralValue(Object value, BemTypeInfo typeInfo, BemTypeEnum type) {
		assert(typeInfo != null);
		assert(!(typeInfo instanceof BemSelectTypeInfo));
		assert(!(value instanceof BemLiteralValue));

//		TODO: Uncomment the following line
//		assert(!(typeInfo instanceof BemLogicalTypeInfo) || (value instanceof LogicalEnum));		
		
		this.value = value;
		this.typeInfo = typeInfo;
		this.type = type;
	}
	
	public BemTypeInfo getType() {
		return typeInfo;
	}
	
	@Override
	public String toString() {
		return value != null ? value.toString() : "null";
	}

	@Override
	public Boolean isLiteralType() {
		return Boolean.TRUE;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public BemTypeEnum getValueType() {
		return type;
	}

//	@Override
//	public RdfNodeTypeEnum getRdfNodeType() {
//		return RdfNodeTypeEnum.Literal;
//	}
//
//	@Override
//	public RdfUri toRdfUri() {
//		return Bem2RdfConverter.getDefaultConverter().convertLiteralToRdfUri(this);
//	}
//
//	@Override
//	public List<IRdfLink> getRdfLinks() {
//		return null;
//	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BemLiteralValue) {
			return getValue().equals(((BemLiteralValue) other).getValue())
					&& getValueType().equals(((BemLiteralValue) other).getValueType());
		}
		return false;
	}
}
