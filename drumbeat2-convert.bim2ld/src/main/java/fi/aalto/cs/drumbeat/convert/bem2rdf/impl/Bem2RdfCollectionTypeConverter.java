package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemCollectionValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public abstract class Bem2RdfCollectionTypeConverter {
	
	protected final Bem2RdfConverterManager manager;

	public Bem2RdfCollectionTypeConverter(Bem2RdfConverterManager manager) {
		this.manager = manager;
	}
	
	//*****************************************
	// Region COLLECTION TYPES AND VALUES
	//*****************************************
	
	abstract void exportPermanentBuiltInDefinitions(Model jenaModel);
	
	abstract Resource convertCollectionTypeInfo(Model jenaModel, BemCollectionTypeInfo typeInfo, boolean includeDetails);	
	
	abstract Resource convertListToResource(Model jenaModel, BemCollectionValue<? extends BemValue> listValue, BemCollectionTypeInfo collectionTypeInfo,
			Resource parentResource, long childNodeCount);	 

	//*****************************************
	// EndRegion COLLECTION TYPES AND VALUES
	//*****************************************
		

}
