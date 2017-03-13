package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.math.BigDecimal;
import java.util.*;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.common.params.StringParam;
import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

class Bem2RdfPrimitiveTypeConverter {
	
	private final Bem2RdfConverterManager manager;

	private final Resource baseTypeForDoubles;
	
	public Bem2RdfPrimitiveTypeConverter(Bem2RdfConverterManager manager) throws Bem2RdfConverterConfigurationException {
		this.manager = manager;
		
		//
		// calculate baseTypeForDoubles
		//		
		String convertDoubleValuesTo = (String)manager.contextParams.convertDoublesTo();
		switch (convertDoubleValuesTo) {
		case StringParam.VALUE_AUTO:
		case Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED:
		case Bem2RdfConversionContextParams.VALUE_XSD_DECIMAL:
			baseTypeForDoubles = XSD.decimal;
			break;
		case Bem2RdfConversionContextParams.VALUE_XSD_DOUBLE:
			baseTypeForDoubles = XSD.xdouble;
			break;
		case Bem2RdfConversionContextParams.VALUE_OWL_REAL:
			baseTypeForDoubles = OwlVocabulary.OWL.real;
			break;
		case Bem2RdfConversionContextParams.VALUE_XSD_STRING:
			baseTypeForDoubles = XSD.xstring;
			break;
		case Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT:
			List<Resource> preferredTypes = Arrays.asList(XSD.xdouble, OwlVocabulary.OWL.real, XSD.decimal);
			baseTypeForDoubles = manager.context.getTargetOwlProfileList().getFirstSupportedDatatype(preferredTypes);						
			break;
		default:
			throw new Bem2RdfConverterConfigurationException("Invalid value of option " + Bem2RdfConversionContextParams.PARAM_CONVERT_DOUBLES_TO);
		}
		
		
	}
	
	public Resource convertPrimitiveTypeInfo(Model jenaModel, BemPrimitiveTypeInfo typeInfo, boolean includeDetails) {
		
		Resource typeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo));
		
		if (includeDetails) {
		
			typeResource.addProperty(RDF.type, OWL.Class);
			
			Property property_hasXXX = manager.getProperty_hasXXX(jenaModel, typeInfo.getValueKind());
			
	//		jenaModel.add(property, RDF.type, OWL.DatatypeProperty);
	//		if (targetOwlProfileList.supportsRdfProperty(OWL.FunctionalProperty, null)) {
	//			jenaModel.add(property, RDF.type, OwlVocabulary.OWL.FunctionalDataProperty);
	//		}
			
			Property property_hasValue = jenaModel.createProperty(
					manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)); 
			
			jenaModel.add(
					property_hasXXX,
					RDFS.subPropertyOf, property_hasValue);
	//		jenaModel.add(property, RDFS.domain, typeResource);
	
			Resource baseDataType = getBaseTypeForPrimitiveValues(typeInfo.getValueKind());
	//		jenaModel.add(property, RDFS.range, baseDataType);
			
			// TODO: check if it is needed to export domains and ranges
			manager.convertPropertyRestrictions(jenaModel, property_hasXXX, typeResource, baseDataType, false, 1, 1, true, true);
	
	//		if (targetOwlProfileList.supportsRdfProperty(OWL.allValuesFrom, null)) {		
	//			
	//			writePropertyRestriction(typeResource, Ifc2RdfVocabulary.EXPRESS.value, OWL.allValuesFrom, getXsdDataType((IfcLiteralTypeInfo)baseTypeInfo));
	//		
	//		}
			
		}
		
		return typeResource;
		
	}
	
	
	/**
	 * Returns the equivalent XSD datatype of an IFC literal type
	 * @param typeInfo
	 * @return
	 * @throws IllegalArgumentException
	 *  
	 */
	public Resource getBaseTypeForPrimitiveValues(BemValueKindEnum valueKind) {
		
		if (valueKind == BemValueKindEnum.BINARY) {
//			return XSD.nonNegativeInteger;
			return XSD.hexBinary;			
		} else if (valueKind == BemValueKindEnum.INTEGER) {
			return XSD.integer;			
		} else if (valueKind == BemValueKindEnum.NUMBER || valueKind == BemValueKindEnum.REAL) {
			return baseTypeForDoubles;
		} else if (valueKind == BemValueKindEnum.DATETIME) {
			return XSD.dateTime;			
		} else if (valueKind == BemValueKindEnum.STRING) {
			return XSD.xstring;
		} else {
			throw new IllegalArgumentException(String.format("Not a primitive value kind '%s'", valueKind));
		}
		
	}
	
	/**
	 * Returns the equivalent XSD datatype for IFC types: REAL and NUMBER
	 * @return
	 *  
	 */
	public Resource getBaseTypeForDoubles() {		
		return baseTypeForDoubles;
		
	}
	
	
	public Resource convertPrimitiveValue(Model jenaModel, Object primitiveValue, BemTypeInfo typeInfo, Resource parentResource, long childNodeCount) {
		
		assert(typeInfo instanceof BemPrimitiveTypeInfo || typeInfo instanceof BemDefinedTypeInfo);
		
		RDFNode valueNode = convertLiteral(jenaModel, primitiveValue, typeInfo.getValueKind());		
		Property hasXXXProperty = manager.getProperty_hasXXX(jenaModel, typeInfo.getValueKind());

		Resource resource;
		if (manager.nameAllBlankNodes) {
			String rawNodeName = manager.uriBuilder.buildDatasetBlankNodeUri(String.format("%s_%s", parentResource.getLocalName(), childNodeCount));
			resource = jenaModel.createResource(rawNodeName);
		} else {
			resource = jenaModel.createResource();
		}

		resource.addProperty(RDF.type, jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo)));
		
		resource.addProperty(hasXXXProperty, valueNode);
		
		return resource;
		
	}
	
	
	public Literal convertLiteral(Model jenaModel, Object value, BemValueKindEnum valueKind) {
		
		String dataTypeUri = getBaseTypeForPrimitiveValues(valueKind).getURI();
		
		if (XSD.decimal.getURI().equals(dataTypeUri)) {
			// this must be done to avoid error when Jena rounds xsd:decimal as xsd:integer
			value = BigDecimal.valueOf((double)value);
		}
		
		return jenaModel.createTypedLiteral(value, dataTypeUri);
		
	}
	
}
