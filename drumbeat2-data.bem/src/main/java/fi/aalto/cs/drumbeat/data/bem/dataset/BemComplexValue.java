package fi.aalto.cs.drumbeat.data.bem.dataset;

import fi.aalto.cs.drumbeat.common.string.StringUtils;

public abstract class BemComplexValue extends BemValue {
	
	private String subSetId;

	public String getSubSetId() {
		return subSetId != null ? subSetId : StringUtils.EMPTY;
	}

	public void setSubSetId(String setId) {
		this.subSetId = setId;
	}
	

}
