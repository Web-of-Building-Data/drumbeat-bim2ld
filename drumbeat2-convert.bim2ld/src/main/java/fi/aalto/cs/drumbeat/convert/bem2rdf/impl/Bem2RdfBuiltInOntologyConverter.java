package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class Bem2RdfBuiltInOntologyConverter {

	private final Bem2RdfConverterManager manager;

	public Bem2RdfBuiltInOntologyConverter(Bem2RdfConverterManager manager) {
		this.manager = manager;
	}
	
	public void exportPermanentBuiltInDefinitions(Model jenaModel) {


		// TODO: Generate literal and logical types automatically (not manually
		// as below)
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Enum)),
				RDF.type,
				OWL.Class);
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Defined)),
				RDF.type,
				OWL.Class);
		jenaModel.add(jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Select)),
				RDF.type,
				OWL.Class);
		jenaModel.add(jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Entity)),
				RDF.type,
				OWL.Class);
		
//		switch (manager.contextParams.convertCollectionsTo()) {
//			
//		}

//		jenaModel
//				.add(Ifc2RdfVocabulary.EXPRESS.Collection, RDF.type, OWL.Class);

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
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
				RDF.type,
				OWL.ObjectProperty);
		
		jenaModel.add(jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDF.type,
				OWL.ObjectProperty);
		
		jenaModel.add(jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasSetItem)),
				RDF.type,
				OWL.ObjectProperty);

		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
				RDFS.domain,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDFS.domain,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasSetItem)),
				RDFS.domain,
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Set)));
		

		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.slot, RDF.type,
		// OWL.ObjectProperty);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.slot, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.slot, RDFS.range,
		// Ifc2RdfVocabulary.EXPRESS.Slot);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.isOrdered, RDF.type,
		// OWL.ObjectProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.isOrdered, RDF.type,
		// OWL.FunctionalProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.isOrdered, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.isOrdered, RDFS.range,
		// Ifc2RdfVocabulary.EXPRESS.BOOLEAN);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.size, RDF.type,
		// OWL.DatatypeProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.size, RDF.type,
		// OwlVocabulary.OWL.FunctionalDataProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.size, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.size, RDFS.range,
		// XSD.integer);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.startIndex, RDF.type,
		// OWL.DatatypeProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.startIndex, RDF.type,
		// OwlVocabulary.OWL.FunctionalDataProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.startIndex, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.startIndex, RDFS.range,
		// XSD.integer);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.endIndex, RDF.type,
		// OWL.DatatypeProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.endIndex, RDF.type,
		// OwlVocabulary.OWL.FunctionalDataProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.endIndex, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.endIndex, RDFS.range,
		// XSD.integer);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.List, RDF.type, OWL.Class);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.List, RDFS.subClassOf,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.Array, RDF.type, OWL.Class);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.Array, RDFS.subClassOf,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.Set, RDF.type, OWL.Class);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.Set, RDFS.subClassOf,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.Bag, RDF.type, OWL.Class);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.Bag, RDFS.subClassOf,
		// Ifc2RdfVocabulary.EXPRESS.Collection);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.Slot, RDF.type, OWL.Class);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.item, RDF.type,
		// OWL.ObjectProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.item, RDF.type,
		// OWL.FunctionalProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.item, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Slot);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.index, RDF.type,
		// OWL.DatatypeProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.index, RDF.type,
		// OwlVocabulary.OWL.FunctionalDataProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.index, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Slot);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.index, RDFS.range,
		// XSD.integer);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.previous, RDF.type,
		// OWL.ObjectProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.previous, RDF.type,
		// OWL.FunctionalProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.previous, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Slot);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.previous, RDFS.range,
		// Ifc2RdfVocabulary.EXPRESS.Slot);
		//
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.next, RDF.type,
		// OWL.ObjectProperty);
		// if (declareFunctionalProperties) {
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.next, RDF.type,
		// OWL.FunctionalProperty);
		// }
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.next, RDFS.domain,
		// Ifc2RdfVocabulary.EXPRESS.Slot);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.next, RDFS.range,
		// Ifc2RdfVocabulary.EXPRESS.Slot);
		//
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EntityProperty)),
				RDF.type,
				OWL.Class);
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EntityProperty)),
				RDFS.subClassOf,
				OWL.ObjectProperty);
		//
		// // jenaModel.add(Ifc2RdfVocabulary.EXPRESS.propertyIndex, RDF.type,
		// // OWL.DatatypeProperty);
		// // jenaModel.add(Ifc2RdfVocabulary.EXPRESS.propertyIndex,
		// RDFS.domain,
		// // Ifc2RdfVocabulary.EXPRESS.EntityProperty);
		// // jenaModel.add(Ifc2RdfVocabulary.EXPRESS.propertyIndex, RDFS.range,
		// // XSD.integer);
		//
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)),
				RDF.type,
				OWL.DatatypeProperty);

		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.hasBinary, RDF.type,
		// OWL.DatatypeProperty);
		// // jenaModel.add(Ifc2RdfVocabulary.EXPRESS.hasBoolean, RDF.type,
		// // OWL.DatatypeProperty);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.hasInteger, RDF.type,
		// OWL.DatatypeProperty);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.hasLogical, RDF.type,
		// OWL.DatatypeProperty);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.hasNumber, RDF.type,
		// OWL.DatatypeProperty);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.hasReal, RDF.type,
		// OWL.DatatypeProperty);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.hasString, RDF.type,
		// OWL.DatatypeProperty);

	}
	
	

}
