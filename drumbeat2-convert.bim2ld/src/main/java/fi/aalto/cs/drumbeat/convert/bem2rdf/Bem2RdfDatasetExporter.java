package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.convert.bem2rdf.impl.Bem2RdfConverterManager;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class Bem2RdfDatasetExporter {
	
	private final BemSchema schema;
	private final BemDataset dataset;
	
	private final Bem2RdfConversionContext context;
	private final Model jenaModel;
	private final Bem2RdfUriBuilder uriBuilder;
	private final Bem2RdfConverterManager converterManager;
	private final boolean nameAllBlankNodes;

	public Bem2RdfDatasetExporter(BemDataset sourceBemDataset, Bem2RdfConversionContext context, Model targetJenaModel) throws Bem2RdfConverterConfigurationException {
		this.dataset = sourceBemDataset;
		this.schema = sourceBemDataset.getSchema();
		this.context = context;
		this.jenaModel = targetJenaModel;
		
		this.uriBuilder = Bem2RdfUriBuilder.createUriBuilder(context, dataset);		
		this.converterManager = new Bem2RdfConverterManager(context, uriBuilder);
		this.nameAllBlankNodes = context.getConversionParams().nameAllBlankNodes();
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
