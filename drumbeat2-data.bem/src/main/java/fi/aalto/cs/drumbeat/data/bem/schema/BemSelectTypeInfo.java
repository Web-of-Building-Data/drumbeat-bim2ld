/**
 * 
 */
package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.LinkedList;
import java.util.List;

/**
 * @author nam
 *
 */
public class BemSelectTypeInfo extends BemComplexTypeInfo {

	private List<BemTypeInfo> itemTypeInfos;
	
	public BemSelectTypeInfo(BemSchema schema, String typeName) {
		super(schema, typeName);
		itemTypeInfos = new LinkedList<>();
	}
	
	@Override
	public BemValueKindEnum getValueKind() {
		return BemValueKindEnum.SELECT;		
	}
	
	/**
	 * @return the selectTypeInfos
	 */
	public List<BemTypeInfo> getItemTypeInfos() {
		return itemTypeInfos;
	}
	
	public void addItemTypeInfo(BemTypeInfo itemTypeInfo) {
		itemTypeInfos.add(itemTypeInfo);
	}


}
