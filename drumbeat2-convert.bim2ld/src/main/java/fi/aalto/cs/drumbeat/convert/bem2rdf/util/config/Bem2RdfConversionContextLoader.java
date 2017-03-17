package fi.aalto.cs.drumbeat.convert.bem2rdf.util.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import fi.aalto.cs.drumbeat.common.config.ConfigurationItemEx;
import fi.aalto.cs.drumbeat.common.config.document.ConfigurationDocument;
import fi.aalto.cs.drumbeat.common.config.document.ConfigurationParserException;
import fi.aalto.cs.drumbeat.common.config.document.ConverterPoolConfigurationSection;
import fi.aalto.cs.drumbeat.common.params.DrbParamNotFoundException;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class Bem2RdfConversionContextLoader {
	
	/////////////////////////
	// STATIC MEMBERS
	/////////////////////////
	
	public static final String CONFIGURATION_SECTION_CONVERTER_TYPE_NAME = "Bem2Rdf";
	public static final String CONFIGURATION_PROPERTY_ONTOLOGY_VERSION = "Ontology.Version";

	private static final String CONFIGURATION_PROPERTY_CONVERSION_OPTIONS_PREFIX = "Options.";
	
	private static final String CONFIGURATION_PROPERTY_OWL_PROFILE = "OwlProfile";
	
	private static final String CONFIGURATION_PROPERTY_BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT = "BuiltInOntology.NamespacePrefixFormat";
	private static final String CONFIGURATION_PROPERTY_BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT = "BuiltInOntology.NamespaceUriFormat";
	
	private static final String CONFIGURATION_PROPERTY_ONTOLOGY_NAMESPACE_PREFIX_FORMAT = "Ontology.NamespacePrefixFormat";
	private static final String CONFIGURATION_PROPERTY_ONTOLOGY_NAMESPACE_URI_FORMAT = "Ontology.NamespaceUriFormat";
	
	private static final String CONFIGURATION_PROPERTY_DATASET_NAMESPACE_PREFIX_FORMAT = "Dataset.NamespacePrefixFormat";
	private static final String CONFIGURATION_PROPERTY_DATASET_NAMESPACE_URI_FORMAT = "Dataset.NamespaceUriFormat";
	
	private static final String CONFIGURATION_PROPERTY_DATASET_BLANK_NODE_NAMESPACE_PREFIX_FORMAT = "Dataset.BlankNodeNamespacePrefixFormat";
	private static final String CONFIGURATION_PROPERTY_DATASET_BLANK_NODE_NAMESPACE_URI_FORMAT = "Dataset.BlankNodeNamespaceUriFormat";
	
	
	public static Bem2RdfConversionContext loadConversionContext(
			ConfigurationDocument configurationDocument,
			String contextName) throws ConfigurationParserException, DrbParamNotFoundException {
		
		ConverterPoolConfigurationSection configurationSection =
				ConverterPoolConfigurationSection.getInstance(configurationDocument, CONFIGURATION_SECTION_CONVERTER_TYPE_NAME);
		
		ConfigurationItemEx configuration;
		if (contextName != null) {
			configuration = configurationSection.getConfigurationPool().getByName(contextName);
		} else {
			configuration = configurationSection.getConfigurationPool().getDefault(); 
		}		
				
		Bem2RdfConversionContext context = internalLoadConversionContext(configuration);
		return context;
	}
	
	public static Map<String, Bem2RdfConversionContext> loadConversionContexts(
			ConfigurationDocument configurationDocument) throws ConfigurationParserException, DrbParamNotFoundException {
		
		ConverterPoolConfigurationSection configurationSection =
				ConverterPoolConfigurationSection.getInstance(configurationDocument, CONFIGURATION_SECTION_CONVERTER_TYPE_NAME);
		
		Map<String, Bem2RdfConversionContext> contexts = new HashMap<>();
		
		for (ConfigurationItemEx configuration : configurationSection.getConfigurationPool()) {
			if (configuration.isEnabled()) {
				Bem2RdfConversionContext context = internalLoadConversionContext(configuration);
				contexts.put(context.getName(), context);
			}
		}
		
		return contexts;
	}
	
	private static Bem2RdfConversionContext internalLoadConversionContext(ConfigurationItemEx configuration) throws DrbParamNotFoundException {
		
		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
		Bem2RdfConversionContextParams conversionParams = context.getConversionParams();		

//		context.setBemOntologyVersion(configuration.getProperties().getProperty(CONFIGURATION_PROPERTY_ONTOLOGY_VERSION, "1.0.0"));
		
		String name = configuration.getName();
		context.setName(name);
		
		Properties properties = configuration.getProperties();		
		
		for (Entry<Object, Object> property : properties.entrySet()) {
			
			String propertyName = (String)property.getKey();
			String propertyValue = (String)property.getValue();
			
			if (propertyName.startsWith(CONFIGURATION_PROPERTY_CONVERSION_OPTIONS_PREFIX)) {
				
				String conversionParamName = propertyName.substring(CONFIGURATION_PROPERTY_CONVERSION_OPTIONS_PREFIX.length());
				String conversionParamValue = properties.getProperty(propertyName);
				// TODO: enable allowing unknown params
				//conversionParams.getParam(conversionParamName, true).setValue(conversionParamValue);
				conversionParams.getParam(conversionParamName).setStringValue(conversionParamValue);
				
			} else {
				
				switch (propertyName) {
				case CONFIGURATION_PROPERTY_OWL_PROFILE:
					String[] owlProfileNames = propertyValue.split(",");
					OwlProfileList owlProfileList = new OwlProfileList(owlProfileNames);
					context.setLimitingOwlProfileList(owlProfileList);
					break;
				case CONFIGURATION_PROPERTY_BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT:
					context.setBuiltInOntologyNamespacePrefixFormat(propertyValue);
					break;
				case CONFIGURATION_PROPERTY_BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT:
					context.setBuiltInOntologyNamespaceUriFormat(propertyValue);
					break;
				case CONFIGURATION_PROPERTY_ONTOLOGY_NAMESPACE_PREFIX_FORMAT:
					context.setOntologyNamespacePrefixFormat(propertyValue);
					break;
				case CONFIGURATION_PROPERTY_ONTOLOGY_NAMESPACE_URI_FORMAT:
					context.setOntologyNamespaceUriFormat(propertyValue);
					break;
				case CONFIGURATION_PROPERTY_DATASET_NAMESPACE_PREFIX_FORMAT:
					context.setDatasetNamespacePrefixFormat(propertyValue);
					break;
				case CONFIGURATION_PROPERTY_DATASET_NAMESPACE_URI_FORMAT:
					context.setDatasetNamespaceUriFormat(propertyValue);
					break;
				case CONFIGURATION_PROPERTY_DATASET_BLANK_NODE_NAMESPACE_PREFIX_FORMAT:
					context.setDatasetBlankNodeNamespacePrefixFormat(propertyValue);
					break;
				case CONFIGURATION_PROPERTY_DATASET_BLANK_NODE_NAMESPACE_URI_FORMAT:
					context.setDatasetBlankNodeNamespaceUriFormat(propertyValue);
					break;
				default:
					throw new DrbParamNotFoundException("Unknown conversion context parameter: " + propertyName);
				}
				
			}
		}
		
		return context;
		
	}

}
