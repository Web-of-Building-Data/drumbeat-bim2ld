package fi.aalto.cs.drumbeat.data.bem.schema;

/**
 * A data type which is super to another data type.
 * 
 * @author Nam Vu
 *
 */
public class BemDefinedTypeInfo extends BemTypeInfo {
	
	private BemTypeInfo wrappedTypeInfo;

	public BemDefinedTypeInfo(BemSchema schema, String name) {
		super(schema, name);
	}
	
	@Override
	public BemValueKindEnum getValueKind() {
		return getWrappedTypeInfo().getValueKind();		
	}
	
	public BemTypeInfo getWrappedTypeInfo() {
		return wrappedTypeInfo;
	}
	
	public void setWrappedTypeInfo(BemTypeInfo wrappedTypedInfo) {
		this.wrappedTypeInfo = wrappedTypedInfo;		
	}

}
