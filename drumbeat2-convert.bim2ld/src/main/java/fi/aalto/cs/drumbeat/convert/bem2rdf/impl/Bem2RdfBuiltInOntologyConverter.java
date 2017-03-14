package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemSpecialValue;

class Bem2RdfBuiltInOntologyConverter {

	private final Bem2RdfConverterManager manager;
	
	public Bem2RdfBuiltInOntologyConverter(Bem2RdfConverterManager manager) {
		this.manager = manager;
	}
	
	public void exportPermanentBuiltInDefinitions(Model jenaModel) {
		
		final boolean declareFunctionalProperty = manager.targetOwlProfileList.supportsStatement(RDF.type, OWL.FunctionalProperty);
		
		manager.collectionTypeConverter.exportPermanentBuiltInDefinitions(jenaModel);		

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
		
		if (declareFunctionalProperty) {
			jenaModel.add(
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)),
					RDF.type,
					OWL.FunctionalProperty);
		}
		
		
		jenaModel.add(jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.SpecialValue)),
				RDF.type,
				OWL.Class);
		

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
