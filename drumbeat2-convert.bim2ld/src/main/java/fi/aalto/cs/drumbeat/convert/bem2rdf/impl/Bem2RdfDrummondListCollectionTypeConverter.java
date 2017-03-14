package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.*;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemCollectionValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

class Bem2RdfDrummondListCollectionTypeConverter extends Bem2RdfCollectionTypeConverter {

	Bem2RdfDrummondListCollectionTypeConverter(Bem2RdfConverterManager manager) {
		super(manager);
	}
	
	@Override
	void exportPermanentBuiltInDefinitions(Model jenaModel) {
		
		boolean declareFunctionalProperties = manager.targetOwlProfileList.supportsStatement(RDF.type, OWL.FunctionalProperty);
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)),
				RDF.type,
				OWL.Class);

		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EmptyList)),
				RDF.type,
				OWL.Class);
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EmptyList)),
				RDFS.subClassOf,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)),
				RDF.type,
				OWL.Class);
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
				RDF.type,
				OWL.ObjectProperty);
		
		if (declareFunctionalProperties) {
			jenaModel.add(
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
					RDF.type,
					OWL.FunctionalProperty);
		}

		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
				RDFS.domain,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));

		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDF.type,
				OWL.ObjectProperty);
		
		if (declareFunctionalProperties) {
			jenaModel.add(
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
					RDF.type,
					OWL.FunctionalProperty);
		}

		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDFS.domain,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));

		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDFS.range,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));

	}
	
	@Override
	Resource convertCollectionTypeInfo(Model jenaModel, BemCollectionTypeInfo collectionTypeInfo, boolean includeDetails) {
		
		if (!collectionTypeInfo.isSorted()) {
			throw new IllegalArgumentException("Collection type must be sorted:" + collectionTypeInfo);
		}
		
		Resource collectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo));
		
		if (includeDetails && !collectionTypeResource.listProperties().hasNext()) {
			
			String collectionKindName = collectionTypeInfo.getCollectionKind().name();
			
			String additionalCollectionTypeName = String.format("%s_%s", collectionTypeInfo.getItemTypeInfo(), collectionKindName);
			Resource additionalCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(additionalCollectionTypeName));
			
			if (!collectionTypeResource.getURI().equals(additionalCollectionTypeResource.getURI())) {
				jenaModel.add(collectionTypeResource, RDF.type, OWL.Class);	
				jenaModel.add(collectionTypeResource, RDFS.subClassOf, additionalCollectionTypeResource);
				collectionTypeResource = additionalCollectionTypeResource;
			}

			jenaModel.add(collectionTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(collectionTypeResource, RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List))); // List, Array or Bag
			
			Resource itemTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo.getItemTypeInfo()));
			manager.convertPropertyRestrictions(
					jenaModel,
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
					collectionTypeResource, itemTypeResource, true, 0, 1, false, false);
			 
			manager.convertPropertyRestrictions(
					jenaModel,
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
					collectionTypeResource,
					additionalCollectionTypeResource, true, 1, 1, false, false);
			 
			Resource emptyListTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo).replace(collectionKindName, "Empty" + collectionKindName));
			jenaModel.add(emptyListTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(emptyListTypeResource, RDFS.subClassOf, collectionTypeResource);
			jenaModel.add(
					collectionTypeResource,
					RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EmptyList)));
				 
		}
		
		return collectionTypeResource;
	}
	
	@Override
	Resource convertListToResource(Model jenaModel, BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
			Resource parentResource, int childNodeCount)
	{
		if (!collectionTypeInfo.isSorted()) {
			throw new IllegalArgumentException("Collection type must be sorted:" + collectionTypeInfo);
		}

		Resource listTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo)); 
		Resource emptyListTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo).replace("List", "EmptyList"));
		BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();			
		
		List<? extends BemValue> values = listValue.getSingleValues(); 

		int index = values.size();
		
		Resource currentListResource;
		assert(parentResource != null);
		if (manager.nameAllBlankNodes) {
			assert(parentResource.isURIResource());
			assert(parentResource.getLocalName() != null);
			String currentResourceName = manager.uriBuilder.buildDatasetBlankNodeUri(
					String.format("%s_%d_%d", parentResource.getLocalName(), childNodeCount, index + Bem2RdfConverterManager.MIN_CHILD_NODE_INDEX));
			currentListResource = jenaModel.createResource(currentResourceName);			
		} else {
			currentListResource = jenaModel.createResource();
		}
		
		currentListResource.addProperty(RDF.type, emptyListTypeResource);
		
		while (index > 0) {
			index--;
			Resource nextListResource = currentListResource;
			if (manager.nameAllBlankNodes) {
				assert(parentResource.isURIResource());
				assert(parentResource.getLocalName() != null);
				String currentResourceName = manager.uriBuilder.buildDatasetBlankNodeUri(
						String.format("%s_%d_%d", parentResource.getLocalName(), childNodeCount, index + Bem2RdfConverterManager.MIN_CHILD_NODE_INDEX));
				currentListResource = jenaModel.createResource(currentResourceName);			
			} else {
				currentListResource = jenaModel.createResource();
			}

			currentListResource.addProperty(RDF.type, listTypeResource);
			currentListResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
					nextListResource);
			
			BemValue value = values.get(index);
			RDFNode valueNode = manager.convertValue(jenaModel, value, itemTypeInfo, currentListResource, Bem2RdfConverterManager.MIN_CHILD_NODE_INDEX, false);
			
			currentListResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
					valueNode);
		}

		return currentListResource;			
			
	}


}
