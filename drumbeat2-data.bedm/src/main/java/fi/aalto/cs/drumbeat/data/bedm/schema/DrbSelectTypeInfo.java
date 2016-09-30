/**
 * 
 */
package fi.aalto.cs.drumbeat.data.bedm.schema;

import java.util.ArrayList;
import java.util.List;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;

/**
 * @author nam
 *
 */
public class DrbSelectTypeInfo extends DrbTypeInfo {

	private List<String> selectTypeInfoNames;
	private List<DrbTypeInfo> selectTypeInfos;
	
	public DrbSelectTypeInfo(DrbSchema schema, String typeName, List<String> selectTypeInfoNames) {
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
	public List<DrbTypeInfo> getSelectTypeInfos() {
		if (selectTypeInfos == null) {
			selectTypeInfos = new ArrayList<DrbTypeInfo>();
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
	 * @see fi.aalto.cs.drumbeat.data.bedm.schema.DrbTypeInfo#isCollectionType()
	 */
	@Override
	public boolean isCollectionType() {
		return false;
	}

}
