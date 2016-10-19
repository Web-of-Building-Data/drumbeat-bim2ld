package fi.aalto.cs.drumbeat.data.bedm.model;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbEntityTypeInfo;

public class DrbValueFormatter {

	public static String formatEntityName(DrbEntityTypeInfo typeInfo, String localId) {
		return String.format("%s_%s", typeInfo, localId);
	}
	
	public static String formatChildEntityId(String parentEntityId, int childCount) {
		return String.format("%s-%s", parentEntityId, childCount);
	}

}
