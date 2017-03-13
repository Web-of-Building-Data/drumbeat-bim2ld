package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.data.bem.schema.*;

class Bem2RdfDefinedTypeConverter {
	
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
			
			Resource superTypeResource = manager.convertTypeInfo(jenaModel, baseTypeInfo, false);
			
			if (baseTypeInfo instanceof BemDefinedTypeInfo) {
				
				// subclass of another Defined class		
				jenaModel.add(typeResource, RDFS.subClassOf, superTypeResource);
	
			} else if (baseTypeInfo instanceof BemPrimitiveTypeInfo || baseTypeInfo instanceof BemLogicalTypeInfo) {
	
				jenaModel.add(typeResource, RDFS.subClassOf, superTypeResource);
				jenaModel.add(
						typeResource,
						RDFS.subClassOf,
						jenaModel.createResource(manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Defined)));
				
//			} else if (baseTypeInfo instanceof BemEnumerationTypeInfo) {
//				
//				Property property = manager.getProperty_hasXXX(jenaModel, baseTypeInfo.getValueKind());
//				manager.convertPropertyRestrictions(property, typeResource, superTypeResource, true, 1, 1, jenaModel);		
				
			} else {
				
				throw new IllegalArgumentException("Invalid defined type: " + typeInfo);
				
			}
		}
		
		return typeResource;
	}
	
	//*****************************************
	// EndRegion DEFINED TYPES & VALUES
	//*****************************************
		

}
