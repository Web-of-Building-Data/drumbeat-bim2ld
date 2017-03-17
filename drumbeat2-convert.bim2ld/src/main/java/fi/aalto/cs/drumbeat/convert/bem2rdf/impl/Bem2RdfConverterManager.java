package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConverterConfigurationException;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfUriBuilder;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemCollectionValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemComplexValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEnumerationValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemLogicalValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemSimpleValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemSpecialValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemTypedSimpleValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

public class Bem2RdfConverterManager {
	
	/**
	 * Package variables
	 */
	final private boolean nameAllBlankNodes;

	final Bem2RdfConversionContext context;
	final Bem2RdfUriBuilder uriBuilder;
	final Bem2RdfConversionContextParams contextParams;
	final OwlProfileList limitingOwlProfileList;
	
	final Bem2RdfBuiltInOntologyConverter builtInOntologyConverter;
	final Bem2RdfCollectionTypeConverter collectionTypeConverter;
	final Bem2RdfDefinedTypeConverter definedTypeConverter;
	final Bem2RdfEnumerationTypeConverter enumerationTypeConverter;
	final Bem2RdfEntityTypeConverter entityTypeConverter;
	final Bem2RdfLogicalTypeConverter logicalTypeConverter;
	final Bem2RdfPrimitiveTypeConverter primitiveTypeConverter;
	final Bem2RdfSelectTypeConverter selectTypeConverter;
	
	final Map<BemValueKindEnum, Property> map_Type_hasXXXProperty;
	
	public Bem2RdfConverterManager(Bem2RdfConversionContext context, Bem2RdfUriBuilder uriBuilder) throws Bem2RdfConverterConfigurationException {
		this.context = context;
		this.uriBuilder = uriBuilder;
		
		contextParams = context.getConversionParams();
		limitingOwlProfileList = context.getLimitingOwlProfileList();
		nameAllBlankNodes = contextParams.nameAllBlankNodes() || !limitingOwlProfileList.supportsAnonymousIndividual();
		
		builtInOntologyConverter = new Bem2RdfBuiltInOntologyConverter(this);
//		collectionTypeConverter = new Bem2RdfCollectionTypeConverter(this);
		definedTypeConverter = new Bem2RdfDefinedTypeConverter(this);
		enumerationTypeConverter = new Bem2RdfEnumerationTypeConverter(this);
		entityTypeConverter = new Bem2RdfEntityTypeConverter(this);
		logicalTypeConverter = new Bem2RdfLogicalTypeConverter(this);
		primitiveTypeConverter = new Bem2RdfPrimitiveTypeConverter(this);
		selectTypeConverter = new Bem2RdfSelectTypeConverter(this);
		
		String convertCollectionsTo = contextParams.convertCollectionsTo();		
		switch (convertCollectionsTo) {
		case Bem2RdfConversionContextParams.VALUE_DRUMMOND_LIST:
			collectionTypeConverter = new Bem2RdfDrummondListCollectionTypeConverter(this);
			break;
			
		case Bem2RdfConversionContextParams.VALUE_OLO_SIMILAR_LIST:
			collectionTypeConverter = new Bem2RdfOloSimilarListCollectionTypeConverter(this);
			break;

		default:
			 throw new Bem2RdfConverterConfigurationException("Unknown collection type: " + convertCollectionsTo);
		}
		

		
		map_Type_hasXXXProperty = new HashMap<>();
	}
	
	public Bem2RdfConverterManager(Bem2RdfConversionContext context, BemSchema bemSchema) throws Bem2RdfConverterConfigurationException {
		this(context, Bem2RdfUriBuilder.createUriBuilder(context, bemSchema));
	}
	
	public Bem2RdfUriBuilder getUriBuilder() {
		return uriBuilder;
	}
	
	public void exportPermanentBuiltInDefinitions(Model jenaModel) {
		builtInOntologyConverter.exportPermanentBuiltInDefinitions(jenaModel);
	}
	
	public Resource convertTypeInfo(Model jenaModel, BemTypeInfo typeInfo, boolean includeDetails) {
		
		if (typeInfo instanceof BemSimpleTypeInfo) {
			
			if (typeInfo instanceof BemEnumerationTypeInfo) {
				
				return enumerationTypeConverter.convertEnumerationTypeInfo(jenaModel, (BemEnumerationTypeInfo) typeInfo, includeDetails);
				
			} else if (typeInfo instanceof BemLogicalTypeInfo) {
				
				return logicalTypeConverter.convertLogicalTypeInfo(jenaModel, (BemLogicalTypeInfo) typeInfo, includeDetails);
				
			} else if (typeInfo instanceof BemPrimitiveTypeInfo) {
				
				return primitiveTypeConverter.convertPrimitiveTypeInfo(jenaModel, (BemPrimitiveTypeInfo) typeInfo, includeDetails);				
				
			}
			
		} else {
			
			if (typeInfo instanceof BemEntityTypeInfo) {
				
				return entityTypeConverter.convertEntityTypeInfo(jenaModel, (BemEntityTypeInfo) typeInfo, includeDetails);
				
			} else if (typeInfo instanceof BemSelectTypeInfo) {
				
				return selectTypeConverter.convertSelectTypeInfo(jenaModel, (BemSelectTypeInfo) typeInfo, includeDetails);
				
			} else if (typeInfo instanceof BemDefinedTypeInfo) {
				
				return definedTypeConverter.convertDefinedTypeInfo(jenaModel, (BemDefinedTypeInfo) typeInfo, includeDetails);
				
			} else if (typeInfo instanceof BemCollectionTypeInfo) {
				
				return collectionTypeConverter.convertCollectionTypeInfo(jenaModel, (BemCollectionTypeInfo) typeInfo, includeDetails);
				
			} 
			
		}
		
		throw new IllegalArgumentException("Unexpected type: " + typeInfo);
	}
	
	/**
	 * Converts any {@link BemValue} value to {@link RDFNode}
	 * @param value
	 * @param typeInfo
	 * @return
	 * @
	 */
	public RDFNode convertValue(Model jenaModel, BemValue value, BemTypeInfo typeInfo, Resource parentResource, int childNodeCount, boolean includeEntityAttributes) {		
		
		if (value instanceof BemSimpleValue) {
			
			Object internalValue = ((BemSimpleValue)value).getValue();
			
			if (value instanceof BemEnumerationValue) {
				
				return enumerationTypeConverter.convertEnumerationValue(
						jenaModel, (String)internalValue, (BemEnumerationTypeInfo)typeInfo);
				
			} else if (value instanceof BemPrimitiveValue) {
				
				assert(typeInfo instanceof BemPrimitiveTypeInfo || typeInfo instanceof BemDefinedTypeInfo);
				
//				assert(typeInfo.getValueKind() != BemValueKindEnum.LOGICAL) : value;
				
				return primitiveTypeConverter.convertPrimitiveValue(jenaModel, internalValue, typeInfo, parentResource, childNodeCount);
				
			} else if (value instanceof BemLogicalValue) {
				
				return logicalTypeConverter.convertLogicalValue(jenaModel, (BemLogicalEnum)internalValue, typeInfo);			
				
			}
			
		} else if (value instanceof BemComplexValue) {
			
			if (value instanceof BemEntity) {			
				return entityTypeConverter.convertEntityValue(jenaModel, (BemEntity)value, includeEntityAttributes);
			} else if (value instanceof BemTypedSimpleValue) {
				//return convertTypedSimpleValue(jenaModel, (BemTypedSimpleValue)value, parentResource, childNodeCount);
				return convertValue(jenaModel, ((BemTypedSimpleValue)value).getValue(), ((BemTypedSimpleValue)value).getType(), parentResource, childNodeCount, false);
			} else if (value instanceof BemCollectionValue<?>) {
				return collectionTypeConverter.convertListToResource(
						jenaModel, (BemCollectionValue<?>)value, (BemCollectionTypeInfo)typeInfo, parentResource, childNodeCount);
			}
			
		} if (value instanceof BemSpecialValue) {
			
			return jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(((BemSpecialValue)value).getName()));
			
		}
		
		throw new IllegalArgumentException(String.format("Unexpected value: %s (type: %s)", value, typeInfo));
		
	}
	
	public Resource convertEntityValue(Model jenaModel, BemEntity entity) {
		return entityTypeConverter.convertEntityValue(jenaModel, entity, true);
	}
	
	
	
	public Resource createLocalResource(Model jenaModel, String localName) {
		if (nameAllBlankNodes) {
			return jenaModel.createResource(uriBuilder.buildDatasetBlankNodeUri(localName));
		} else {
			return jenaModel.createResource(new AnonId(localName));
		}		
	}
	
	public Resource createLocalResource(Model jenaModel, Resource parentResource, int childNodeCount) {
		assert(parentResource != null);
		String localName = String.format("%s_%s",
				parentResource.isURIResource() ? parentResource.getLocalName() : parentResource.getId(),
				childNodeCount);
		return createLocalResource(jenaModel, localName);
	}
	
	/**
	 * Returns an RDF property with name in format hasXXX, where XXX is the name of the original type (e.g.: hasReal, hasNumber, hasLogical, etc.)  
	 * @param baseTypeInfo
	 * @return
	 */
	Property getProperty_hasXXX(Model jenaModel, BemValueKindEnum valueKind) {
		Property property_hasXXX = map_Type_hasXXXProperty.get(valueKind);
		if (property_hasXXX == null) {
			String valueTypeName = valueKind.toString();			
			String propertyName = String.format("has%s%s", valueTypeName.substring(0, 1).toUpperCase(), valueTypeName.substring(1).toLowerCase());
			property_hasXXX = jenaModel.createProperty(uriBuilder.buildBuiltInOntologyUri(propertyName));
			map_Type_hasXXXProperty.put(valueKind, property_hasXXX);
		}
		return property_hasXXX;
	}
	
	

//	//*****************************************
//	// Region ONTOLOGY
//	//*****************************************	
//	
//	
//	/**
//	 * Creates an RDF resource which has type {@link OWL.Ontology}, predefined version string and comment
//	 * @param uri
//	 * @param version
//	 * @param comment
//	 * @return
//	 */
//	private Resource createOntologyResource(String uri, String version, String comment, Model jenaModel) {
//		
//		// TODO: Update ontology
//		
//		Resource ontology = jenaModel.createResource(uri);
//		ontology.addProperty(RDF.type, OWL.Ontology);
//		ontology.addProperty(OWL.versionInfo, String.format("\"v%1$s %2$tY/%2$tm/%2$te %2$tH:%2$tM:%2$tS\"", version, new Date()));
//		if (comment != null) {
//			ontology.addProperty(RDFS.comment, String.format("\"\"\"%s\"\"\"", comment));
//		}
//		return ontology;
//		
//	}
	
	public void exportNsPrefixes(Model jenaModel) {
		jenaModel.setNsPrefixes(uriBuilder.getNamespacePrefixMap());		
	}
	
	
	public void exportOntologyHeader(Model jenaModel) {
//
//		TODO: Uncomment the following part
//		
//		// define expr:
//		jenaModel.setNsPrefix(
//				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.BASE_PREFIX)),
//				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.getBaseUri());
//
//		if (!context.getConversionParams().ignorePrimitiveTypes()) {
//			// define ifc:
//			jenaModel.setNsPrefix(Bem2RdfVocabulary.IFC.BASE_PREFIX,
//					converter.getIfcOntologyNamespaceUri());
//		}
//
//		// 
//
//		String conversionParamsString = context.getConversionParams()
//				.toString().replaceFirst("\\[", "[\r\n\t\t\t ")
//				.replaceFirst("\\]", "\r\n\t\t]").replaceAll(",", "\r\n\t\t\t");
//
//		// TODO: Format ontology comment here
//		conversionParamsString = String.format(
//				"OWL profile: %s.\r\n\t\tConversion options: %s",
//				context.getOwlProfileList().getOwlProfileIds(), conversionParamsString);
//
//		// adapter.exportOntologyHeader(converter.getIfcOntologyNamespaceUri(), "1.0",
//		// conversionParamsString);
//
//		Resource ontology = jenaModel.createResource(converter.getIfcOntologyNamespaceUri());
//		ontology.addProperty(RDF.type, OWL.Ontology);
//		String version = "1.0";
//		ontology.addProperty(OWL.versionInfo, String.format(
//				"v%1$s %2$tY/%2$tm/%2$te %2$tH:%2$tM:%2$tS", version,
//				new Date()));
//		if (conversionParamsString != null) {
//			// ontology.addProperty(RDFS.comment,
//			// String.format("\"\"\"%s\"\"\"", comment));
//			ontology.addProperty(RDFS.comment, conversionParamsString);
//		}
		
	}
		
//	
//
//	//*****************************************
//	// EndRegion ONTOLOGY
//	//*****************************************
	

	
	//*****************************************
	// Region PROPERTIES
	//*****************************************
	
	void convertPropertyRestrictions(
			Model jenaModel,
			Property property,
			Resource domain,
			Resource range,
			boolean isObjectProperty,
			Integer min,
			Integer max,
			boolean exportRdfsDomain,
			boolean exportRdfsRange) {
		
		property.addProperty(RDF.type, isObjectProperty ? OWL.ObjectProperty : OWL.DatatypeProperty);
		
		assert(domain != null);
		
		// TODO: double check if domains and ranges are really needed
		if (!exportRdfsDomain) {
			property.addProperty(RDFS.domain, domain);
		}
		
		if (range != null && !exportRdfsRange) {
			property.addProperty(RDFS.range, range);
		}

		if (max != null && max == 1 && limitingOwlProfileList.supportsStatement(RDF.type, OWL.FunctionalProperty)) {			
			// TODO: detect when FunctionalDataProperty is supported
			property.addProperty(RDF.type, isObjectProperty ? OWL.FunctionalProperty : OwlVocabulary.OWL.FunctionalDataProperty);
		}

		
//		jenaModel.add(attributeResource, RDF.type, Ifc2RdfVocabulary.EXPRESS.EntityProperty);						

		if (limitingOwlProfileList.supportsStatement(RDF.type, OWL.Restriction)) {

			//
			// write constraint about property type
			//
			if (range != null && limitingOwlProfileList.supportsStatement(OWL.allValuesFrom, null)) {
				exportPropertyRestriction(domain, property, OWL.allValuesFrom, range, jenaModel);
			}
			
			RDFNode minNode = null;
			if (min != null) {
				minNode = primitiveTypeConverter.convertLiteral(jenaModel, min, BemValueKindEnum.INTEGER);
			}
			
			RDFNode maxNode = null;
			if (max != null && max != Integer.MAX_VALUE) {
				maxNode = primitiveTypeConverter.convertLiteral(jenaModel, max, BemValueKindEnum.INTEGER);
			}
			
			if (minNode != null) {
				if (minNode.equals(maxNode) && limitingOwlProfileList.supportsStatement(OWL.cardinality, minNode)) {
					exportPropertyRestriction(domain, property, OWL.cardinality, minNode, jenaModel);
					minNode = null;
					maxNode = null;
				} else if (limitingOwlProfileList.supportsStatement(OWL.minCardinality, minNode)) {
					exportPropertyRestriction(domain, property, OWL.minCardinality, minNode, jenaModel);
					minNode = null;
				}
			}
			
			if (maxNode != null) {
				if (limitingOwlProfileList.supportsStatement(OWL.maxCardinality, maxNode)) {
					exportPropertyRestriction(domain, property, OWL.maxCardinality, maxNode, jenaModel);
					minNode = null;
				}
			}
		}		
	}
	

	private void exportPropertyRestriction(Resource classResource, Resource propertyResource, Property restrictionProperty, RDFNode propertyValue, Model jenaModel) {
		Resource baseTypeResource = jenaModel.createResource();
		jenaModel.add(baseTypeResource, RDF.type, OWL.Restriction);
		jenaModel.add(baseTypeResource, OWL.onProperty, propertyResource);
		jenaModel.add(baseTypeResource, restrictionProperty, propertyValue);
		
		jenaModel.add(classResource, RDFS.subClassOf, baseTypeResource);		
	}
	
	
	
	//*****************************************
	// EndRegion PROPERTIES
	//*****************************************
	

	
	

}
