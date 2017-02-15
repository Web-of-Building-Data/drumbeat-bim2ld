package fi.aalto.cs.drumbeat.convert.bem2rdf;

import fi.aalto.cs.drumbeat.rdf.OwlProfileEnum;
import fi.aalto.cs.drumbeat.rdf.OwlProfileList;

public class Bem2RdfConversionContext {
	
	private OwlProfileList targetOwlProfileList;
	private Bem2RdfConversionContextParams conversionParams;
	
	private String builtInOntologyNamespacePrefixFormat;
	private String builtInOntologyNamespaceUriFormat;
	
	private String ontologyNamespacePrefixFormat;
	private String ontologyNamespaceUriFormat;
	
	private String datasetPrefix;
	private String datasetNamespaceUriFormat;	
	

	public Bem2RdfConversionContext() {
		this(null, null);
	}

	public Bem2RdfConversionContext(OwlProfileList targetOwlProfileList, Bem2RdfConversionContextParams conversionParams) {
		this.targetOwlProfileList = targetOwlProfileList != null ?
				targetOwlProfileList : new OwlProfileList(OwlProfileEnum.OWL2_Full);
		this.conversionParams = conversionParams != null ? conversionParams :new Bem2RdfConversionContextParams();
	}

	public OwlProfileList getTargetOwlProfileList() {
		return targetOwlProfileList;
	}

	public void setTargetOwlProfileList(OwlProfileList targetOwlProfileList) {
		this.targetOwlProfileList = targetOwlProfileList;
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
	 * @return the datasetPrefix
	 */
	public String getDatasetPrefix() {
		return datasetPrefix;
	}

	/**
	 * @param datasetPrefix the datasetPrefix to set
	 */
	public void setDatasetPrefix(String datasetPrefix) {
		this.datasetPrefix = datasetPrefix;
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
	
	
}
