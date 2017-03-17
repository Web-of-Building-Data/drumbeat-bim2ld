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
		
		boolean declareFunctionalProperties = manager.limitingOwlProfileList.supportsStatement(RDF.type, OWL.FunctionalProperty);
		
		Resource topCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)); 
		
		jenaModel.add(
				topCollectionTypeResource,
				RDF.type,
				OWL.Class);
		
		for (BemCollectionKindEnum collectionKind : BemCollectionKindEnum.values()) {
			
			String collectionTypeName = getCollectionTypeName(null, collectionKind, false);
			Resource collectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(collectionTypeName));
			
			jenaModel.add(
					collectionTypeResource,
					RDF.type,
					OWL.Class);
			
			jenaModel.add(
					collectionTypeResource,
					RDFS.subClassOf,
					topCollectionTypeResource);
			
			String emptyCollectionTypeName = getCollectionTypeName(null, collectionKind, true);
			Resource emptyCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(emptyCollectionTypeName));
			
			jenaModel.add(
					emptyCollectionTypeResource,
					RDF.type,
					OWL.Class);

			jenaModel.add(
					emptyCollectionTypeResource,
					RDFS.subClassOf,
					collectionTypeResource);			
		}

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
				topCollectionTypeResource);

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
				topCollectionTypeResource);

		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDFS.range,
				topCollectionTypeResource);

	}
	
	@Override
	Resource convertCollectionTypeInfo(Model jenaModel, BemCollectionTypeInfo collectionTypeInfo, boolean includeDetails) {
		
		BemCollectionKindEnum collectionKind = collectionTypeInfo.getCollectionKind();
		
		String collectionTypeName = collectionTypeInfo.isDerivedType() ?
				getCollectionTypeName(collectionTypeInfo.getItemTypeInfo(), collectionKind, false) : collectionTypeInfo.getName(); 

		Resource collectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(collectionTypeName));
		
		if (includeDetails) {
			
			BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();
			
			if (!collectionTypeInfo.isDerivedType()) {
				
				String additionalCollectionTypeName = getCollectionTypeName(itemTypeInfo, collectionKind, false);
				
				if (!collectionTypeName.equals(additionalCollectionTypeName)) {
					Resource additionalCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(additionalCollectionTypeName));
					jenaModel.add(collectionTypeResource, RDF.type, OWL.Class);	
					jenaModel.add(collectionTypeResource, RDFS.subClassOf, additionalCollectionTypeResource);
					collectionTypeResource = additionalCollectionTypeResource;
				}
				
			}			
			
			jenaModel.add(collectionTypeResource, RDF.type, OWL.Class);
			
			String topCollectionTypeName = getCollectionTypeName(null, collectionKind, false);
			Resource topCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(topCollectionTypeName));
			jenaModel.add(collectionTypeResource, RDFS.subClassOf, topCollectionTypeResource); // List, Array, Set or Bag
			
			Resource itemTypeResource = manager.convertTypeInfo(jenaModel, itemTypeInfo, itemTypeInfo.isDerivedType());
			
			manager.convertPropertyRestrictions(
					jenaModel,
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
					collectionTypeResource, itemTypeResource, true, 0, 1, false, false);
			 
			manager.convertPropertyRestrictions(
					jenaModel,
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
					collectionTypeResource,
					collectionTypeResource, true, 1, 1, false, false);
			 
			String emptyCollectionTypeName = getCollectionTypeName(itemTypeInfo, collectionKind, true);
			Resource emptyCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(emptyCollectionTypeName));
			jenaModel.add(emptyCollectionTypeResource, RDF.type, OWL.Class);	
			jenaModel.add(emptyCollectionTypeResource, RDFS.subClassOf, collectionTypeResource);
			
			String emptyTopCollectionTypeName = getCollectionTypeName(null, collectionKind, true);
			Resource emptyTopCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(emptyTopCollectionTypeName));
			
			jenaModel.add(
					emptyCollectionTypeResource,
					RDFS.subClassOf,
					emptyTopCollectionTypeResource);
				 
		}
		
		return collectionTypeResource;
	}
	
	@Override
	Resource convertListToResource(Model jenaModel, BemCollectionValue<? extends BemValue> collectionValue, BemCollectionTypeInfo collectionTypeInfo,
			Resource parentResource, int childNodeCount)
	{
		if (!collectionTypeInfo.isSorted()) {
			throw new IllegalArgumentException("Collection type must be sorted:" + collectionTypeInfo);
		}
		
		BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();		
		

		parentResource = manager.createLocalResource(jenaModel, parentResource, childNodeCount);
		
		String collectionTypeName = getCollectionTypeName(itemTypeInfo, collectionTypeInfo.getCollectionKind(), false);
		Resource collectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(collectionTypeName));
		
		String emptyCollectionTypeName = getCollectionTypeName(itemTypeInfo, collectionTypeInfo.getCollectionKind(), true);
		Resource emptyCollectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(emptyCollectionTypeName));
		
		
		List<? extends BemValue> values = collectionValue.getSingleValues(); 

		int index = values.size();
		
		Resource currentCollectionResource;
		assert(parentResource != null);
		currentCollectionResource = manager.createLocalResource(jenaModel, parentResource, index);
		
		currentCollectionResource.addProperty(RDF.type, emptyCollectionTypeResource);
		
		while (index > 0) {
			index--;
			Resource nextCollectionResource = currentCollectionResource;
			currentCollectionResource = manager.createLocalResource(jenaModel, parentResource, index);

			currentCollectionResource.addProperty(RDF.type, collectionTypeResource);
			currentCollectionResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
					nextCollectionResource);
			
			BemValue value = values.get(index);
			RDFNode valueNode = manager.convertValue(jenaModel, value, itemTypeInfo, currentCollectionResource, 0, false);
			
			currentCollectionResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
					valueNode);
		}

		return currentCollectionResource;			
			
	}
	
	private static String getCollectionTypeName(BemTypeInfo itemTypeInfo, BemCollectionKindEnum collectionKind, boolean isEmptyCollection) {
		return String.format("%s%s%s",
				itemTypeInfo != null ? itemTypeInfo.getName() + "_" : "",
				isEmptyCollection ? "Empty" : "",
				collectionKind);		
	}


}
