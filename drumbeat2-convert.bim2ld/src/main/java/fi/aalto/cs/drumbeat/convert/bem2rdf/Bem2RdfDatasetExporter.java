//package fi.aalto.cs.drumbeat.convert.bem2rdf;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.lang3.NotImplementedException;
//import org.apache.log4j.Logger;
//
//import org.apache.jena.rdf.model.AnonId;
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.Property;
//import org.apache.jena.rdf.model.RDFNode;
//import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.vocabulary.OWL;
//import org.apache.jena.vocabulary.RDF;
//import org.apache.jena.vocabulary.RDFS;
//import org.apache.jena.vocabulary.XSD;
//
//import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
//import fi.aalto.cs.drumbeat.data.bem.BemException;
//import fi.aalto.cs.drumbeat.data.bem.dataset.*;
//import fi.aalto.cs.drumbeat.data.bem.schema.*;
//import fi.aalto.cs.drumbeat.rdf.OwlProfileList;
//import fi.aalto.cs.drumbeat.rdf.RdfVocabulary;
//
//
//public class Bem2RdfDatasetExporter {
//	
//	private static final Logger logger = Logger.getLogger(Bem2RdfDatasetExporter.class); 
//	
//	private final BemSchema bemSchema;
//	private final BemDataset bemModel;
//	private final Bem2RdfConversionContext context;
//	private final Model jenaModel;
//	private final Bem2RdfConverter converter;
//	private final String modelNamespacePrefix;
//	private final String modelNamespaceUri;
//	private final boolean nameAllBlankNodes;
//	private final String blankNodeNamespaceUri;
//	
//	private final OwlProfileList owlProfileList;
//	
//	public Bem2RdfDatasetExporter(BemDataset bemModel, Bem2RdfConversionContext context, Model jenaModel) {
//		
//		this.bemSchema = bemModel.getSchema();
//		this.bemModel = bemModel;		
//		this.context = context;
//		this.nameAllBlankNodes = context.getConversionParams().nameAllBlankNodes();
//		
//		
//		this.owlProfileList = context.getOwlProfileList();
//		this.jenaModel = jenaModel;
//		
//		converter = new Bem2RdfConverter(context, bemSchema);
////		if (context.getOntologyNamespaceUriFormat() != null) {
////			converter.setBemOntologyNamespaceUri(String.format(
////					context.getOntologyNamespaceUriFormat(),
////					bemSchema.getName(), context.getName()));
////		}
//		
//		if (context.getModelNamespacePrefix() != null) {
//			modelNamespacePrefix = context.getModelNamespacePrefix();
//		} else {
//			throw new IllegalArgumentException("Model's namespace prefix is undefined");
//		}
//		
//		if (context.getModelNamespaceUriFormat() != null) {			
//			modelNamespaceUri = context.generateModelNamespaceUri(bemSchema.getName());
//		} else {
//			throw new IllegalArgumentException("Model's namespace URI format is undefined");
//		}		
//		
//		
//		if (nameAllBlankNodes) {
//			if (context.getModelBlankNodeNamespaceUriFormat() != null) {
//				// TODO: Replace with variables such as $schema.version$
//				blankNodeNamespaceUri = context.generateModelBlankNodeNamespaceUri(bemSchema.getName());
//			} else {
//				throw new IllegalArgumentException("Namespace URI format of model blank nodes is undefined");
//			}
//		} else {
//			blankNodeNamespaceUri = null;
//		}
//		
//		
//		
////		String ontologyNamespaceUri = String.format(context.getOntologyNamespaceFormat(), bemSchema.getName(), context.getName());
//		
//		// TODO: check URIs
////		converter.setOntologyNamespaceUri(ontologyNamespaceUri);		
////		
////		converter.setModelNamespacePrefix(modelNamespacePrefix);
////		converter.setModelNamespaceUri(modelNamespaceUri);		
//	}
//	
//	public Model export() throws BemException {
//		
//		//
//		// write header and prefixes
//		//
//		exportOntologyHeader();
//		
//		jenaModel.setNsPrefix(modelNamespacePrefix, modelNamespaceUri);
//		//adapter.exportEmptyLine();
//
//		String conversionParamsString = context.getConversionParams().toString()
//				.replaceFirst("\\[", "[\r\n\t\t\t ")
//				.replaceFirst("\\]", "\r\n\t\t]")
//				.replaceAll(",", "\r\n\t\t\t");
//		
//		conversionParamsString = String.format("OWL profile: %s.\r\n\t\tConversion options: %s",
//				owlProfileList.getOwlProfileIds(),
//				conversionParamsString); 
//		
//		Resource ontology = jenaModel.createResource(converter.getBemOntologyNamespaceUri());
//		ontology.addProperty(RDF.type, OWL.Ontology);
//		String version = "1.0";
//		ontology.addProperty(OWL.versionInfo, String.format("v%1$s %2$tY/%2$tm/%2$te %2$tH:%2$tM:%2$tS", version, new Date()));
//		if (conversionParamsString != null) {
//			//ontology.addProperty(RDFS.comment, String.format("\"\"\"%s\"\"\"", comment));
//			ontology.addProperty(RDFS.comment, conversionParamsString);
//		}
//		
//		for (BemEntity entity : bemModel.getAllEntities()) {
//			writeEntity(entity);
//		}
//		
//		//adapter.endExport();
//		
//		return jenaModel;
//	}
//	
//	
//	private void exportOntologyHeader() {
//		
//		// define owl:
//		jenaModel.setNsPrefix(RdfVocabulary.OWL.BASE_PREFIX, OWL.getURI());
//
//		// define rdf:
//		jenaModel.setNsPrefix(RdfVocabulary.RDF.BASE_PREFIX, RDF.getURI());
//
//		// define rdfs:
//		jenaModel.setNsPrefix(RdfVocabulary.RDFS.BASE_PREFIX, RDFS.getURI());
//
//		// define xsd:
//		jenaModel.setNsPrefix(RdfVocabulary.XSD.BASE_PREFIX, XSD.getURI());
//
//		// define expr:
//		jenaModel.setNsPrefix(Bem2RdfVocabulary.EXPRESS.BASE_PREFIX,
//				Bem2RdfVocabulary.EXPRESS.getBaseUri());
//
//		if (!context.getConversionParams().ignoreBemSchema()) {
//			// define bem:
//			jenaModel.setNsPrefix(Bem2RdfVocabulary.IFC.BASE_PREFIX,
//					converter.getBemOntologyNamespaceUri());
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
//		// adapter.exportOntologyHeader(converter.getBemOntologyNamespaceUri(), "1.0",
//		// conversionParamsString);
//
//		Resource ontology = jenaModel.createResource(converter.getBemOntologyNamespaceUri());
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
//		
//	}
//	
//	
//	private void writeEntity(BemEntity entity) {
//		if (entity.isDuplicated()) {
//			return;
//		}
//		
//		Resource entityResource = convertEntityToResource(entity);
//		
//		long childNodeCount = 1L;
//		
//		BemEntityTypeInfo entityTypeInfo = entity.getTypeInfo();		
//		entityResource.addProperty(RDF.type, jenaModel.createResource(converter.formatTypeName(entityTypeInfo)));
//		
//		for (BemLink link : entity.getOutgoingLinks()) {
//			writeAttribute(entityResource, link, childNodeCount++);
//		}
//		
//		for (BemPrimitiveAttribute attribute : entity.getPrimitiveAttributes()) {
//			writeAttribute(entityResource, attribute, childNodeCount++);
//		}
//		
//		if ((Boolean)context.getConversionParams().getParam(Bem2RdfConversionContextParams.PARAM_EXPORT_DEBUG_INFO).getValue()) {
//		
////			if (entity.isPrimitiveValueContainer()) {
////				jenaModel.add(entityResource, RDF.type, jenaModel.createResource(
////						converter.formatOntologyName(Bem2RdfVocabulary.IFC.LITERAL_VALUE_CONTAINER_ENTITY)));
////			}
//			
////			if (entity.isSharedBlankNode()) {
////				jenaModel.add(
////						entityResource,
////						RDF.type,
////						jenaModel.createResource(converter.formatOntologyName(Bem2RdfVocabulary.IFC.SUPER_ENTITY)));				
////			}		
////			
////			
////			if (entity.hasName()) {
////				String entityRawName = entity.getRawName();
////				if (!entityRawName.equals(entity.getName())) {
////					jenaModel.add(
////							entityResource,
////							jenaModel.createResource(converter.formatOntologyName(Bem2RdfVocabulary.IFC.RAW_NAME)).as(Property.class),
////									jenaModel.createTypedPrimitive(entityRawName));
////				}
////			}
//			
//			String debugMessage = entity.getDebugMessage();
//			if (debugMessage != null) {
//				jenaModel.add(
//				entityResource,
//				jenaModel.createResource(converter.formatBemOntologyName(Bem2RdfVocabulary.IFC.PROPERTY_DEBUG_MESSAGE)).as(Property.class),
//						jenaModel.createTypedPrimitive(debugMessage));				
//			}
//			
//			jenaModel.add(
//					entityResource,
//					jenaModel.createResource(converter.formatBemOntologyName(Bem2RdfVocabulary.IFC.PROPERTY_LINE_NUMBER_PROPERTY)).as(Property.class),
//					jenaModel.createTypedPrimitive(entity.getLocalId()));
//		}		
//
//		//adapter.exportEmptyLine();
//	}
//	
//	private void writeAttribute(Resource entityResource, BemAttribute attribute, long childNodeCount) {		
//		BemAttributeInfo attributeInfo = attribute.getAttributeInfo();
//		Property attributeProperty = convertAttributeInfoToResource(attributeInfo);
//		Property inverseAttributeProperty = null;
//		if (attribute instanceof BemLink) {
//			BemInverseAttributeInfo inverseAttributeInfo = ((BemLink)attribute).getInverseAttributeInfo();
//			if (inverseAttributeInfo != null) {				
//				inverseAttributeProperty = convertAttributeInfoToResource(inverseAttributeInfo);
//			}
//		}
//		
//		BemValue value = attribute.getValue();
//		logger.debug("Writing attribute: " + attributeInfo + " value: " + value);
//		List<RDFNode> valueNodes = convertValueToNode(value, attributeInfo.getAttributeTypeInfo(), entityResource, childNodeCount);
//		
//		for (RDFNode valueNode : valueNodes) {		
//			jenaModel.add(entityResource, attributeProperty, valueNode);
//			if (inverseAttributeProperty != null) {
//				jenaModel.add((Resource)valueNode, inverseAttributeProperty, entityResource);
//			}
//		}
//	}
//	
//	/**
//	 * Converts any {@link BemValue} value to {@link RDFNode}
//	 * @param value
//	 * @param typeInfo
//	 * @return
//	 */
//	public RDFNode convertSingleValueToNode(BemSingleValue value, BemTypeInfo typeInfo, Resource entityResource, long childNodeCount) {
//		if (typeInfo instanceof BemCollectionTypeInfo) {
//			throw new IllegalArgumentException("Expected non-collection type info: " + typeInfo);
//		} else {
//			if (value instanceof BemEntity) {
//				return convertEntityToResource((BemEntity) value);
//			} if (value instanceof BemShortEntity) {
//				return convertShortEntityToResource((BemShortEntity) value, childNodeCount);				
//			} else {
//				assert(value instanceof BemPrimitiveValue);
//				return convertPrimitiveValue((BemPrimitiveValue) value, entityResource, childNodeCount, jenaModel);
//			}
//		}
//	}
//	
//	public List<RDFNode> convertValueToNode(BemValue value, BemTypeInfo typeInfo, Resource entityResource, long childNodeCount) {
//		if (value instanceof BemSingleValue) {
//			List<RDFNode> nodes = new ArrayList<>();
//			nodes.add(convertSingleValueToNode((BemSingleValue)value, typeInfo, entityResource, childNodeCount));
//			return nodes;
//		} else {
//			assert(typeInfo instanceof BemCollectionTypeInfo);
//			return convertListToResource((BemCollectionValue<?>) value, (BemCollectionTypeInfo)typeInfo, entityResource, childNodeCount);		
//		}
//	}
//	
//
//	/**
//	 * Converts an IFC literal value to an RDF resource 
//	 * @param literalValue
//	 * @return
//	 */
//	public RDFNode convertPrimitiveValue(BemPrimitiveValue literalValue, Model jenaModel) {
//		return convertPrimitiveValue(literalValue, null, 0L, jenaModel);
//	}
//	
//	
//
//	/**
//	 * Converts an IFC literal value to an RDF resource 
//	 * @param literalValue
//	 * @return
//	 */
//	public RDFNode convertPrimitiveValue(BemPrimitiveValue literalValue, Resource parentResource, long childNodeCount, Model jenaModel) {
//		
//		BemTypeInfo typeInfo = literalValue.getType();
//		
//		assert(typeInfo != null) : literalValue;
//
//		if (typeInfo instanceof BemDefinedTypeInfo || typeInfo instanceof BemPrimitiveTypeInfo) {
//			
//			return convertValueOfPrimitiveOrDefinedType(typeInfo, literalValue.getValue(), parentResource, childNodeCount, jenaModel);
//			
//			
//		} else if (typeInfo instanceof BemEnumerationTypeInfo) {
//			
//			return converter.convertEnumerationValue((BemEnumerationTypeInfo)typeInfo, (String)literalValue.getValue(), jenaModel);			
//			
//		} else if (typeInfo instanceof BemLogicalTypeInfo) {
//			
//			assert(literalValue.getValue() instanceof BemLogicalEnum);
//			return converter.convertBooleanValue((BemLogicalTypeInfo)typeInfo, (BemLogicalEnum)literalValue.getValue(), jenaModel);
//		
//		} else {
//			
//			throw new RuntimeException(String.format("Invalid literal value type: %s (%s)", typeInfo, typeInfo.getClass()));			
//		}
//		
//	}
//	
//	
//	private Resource convertValueOfPrimitiveOrDefinedType(BemTypeInfo typeInfo, Object value, Resource parentResource, long childNodeCount, Model jenaModel) {
//		
//		assert(typeInfo instanceof BemPrimitiveTypeInfo || typeInfo instanceof BemDefinedTypeInfo);
//		
//		RDFNode valueNode;
//		BemValueKindEnum valueType = typeInfo.getValueTypes().iterator().next();
//		
//		if (valueType == BemValueKindEnum.STRING) {
//			valueNode = jenaModel.createTypedPrimitive((String)value);				
//		} else if (valueType == BemValueKindEnum.REAL || valueType == BemValueKindEnum.NUMBER) {				
//			valueNode = jenaModel.createTypedPrimitive((double)value, converter.getBaseTypeForDoubles().getURI());				
//		} else if (valueType == BemValueKindEnum.INTEGER) {				
//			valueNode = jenaModel.createTypedPrimitive((long)value);				
//		} else if (valueType == BemValueKindEnum.LOGICAL) {
////			assert(typeInfo instanceof BemLogicalTypeInfo) : typeInfo;
//			assert(value instanceof BemLogicalEnum) : value;
//			valueNode = converter.convertBooleanValue(typeInfo, (BemLogicalEnum)value, jenaModel);
//		} else {
//			assert (valueType == BemValueKindEnum.DATETIME) : "Expected: valueType == BemValueKindEnum.DATETIME. Actual: valueType = " + valueType + ", " + typeInfo;
//			valueNode = jenaModel.createTypedPrimitive((Calendar)value);				
//		}
//		
//		Property hasXXXProperty = converter.getHasXXXProperty(typeInfo.getValueTypes().iterator().next(), jenaModel);
//
//		Resource resource;
//		if (nameAllBlankNodes) {
//			//String rawNodeName = String.format("%s_%s", hasXXXProperty.getLocalName(), value);
////			String encodedNodeName = EncoderTypeEnum.encode(EncoderTypeEnum.SafeUrl, rawNodeName);			
//
//			String rawNodeName = formatModelBlankNodeName(String.format("%s_%s", parentResource.getLocalName(), childNodeCount));
//			resource = jenaModel.createResource(rawNodeName);
//		} else {
//			resource = jenaModel.createResource();
//		}
//
//		resource.addProperty(RDF.type, jenaModel.createResource(converter.buildTypeUri(typeInfo)));
//		
//		resource.addProperty(hasXXXProperty, valueNode);
//		
//		return resource;
//		
//	}
//		
//	
//		
//	
//	
//	public List<RDFNode> convertListToResource(BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
//			Resource parentResource, long childNodeCount)
//	{		
//		final String convertCollectionsTo = context.getConversionParams().convertCollectionsTo();		
//		
//		switch (convertCollectionsTo) {
//		case Bem2RdfConversionContextParams.VALUE_DRUMMOND_LIST:			
//			return convertListToDrummondList(listValue, collectionTypeInfo, parentResource, childNodeCount);
//			
//		case Bem2RdfConversionContextParams.VALUE_OLO_SIMILAR_LIST:
//			return convertListToOloSimilarList(listValue, collectionTypeInfo, parentResource, childNodeCount);
//
//		default:
//			 throw new NotImplementedException("Unknown collection type");
//		}
//	}
//		
//	private List<RDFNode> convertListToDrummondList(BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
//			Resource parentResource, long childNodeCount)
//	{
//		if (collectionTypeInfo.isSorted()) {
//			
//			Resource listTypeResource = jenaModel.createResource(converter.formatTypeName(collectionTypeInfo)); 
//			Resource emptyListTypeResource = jenaModel.createResource(converter.formatTypeName(collectionTypeInfo).replace("List", "EmptyList"));
//			BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();			
//			
//			List<? extends BemSingleValue> values = listValue.getSingleValues(); 
//
//			int index = values.size();
//			
//			Resource currentListResource;
//			assert(parentResource != null);
//			if (nameAllBlankNodes) {
//				String currentResourceName = formatModelBlankNodeName(String.format("%s_%d_%d", parentResource.getLocalName(), childNodeCount, index));
//				currentListResource = jenaModel.createResource(currentResourceName);			
//			} else {
//				currentListResource = jenaModel.createResource();
//			}
//			
//			currentListResource.addProperty(RDF.type, emptyListTypeResource);
//			
//			while (index > 0) {
//				index--;
//				Resource nextListResource = currentListResource;
//				if (nameAllBlankNodes) {
//					String currentResourceName = formatModelBlankNodeName(String.format("%s_%d_%d", parentResource.getLocalName(), childNodeCount, index));
//					currentListResource = jenaModel.createResource(currentResourceName);			
//				} else {
//					currentListResource = jenaModel.createResource();
//				}
//
//				currentListResource.addProperty(RDF.type, listTypeResource);
//				currentListResource.addProperty(Bem2RdfVocabulary.EXPRESS.hasNext, nextListResource);
//				
//				BemSingleValue value = values.get(index);
//				RDFNode valueNode = convertSingleValueToNode(value, itemTypeInfo, currentListResource, 0);
//				
//				currentListResource.addProperty(Bem2RdfVocabulary.EXPRESS.hasValue, valueNode);
//			}
//
//			List<RDFNode> nodes = new ArrayList<>();
//			nodes.add(currentListResource);			
//			return nodes;
//			
//		} else {
//			List<RDFNode> nodes = new ArrayList<>();
//			
//			for (BemSingleValue value : listValue.getSingleValues()) {
//				RDFNode node = convertSingleValueToNode(value, collectionTypeInfo.getItemTypeInfo(), parentResource, childNodeCount);
//				nodes.add(node);
//			}
//			
//			return nodes;
//		}
//
//	}
//
//	private List<RDFNode> convertListToOloSimilarList(BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
//			Resource parentResource, long childNodeCount)
//	{
//		Resource listResource;
//		if (nameAllBlankNodes) {
//			assert(parentResource != null);
//			String listResourceName = formatModelBlankNodeName(String.format("%s_%d", parentResource.getLocalName(), childNodeCount));
//			listResource = jenaModel.createResource(listResourceName);			
//		} else {
//			listResource = jenaModel.createResource();
//		}
//		
//		BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();
//		
//		List<RDFNode> nodeList = new ArrayList<>();
//		long count = 1;
//		for (BemSingleValue value : listValue.getSingleValues()) {
//			nodeList.add(convertSingleValueToNode(value, itemTypeInfo, listResource, count++));
//		}
//		
//		int length = nodeList.size();
//		
//		
//		Resource typeResource = jenaModel.createResource(converter.formatTypeName(collectionTypeInfo)); 
//		listResource.addProperty(RDF.type, typeResource);
//		listResource.addProperty(Bem2RdfVocabulary.EXPRESS.size, jenaModel.createTypedPrimitive(length));
//		listResource.addProperty(Bem2RdfVocabulary.EXPRESS.isOrdered,
//				collectionTypeInfo.isSorted() ? Bem2RdfVocabulary.EXPRESS.TRUE : Bem2RdfVocabulary.EXPRESS.FALSE);
////		listResource.addProperty(Bem2RdfVocabulary.EXPRESS.itemType, jenaModel.createResource(converter.formatTypeName(itemTypeInfo)));
//		
//		for (int i = 0; i < nodeList.size(); ++i) {
//			Resource slotResource;
//			if (nameAllBlankNodes) {
//				String slotResourceName = formatModelBlankNodeName(String.format("%s_slot_%d", listResource.getLocalName(), i+1));
//				slotResource = jenaModel.createResource(formatModelBlankNodeName(slotResourceName));
//			} else {
//				slotResource = jenaModel.createResource();
//			}
//			slotResource.addProperty(Bem2RdfVocabulary.EXPRESS.index, jenaModel.createTypedPrimitive(i + 1));
//			slotResource.addProperty(Bem2RdfVocabulary.EXPRESS.item, nodeList.get(i));
//			listResource.addProperty(Bem2RdfVocabulary.EXPRESS.slot, slotResource);
//		}
//		
//		List<RDFNode> nodes = new ArrayList<RDFNode>();
//		nodes.add(listResource);
//		return nodes;
//	}
//	
//	public Resource convertEntityToResource(BemEntity entity) {
//		if (entity.hasName()) {
//			return jenaModel.createResource(formatModelName(entity.getName()));
//		} else {
//			String nodeName = String.format(Bem2RdfVocabulary.IFC.BLANK_NODE_ENTITY_URI_FORMAT, entity.getLocalId());
//			if (nameAllBlankNodes) {
//				return jenaModel.createResource(formatModelBlankNodeName(nodeName));				
//			} else {
//				return jenaModel.createResource(new AnonId(nodeName));				
//			}
//		}
//	}
//	
//	public Resource convertShortEntityToResource(BemShortEntity entity, long childNodeCount) {
//		String nodeName = String.format("%s_%s", entity.getTypeInfo(), entity.getValue());
//		Resource entityResource;
//		if (nameAllBlankNodes) {
//			entityResource = jenaModel.createResource(formatModelBlankNodeName(nodeName));				
//		} else {
//			entityResource = jenaModel.createResource(new AnonId(nodeName));				
//		}
//		
//		entityResource.addProperty(RDF.type, jenaModel.createResource(converter.buildTypeUri(entity.getTypeInfo())));
//		BemPrimitiveValue value = entity.getValue();
//		RDFNode valueNode = convertPrimitiveValue((BemPrimitiveValue) value, entityResource, childNodeCount, jenaModel);
//		entityResource.addProperty(EXPRESS.hasValue, valueNode);
//		return entityResource;
//	}
//
//	public Property convertAttributeInfoToResource(BemAttributeInfo attributeInfo) {
//		return jenaModel.createResource(converter.formatAttributeName(attributeInfo)).as(Property.class);		
//	}
//	
//	/**
//	 * Creates a URI in the IFC data namespace 
//	 * @param name
//	 * @return
//	 */
//	public String formatModelName(String name) {
//		return modelNamespaceUri + name;
//	}
//	
//	
//	private String formatModelBlankNodeName(String name) {
//		return blankNodeNamespaceUri + name;
//	}
//	
//	
//}
