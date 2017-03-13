package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

class Bem2RdfBuiltInOntologyConverter {

	private final Bem2RdfConverterManager manager;
	
	public Bem2RdfBuiltInOntologyConverter(Bem2RdfConverterManager manager) {
		this.manager = manager;
	}
	
	public void exportPermanentBuiltInDefinitions(Model jenaModel) {
		
		manager.collectionTypeConverter.exportPermanentBuiltInDefinitions(jenaModel);
		
		

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
		
		jenaModel.add(
				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)),
				RDF.type,
				OWL.DatatypeProperty);
		
		

//		jenaModel.add(
//				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EntityProperty)),
//				RDF.type,
//				OWL.Class);
//		jenaModel.add(
//				jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EntityProperty)),
//				RDFS.subClassOf,
//				OWL.ObjectProperty);
		
		//
		// // jenaModel.add(Ifc2RdfVocabulary.EXPRESS.propertyIndex, RDF.type,
		// // OWL.DatatypeProperty);
		// // jenaModel.add(Ifc2RdfVocabulary.EXPRESS.propertyIndex,
		// RDFS.domain,
		// // Ifc2RdfVocabulary.EXPRESS.EntityProperty);
		// // jenaModel.add(Ifc2RdfVocabulary.EXPRESS.propertyIndex, RDFS.range,
		// // XSD.integer);
		//

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
