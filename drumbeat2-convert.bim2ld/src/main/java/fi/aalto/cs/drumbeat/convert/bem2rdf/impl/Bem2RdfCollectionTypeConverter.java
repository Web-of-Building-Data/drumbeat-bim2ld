package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.*;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConverterConfigurationException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemCollectionValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class Bem2RdfCollectionTypeConverter {
	
	private final Bem2RdfConverterManager manager;
	private final String convertCollectionsTo;

	public Bem2RdfCollectionTypeConverter(Bem2RdfConverterManager manager) throws Bem2RdfConverterConfigurationException {
		this.manager = manager;
		this.convertCollectionsTo = manager.contextParams.convertCollectionsTo();
	}
	
	//*****************************************
	// Region COLLECTION TYPES AND VALUES
	//*****************************************
	
	public Resource convertCollectionTypeInfo(Model jenaModel, BemCollectionTypeInfo typeInfo, boolean includeDetails) {

		Resource listTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo));
		
		if (jenaModel.contains(listTypeResource, RDF.type, (RDFNode)null)) {
			return listTypeResource;
		}
		
		switch (convertCollectionsTo) {
		case Bem2RdfConversionContextParams.VALUE_DRUMMOND_LIST:
			return convertCollectionTypeInfoToDrummondList(jenaModel, typeInfo);
		case Bem2RdfConversionContextParams.VALUE_OLO_SIMILAR_LIST:
			throw new NotImplementedException("Converting collections to " + Bem2RdfConversionContextParams.VALUE_OLO_SIMILAR_LIST);
		default:
			throw new NotImplementedException("Unknown collection type");
		}			

	}
	 
	private Resource convertCollectionTypeInfoToDrummondList(Model jenaModel, BemCollectionTypeInfo typeInfo) {
		if (typeInfo.isSorted()) {
				
			Resource listTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo));
			jenaModel.add(listTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(listTypeResource, RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
			
			Resource itemTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo.getItemTypeInfo()));
			manager.convertPropertyRestrictions(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
					listTypeResource, itemTypeResource, true, 0, 1, jenaModel);
			
			String additionalCollectionTypeName = String.format("%s_%s", typeInfo.getItemTypeInfo(), typeInfo.getCollectionKind());
			Resource additionalListTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(additionalCollectionTypeName));
			 
			manager.convertPropertyRestrictions(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
					listTypeResource,
					additionalListTypeResource, true, 1, 1, jenaModel);
			 
			Resource emptyListTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo).replace("List", "EmptyList"));
			jenaModel.add(emptyListTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(emptyListTypeResource, RDFS.subClassOf, listTypeResource);
			jenaModel.add(
					listTypeResource,
					RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EmptyList)));
			 
			return listTypeResource;			 
			 
		} else {
			
			Resource setTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo));
			jenaModel.add(setTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(
					setTypeResource,
					RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Set)));
			
			Resource itemTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo.getItemTypeInfo()));
			int min = typeInfo.getCardinality().getMinCardinality();
			int max = typeInfo.getCardinality().getMaxCardinality();
			manager.convertPropertyRestrictions(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasSetItem)),
					setTypeResource, itemTypeResource, true, min, max, jenaModel);
			
			return setTypeResource;
			 
		}		 
	}
	
	public List<RDFNode> convertListToResource(Model jenaModel, BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
			Resource parentResource, long childNodeCount)
	{		
		switch (convertCollectionsTo) {
		case Bem2RdfConversionContextParams.VALUE_DRUMMOND_LIST:			
			return convertListToDrummondList(jenaModel, listValue, collectionTypeInfo, parentResource, childNodeCount);
			
		case Bem2RdfConversionContextParams.VALUE_OLO_SIMILAR_LIST:
			return convertListToOloSimilarList(jenaModel, listValue, collectionTypeInfo, parentResource, childNodeCount);

		default:
			 throw new NotImplementedException("Unknown collection type");
		}
	}
		
	private List<RDFNode> convertListToDrummondList(Model jenaModel, BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
			Resource parentResource, long childNodeCount)
	{
		if (collectionTypeInfo.isSorted()) {
			
			Resource listTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo)); 
			Resource emptyListTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo).replace("List", "EmptyList"));
			BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();			
			
			List<? extends BemValue> values = listValue.getSingleValues(); 

			int index = values.size();
			
			Resource currentListResource;
			assert(parentResource != null);
			if (manager.nameAllBlankNodes) {
				String currentResourceName = manager.uriBuilder.buildDatasetBlankNodeUri(String.format("%s_%d_%d", parentResource.getLocalName(), childNodeCount, index));
				currentListResource = jenaModel.createResource(currentResourceName);			
			} else {
				currentListResource = jenaModel.createResource();
			}
			
			currentListResource.addProperty(RDF.type, emptyListTypeResource);
			
			while (index > 0) {
				index--;
				Resource nextListResource = currentListResource;
				if (manager.nameAllBlankNodes) {
					String currentResourceName = manager.uriBuilder.buildDatasetBlankNodeUri(String.format("%s_%d_%d", parentResource.getLocalName(), childNodeCount, index));
					currentListResource = jenaModel.createResource(currentResourceName);			
				} else {
					currentListResource = jenaModel.createResource();
				}

				currentListResource.addProperty(RDF.type, listTypeResource);
				currentListResource.addProperty(
						jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
						nextListResource);
				
				BemValue value = values.get(index);
				RDFNode valueNode = manager.convertValue(jenaModel, value, itemTypeInfo, currentListResource, 0, false);
				
				currentListResource.addProperty(
						jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)),
						valueNode);
			}

			List<RDFNode> nodes = new ArrayList<>();
			nodes.add(currentListResource);			
			return nodes;
			
		} else {
			List<RDFNode> nodes = new ArrayList<>();
			
			for (BemValue value : listValue.getSingleValues()) {
				RDFNode node = manager.convertValue(jenaModel, value, collectionTypeInfo.getItemTypeInfo(), parentResource, childNodeCount, false);
				nodes.add(node);
			}
			
			return nodes;
		}

	}

	private List<RDFNode> convertListToOloSimilarList(Model jenaModel, BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
			Resource parentResource, long childNodeCount)
	{
		Resource listResource;
		if (manager.nameAllBlankNodes) {
			assert(parentResource != null);
			String listResourceName = manager.uriBuilder.buildDatasetBlankNodeUri(String.format("%s_%d", parentResource.getLocalName(), childNodeCount));
			listResource = jenaModel.createResource(listResourceName);			
		} else {
			listResource = jenaModel.createResource();
		}
		
		BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();
		
		List<RDFNode> nodeList = new ArrayList<>();
		long count = 1;
		for (BemValue value : listValue.getSingleValues()) {
			nodeList.add(manager.convertValue(jenaModel, value, itemTypeInfo, listResource, count++, false));
		}
		
		int length = nodeList.size();
		
		
		Resource typeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo)); 
		listResource.addProperty(RDF.type, typeResource);
		listResource.addLiteral(
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.size)),
				length);
		listResource.addProperty(
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.isOrdered)),
				manager.uriBuilder.buildBuiltInOntologyUri(
						collectionTypeInfo.isSorted() ? Bem2RdfVocabulary.BuiltInOntology.TRUE : Bem2RdfVocabulary.BuiltInOntology.FALSE));
//		listResource.addProperty(Bem2RdfVocabulary.BuiltInOntology.itemType, jenaModel.createResource(manager.uriBuilder.buildOntologyUri(itemTypeInfo)));
		
		for (int i = 0; i < nodeList.size(); ++i) {
			Resource slotResource;
			if (manager.nameAllBlankNodes) {
				String slotResourceName = manager.uriBuilder.buildDatasetBlankNodeUri(String.format("%s_slot_%d", listResource.getLocalName(), i+1));
				slotResource = jenaModel.createResource(manager.uriBuilder.buildDatasetBlankNodeUri(slotResourceName));
			} else {
				slotResource = jenaModel.createResource();
			}
			slotResource.addLiteral(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.index)),
					i + 1);
			slotResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.item)),
					nodeList.get(i));
			listResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.slot)),
					slotResource);
		}
		
		List<RDFNode> nodes = new ArrayList<RDFNode>();
		nodes.add(listResource);
		return nodes;
	}
		
	 
	//*****************************************
	// EndRegion COLLECTION TYPES AND VALUES
	//*****************************************
		

}
