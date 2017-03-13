package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.*;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.common.params.StringParam;
import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

class Bem2RdfLogicalTypeConverter{
	
	private final Bem2RdfConverterManager manager;

	final Map<BemValueKindEnum, Property> map_Type_hasXXXProperty;
	
	private final Resource baseTypeForLogical;
	
	public Bem2RdfLogicalTypeConverter(Bem2RdfConverterManager manager) throws Bem2RdfConverterConfigurationException {
		this.manager = manager;
		map_Type_hasXXXProperty = new HashMap<>();
		
		//
		// calculate baseTypeForLogicals
		//		
		String convertLogicalValuesTo = manager.contextParams.convertLogicalsTo();
		switch (convertLogicalValuesTo) {
		case StringParam.VALUE_AUTO:
		case Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL:
			baseTypeForLogical = OWL2.NamedIndividual;
			break;
		case Bem2RdfConversionContextParams.VALUE_XSD_STRING:
			baseTypeForLogical = XSD.xstring;
			break;
		case Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN:
			baseTypeForLogical = XSD.xboolean;
			break;
		default:
			throw new Bem2RdfConverterConfigurationException("Invalid value of option " + Bem2RdfConversionContextParams.PARAM_CONVERT_LOGICALS_TO);
		}

	}
	
	
	public Resource convertLogicalTypeInfo(Model jenaModel, BemLogicalTypeInfo typeInfo, boolean includeDetails) {
		String typeUri = manager.uriBuilder.buildTypeUri(typeInfo);
		Resource typeResource = jenaModel.createResource(typeUri);
		
		if (includeDetails) {
			typeResource.addProperty(RDF.type, OWL.Class);
	
			List<BemLogicalEnum> enumValues = typeInfo.getValues(); 
			List<RDFNode> enumValueNodes = new ArrayList<>();
	
			for (BemLogicalEnum value : enumValues) {
				enumValueNodes.add(convertLogicalValue(jenaModel, value, typeInfo));
			}
			
	//		assert(enumValueNodes.size() > 1) : String.format("Type %s has only 1 enum value", typeInfo);
			
			final boolean enumerationIsSupported = 
					manager.targetOwlProfileList.supportsStatement(OWL.oneOf, 
							baseTypeForLogical.equals(OWL2.NamedIndividual) ? OwlVocabulary.DumpData.ANY_URI_LIST : OwlVocabulary.DumpData.ANY_LITERAL_LIST);	
					
			if (enumerationIsSupported) {
				
				Resource equivalentTypeResource = jenaModel.createResource();
				jenaModel.add(typeResource, OWL.equivalentClass, equivalentTypeResource);
	
				RDFList rdfList = jenaModel.createList(enumValueNodes.iterator());			
				jenaModel.add(equivalentTypeResource, OWL.oneOf, rdfList);
				
			} else {
				
				if (baseTypeForLogical.equals(OWL2.NamedIndividual)) {
					enumValueNodes.stream().forEach(node ->
					((Resource)node).addProperty(RDF.type, typeResource));			
				}
				
			}
		}
		
		return typeResource;
	}
	

	/**
	 * Returns the equivalent XSD datatype for IFC types: BOOLEAN and LOGICAL
	 * @return
	 */
	public Resource getBaseTypeForLogical() {		
		return baseTypeForLogical;		
	}	
	
	public RDFNode convertLogicalValue(Model jenaModel, BemLogicalEnum value, BemTypeInfo typeInfo) {
		
		assert(typeInfo.getValueKind().isLogical());
		
		if (baseTypeForLogical.equals(OWL2.NamedIndividual)) {
			
			String resourceUri = typeInfo.isBuiltInType() ?
					manager.uriBuilder.buildBuiltInOntologyUri(value.name()) : manager.uriBuilder.buildOntologyUri(value.name());
			return jenaModel.createResource(resourceUri);
			
		} else {
			
//			String resourceUri = typeInfo.isBuiltInType() ?
//					manager.uriBuilder.buildBuiltInOntologyUri(value.name()) : manager.uriBuilder.buildOntologyUri(value.name());
//			
//			Resource resource = jenaModel.createResource(resourceUri);				
//			resource.addProperty(RDF.type, jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo)));				
//			
//			Property property = manager.getProperty_hasXXX(jenaModel, BemValueKindEnum.LOGICAL);
			
			RDFNode valueNode = jenaModel.createTypedLiteral(value.toString(), baseTypeForLogical.getURI());					
//			resource.addProperty(property, valueNode);
			
//			return resource;
			return valueNode;
		}
		
	}
//	



	
	
}
