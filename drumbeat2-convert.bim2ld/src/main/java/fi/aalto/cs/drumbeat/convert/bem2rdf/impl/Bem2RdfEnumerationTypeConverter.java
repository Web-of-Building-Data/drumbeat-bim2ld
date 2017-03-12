package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.*;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.common.params.StringParam;
import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

public class Bem2RdfEnumerationTypeConverter {

	private final Bem2RdfConverterManager manager;
	private final Resource baseTypeForEnums;
	private final String convertEnumValuesTo;

	public Bem2RdfEnumerationTypeConverter(Bem2RdfConverterManager manager) throws Bem2RdfConverterConfigurationException {
		this.manager = manager;
		convertEnumValuesTo = (String)manager.contextParams.convertEnumsTo();
		
		//
		// define baseTypeForEnums
		//
		switch (convertEnumValuesTo) {
		case StringParam.VALUE_AUTO:
		case Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL:
			baseTypeForEnums = OWL2.NamedIndividual;
			break;
		case Bem2RdfConversionContextParams.VALUE_XSD_STRING:
			baseTypeForEnums = XSD.xstring;
			break;
		default:
			throw new Bem2RdfConverterConfigurationException("Invalid value of option " + Bem2RdfConversionContextParams.PARAM_CONVERT_ENUMS_TO);
		}
	}
	
	
	//*****************************************
	// Region ENUM TYPES & VALUES
	//*****************************************
	
	Resource convertEnumerationTypeInfo(Model jenaModel, BemEnumerationTypeInfo typeInfo, boolean includeDetails) {
		
		String typeUri = manager.uriBuilder.buildTypeUri(typeInfo);
		Resource typeResource = jenaModel.createResource(typeUri);
		
		if (includeDetails) {
			typeResource.addProperty(RDF.type, OWL.Class);
			typeResource.addProperty(
					RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Enum)));		
	
			List<String> enumValues = typeInfo.getValues(); 
			List<RDFNode> enumValueNodes = new ArrayList<>();
	
			for (String value : enumValues) {
				enumValueNodes.add(convertEnumerationValue(jenaModel, value, typeInfo));
			}
			
	//		assert(enumValueNodes.size() > 1) : String.format("Type %s has only 1 enum value", typeInfo);
			
			final boolean enumerationIsSupported = manager.targetOwlProfileList.supportsStatement(OWL.oneOf, OwlVocabulary.DumpData.ANY_URI_LIST);	
					
			if (enumerationIsSupported) {
				
				Resource equivalentTypeResource = jenaModel.createResource();
				jenaModel.add(typeResource, OWL.equivalentClass, equivalentTypeResource);
	
				RDFList rdfList = jenaModel.createList(enumValueNodes.iterator());			
				jenaModel.add(equivalentTypeResource, OWL.oneOf, rdfList);
				
			} else {
				
				if (baseTypeForEnums.equals(OWL2.NamedIndividual)) {
					enumValueNodes.stream().forEach(node ->
					((Resource)node).addProperty(RDF.type, typeResource));			
				}
			}
		}
		
		return typeResource;
		
	}
	
	Resource convertEnumerationValue(Model jenaModel, String value, BemEnumerationTypeInfo typeInfo) {

		if (baseTypeForEnums.equals(OWL2.NamedIndividual)) {
			String valueUri = typeInfo.isBuiltInType() ? manager.uriBuilder.buildBuiltInOntologyUri(value) : manager.uriBuilder.buildOntologyUri(value); 
			return jenaModel.createResource(valueUri);			
		} else {			
			Resource resource = jenaModel.createResource();
			resource.addLiteral(RDF.type, jenaModel);
			resource.addLiteral(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)),
					baseTypeForEnums.getURI());
			return resource;
		}		
		
	}
	
	
	//*****************************************
	// EndRegion ENUM TYPES & VALUES
	//*****************************************
	
		

}
