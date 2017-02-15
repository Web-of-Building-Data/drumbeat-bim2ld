package fi.aalto.cs.drumbeat.convert.bem2rdf;

import fi.aalto.cs.drumbeat.common.params.*;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.rdf.OwlProfileList;
import fi.aalto.cs.drumbeat.rdf.RdfVocabulary;

import java.util.*;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

public class Bem2RdfConverter {
	
	private static final Object MUTEX = new Object();

	private final Bem2RdfConversionContext context;
	private final Bem2RdfConversionContextParams contextParams;
	private final Bem2RdfUriBuilder uriBuilder;
	private final Map<BemValueKindEnum, Property> map_Type_hasXXXProperty;
	private final OwlProfileList targetOwlProfileList;
	
	private Resource baseTypeForDoubles;
	private Resource baseTypeForBooleans;	
	private Resource baseTypeForEnums;

	public Bem2RdfConverter(Bem2RdfConversionContext context, Bem2RdfUriBuilder uriBuilder) {
		this.context = context;
		this.contextParams = context.getConversionParams();
		this.targetOwlProfileList = context.getTargetOwlProfileList();
		this.uriBuilder = uriBuilder;
		map_Type_hasXXXProperty = new HashMap<>();
	}
	
	public Bem2RdfConverter(Bem2RdfConversionContext context, BemSchema bemSchema) throws Bem2RdfConverterConfigurationException {
		this(context, Bem2RdfUriBuilder.createUriBuilder(context, bemSchema));
	}
	
	

	//*****************************************
	// Region ONTOLOGY
	//*****************************************	
	
	
	/**
	 * Creates an RDF resource which has type {@link OWL.Ontology}, predefined version string and comment
	 * @param uri
	 * @param version
	 * @param comment
	 * @return
	 */
	private Resource createOntologyResource(String uri, String version, String comment, Model jenaModel) {
		
		// TODO: Update ontology
		
		Resource ontology = jenaModel.createResource(uri);
		ontology.addProperty(RDF.type, OWL.Ontology);
		ontology.addProperty(OWL.versionInfo, String.format("\"v%1$s %2$tY/%2$tm/%2$te %2$tH:%2$tM:%2$tS\"", version, new Date()));
		if (comment != null) {
			ontology.addProperty(RDFS.comment, String.format("\"\"\"%s\"\"\"", comment));
		}
		return ontology;
		
	}
	

	//*****************************************
	// EndRegion ONTOLOGY
	//*****************************************
	
	
	
	//*****************************************
	// Region LITERAL TYPES AND VALUES
	//*****************************************
	
	public Resource convertPrimitiveTypeInfo(BemPrimitiveTypeInfo typeInfo, Model jenaModel) throws Bem2RdfConverterConfigurationException {
		
		Resource typeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo)); 
		
		typeResource.addProperty(RDF.type, OWL.Class);
		
		Property property_hasXXX = getProperty_hasXXX(typeInfo.getValueKind(), jenaModel);
		
//		jenaModel.add(property, RDF.type, OWL.DatatypeProperty);
//		if (targetOwlProfileList.supportsRdfProperty(OWL.FunctionalProperty, null)) {
//			jenaModel.add(property, RDF.type, RdfVocabulary.OWL.FunctionalDataProperty);
//		}
		
		Property property_hasValue = jenaModel.createProperty(
				uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)); 
		
		jenaModel.add(
				property_hasXXX,
				RDFS.subPropertyOf, property_hasValue);
//		jenaModel.add(property, RDFS.domain, typeResource);

		Resource baseDataType = getBaseTypeForPrimitiveValues(typeInfo);
//		jenaModel.add(property, RDFS.range, baseDataType);
		
		// TODO: check if it is needed to export domains and ranges
		convertPropertyRestrictions(property_hasXXX, typeResource, baseDataType, false, 1, 1, jenaModel);

//		if (targetOwlProfileList.supportsRdfProperty(OWL.allValuesFrom, null)) {		
//			
//			writePropertyRestriction(typeResource, Ifc2RdfVocabulary.EXPRESS.value, OWL.allValuesFrom, getXsdDataType((IfcLiteralTypeInfo)baseTypeInfo));
//		
//		}
		
		return typeResource;
		
	}	
	
	
	
	/**
	 * Returns the equivalent XSD datatype of an IFC literal type
	 * @param typeInfo
	 * @return
	 * @throws Bem2RdfConverterConfigurationException 
	 *  
	 */
	public Resource getBaseTypeForPrimitiveValues(BemPrimitiveTypeInfo typeInfo) throws Bem2RdfConverterConfigurationException {
		
		BemValueKindEnum valueKind = typeInfo.getValueKind();
		if (valueKind == BemValueKindEnum.BINARY) {
//			return XSD.nonNegativeInteger;
			return XSD.hexBinary;			
		} else if (valueKind == BemValueKindEnum.BOOLEAN) {
			return getBaseTypeForBooleans();
		} else if (valueKind == BemValueKindEnum.INTEGER) {
			return XSD.integer;			
		} else if (valueKind == BemValueKindEnum.NUMBER || valueKind == BemValueKindEnum.REAL) {
			return getBaseTypeForDoubles();
		} else if (valueKind == BemValueKindEnum.DATETIME) {
			return XSD.dateTime;			
		} else if (valueKind == BemValueKindEnum.STRING) {
			return XSD.xstring;
		} else {
			throw new Bem2RdfConverterConfigurationException(String.format("Unknown primitive type '%s'", typeInfo.getName()));
		}
		
	}
	
	
	/**
	 * Returns the equivalent XSD datatype for IFC types: BOOLEAN and LOGICAL
	 * @return
	 * @throws Bem2RdfConverterConfigurationException 
	 */
	public Resource getBaseTypeForBooleans() throws Bem2RdfConverterConfigurationException {
		
		if (baseTypeForBooleans == null) {
			String convertBooleanValuesTo = context.getConversionParams().convertBooleansTo();
			switch (convertBooleanValuesTo) {
			case StringParam.VALUE_AUTO:
			case Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL:
				baseTypeForBooleans = OWL2.NamedIndividual;
				break;
			case Bem2RdfConversionContextParams.VALUE_XSD_STRING:
				baseTypeForBooleans = XSD.xstring;
				break;
			case Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN:
				baseTypeForBooleans = XSD.xboolean;
				break;
			default:
				throw new Bem2RdfConverterConfigurationException("Invalid value of option " + Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO);
			}
		}
		return baseTypeForBooleans;		
	}	
	
	/**
	 * Returns the equivalent XSD datatype for IFC types: REAL and NUMBER
	 * @return
	 * @throws Bem2RdfConverterConfigurationException 
	 *  
	 */
	public Resource getBaseTypeForDoubles() throws Bem2RdfConverterConfigurationException {
		
		if (baseTypeForDoubles == null) {
			String convertDoubleValuesTo = (String)contextParams.convertDoublesTo();
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
				baseTypeForDoubles = RdfVocabulary.OWL.real;
				break;
			case Bem2RdfConversionContextParams.VALUE_XSD_STRING:
				baseTypeForDoubles = XSD.xstring;
				break;
			case Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT:
				List<Resource> preferredTypes = Arrays.asList(XSD.xdouble, RdfVocabulary.OWL.real, XSD.decimal);
				baseTypeForDoubles = context.getTargetOwlProfileList().getFirstSupportedDatatype(preferredTypes);						
				break;
			default:
				throw new Bem2RdfConverterConfigurationException("Invalid value of option " + Bem2RdfConversionContextParams.PARAM_CONVERT_DOUBLES_TO);
			}
		}
		return baseTypeForDoubles;		
		
	}
	
	/**
	 * Returns an RDF property with name in format hasXXX, where XXX is the name of the original type (e.g.: hasReal, hasNumber, hasLogical, etc.)  
	 * @param baseTypeInfo
	 * @return
	 */
	private Property getProperty_hasXXX(BemValueKindEnum valueKind, Model jenaModel) {
		Property property_hasXXX = map_Type_hasXXXProperty.get(valueKind);
		if (property_hasXXX == null) {
			String valueTypeName = valueKind.toString();			
			String propertyName = String.format("has%s%s", valueTypeName.substring(0, 1).toUpperCase(), valueTypeName.substring(1).toLowerCase());
			property_hasXXX = jenaModel.createProperty(
					uriBuilder.buildBuiltInOntologyUri(propertyName));
			map_Type_hasXXXProperty.put(valueKind, property_hasXXX);
		}
		return property_hasXXX;
	}
	
//	private Resource convertBooleanValue(Boolean value, BemTypeInfo typeInfo, Model jenaModel) throws Bem2RdfConverterConfigurationException {
//		
//		Resource baseType = getBaseTypeForBooleans();
//		if (baseType.equals(OWL2.NamedIndividual)) {
//			
//			return jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(value.toString()));
//			
//		} else {
//			
//			Resource resource = jenaModel.createResource();				
//			resource.addProperty(RDF.type, jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo)));				
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
//	public RDFNode convertPrimitiveValue(BemPrimitiveValue value, BemPrimitiveTypeInfo typeInfo, Model jenaModel) throws Bem2RdfConverterConfigurationException {
//		
//		BemValueKindEnum valueKind = value.getValueKind();
//		
//		if (valueKind == BemValueKindEnum.LOGICAL) {
//			return convertBooleanValue((BemLogicalEnum)value.getValue(), typeInfo, jenaModel);
//		}
//		
//		return null;
//	}
	
	
	
	//*****************************************
	// EndRegion LITERAL TYPES AND VALUES
	//*****************************************
	
	
	
	
	//*****************************************
	// Region ENUM TYPES & VALUES
	//*****************************************
	
	/**
	 * Returns the equivalent XSD datatype for IFC types: ENUM and LOGICAL
	 * @return
	 * @throws Bem2RdfConverterConfigurationException 
	 *  
	 */
	private Resource getBaseTypeForEnums() throws Bem2RdfConverterConfigurationException {
		
		if (baseTypeForEnums == null) {
			String convertEnumValuesTo = (String)contextParams.convertEnumsTo();
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
		return baseTypeForEnums;
		
	}
	
	
	Resource convertEnumerationValue(BemEnumerationTypeInfo typeInfo, String value, Model jenaModel) throws Bem2RdfConverterConfigurationException {

		Resource baseType = getBaseTypeForEnums();
		if (baseType.equals(OWL2.NamedIndividual)) {
			String valueUri = typeInfo.isBuiltInType() ? uriBuilder.buildBuiltInOntologyUri(value) : uriBuilder.buildOntologyUri(value); 
			return jenaModel.createResource(valueUri);			
		} else {			
			Resource resource = jenaModel.createResource();
			resource.addLiteral(RDF.type, jenaModel);
			resource.addLiteral(
					jenaModel.createProperty(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)),
					baseType.getURI());
			return resource;

//			return jenaModel.createTypedLiteral(value, baseType.getURI());
		}		
		
	}
	
	public Resource convertEnumerationTypeInfo(BemEnumerationTypeInfo typeInfo, Model jenaModel) throws Bem2RdfConverterConfigurationException {
		
		String typeUri = uriBuilder.buildTypeUri(typeInfo);
		Resource typeResource = jenaModel.createResource(typeUri);
		typeResource.addProperty(RDF.type, OWL.Class);
		typeResource.addProperty(
				RDFS.subClassOf,
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Enum)));		

		List<String> enumValues = typeInfo.getValues(); 
		List<RDFNode> enumValueNodes = new ArrayList<>();

		for (String value : enumValues) {
			enumValueNodes.add(convertEnumerationValue(typeInfo, value, jenaModel));
		}
		
//		assert(enumValueNodes.size() > 1) : String.format("Type %s has only 1 enum value", typeInfo);
		
		final boolean enumerationIsSupported = targetOwlProfileList.supportsStatement(OWL.oneOf, RdfVocabulary.DUMP_URI_LIST);	
				
		if (enumerationIsSupported) {
			
			Resource equivalentTypeResource = jenaModel.createResource();
			jenaModel.add(typeResource, OWL.equivalentClass, equivalentTypeResource);

			RDFList rdfList = jenaModel.createList(enumValueNodes.iterator());			
			jenaModel.add(equivalentTypeResource, OWL.oneOf, rdfList);
			
		} else {
			
			Resource baseType = getBaseTypeForEnums();	
			
			if (baseType.equals(OWL2.NamedIndividual)) {
				enumValueNodes.stream().forEach(node ->
				((Resource)node).addProperty(RDF.type, typeResource));			
			}
		}
		
		return typeResource;
		
	}
	
	
	//*****************************************
	// EndRegion ENUM TYPES & VALUES
	//*****************************************
	
	
	
	//*****************************************
	// Region COLLECTION TYPES AND VALUES
	//*****************************************
	
	public Resource convertCollectionTypeInfo(BemCollectionTypeInfo typeInfo, Model jenaModel) throws Bem2RdfConverterConfigurationException {

		Resource listTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo));
		
		if (jenaModel.contains(listTypeResource, RDF.type, (RDFNode)null)) {
			return listTypeResource;
		}
		
		synchronized (MUTEX) {
			final String convertCollectionsTo = context.getConversionParams().convertCollectionsTo();

			switch (convertCollectionsTo) {
			case Bem2RdfConversionContextParams.VALUE_DRUMMOND_LIST:
				return convertCollectionTypeInfoToDrummondList(typeInfo, jenaModel);
			case Bem2RdfConversionContextParams.VALUE_OLO_SIMILAR_LIST:
				throw new NotImplementedException("Converting collections to " + Bem2RdfConversionContextParams.VALUE_OLO_SIMILAR_LIST);
			default:
				throw new NotImplementedException("Unknown collection type");
			}			
		}

	}
	 
	private Resource convertCollectionTypeInfoToDrummondList(BemCollectionTypeInfo typeInfo, Model jenaModel) {
		if (typeInfo.isSorted()) {
				
			Resource listTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo));
			jenaModel.add(listTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(listTypeResource, RDFS.subClassOf,
					jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
			
			Resource itemTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo.getItemTypeInfo()));
			convertPropertyRestrictions(
					jenaModel.createProperty(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
					listTypeResource, itemTypeResource, true, 0, 1, jenaModel);
			
			String additionalCollectionTypeName = String.format("%s_%s", typeInfo.getItemTypeInfo(), typeInfo.getCollectionKind());
			Resource additionalListTypeResource = jenaModel.createResource(uriBuilder.buildOntologyUri(additionalCollectionTypeName));
			 
			convertPropertyRestrictions(
					jenaModel.createProperty(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
					listTypeResource,
					additionalListTypeResource, true, 1, 1, jenaModel);
			 
			Resource emptyListTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo).replace("List", "EmptyList"));
			jenaModel.add(emptyListTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(emptyListTypeResource, RDFS.subClassOf, listTypeResource);
			jenaModel.add(
					listTypeResource,
					RDFS.subClassOf,
					jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EmptyList)));
			 
			return listTypeResource;			 
			 
		} else {
			
			Resource setTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo));
			jenaModel.add(setTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(
					setTypeResource,
					RDFS.subClassOf,
					jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Set)));
			
			Resource itemTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo.getItemTypeInfo()));
			int min = typeInfo.getCardinality().getMinCardinality();
			int max = typeInfo.getCardinality().getMaxCardinality();
			convertPropertyRestrictions(
					jenaModel.createProperty(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasSetItem)),
					setTypeResource, itemTypeResource, true, min, max, jenaModel);
			
			return setTypeResource;
			 
		}		 
	}
	 
	//*****************************************
	// EndRegion COLLECTION TYPES AND VALUES
	//*****************************************
	
	

	//*****************************************
	// Region DEFINED TYPES & VALUES
	//*****************************************
	
	
	public Resource convertDefinedTypeInfo(BemDefinedTypeInfo typeInfo, Model jenaModel) {
		
		Resource typeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo));
		jenaModel.add(typeResource, RDF.type, OWL.Class);
		
		BemTypeInfo baseTypeInfo = typeInfo.getWrappedTypeInfo();
		assert(baseTypeInfo != null);
		
		if (baseTypeInfo instanceof BemDefinedTypeInfo) {
			
			// subclass of another Defined class				
			Resource superTypeResource = jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(baseTypeInfo.getName()));
			jenaModel.add(typeResource, RDFS.subClassOf, superTypeResource);

		} else if (baseTypeInfo instanceof BemPrimitiveTypeInfo) {

			Resource superTypeResource = jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(baseTypeInfo.getName()));
			jenaModel.add(typeResource, RDFS.subClassOf, superTypeResource);
			jenaModel.add(
					typeResource,
					RDFS.subClassOf,
					jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Defined)));
			
		} else {
			
			assert(false) : baseTypeInfo.getClass();
			
//			Resource superTypeResource = jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(baseTypeInfo.getName()));			
//			Property property = getHasXXXProperty(baseTypeInfo.getValueKind(), jenaModel);
//			
//			convertPropertyRestrictions(property, typeResource, superTypeResource, true, 1, 1, jenaModel);			
			
		}
		
		return typeResource;
	}
	
	//*****************************************
	// EndRegion DEFINED TYPES & VALUES
	//*****************************************
	
	
	//*****************************************
	// Region SELECT TYPES & VALUES
	//*****************************************
	
	public Resource convertSelectTypeInfo(BemSelectTypeInfo typeInfo, Model jenaModel) {

		Resource typeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo));
		jenaModel.add(typeResource, RDF.type, OWL.Class);
		jenaModel.add(typeResource, RDFS.subClassOf,
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Select)));

		List<BemTypeInfo> subTypes = typeInfo.getItemTypeInfos();
		List<Resource> subTypeResources = new ArrayList<>();
		for (BemTypeInfo subType : subTypes) {
			subTypeResources.add(jenaModel.createResource(uriBuilder.buildTypeUri(subType)));
		}

		final boolean unionIsSupported = targetOwlProfileList.supportsStatement(
				OWL.unionOf, RdfVocabulary.DUMP_URI_LIST);

		if (unionIsSupported && subTypeResources.size() > 1) {
			RDFList rdfList = jenaModel.createList(subTypeResources.iterator());
			// See samples: [2, p.250]
			jenaModel.add(typeResource, OWL.unionOf, rdfList);
		} else {
			subTypeResources.stream().forEach(
					subTypeResource -> jenaModel.add(
							(Resource) subTypeResource, RDFS.subClassOf,
							typeResource));
		}
		
		return typeResource;
	}
	
	//*****************************************
	// EndRegion SELECT TYPES & VALUES
	//*****************************************
	
	
	//*****************************************
	// Region ENTITY TYPES & VALUES
	//*****************************************
	
	public Resource convertEntityTypeInfo(BemEntityTypeInfo typeInfo, Model jenaModel) throws BemException {
			
		 Resource typeResource = jenaModel.createResource(uriBuilder.buildTypeUri(typeInfo));
		 jenaModel.add(typeResource, RDF.type, OWL.Class);
	
		 //
		 // OWL2 supports owl:disjointUnionOf
		 // See: http://www.w3.org/2007/OWL/wiki/New_Features_and_Rationale#F1:_DisjointUnion
		 //
		 List<BemEntityTypeInfo> disjointClasses = null;
		
		 final boolean supportsDisjointUnionOf = targetOwlProfileList.supportsStatement(OWL2.disjointUnionOf, RdfVocabulary.DUMP_URI_LIST);
		
		 if (typeInfo.isAbstractSuperType() && supportsDisjointUnionOf) {
			 List<BemEntityTypeInfo> allSubtypeInfos = typeInfo.getSubTypeInfos();
			 if (allSubtypeInfos.size() > 1) { // OWL2.disjointUnionOf requires at least two members
				 List<RDFNode> allSubtypeResources = new ArrayList<>(allSubtypeInfos.size());
				 for (BemEntityTypeInfo subTypeInfo : allSubtypeInfos) {
					 allSubtypeResources.add(jenaModel.createResource(uriBuilder.buildTypeUri(subTypeInfo)));
				 }
				 jenaModel.add(typeResource, OWL2.disjointUnionOf, jenaModel.createList(allSubtypeResources.iterator()));
			 }
		 }
	
		 //
		 // write entity info
		 //
		 BemEntityTypeInfo superTypeInfo = typeInfo.getSuperTypeInfo();
		 if (superTypeInfo != null) {
			 jenaModel.add(typeResource, RDFS.subClassOf,
			 jenaModel.createResource(uriBuilder.buildTypeUri(superTypeInfo)));
			
			 if (!superTypeInfo.isAbstractSuperType() || !supportsDisjointUnionOf) {
			
			 List<BemEntityTypeInfo> allSubtypeInfos = superTypeInfo.getSubTypeInfos();
	
				if (allSubtypeInfos.size() > 1) {
	
					int indexOfCurrentType = allSubtypeInfos.indexOf(typeInfo);
					
					final boolean supportDisjointWithList = targetOwlProfileList.supportsStatement(OWL.disjointWith, RdfVocabulary.DUMP_URI_LIST); 
	
					if (allSubtypeInfos.size() > 2 && supportDisjointWithList) {
						//
						// OWL2 allow object of property "owl:disjointWith" to
						// be rdf:list
						// All classes will be pairwise disjoint
						// See:
						// http://www.w3.org/2007/OWL/wiki/New_Features_and_Rationale#F2:_DisjointClasses
						//
						disjointClasses = allSubtypeInfos;
	
					} else {
						final boolean supportDisjointWithSingleClass = targetOwlProfileList.supportsStatement(OWL.disjointWith, RdfVocabulary.DUMP_URI_1); 
						// context.getOwlVersion() < OwlProfile.OWL_VERSION_2_0
						
						if (supportDisjointWithSingleClass) {
							
							//
							// OWL1 doesn't allow object of property
							// "owl:disjointWith" to be rdf:list
							// See: http://www.w3.org/TR/owl-ref/#disjointWith-def
							//
							if (indexOfCurrentType + 1 < allSubtypeInfos.size()) {
		
								for (int i = indexOfCurrentType + 1; i < allSubtypeInfos.size(); ++i) {
									jenaModel.add(typeResource, OWL.disjointWith, jenaModel.createResource(uriBuilder.buildTypeUri(allSubtypeInfos.get(i))));
								}
		
							}							
							
						}
						
					}
	
				}
	
			}
	
		} else { // superTypeInfo == null
			jenaModel.add(typeResource, RDFS.subClassOf, jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Entity)));
		}

		//
		// write unique keys
		//
		List<BemUniqueKeyInfo> uniqueKeyInfos = typeInfo.getUniqueKeyInfos();
		
		final boolean supportHasKey = targetOwlProfileList.supportsStatement(OWL2.hasKey, RdfVocabulary.DUMP_URI_LIST); 
		
		if (uniqueKeyInfos != null && supportHasKey) {
			for (BemUniqueKeyInfo uniqueKeyInfo : uniqueKeyInfos) {
				List<Resource> attributeResources = new ArrayList<>();
				for (BemAttributeInfo attributeInfo : uniqueKeyInfo.getAttributeInfos()) {
					attributeResources.add(jenaModel.createResource(uriBuilder.buildAttributeUri(attributeInfo, contextParams.useLongAttributeName())));
				}
				jenaModel.add(typeResource, OWL2.hasKey, jenaModel.createList(attributeResources.iterator()));
			}
		}
	
	
		//
		// add attribute info to attribute info map
		//
		//if (context.isEnabledOption(Ifc2RdfConversionParamsEnum.ExportProperties))
		{
			List<BemAttributeInfo> attributeInfos = typeInfo.getAttributeInfos(false);
			if (attributeInfos != null) {
				for (BemAttributeInfo attributeInfo : attributeInfos) {
					Property property = jenaModel.createProperty(uriBuilder.buildAttributeUri(attributeInfo, contextParams.useLongAttributeName()));
					
					jenaModel.add(
							property,
							RDF.type,
							jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EntityProperty)));
					
					BemTypeInfo attributeTypeInfo = attributeInfo.getValueTypeInfo();
					
					if (attributeTypeInfo instanceof BemCollectionTypeInfo) {
						BemCollectionTypeInfo collectionAttributeTypeInfo = (BemCollectionTypeInfo)attributeTypeInfo;
						boolean exportAsSingleCollection = collectionAttributeTypeInfo.isSorted() ||
								context.getConversionParams().convertSetAttributesAsMultipleProperties();
						
						if (exportAsSingleCollection) {
							Resource collectionTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(collectionAttributeTypeInfo));
							int min = attributeInfo.isOptional() ? 0 : 1;
							int max = 1;
							convertPropertyRestrictions(property, typeResource, collectionTypeResource, true, min, max, jenaModel);
							
							convertCollectionTypeInfo(collectionAttributeTypeInfo, jenaModel);							
							
						} else {
							Resource itemTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(collectionAttributeTypeInfo.getItemTypeInfo()));
							int min = attributeInfo.isOptional() ? 0 : collectionAttributeTypeInfo.getCardinality().getMinCardinality();
							int max = collectionAttributeTypeInfo.getCardinality().getMaxCardinality();
							convertPropertyRestrictions(property, typeResource, itemTypeResource, true, min, max, jenaModel);
						}
						
					} else {
						
						Resource itemTypeResource = jenaModel.createResource(uriBuilder.buildTypeUri(attributeTypeInfo));
						int min = attributeInfo.isOptional() ? 0 : 1;
						int max = 1;
						convertPropertyRestrictions(property, typeResource, itemTypeResource, true, min, max, jenaModel);

					}
					
				}
			}
		}
	
		 
		if (disjointClasses != null) {
			 //
			 // OWL2 allow object of property "owl:disjointWith" to be rdf:list
			 // All classes will be pairwise disjoint
			 // See: http://www.w3.org/2007/OWL/wiki/New_Features_and_Rationale#F2:_DisjointClasses
			 //
		
			 //adapter.exportEmptyLine();
			
			 Resource blankNode = jenaModel.createResource();
			 jenaModel.add(blankNode, RDF.type, OWL2.AllDisjointClasses);
			
			 List<Resource> disjointClassResources = new ArrayList<>();
			 for (BemTypeInfo disjointClassTypeInfo : disjointClasses) {
				 disjointClassResources.add(jenaModel.createResource(uriBuilder.buildTypeUri(disjointClassTypeInfo)));
			 }
			
			 jenaModel.add(blankNode, OWL2.members, jenaModel.createList(disjointClassResources.iterator()));
		 }
		
		
		return typeResource;
		
	 }
	
	
	//*****************************************
	// EndRegion ENTITY TYPES & VALUES
	//*****************************************
	
			

	
	//*****************************************
	// Region PROPERTIES
	//*****************************************
	
	private void convertPropertyRestrictions(
			Property property,
			Resource domain,
			Resource range,
			boolean isObjectProperty,
			Integer min,
			Integer max,
			Model jenaModel) {
		
		property.addProperty(RDF.type, isObjectProperty ? OWL.ObjectProperty : OWL.DatatypeProperty);
		
		// TODO: double check if domains and ranges are really needed
		property.addProperty(RDFS.domain, domain);
		property.addProperty(RDFS.range, range);		

		if (max != null && max == 1 && targetOwlProfileList.supportsStatement(RDF.type, OWL.FunctionalProperty)) {			
			// TODO: detect when FunctionalDataProperty is supported
			property.addProperty(RDF.type, isObjectProperty ? OWL.FunctionalProperty : RdfVocabulary.OWL.FunctionalDataProperty);
		}

		
//		jenaModel.add(attributeResource, RDF.type, Ifc2RdfVocabulary.EXPRESS.EntityProperty);						

		if (targetOwlProfileList.supportsStatement(RDF.type, OWL.Restriction)) {

			//
			// write constraint about property type
			//
			if (targetOwlProfileList.supportsStatement(OWL.allValuesFrom, null)) {
				exportPropertyRestriction(domain, property, OWL.allValuesFrom, range, jenaModel);
			}
			
			RDFNode minNode = min != null ? jenaModel.createTypedLiteral(min) : null;
			RDFNode maxNode = max != null && max != Integer.MAX_VALUE ? jenaModel.createTypedLiteral(max) : null;
			
			if (minNode != null) {
				if (minNode.equals(maxNode)) {
					if (targetOwlProfileList.supportsStatement(OWL.cardinality, minNode)) {
						exportPropertyRestriction(domain, property, OWL.cardinality, minNode, jenaModel);
						minNode = null;
						maxNode = null;
					}
				} else {
					if (targetOwlProfileList.supportsStatement(OWL.minCardinality, minNode)) {
						exportPropertyRestriction(domain, property, OWL.minCardinality, minNode, jenaModel);
						minNode = null;
					}					
				}
			}
			
			if (maxNode != null) {
				if (targetOwlProfileList.supportsStatement(OWL.maxCardinality, maxNode)) {
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
