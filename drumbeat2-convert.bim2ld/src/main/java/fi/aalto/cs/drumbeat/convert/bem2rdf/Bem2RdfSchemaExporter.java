package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;

import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.convert.bem2rdf.impl.Bem2RdfConverterManager;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class Bem2RdfSchemaExporter {
	
	private final BemSchema schema;
	private final Bem2RdfConversionContext context;
	private final Model jenaModel;
	private final Bem2RdfUriBuilder uriBuilder;
	private final Bem2RdfConverterManager converterManager;

	public Bem2RdfSchemaExporter(BemSchema sourceBemSchema, Bem2RdfConversionContext context, Model targetJenaModel) throws Bem2RdfConverterConfigurationException {
		this.schema = sourceBemSchema;
		this.context = context;
		this.jenaModel = targetJenaModel;
		
		this.uriBuilder = Bem2RdfUriBuilder.createUriBuilder(context, sourceBemSchema);		
		this.converterManager = new Bem2RdfConverterManager(context, uriBuilder);
	}
	
	public Model export() throws IOException, BemException {

		converterManager.exportNsPrefixes(jenaModel);
		converterManager.exportOntologyHeader(jenaModel);
		
		boolean exportBuiltInTypes = !context.getConversionParams().ignoreBuiltInTypes();
		boolean exportNonBuiltInTypes = !context.getConversionParams().ignoreNonBuiltInTypes();

		if (exportBuiltInTypes) {
			converterManager.exportPermanentBuiltInDefinitions(jenaModel);
		}
			
		for (BemTypeInfo typeInfo : schema.getAllTypeInfos()) {
			boolean isBuiltInType = typeInfo.isBuiltInType();
			if ((isBuiltInType && exportBuiltInTypes) || (!isBuiltInType && exportNonBuiltInTypes)) {
				converterManager.convertTypeInfo(jenaModel, typeInfo, true);
			}
		}
			
		return jenaModel;
	}


}
