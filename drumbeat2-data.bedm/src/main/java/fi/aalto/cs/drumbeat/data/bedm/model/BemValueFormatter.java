package fi.aalto.cs.drumbeat.data.bem.model;

import fi.aalto.cs.drumbeat.data.bem.schema.BemEntityTypeInfo;

public class BemValueFormatter {

	public static String formatEntityName(BemEntityTypeInfo typeInfo, String localId) {
		return String.format("%s_%s", typeInfo, localId);
	}
	
	public static String formatChildEntityId(String parentEntityId, int childCount) {
		return String.format("%s-%s", parentEntityId, childCount);
	}

}
