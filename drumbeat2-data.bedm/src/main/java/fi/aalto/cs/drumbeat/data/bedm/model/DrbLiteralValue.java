package fi.aalto.cs.drumbeat.data.bedm.model;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbSelectTypeInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbTypeEnum;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbTypeInfo;

public class DrbLiteralValue extends DrbNonCollectionValue {
	
	private final DrbTypeInfo typeInfo;
	private final DrbTypeEnum type;
	private Object value;
	
	public DrbLiteralValue(Object value, DrbTypeInfo typeInfo, DrbTypeEnum type) {
		assert(typeInfo != null);
		assert(!(typeInfo instanceof DrbSelectTypeInfo));
		assert(!(value instanceof DrbLiteralValue));

//		TODO: Uncomment the following line
//		assert(!(typeInfo instanceof DrbLogicalTypeInfo) || (value instanceof LogicalEnum));		
		
		this.value = value;
		this.typeInfo = typeInfo;
		this.type = type;
	}
	
	public DrbTypeInfo getType() {
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
	
	public DrbTypeEnum getValueType() {
		return type;
	}

//	@Override
//	public RdfNodeTypeEnum getRdfNodeType() {
//		return RdfNodeTypeEnum.Literal;
//	}
//
//	@Override
//	public RdfUri toRdfUri() {
//		return Drb2RdfConverter.getDefaultConverter().convertLiteralToRdfUri(this);
//	}
//
//	@Override
//	public List<IRdfLink> getRdfLinks() {
//		return null;
//	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof DrbLiteralValue) {
			return getValue().equals(((DrbLiteralValue) other).getValue())
					&& getValueType().equals(((DrbLiteralValue) other).getValueType());
		}
		return false;
	}
}
