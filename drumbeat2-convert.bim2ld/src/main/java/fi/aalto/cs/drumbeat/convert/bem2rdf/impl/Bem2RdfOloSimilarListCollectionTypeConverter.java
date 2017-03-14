package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.*;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemCollectionValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

class Bem2RdfOloSimilarListCollectionTypeConverter extends Bem2RdfCollectionTypeConverter {

	Bem2RdfOloSimilarListCollectionTypeConverter(Bem2RdfConverterManager manager) {
		super(manager);
	}
	
	@Override
	void exportPermanentBuiltInDefinitions(Model jenaModel) {
		
		// class Collection
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)),
				RDF.type,
				OWL.Class);		

		
		// class List
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)),
				RDF.type,
				OWL.Class);		
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)),
				RDFS.subClassOf,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)));		

		
		// class Array
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Array)),
				RDF.type,
				OWL.Class);		
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Array)),
				RDFS.subClassOf,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)));		

		
		// class Bag
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Bag)),
				RDF.type,
				OWL.Class);		
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Bag)),
				RDFS.subClassOf,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)));
		
		// class Slot
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				RDF.type,
				OWL.Class);		
		
		
		
		// property Collection.size		
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.size)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)),
				XSD.integer,
				false,
				1,
				1,
				true,
				true);
		
		// property Collection.startIndex
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.startIndex)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)),
				XSD.integer,
				false,
				1,
				1,
				true,
				true);

		// property Collection.endIndex
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.endIndex)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)),
				XSD.integer,
				false,
				1,
				1,
				true,
				true);		
		
		// property Collection.slot
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.slot)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Collection)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				true,
				null,
				null,
				true,
				true);		
		
		// property Slot.index
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.index)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				XSD.integer,
				false,
				1,
				1,
				true,
				true);		
		
		
		// property Slot.item
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.item)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				null,
				true,
				1,
				1,
				true,
				true);		

		// property Slot.previous
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.previous)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				true,
				0,
				1,
				true,
				true);
		
		// property Slot.next
		manager.convertPropertyRestrictions(
				jenaModel,
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.next)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
				true,
				0,
				1,
				true,
				true);		
			
	}
	
	private Resource convertCollectionSlot(
		Model jenaModel,
		BemTypeInfo itemTypeInfo)
	{
		String slotTypeName = itemTypeInfo.getName() + "_" + Bem2RdfVocabulary.BuiltInOntology.Slot;
		Resource slotTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(slotTypeName));
		
		if (!slotTypeResource.listProperties().hasNext()) {
		
			slotTypeResource.addProperty(RDF.type, OWL.Class);
			slotTypeResource.addProperty(RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)));
			
			manager.convertPropertyRestrictions(
					jenaModel,
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.item)),
					slotTypeResource,
					manager.convertTypeInfo(jenaModel, itemTypeInfo, false),
					true,
					null,
					null,
					false,
					false);
		}
		
		return slotTypeResource;
	}
	
	private Resource convertCollectionTypeInfo(
			Model jenaModel,
			BemTypeInfo itemTypeInfo,
			BemCollectionKindEnum collectionKind,
			Integer min,
			Integer max)
	{
		
		
		String collectionTypeName = String.format("%s%s",
				itemTypeInfo != null ? itemTypeInfo.getName() + "_" : "",
				collectionKind.name());
		
		if (min != null || max != null) {
			collectionTypeName += String.format("_%s_%s", min != null ? min : "UNBOUNDED", max != null ? max : "UNBOUNDED");
		}
		
		Resource collectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildOntologyUri(collectionTypeName));
		
		if (!collectionTypeResource.listProperties().hasNext()) {
			
			jenaModel.add(collectionTypeResource, RDF.type, OWL.Class);	

			boolean hasSomeBaseType = false;
			
			if (itemTypeInfo != null) {
				
				if (min != null || max != null) {
					
					hasSomeBaseType = true;					
					Resource baseCollectionTypeResource = convertCollectionTypeInfo(jenaModel, itemTypeInfo, collectionKind, null, null);
					collectionTypeResource.addProperty(RDFS.subClassOf, baseCollectionTypeResource);
					
				} else {
					
					Resource slotTypeResource = convertCollectionSlot(jenaModel, itemTypeInfo);
					manager.convertPropertyRestrictions(
							jenaModel,
							jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.slot)),
							collectionTypeResource,
							slotTypeResource,
							true,
							null,
							null,
							false,
							false);
					
				}
				
			}
			
			if (min != null && max != null) {
				hasSomeBaseType = true;
				
				Resource baseCollectionTypeResource1 = convertCollectionTypeInfo(jenaModel, null, collectionKind, min, null);
				collectionTypeResource.addProperty(RDFS.subClassOf, baseCollectionTypeResource1);
				
				Resource baseCollectionTypeResource2 = convertCollectionTypeInfo(jenaModel, null, collectionKind, null, max);
				collectionTypeResource.addProperty(RDFS.subClassOf, baseCollectionTypeResource2);					
					
			} else if (min != null || max != null) {
				manager.convertPropertyRestrictions(
						jenaModel,
						jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.slot)),
						collectionTypeResource,
						jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Slot)),
						true,
						min,
						max,
						false,
						false);
			}
			
			if (!hasSomeBaseType) {
				collectionTypeResource.addProperty(RDFS.subClassOf, 
						jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(collectionKind.name())));
				
			}
			
		}
		
		return collectionTypeResource;
	}

	@Override
	Resource convertCollectionTypeInfo(Model jenaModel, BemCollectionTypeInfo collectionTypeInfo, boolean includeDetails) {
		
		if (!collectionTypeInfo.isSorted()) {
			throw new IllegalArgumentException("Collection type must be sorted:" + collectionTypeInfo);
		}
		
		Resource collectionTypeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo));
		
		if (includeDetails && !collectionTypeResource.listProperties().hasNext()) {
			
			Resource baseCollectionTypeResource = convertCollectionTypeInfo(
					jenaModel,
					collectionTypeInfo.getItemTypeInfo(),
					collectionTypeInfo.getCollectionKind(),
					collectionTypeInfo.getCardinality().getMinCardinality(),
					collectionTypeInfo.getCardinality().getMaxCardinality() != BemCardinality.UNBOUNDED ?
							collectionTypeInfo.getCardinality().getMaxCardinality() : null);
			
			if (!collectionTypeResource.equals(baseCollectionTypeResource)) {
				jenaModel.add(collectionTypeResource, RDF.type, OWL.Class);	
				jenaModel.add(collectionTypeResource, RDFS.subClassOf, baseCollectionTypeResource);
			}

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

		Resource listResource = manager.createLocalResource(jenaModel, parentResource, childNodeCount);
		
		BemTypeInfo itemTypeInfo = collectionTypeInfo.getItemTypeInfo();
		
		List<? extends BemValue> values = listValue.getSingleValues();
		int size = values.size();		
		
		Resource typeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(collectionTypeInfo)); 
		listResource.addProperty(RDF.type, typeResource);
		listResource.addLiteral(
				jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.size)),
				manager.primitiveTypeConverter.convertLiteral(jenaModel, size, BemValueKindEnum.INTEGER));
//		listResource.addProperty(Bem2RdfVocabulary.BuiltInOntology.itemType, jenaModel.createResource(manager.uriBuilder.buildOntologyUri(itemTypeInfo)));
		
		for (int i = 0; i < size; ++i) {
			Resource slotResource = manager.createLocalResource(jenaModel, listResource, i);			
			listResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.slot)),
					slotResource);

			slotResource.addLiteral(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.index)),
					manager.primitiveTypeConverter.convertLiteral(jenaModel, i, BemValueKindEnum.INTEGER));
			
			RDFNode valueNode = manager.convertValue(jenaModel, values.get(i), itemTypeInfo, slotResource, i, false);			
			slotResource.addProperty(
					jenaModel.createProperty(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.item)),
					valueNode);
		}
		
		return listResource;
	}

}
