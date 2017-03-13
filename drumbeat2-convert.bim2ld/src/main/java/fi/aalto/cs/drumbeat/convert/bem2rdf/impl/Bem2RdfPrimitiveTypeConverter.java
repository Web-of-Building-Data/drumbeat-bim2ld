package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang3.NotImplementedException;
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
	
			Resource baseDataType = getBaseTypeForPrimitiveValues(typeInfo);
	//		jenaModel.add(property, RDFS.range, baseDataType);
			
			// TODO: check if it is needed to export domains and ranges
			manager.convertPropertyRestrictions(property_hasXXX, typeResource, baseDataType, false, 1, 1, jenaModel, true, true);
	
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
	public Resource getBaseTypeForPrimitiveValues(BemPrimitiveTypeInfo typeInfo) {
		
		BemValueKindEnum valueKind = typeInfo.getValueKind();
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
			throw new IllegalArgumentException(String.format("Unknown primitive type '%s'", typeInfo.getName()));
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
	
//	private Resource convertBooleanValue(Boolean value, BemTypeInfo typeInfo, Model jenaModel) {
//		
//		Resource baseType = getBaseTypeForBooleans();
//		if (baseType.equals(OWL2.NamedIndividual)) {
//			
//			return jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(value.toString()));
//			
//		} else {
//			
//			Resource resource = jenaModel.createResource();				
//			resource.addProperty(RDF.type, jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo)));				
//			
//			Property property = getProperty_hasXXX(BemValueKindEnum.LOGICAL, jenaModel);
//			
//			RDFNode valueNode;
//			if (value == BemLogicalEnum.TRUE) {
//				valueNode = jenaModel.createTypedLiteral("true", baseType.getURI());					
//			} else if (value == BemLogicalEnum.FALSE) {
//				valueNode = jenaModel.createTypedLiteral("false", baseType.getURI());					
//			} else {
//				valueNode = jenaModel.createTypedLiteral("unknown");					
//			}				
//						
//			resource.addProperty(property, valueNode);
//			
//			return resource;
//		}
//		
//	}
//	
//
//	public RDFNode convertPrimitiveValue(BemPrimitiveValue value, BemPrimitiveTypeInfo typeInfo, Model jenaModel) {
//		
//		BemValueKindEnum valueKind = value.getValueKind();
//		
//		if (valueKind == BemValueKindEnum.LOGICAL) {
//			return convertBooleanValue((BemLogicalEnum)value.getValue(), typeInfo, jenaModel);
//		}
//		
//		return null;
//	}
	
	
	
	public Resource convertPrimitiveValue(Model jenaModel, Object primitiveValue, BemTypeInfo typeInfo, Resource parentResource, long childNodeCount) {
		
		assert(typeInfo instanceof BemPrimitiveTypeInfo || typeInfo instanceof BemDefinedTypeInfo);
		
		RDFNode valueNode;
		BemValueKindEnum valueKind = typeInfo.getValueKind();
		
		if (valueKind == BemValueKindEnum.STRING) {
			valueNode = jenaModel.createTypedLiteral((String)primitiveValue);
		} else if (valueKind == BemValueKindEnum.REAL || valueKind == BemValueKindEnum.NUMBER) {
			Resource decimalTypeResource = getBaseTypeForDoubles();
			Object decimalValue = XSD.decimal.equals(decimalTypeResource) ?
					BigDecimal.valueOf((double)primitiveValue) : (Double)primitiveValue;
			valueNode = jenaModel.createTypedLiteral(decimalValue, decimalTypeResource.getURI());				
		} else if (valueKind == BemValueKindEnum.INTEGER) {				
			valueNode = jenaModel.createTypedLiteral((long)primitiveValue);
		} else if (valueKind == BemValueKindEnum.LOGICAL) {
			assert(false) : primitiveValue + ", type: " + typeInfo;
//			valueNode = converter.convertBooleanValue(typeInfo, (BemLogicalEnum)value, jenaModel);
			throw new NotImplementedException("");
		} else {
			assert (valueKind == BemValueKindEnum.DATETIME) : "Expected: valueType == BemValueKindEnum.DATETIME. Actual: valueType = " + valueKind + ", " + typeInfo;
			valueNode = jenaModel.createTypedLiteral((Calendar)primitiveValue);				
		}
		
		Property hasXXXProperty = manager.getProperty_hasXXX(jenaModel, valueKind);

		Resource resource;
		if (manager.nameAllBlankNodes) {
			//String rawNodeName = String.format("%s_%s", hasXXXProperty.getLocalName(), value);
//			String encodedNodeName = EncoderTypeEnum.encode(EncoderTypeEnum.SafeUrl, rawNodeName);			

			String rawNodeName = manager.uriBuilder.buildDatasetBlankNodeUri(String.format("%s_%s", parentResource.getLocalName(), childNodeCount));
			resource = jenaModel.createResource(rawNodeName);
		} else {
			resource = jenaModel.createResource();
		}

		resource.addProperty(RDF.type, jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo)));
		
		resource.addProperty(hasXXXProperty, valueNode);
		
		return resource;
		
	}
		
	
	
}
