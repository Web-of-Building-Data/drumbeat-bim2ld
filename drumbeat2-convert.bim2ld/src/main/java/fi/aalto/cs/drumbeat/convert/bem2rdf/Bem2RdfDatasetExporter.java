package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;

import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.convert.bem2rdf.impl.Bem2RdfConverterManager;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;

public class Bem2RdfDatasetExporter {
	
	private final BemDataset dataset;
	
	private final Model jenaModel;
	private final Bem2RdfUriBuilder uriBuilder;
	private final Bem2RdfConverterManager converterManager;

	public Bem2RdfDatasetExporter(BemDataset sourceBemDataset, Bem2RdfConversionContext context, Model targetJenaModel) throws Bem2RdfConverterConfigurationException {
		this.dataset = sourceBemDataset;
		this.jenaModel = targetJenaModel;
		
		this.uriBuilder = Bem2RdfUriBuilder.createUriBuilder(context, dataset);		
		this.converterManager = new Bem2RdfConverterManager(context, uriBuilder);
	}
	
	public Model export() throws IOException, BemException {
		
		converterManager.exportNsPrefixes(jenaModel);
		converterManager.exportOntologyHeader(jenaModel);
		
		for (BemEntity entity : dataset.getAllEntities()) {		
			if (!entity.isDuplicated()) {
				converterManager.convertEntityValue(jenaModel, entity);
			}
		}
			
		return jenaModel;
	}
	

}
