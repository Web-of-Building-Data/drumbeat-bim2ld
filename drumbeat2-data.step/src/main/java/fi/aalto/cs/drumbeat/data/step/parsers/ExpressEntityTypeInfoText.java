package fi.aalto.cs.drumbeat.data.step.parsers;

import java.util.ArrayList;
import java.util.List;

import fi.aalto.cs.drumbeat.data.bedm.schema.DrbEntityTypeInfo;


class ExpressEntityTypeInfoText {
	
	private DrbEntityTypeInfo entityTypeInfo;
	private String superTypeName;
	private List<String> attributeStatements = new ArrayList<>();	
	private List<String> inverseLinkStatements = new ArrayList<>();
	private List<String> uniqueKeysStatements = new ArrayList<>();

	public ExpressEntityTypeInfoText(DrbEntityTypeInfo entityTypeInfo) {
		this.entityTypeInfo = entityTypeInfo;
	}
	

	/**
	 * @return the superTypeName
	 */
	public String getSuperTypeName() {
		return superTypeName;
	}


	/**
	 * @param superTypeName the superTypeName to set
	 */
	public void setSuperTypeName(String superTypeName) {
		this.superTypeName = superTypeName;
	}


	/**
	 * @return the entityTypeInfo
	 */
	public DrbEntityTypeInfo getEntityTypeInfo() {
		return entityTypeInfo;
	}

	/**
	 * @return the attributeStatements
	 */
	public List<String> getAttributeStatements() {
		return attributeStatements;
	}

	/**
	 * @return the inverseLinkStatements
	 */
	public List<String> getInverseLinkStatements() {
		return inverseLinkStatements;
	}


	/**
	 * @return the uniqueKeysStatements
	 */
	public List<String> getUniqueKeysStatements() {
		return uniqueKeysStatements;
	}


}
