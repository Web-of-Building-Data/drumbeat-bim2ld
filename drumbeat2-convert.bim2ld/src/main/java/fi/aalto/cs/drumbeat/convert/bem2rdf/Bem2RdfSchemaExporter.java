package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.common.DrbException;
import fi.aalto.cs.drumbeat.common.params.DrbParamNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.rdf.RdfVocabulary;

public class Bem2RdfSchemaExporter {
	
	private final BemSchema schema;
	private final Bem2RdfConversionContext context;
	private final Model jenaModel;
	private final Bem2RdfUriBuilder uriBuilder;
	private final Bem2RdfConverter converter;

	public Bem2RdfSchemaExporter(BemSchema sourceBemSchema, Bem2RdfConversionContext context, Model targetJenaModel) throws Bem2RdfConverterConfigurationException {
		this.schema = sourceBemSchema;
		this.context = context;
		this.jenaModel = targetJenaModel;
		
		this.uriBuilder = Bem2RdfUriBuilder.createUriBuilder(context, sourceBemSchema);		
		this.converter = new Bem2RdfConverter(context, uriBuilder);
	}
	
	public Model export() throws IOException, BemException {

		//
		// write header and prefixes
		//
		exportOntologyHeader();

		if (!context.getConversionParams().ignoreBuiltInTypes()) {
			exportExpressOntology();
		}

		if (!context.getConversionParams().ignoreNonBuiltInTypes()) {
			
			for (BemTypeInfo typeInfo : schema.getAllTypeInfos()) {
				if (!typeInfo.isBuiltInType()) {
					if (typeInfo instanceof BemEntityTypeInfo) {
						converter.convertEntityTypeInfo((BemEntityTypeInfo) typeInfo, jenaModel);					
					} else if (typeInfo instanceof BemEnumerationTypeInfo) {
						converter.convertEnumerationTypeInfo((BemEnumerationTypeInfo) typeInfo, jenaModel);					
					} else if (typeInfo instanceof BemSelectTypeInfo) {
						converter.convertSelectTypeInfo((BemSelectTypeInfo) typeInfo, jenaModel);					
					} else if (typeInfo instanceof BemDefinedTypeInfo) {
						converter.convertDefinedTypeInfo((BemDefinedTypeInfo) typeInfo, jenaModel);					
					} else if (typeInfo instanceof BemCollectionTypeInfo) {
						converter.convertCollectionTypeInfo((BemCollectionTypeInfo) typeInfo, jenaModel);					
					} else {
						throw new BemException("Unexpected type: " + typeInfo);
					}
				}
			}
		}

		return jenaModel;
	}

	private void exportOntologyHeader() {
		
		jenaModel.setNsPrefixes(converter.getUriBuilder().getNamespacePrefixMap());

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
	
	private void exportExpressOntology() throws Bem2RdfConverterException {

//		final boolean declareFunctionalProperties = context
//				.getOwlProfileList()
//				.supportsStatement(RDF.type, OWL.FunctionalProperty);

		// TODO: Generate literal and logical types automatically (not manually
		// as below)

		// simple types
		// adapter.startSection("SIMPLE TYPES");
		for (BemTypeInfo typeInfo : schema.getAllTypeInfos()) {
			if (typeInfo.isBuiltInType()) {
				if (typeInfo instanceof BemPrimitiveTypeInfo) {
					converter.convertPrimitiveTypeInfo((BemPrimitiveTypeInfo) typeInfo, jenaModel);
				} else if (typeInfo instanceof BemEnumerationTypeInfo) {
					converter.convertEnumerationTypeInfo((BemEnumerationTypeInfo) typeInfo, jenaModel);
				} else {
					throw new Bem2RdfConverterException("Unexpected built-in type: " + typeInfo);
				}  
			}			
		}
		// adapter.endSection();

		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Enum)),
				RDF.type,
				OWL.Class);
		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Defined)),
				RDF.type,
				OWL.Class);
		jenaModel.add(jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Select)),
				RDF.type,
				OWL.Class);
		jenaModel.add(jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Entity)),
				RDF.type,
				OWL.Class);

		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.LOGICAL, RDF.type,
		// OWL.Class);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.BOOLEAN, RDF.type,
		// OWL.Class);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.BOOLEAN, RDFS.subClassOf,
		// Ifc2RdfVocabulary.EXPRESS.LOGICAL);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.TRUE, RDF.type,
		// Ifc2RdfVocabulary.EXPRESS.BOOLEAN);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.FALSE, RDF.type,
		// Ifc2RdfVocabulary.EXPRESS.BOOLEAN);
		// jenaModel.add(Ifc2RdfVocabulary.EXPRESS.UNKNOWN, RDF.type,
		// Ifc2RdfVocabulary.EXPRESS.LOGICAL);
		//
		//
//		jenaModel
//				.add(Ifc2RdfVocabulary.EXPRESS.Collection, RDF.type, OWL.Class);

		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)),
				RDF.type,
				OWL.Class);
		
		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EmptyList)),
				RDF.type,
				OWL.Class);
		
		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EmptyList)),
				RDFS.subClassOf,
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
		
		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
				RDF.type,
				OWL.ObjectProperty);
		
		jenaModel.add(jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDF.type,
				OWL.ObjectProperty);
		
		jenaModel.add(jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasSetItem)),
				RDF.type,
				OWL.ObjectProperty);

		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasContent)),
				RDFS.domain,
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
		
		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasNext)),
				RDFS.domain,
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.List)));
		
		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasSetItem)),
				RDFS.domain,
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Set)));
		

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
		// RdfVocabulary.OWL.FunctionalDataProperty);
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
		// RdfVocabulary.OWL.FunctionalDataProperty);
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
		// RdfVocabulary.OWL.FunctionalDataProperty);
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
		// RdfVocabulary.OWL.FunctionalDataProperty);
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
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EntityProperty)),
				RDF.type,
				OWL.Class);
		jenaModel.add(
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.EntityProperty)),
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
				jenaModel.createResource(uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.hasValue)),
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
