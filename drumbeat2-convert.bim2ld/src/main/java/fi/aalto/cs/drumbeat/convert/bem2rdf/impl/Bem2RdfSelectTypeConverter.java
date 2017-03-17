package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.*;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

class Bem2RdfSelectTypeConverter {
	
	private final Bem2RdfConverterManager manager;

	public Bem2RdfSelectTypeConverter(Bem2RdfConverterManager manager) {
		this.manager = manager;
	}
	
	
	//*****************************************
	// Region SELECT TYPES & VALUES
	//*****************************************
	
	public Resource convertSelectTypeInfo(Model jenaModel, BemSelectTypeInfo typeInfo, boolean includeDetails) {

		Resource typeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo));
		
		if (includeDetails) {
			jenaModel.add(typeResource, RDF.type, OWL.Class);
			jenaModel.add(typeResource, RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Select)));
	
			List<BemTypeInfo> subTypes = typeInfo.getItemTypeInfos();
			List<Resource> subTypeResources = new ArrayList<>();
			for (BemTypeInfo subType : subTypes) {
				subTypeResources.add(jenaModel.createResource(manager.uriBuilder.buildTypeUri(subType)));
			}
	
			final boolean unionIsSupported = manager.limitingOwlProfileList.supportsStatement(
					OWL.unionOf, OwlVocabulary.DumpData.SAMPLE_URI_LIST);
	
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
		}
		
		return typeResource;
	}
	
	//*****************************************
	// EndRegion SELECT TYPES & VALUES
	//*****************************************
	
	
	

}
