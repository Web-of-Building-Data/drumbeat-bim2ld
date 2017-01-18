/**
 * 
 */
package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.ArrayList;
import java.util.List;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

/**
 * @author nam
 *
 */
public class BemSelectTypeInfo extends BemTypeInfo {

	private List<String> selectTypeInfoNames;
	private List<BemTypeInfo> selectTypeInfos;
	
	public BemSelectTypeInfo(BemSchema schema, String typeName, List<String> selectTypeInfoNames) {
		super(schema, typeName);
		this.selectTypeInfoNames = selectTypeInfoNames;
	}
	
	
	/**
	 * @return the selectTypeInfoNames
	 */
	public List<String> getSelectTypeInfoNames() {
		return selectTypeInfoNames;
	}

	/**
	 * @return the selectTypeInfos
	 */
	public List<BemTypeInfo> getSelectTypeInfos() {
		if (selectTypeInfos == null) {
			selectTypeInfos = new ArrayList<BemTypeInfo>();
			for (String selectTypeInfoName : selectTypeInfoNames) {
				try {
					selectTypeInfos.add(getSchema().getTypeInfo(selectTypeInfoName));
				} catch (DrbNotFoundException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}			
		}
		return selectTypeInfos;
	}

	/* (non-Javadoc)
	 * @see fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo#isCollectionType()
	 */
	@Override
	public boolean isCollectionType() {
		return false;
	}

}
