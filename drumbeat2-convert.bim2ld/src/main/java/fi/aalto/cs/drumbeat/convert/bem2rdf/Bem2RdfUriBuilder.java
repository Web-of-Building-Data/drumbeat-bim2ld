package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import fi.aalto.cs.drumbeat.data.bem.schema.BemAttributeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemTypeInfo;
import fi.aalto.cs.drumbeat.rdf.RdfVocabulary;

public class Bem2RdfUriBuilder {
	
	public static final String TEXT_FORMAT_VARIABLE_ONTOLOGY_NAME = "Ontology.Name";
	public static final String TEXT_FORMAT_VARIABLE_LANGUAGE_NAME = "Language.Name";
	
	private String builtInOntologyNamespacePrefix;
	private String builtInOntologyNamespaceUri;	
	private String ontologyNamespacePrefix;	
	private String ontologyNamespaceUri;	
	private String datasetNamespaceUri;

	public Bem2RdfUriBuilder() {
	}
	
	public static Bem2RdfUriBuilder createUriBuilder(Bem2RdfConversionContext context, BemSchema bemSchema) throws Bem2RdfConverterConfigurationException {
		Bem2RdfUriBuilder uriBuilder = new Bem2RdfUriBuilder();
		
		HashMap<String, String> variableMap = new HashMap<>();
		if (bemSchema.getName() != null) {
			variableMap.put(TEXT_FORMAT_VARIABLE_ONTOLOGY_NAME, bemSchema.getName());
		}
		
		if (bemSchema.getLanguage() != null) {
			variableMap.put(TEXT_FORMAT_VARIABLE_LANGUAGE_NAME, bemSchema.getLanguage());			
		}
		
		try {
			String ontologyNamespacePrefix = StrSubstitutor.replace(context.getOntologyNamespacePrefixFormat(), variableMap);		
			uriBuilder.setOntologyNamespacePrefix(ontologyNamespacePrefix);
		} catch (Exception e) {
			throw new Bem2RdfConverterConfigurationException("Invalid ongology namepsace URI format: " + context.getOntologyNamespacePrefixFormat(), e);
		}
		
		try {
			String builtInOntologyNamespacePrefix = StrSubstitutor.replace(context.getBuiltInOntologyNamespacePrefixFormat(), variableMap);		
			uriBuilder.setBuiltInOntologyNamespacePrefix(builtInOntologyNamespacePrefix);
		} catch (Exception e) {
			throw new Bem2RdfConverterConfigurationException("Invalid builtin ontology namepsace URI format: " + context.getBuiltInOntologyNamespacePrefixFormat(), e);
		}

		try {
			String ontologyNamespaceUri = StrSubstitutor.replace(context.getOntologyNamespaceUriFormat(), variableMap);		
			uriBuilder.setOntologyNamespaceUri(ontologyNamespaceUri);
		} catch (Exception e) {
			throw new Bem2RdfConverterConfigurationException("Invalid ongology namepsace URI format: " + context.getOntologyNamespaceUriFormat(), e);
		}
		
		try {
			String builtInOntologyNamespaceUri = StrSubstitutor.replace(context.getBuiltInOntologyNamespaceUriFormat(), variableMap);		
			uriBuilder.setBuiltInOntologyNamespaceUri(builtInOntologyNamespaceUri);
		} catch (Exception e) {
			throw new Bem2RdfConverterConfigurationException("Invalid builtin ontology namepsace URI format: " + context.getBuiltInOntologyNamespaceUriFormat(), e);
		}

		return uriBuilder;
	}
	
	public Map<String, String> getNamespacePrefixMap() {
		
		Map<String, String> prefixMap = new TreeMap<>(); 
		
		// define owl:
		prefixMap.put(RdfVocabulary.OWL.BASE_PREFIX, OWL.getURI());

		// define rdf:
		prefixMap.put(RdfVocabulary.RDF.BASE_PREFIX, RDF.getURI());

		// define rdfs:
		prefixMap.put(RdfVocabulary.RDFS.BASE_PREFIX, RDFS.getURI());

		// define xsd:
		prefixMap.put(RdfVocabulary.XSD.BASE_PREFIX, XSD.getURI());
		
		if (builtInOntologyNamespacePrefix != null && builtInOntologyNamespaceUri != null) {		
			prefixMap.put(builtInOntologyNamespacePrefix, builtInOntologyNamespaceUri);
		}
		
		if (ontologyNamespacePrefix != null && ontologyNamespaceUri != null) {		
			prefixMap.put(ontologyNamespacePrefix, ontologyNamespaceUri);
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
	
	

}
