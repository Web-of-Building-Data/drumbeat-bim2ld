package fi.aalto.cs.drumbeat.convert.bem2rdf;

import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class Bem2RdfConversionContext {
	
	private String name;
	
	private OwlProfileList limitingOwlProfileList;
	private Bem2RdfConversionContextParams conversionParams;
	
	private String builtInOntologyNamespacePrefixFormat;
	private String builtInOntologyNamespaceUriFormat;
	
	private String ontologyNamespacePrefixFormat;
	private String ontologyNamespaceUriFormat;
	
	private String datasetNamespacePrefixFormat;
	private String datasetNamespaceUriFormat;	
	
	private String datasetBlankNodeNamespacePrefixFormat;
	private String datasetBlankNodeNamespaceUriFormat;	

	public Bem2RdfConversionContext() {
		this(null, null, null);
	}

	public Bem2RdfConversionContext(String name, OwlProfileList limitingOwlProfileList, Bem2RdfConversionContextParams conversionParams) {
		this.name = name;
		this.limitingOwlProfileList = limitingOwlProfileList != null ?
				limitingOwlProfileList : new OwlProfileList(OwlProfileEnum.OWL2_Full);
		this.conversionParams = conversionParams != null ? conversionParams :new Bem2RdfConversionContextParams();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public OwlProfileList getLimitingOwlProfileList() {
		return limitingOwlProfileList;
	}

	public void setLimitingOwlProfileList(OwlProfileList limitingOwlProfileList) {
		this.limitingOwlProfileList = limitingOwlProfileList;
	}

	public Bem2RdfConversionContextParams getConversionParams() {
		return conversionParams;
	}

	public void setConversionParams(Bem2RdfConversionContextParams conversionParams) {
		this.conversionParams = conversionParams;
	}

	/**
	 * @return the builtInOntologyNamespacePrefixFormat
	 */
	public String getBuiltInOntologyNamespacePrefixFormat() {
		return builtInOntologyNamespacePrefixFormat;
	}

	/**
	 * @param builtInOntologyNamespacePrefixFormat the builtInOntologyNamespacePrefixFormat to set (with no colon)
	 */
	public void setBuiltInOntologyNamespacePrefixFormat(String builtInOntologyNamespacePrefixFormat) {
		this.builtInOntologyNamespacePrefixFormat = builtInOntologyNamespacePrefixFormat;
	}

	/**
	 * @return the builtInOntologyNamespaceUriFormat
	 */
	public String getBuiltInOntologyNamespaceUriFormat() {
		return builtInOntologyNamespaceUriFormat;
	}

	/**
	 * @param builtInOntologyNamespaceUriFormat the builtInOntologyNamespaceUriFormat to set
	 */
	public void setBuiltInOntologyNamespaceUriFormat(String builtInOntologyNamespaceUriFormat) {
		this.builtInOntologyNamespaceUriFormat = builtInOntologyNamespaceUriFormat;
	}

	/**
	 * @return the ontologyNamespacePrefixFormat
	 */
	public String getOntologyNamespacePrefixFormat() {
		return ontologyNamespacePrefixFormat;
	}

	/**
	 * @param ontologyNamespacePrefixFormat the ontologyNamespacePrefixFormat to set  (with no colon)
	 */
	public void setOntologyNamespacePrefixFormat(String ontologyNamespacePrefixFormat) {
		this.ontologyNamespacePrefixFormat = ontologyNamespacePrefixFormat;
	}

	/**
	 * @return the ontologyNamespaceUriFormat
	 */
	public String getOntologyNamespaceUriFormat() {
		return ontologyNamespaceUriFormat;
	}

	/**
	 * @param ontologyNamespaceUriFormat the ontologyNamespaceUriFormat to set  (with no colon)
	 */
	public void setOntologyNamespaceUriFormat(String ontologyNamespaceUriFormat) {
		this.ontologyNamespaceUriFormat = ontologyNamespaceUriFormat;
	}

	/**
	 * @return the datasetNamespacePrefixFormat
	 */
	public String getDatasetNamespacePrefixFormat() {
		return datasetNamespacePrefixFormat;
	}

	/**
	 * @param datasetNamespacePrefixFormat the datasetNamespacePrefixFormat to set
	 */
	public void setDatasetNamespacePrefixFormat(String datasetNamespacePrefixFormat) {
		this.datasetNamespacePrefixFormat = datasetNamespacePrefixFormat;
	}

	/**
	 * @return the datasetNamespaceUriFormat
	 */
	public String getDatasetNamespaceUriFormat() {
		return datasetNamespaceUriFormat;
	}

	/**
	 * @param datasetNamespaceUriFormat the datasetNamespaceUriFormat to set
	 */
	public void setDatasetNamespaceUriFormat(String datasetNamespaceUriFormat) {
		this.datasetNamespaceUriFormat = datasetNamespaceUriFormat;
	}
	
	/**
	 * @return the datasetBlankNodeNamespacePrefixFormat
	 */
	public String getDatasetBlankNodeNamespacePrefixFormat() {
		return datasetBlankNodeNamespacePrefixFormat;
	}

	/**
	 * @param datasetBlankNodeNamespacePrefixFormat the datasetBlankNodeNamespacePrefixFormat to set
	 */
	public void setDatasetBlankNodeNamespacePrefixFormat(String datasetBlankNodeNamespacePrefixFormat) {
		this.datasetBlankNodeNamespacePrefixFormat = datasetBlankNodeNamespacePrefixFormat;
	}

	/**
	 * @return the datasetBlankNodeNamespaceUriFormat
	 */
	public String getDatasetBlankNodeNamespaceUriFormat() {
		return datasetBlankNodeNamespaceUriFormat;
	}

	/**
	 * @param datasetBlankNodeNamespaceUriFormat the datasetBlankNodeNamespaceUriFormat to set
	 */
	public void setDatasetBlankNodeNamespaceUriFormat(String datasetBlankNodeNamespaceUriFormat) {
		this.datasetBlankNodeNamespaceUriFormat = datasetBlankNodeNamespaceUriFormat;
	}
	
	
}
