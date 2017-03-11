package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class Bem2RdfDefinedTypeConverter {
	
	private final Bem2RdfConverterManager manager;

	public Bem2RdfDefinedTypeConverter(Bem2RdfConverterManager manager) {
		this.manager = manager;
	}
	
	
	//*****************************************
	// Region DEFINED TYPES & VALUES
	//*****************************************
	
	
	public Resource convertDefinedTypeInfo(Model jenaModel, BemDefinedTypeInfo typeInfo, boolean includeDetails) {
		
		Resource typeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo));
		
		if (includeDetails) {
			jenaModel.add(typeResource, RDF.type, OWL.Class);
			
			BemTypeInfo baseTypeInfo = typeInfo.getWrappedTypeInfo();
			assert(baseTypeInfo != null);
			
			if (baseTypeInfo instanceof BemDefinedTypeInfo) {
				
				// subclass of another Defined class				
				Resource superTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(baseTypeInfo.getName()));
				jenaModel.add(typeResource, RDFS.subClassOf, superTypeResource);
	
			} else if (baseTypeInfo instanceof BemPrimitiveTypeInfo) {
	
				Resource superTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(baseTypeInfo.getName()));
				jenaModel.add(typeResource, RDFS.subClassOf, superTypeResource);
				jenaModel.add(
						typeResource,
						RDFS.subClassOf,
						jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Defined)));
				
			} else {
				
				assert(baseTypeInfo instanceof BemEnumerationTypeInfo) : "Expected BemEnumerationTypeInfo: " + baseTypeInfo.getClass();
				Resource superTypeResource = jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(baseTypeInfo.getName()));			
				Property property = manager.getProperty_hasXXX(jenaModel, baseTypeInfo.getValueKind());
				manager.convertPropertyRestrictions(property, typeResource, superTypeResource, true, 1, 1, jenaModel);		
				
			}
		}
		
		return typeResource;
	}
	
	//*****************************************
	// EndRegion DEFINED TYPES & VALUES
	//*****************************************
		

}
