package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConverterConfigurationException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.schema.BemAttributeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

public class Bem2RdfUriBuilder {
	
	public static final String TEXT_FORMAT_VARIABLE_ONTOLOGY_NAME = "Ontology.Name";
	public static final String TEXT_FORMAT_VARIABLE_ONTOLOGY_LANGUAGE = "Ontology.Language";
	public static final String TEXT_FORMAT_VARIABLE_DATASET_NAME = "Dataset.Name";
	public static final String TEXT_FORMAT_VARIABLE_DATASET_LANGUAGE = "Dataset.Language";	
	
	private String builtInOntologyNamespacePrefix;
	private String builtInOntologyNamespaceUri;	
	private String ontologyNamespacePrefix;	
	private String ontologyNamespaceUri;	
	private String datasetNamespacePrefix;
	private String datasetNamespaceUri;
	private String datasetBlankNodeNamespacePrefix;
	private String datasetBlankNodeNamespaceUri;

	public Bem2RdfUriBuilder() {
	}
	
	public static Bem2RdfUriBuilder createUriBuilder(
			Bem2RdfConversionContext context,
			String ontologyName,
			String ontologyLanguage,
			String datasetName,
			String datasetLanguage) throws Bem2RdfConverterConfigurationException
	{
		
		Bem2RdfUriBuilder uriBuilder = new Bem2RdfUriBuilder();
		
		HashMap<String, String> variableMap = new HashMap<>();
		if (ontologyName != null) {
			variableMap.put(TEXT_FORMAT_VARIABLE_ONTOLOGY_NAME, ontologyName);
		}
		
		if (ontologyLanguage != null) {
			variableMap.put(TEXT_FORMAT_VARIABLE_ONTOLOGY_LANGUAGE, ontologyLanguage);			
		}
		
		if (datasetName != null) {
			variableMap.put(TEXT_FORMAT_VARIABLE_DATASET_NAME, datasetName);
		}
		
		if (datasetLanguage != null) {
			variableMap.put(TEXT_FORMAT_VARIABLE_DATASET_LANGUAGE, datasetLanguage);			
		}
		
		uriBuilder.setBuiltInOntologyNamespacePrefix(getNullableSubtitutedString(context.getBuiltInOntologyNamespacePrefixFormat(), variableMap));
		uriBuilder.setBuiltInOntologyNamespaceUri(getNullableSubtitutedString(context.getBuiltInOntologyNamespaceUriFormat(), variableMap));
		
		uriBuilder.setOntologyNamespacePrefix(getNullableSubtitutedString(context.getOntologyNamespacePrefixFormat(), variableMap));
		uriBuilder.setOntologyNamespaceUri(getNullableSubtitutedString(context.getOntologyNamespaceUriFormat(), variableMap));
		
		uriBuilder.setDatasetNamespacePrefix(getNullableSubtitutedString(context.getDatasetNamespacePrefixFormat(), variableMap));
		uriBuilder.setDatasetNamespaceUri(getNullableSubtitutedString(context.getDatasetNamespaceUriFormat(), variableMap));
		
		uriBuilder.setDatasetBlankNodeNamespacePrefix(getNullableSubtitutedString(context.getDatasetBlankNodeNamespacePrefixFormat(), variableMap));
		uriBuilder.setDatasetBlankNodeNamespaceUri(getNullableSubtitutedString(context.getDatasetBlankNodeNamespaceUriFormat(), variableMap));

		return uriBuilder;		
		
	}
	
	
	public static Bem2RdfUriBuilder createUriBuilder(Bem2RdfConversionContext context, BemSchema bemSchema) throws Bem2RdfConverterConfigurationException {
		return createUriBuilder(context, bemSchema.getName(), bemSchema.getLanguage(), null, null);
	}
	
	public static Bem2RdfUriBuilder createUriBuilder(Bem2RdfConversionContext context, BemDataset bemDataset) throws Bem2RdfConverterConfigurationException {
		return createUriBuilder(context, bemDataset.getSchema().getName(), bemDataset.getSchema().getLanguage(), bemDataset.getName(), bemDataset.getLanguage());
	}
	
	private static String getNullableSubtitutedString(String format, Map<String, String> variableMap) throws Bem2RdfConverterConfigurationException {
		try {
			return StringUtils.substituteVariables(format, variableMap);		
		} catch (Exception e) {
			throw new Bem2RdfConverterConfigurationException("Invalid namespace prefix or URI format: " + format, e);
		}
	}
	
	public Map<String, String> getNamespacePrefixMap() {
		
		Map<String, String> prefixMap = new TreeMap<>(); 
		
		// define owl:
		prefixMap.put(OwlVocabulary.OWL.BASE_PREFIX, OWL.getURI());

		// define rdf:
		prefixMap.put(OwlVocabulary.RDF.BASE_PREFIX, RDF.getURI());

		// define rdfs:
		prefixMap.put(OwlVocabulary.RDFS.BASE_PREFIX, RDFS.getURI());

		// define xsd:
		prefixMap.put(OwlVocabulary.XSD.BASE_PREFIX, XSD.getURI());
		
		if (builtInOntologyNamespacePrefix != null && builtInOntologyNamespaceUri != null) {		
			prefixMap.put(builtInOntologyNamespacePrefix, builtInOntologyNamespaceUri);
		}
		
		if (ontologyNamespacePrefix != null && ontologyNamespaceUri != null) {		
			prefixMap.put(ontologyNamespacePrefix, ontologyNamespaceUri);
		}
		
		if (datasetNamespacePrefix != null && datasetNamespaceUri != null) {
			prefixMap.put(datasetNamespacePrefix, datasetNamespaceUri);
		}
		
		if (datasetBlankNodeNamespacePrefix != null && datasetBlankNodeNamespaceUri != null) {
			prefixMap.put(datasetBlankNodeNamespacePrefix, datasetBlankNodeNamespaceUri);
		}

		return prefixMap;
		
	}
	
	
	/**
	 * @return the builtInOntologyNamespacePrefix
	 */
	public String getBuiltInOntologyNamespacePrefix() {
		return builtInOntologyNamespacePrefix;
	}

	/**
	 * @param builtInOntologyNamespacePrefix the builtInOntologyNamespacePrefix to set
	 */
	public void setBuiltInOntologyNamespacePrefix(String builtInOntologyNamespacePrefix) {
		this.builtInOntologyNamespacePrefix = builtInOntologyNamespacePrefix;
	}

	/**
	 * @return the ontologyNamespacePrefix
	 */
	public String getOntologyNamespacePrefix() {
		return ontologyNamespacePrefix;
	}

	/**
	 * @param ontologyNamespacePrefix the ontologyNamespacePrefix to set
	 */
	public void setOntologyNamespacePrefix(String ontologyNamespacePrefix) {
		this.ontologyNamespacePrefix = ontologyNamespacePrefix;
	}
	

	/**
	 * @return the builtInOntologyNamespaceUri
	 */
	public String getBuiltInOntologyNamespaceUri() {
		return builtInOntologyNamespaceUri;
	}

	/**
	 * @param builtInOntologyNamespaceUri the builtInOntologyNamespaceUri to set
	 */
	public void setBuiltInOntologyNamespaceUri(String builtInOntologyNamespaceUri) {
		this.builtInOntologyNamespaceUri = builtInOntologyNamespaceUri;
	}

	/**
	 * @return the ontologyNamespaceUri
	 */
	public String getOntologyNamespaceUri() {
		return ontologyNamespaceUri;
	}

	/**
	 * @param ontologyNamespaceUri the ontologyNamespaceUri to set
	 */
	public void setOntologyNamespaceUri(String ontologyNamespaceUri) {
		this.ontologyNamespaceUri = ontologyNamespaceUri;
	}
	
	/**
	 * @return the datasetNamespacePrefix
	 */
	public String getDatasetNamespacePrefix() {
		return datasetNamespacePrefix;
	}

	/**
	 * @param datasetNamespacePrefix the datasetNamespacePrefix to set
	 */
	public void setDatasetNamespacePrefix(String datasetNamespacePrefix) {
		this.datasetNamespacePrefix = datasetNamespacePrefix;
	}
	

	/**
	 * @return the datasetNamespaceUri
	 */
	public String getDatasetNamespaceUri() {
		return datasetNamespaceUri;
	}

	/**
	 * @param datasetNamespaceUri the datasetNamespaceUri to set
	 */
	public void setDatasetNamespaceUri(String datasetNamespaceUri) {
		this.datasetNamespaceUri = datasetNamespaceUri;
	}
	
	/**
	 * @return the datasetBlankNodeNamespacePrefix
	 */
	public String getDatasetBlankNodeNamespacePrefix() {
		return datasetBlankNodeNamespacePrefix;
	}

	/**
	 * @param datasetBlankNodeNamespacePrefix the datasetBlankNodeNamespacePrefix to set
	 */
	public void setDatasetBlankNodeNamespacePrefix(String datasetBlankNodeNamespacePrefix) {
		this.datasetBlankNodeNamespacePrefix = datasetBlankNodeNamespacePrefix;
	}
	
	/**
	 * @return the datasetBlankNodeNamespaceUri
	 */
	public String getDatasetBlankNodeNamespaceUri() {
		return datasetBlankNodeNamespaceUri;
	}

	/**
	 * @param datasetBlankNodeNamespaceUri the datasetBlankNodeNamespaceUri to set
	 */
	public void setDatasetBlankNodeNamespaceUri(String datasetBlankNodeNamespaceUri) {
		this.datasetBlankNodeNamespaceUri = datasetBlankNodeNamespaceUri;
	}
	
	
	public String buildBuiltInOntologyUri(String localName) {
		if (builtInOntologyNamespaceUri == null) {
			throw new IllegalArgumentException("Undefined builtInOntologyNamespaceUri");
		}
		return builtInOntologyNamespaceUri + localName;
	}

	public String buildOntologyUri(String localName) {
		if (ontologyNamespaceUri == null) {
			throw new IllegalArgumentException("Undefined ontologyNamespaceUri");
		}
		return ontologyNamespaceUri + localName;
	}
	
//	public String buildBuiltInTypeUri(BemTypeInfo typeInfo) {
//		return buildBuiltInOntologyUri(typeInfo.getName());
//	}
//	
//	public String buildNonBuiltInTypeUri(BemTypeInfo typeInfo) {
//		return buildOntologyUri(typeInfo.getName());
//	}

	/**
	 * Creates a URI for the type
	 * @param typeInfo
	 * @return
	 */
	public String buildTypeUri(BemTypeInfo typeInfo) {
		return typeInfo.isBuiltInType() ? 
				buildBuiltInOntologyUri(typeInfo.getName()) : buildOntologyUri(typeInfo.getName());
	}
	
	/**
	 * Creates a URI for the attribute 
	 * @param attributeInfo
	 * @return
	 */
	public String buildAttributeUri(BemAttributeInfo attributeInfo, boolean useLongAttributeName) {
		if (useLongAttributeName) {
			return buildOntologyUri(String.format("%s_%s", attributeInfo.getName(), attributeInfo.getEntityTypeInfo()));			
		} else {
			return buildOntologyUri(attributeInfo.getName());
		}		
	}
	
	public String buildDatasetUri(String localName) {
		if (datasetNamespaceUri == null) {
			throw new IllegalArgumentException("Undefined datasetNamespaceUri");
		}
		return datasetNamespaceUri + localName;	
	}
	
	public String buildDatasetBlankNodeUri(String localName) {
		if (datasetBlankNodeNamespaceUri == null) {
			throw new IllegalArgumentException("Undefined datasetBlankNodeNamespaceUri");
		}
		return datasetBlankNodeNamespaceUri + localName;	
	}
	

}
