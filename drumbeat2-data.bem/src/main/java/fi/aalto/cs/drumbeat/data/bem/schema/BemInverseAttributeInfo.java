package fi.aalto.cs.drumbeat.data.bem.schema;

/**
 * An entity's inverse link is a link to another entity. An inverse link is inverse to some outgoing link of that entity.  
 * 
 * @author Nam Vu
 *
 */
public class BemInverseAttributeInfo extends BemAttributeInfo {

	private BemAttributeInfo outgoingAttributeInfo;

	public BemInverseAttributeInfo(BemEntityTypeInfo entityTypeInfo, String name,
			BemEntityTypeInfo linkSourceEntityTypeInfo,
			BemAttributeInfo outgoingAttributeInfo) {
		super(entityTypeInfo, name, linkSourceEntityTypeInfo);
		this.outgoingAttributeInfo = outgoingAttributeInfo;
		outgoingAttributeInfo.addPossibleInverseAttributeInfo(this);
	}

	public BemEntityTypeInfo getSourceEntityTypeInfo() {
		return (BemEntityTypeInfo) getValueTypeInfo();
	}

	public BemAttributeInfo getOutgoingAttributeInfo() {
		return outgoingAttributeInfo;
	}

	public BemEntityTypeInfo getDestinationEntityTypeInfo() {
		return getEntityTypeInfo();
	}

	public boolean isInverseTo(BemAttributeInfo outgoingAttributeInfo) {
		return this.outgoingAttributeInfo.equals(outgoingAttributeInfo);
	}

}
