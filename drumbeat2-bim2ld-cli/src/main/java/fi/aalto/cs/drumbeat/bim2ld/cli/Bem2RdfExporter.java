package fi.aalto.cs.drumbeat.bim2ld.cli;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.common.config.*;
import fi.aalto.cs.drumbeat.common.config.document.*;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfDatasetExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfSchemaExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.util.config.Bem2RdfConversionContextLoader;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchemaPool;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcDatasetParser;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcSchemaParser;
import fi.aalto.cs.drumbeat.rdf.jena.provider.AbstractJenaProvider;
import fi.aalto.cs.drumbeat.rdf.jena.provider.MemoryJenaProvider;
import fi.aalto.cs.drumbeat.rdf.jena.provider.config.JenaProviderPoolConfigurationSection;
import fi.aalto.cs.drumbeat.rdf.utils.RdfUtils;

import org.apache.jena.riot.RDFFormat;

public class Bem2RdfExporter {
	
	private static final Logger logger = Logger.getRootLogger();
	
	private String inputSchemaFilePath;
	private String inputDatasetFilePath;
	private String outputLayerName;
	private String outputSchemaFilePath;
	private String outputSchemaName;
	private String outputDatasetFilePath;
	private String outputDatasetName;
	private String outputMetaModelFilePath;
	private String outputMetaModelName;
	private String outputFileFormatName;
	
	public Bem2RdfExporter(
		 String inputSchemaFilePath,
		 String inputDatasetFilePath,
		 String outputLayerName,
		 String outputSchemaFilePath,
		 String outputSchemaName,
		 String outputDatasetFilePath,
		 String outputDatasetName,
		 String outputMetaModelFilePath,
		 String outputMetaModelName,
		 String outputFileFormatName)
	{
		this.inputSchemaFilePath = inputSchemaFilePath;
		this.inputDatasetFilePath = inputDatasetFilePath;
		this.outputLayerName = outputLayerName;
		this.outputSchemaFilePath = outputSchemaFilePath;
		this.outputSchemaName = outputSchemaName;
		this.outputDatasetFilePath = outputDatasetFilePath;
		this.outputDatasetName = outputDatasetName;
		this.outputMetaModelFilePath = outputMetaModelFilePath;
		this.outputMetaModelName = outputMetaModelName;
		this.outputFileFormatName = outputFileFormatName;
	}
	
	public static void init(String loggerConfigFilePath, String configFilePath) throws ConfigurationParserException {
		//
		// config logger
		//
		loadLoggerConfigration(loggerConfigFilePath);
		
		//
		// load configuration document
		//
		loadConfiguration(configFilePath);
	}
	
	/**
	 * @param args
	 */	
	public void run() throws Exception {
		
		Bem2RdfConversionContext conversionContext = Bem2RdfConversionContextLoader.loadConversionContext(
				ConfigurationDocument.getDefault(),
				outputLayerName);
		
		
		//
		// load jena model factory configuration pool
		//
		ConfigurationPool<ConfigurationItemEx> jenaProviderConfigurationPool;
		if (!StringUtils.isEmptyOrNull(outputSchemaName) || !StringUtils.isEmptyOrNull(outputDatasetName)) {
			jenaProviderConfigurationPool = getJenaProviderConfigurationPool();
		} else {
			jenaProviderConfigurationPool  = null;
		}
		
		//
		// define jena-model factory for the output IFC schema
		//
		AbstractJenaProvider outputSchemaJenaProvider = null;		
		if (!StringUtils.isEmptyOrNull(outputSchemaName)) {
			outputSchemaJenaProvider = getJenaProvider(jenaProviderConfigurationPool, outputSchemaName);
		} else if (!StringUtils.isEmptyOrNull(outputSchemaFilePath)) {
			outputSchemaJenaProvider =  new MemoryJenaProvider();			
		}
		
		//
		// define jena-model factory for the output IFC model 
		//
		AbstractJenaProvider outputDatasetJenaProvider = null;		
		if (!StringUtils.isEmptyOrNull(outputDatasetName)) {
			outputDatasetJenaProvider = getJenaProvider(jenaProviderConfigurationPool, outputDatasetName);
		} else if (!StringUtils.isEmptyOrNull(outputDatasetFilePath)) {
			outputDatasetJenaProvider =  new MemoryJenaProvider();			
		}
		
		//
		// define jena-model factory for the output IFC model 
		//
		AbstractJenaProvider outputMetaModelJenaProvider = null;		
		if (!StringUtils.isEmptyOrNull(outputMetaModelName)) {
			outputMetaModelJenaProvider = getJenaProvider(jenaProviderConfigurationPool, outputMetaModelName);
		} else if (!StringUtils.isEmptyOrNull(outputMetaModelFilePath)) {
			outputMetaModelJenaProvider =  new MemoryJenaProvider();			
		}

		try {
			
			RDFFormat outputFileFormat = null;
			boolean gzipOutputFile = false;
			
			if (outputFileFormatName != null) {
				outputFileFormatName = outputFileFormatName.toUpperCase();
				
				String[] tokens = outputFileFormatName.split("\\.");
				
				if (tokens.length == 2) {
					if (tokens[1].equals("GZIP") || tokens[1].equals("GZ")) {
						gzipOutputFile = true;
					} else {
						throw new BemException(String.format("Unknown ZIP format: '%s'", tokens[1]));
					}
				}
				
				try {
					outputFileFormat = (RDFFormat) RDFFormat.class.getField(tokens[0]).get(null);		
				} catch (NoSuchFieldException e) {
					throw new BemException(
							String.format("Unknown RDF format: '%s', see: %s", tokens[0], Bim2LdCommandLineOptions.URL_RIOT_FORMAT));
				}
			}			
			
		
			//
			// parse and export schema
			//
			System.err.printf("Loading schemas%n");

			final List<BemSchema> schemas = parseSchemas(inputSchemaFilePath);			
			
			if (outputSchemaJenaProvider != null) {
				for (BemSchema schema : schemas) {
					exportSchema(outputSchemaJenaProvider, schema, conversionContext, outputSchemaFilePath, outputFileFormat, gzipOutputFile);
				}
			}
			
			//
			// parse model
			//
			if (outputDatasetJenaProvider != null || outputMetaModelJenaProvider != null) {				
				BemDataset dataset = parseDataset(inputDatasetFilePath);
				
				//
				// export model
				//
				if (outputDatasetJenaProvider != null) {
					exportDataset(outputDatasetJenaProvider, dataset, conversionContext, outputDatasetFilePath, outputFileFormat, gzipOutputFile);
				}
				
//				//
//				// export meta-model
//				//
//				if (outputMetaModelJenaProvider != null) {				
//					exportMetaModel(outputMetaModelJenaProvider, dataset, conversionContext, outputMetaModelFilePath, outputFileFormat, gzipOutputFile);				
//				}
				
			}			

		} finally {
		
			//
			//  release jena-model factories
			//
			if (outputSchemaJenaProvider != null) {
				outputSchemaJenaProvider.release();
			}
			
			if (outputDatasetJenaProvider != null) {
				outputDatasetJenaProvider.release();
			}

		}
		
		
		logger.info("END OF PROGRAM");
		
	}

	/**
	 * Loads logger configuration
	 * @throws FactoryConfigurationError
	 */
	private static void loadLoggerConfigration(String loggerConfigFilePath) throws FactoryConfigurationError {
		if (loggerConfigFilePath.endsWith("xml")) {
			DOMConfigurator.configure(loggerConfigFilePath);			
		} else {
			PropertyConfigurator.configure(loggerConfigFilePath);			
		}
	}
	
	/**
	 * Loads configuration document
	 * @throws ConfigurationParserException
	 */
	private static void loadConfiguration(String configFilePath) throws ConfigurationParserException {
		logger.info(String.format("Loading configuration in '%s'", configFilePath));
		ConfigurationDocument configurationDocument = ConfigurationDocument.load(configFilePath);
		ConfigurationDocument.setDefault(configurationDocument);
		logger.info("Loading configuration has been completed successfully");
	}

	/**
	 * Gets Jena-model factory configuration pool  
	 * @return
	 * @throws ConfigurationParserException
	 */
	private static ConfigurationPool<ConfigurationItemEx> getJenaProviderConfigurationPool()
			throws ConfigurationParserException {
		return JenaProviderPoolConfigurationSection.getInstance().getConfigurationPool();
	}
	
	/**
	 * Gets a Jena model factory by name from a pool
	 * @param jenaProviderConfigurationPool
	 * @param jenaProviderName
	 * @return
	 * @throws Exception
	 */
	private static AbstractJenaProvider getJenaProvider(
			ConfigurationPool<ConfigurationItemEx> jenaProviderConfigurationPool,
			String jenaProviderName) throws Exception {
		
		try {
			ConfigurationItemEx configuration = jenaProviderConfigurationPool.getByName(jenaProviderName);			
			return AbstractJenaProvider.getFactory(configuration.getName(), configuration.getType(), configuration.getProperties(), null);
		} catch(InvalidParameterException e) {
			throw new BemException(String.format("Jena model %s is not found", jenaProviderName), e);
		}
		
	}

	
	/**
	 * Imports IFC schema from EXPRESS file
	 * @param inputSchemaFilePath
	 * @return
	 * @throws IOException
	 * @throws BemException 
	 */
	public static List<BemSchema> parseSchemas(String inputSchemaFilePath) throws IOException, BemException {
		logger.info(String.format("Parsing schema from file (or folder): '%s'", inputSchemaFilePath));
		
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
		final List<BemSchema> schemas = BemParserUtil.parseSchemas(inputSchemaFilePath, null, true, true);		
		BemSchemaPool.addAll(schemas);		
		
		logger.info(String.format("Loaded schemas: %s", Arrays.toString(schemas.toArray())));
		logger.info("Parsing schema is compeleted");
		return schemas;
	}

	/**
	 * Imports IFC model from a STEP file
	 * @param inputDatasetFilePath 
	 * @return
	 * @throws IOException
	 * @throws BemException 
	 */
	public static BemDataset parseDataset(String inputDatasetFilePath) throws IOException, BemException {
		logger.info(String.format("Parsing model from file '%s'", inputDatasetFilePath));

		BemParserUtil.registerDatasetParser(new IfcDatasetParser());
		BemDataset dataset = BemParserUtil.parseDataset(inputDatasetFilePath, true);
		
		logger.info("Parsing model is completed");
		return dataset;
	}
	
	/**
	 * Exports schema
	 * @param outputSchemaJenaProvider
	 * @param schema
	 * @param outputSchemaFilePath
	 * @param outputFileFormat
	 * @param gzipOutputFile 
	 * @throws Exception
	 */
	public static Model exportSchema(
			AbstractJenaProvider outputSchemaJenaProvider,
			BemSchema schema,
			Bem2RdfConversionContext conversionContext,
			String outputSchemaFilePath,
			RDFFormat outputFileFormat,
			boolean gzipOutputFile)
			throws Exception {
		// export model to RDF graph
		logger.info("Exporting schema to RDF graph");
		Model schemaGraph = outputSchemaJenaProvider.openDefaultModel();
		if (schemaGraph.supportsTransactions()) {
			schemaGraph.begin();				
		}
		schemaGraph.removeAll();
		
		Bem2RdfSchemaExporter schemaExporter = new Bem2RdfSchemaExporter(schema, conversionContext, schemaGraph);
		schemaExporter.export();
		
		if (schemaGraph.supportsTransactions()) {
			schemaGraph.commit();
		}
		logger.info("Exporting schema RDF graph is completed");
		
		// export model to RDF file
		if (!StringUtils.isEmptyOrNull(outputSchemaFilePath)) {
			String baseUri;
			if (!conversionContext.getConversionParams().ignoreNonBuiltInTypes()) {
				baseUri = schemaExporter.getUriBuilder().buildOntologyUri("");
			} else {
				baseUri = schemaExporter.getUriBuilder().buildBuiltInOntologyUri("");
			}
			
			RdfUtils.exportJenaModelToRdfFile(schemaGraph, baseUri, outputSchemaFilePath, outputFileFormat, gzipOutputFile);
		}
		return schemaGraph;
	}

	/**
	 * Exports {@link BemDataset} to a Jena {@link Model} (and writes it to a file if needed)
	 * @param outputDatasetJenaProvider
	 * @param model
	 * @param contextName
	 * @param outputDatasetFilePath
	 * @param outputFileLanguage
	 * @throws Exception
	 */
	public static Model exportDataset(
			AbstractJenaProvider outputDatasetJenaProvider,
			BemDataset dataset,
			Bem2RdfConversionContext conversionContext,
			String outputDatasetFilePath,
			RDFFormat outputFileFormat,
			boolean gzipOutputFile) throws Exception {
//		// get default grounding rule sets
//		ComplexProcessorConfiguration groundingConfiguration = BemModelAnalyser.getDefaultGroundingRuleSets();
//		
//		// ground nodes in the model
//		BemModelAnalyser modelAnalyser = new BemModelAnalyser(dataset);			
//		modelAnalyser.groundNodes(groundingConfiguration);
		
		// export model to RDF graph
		logger.info("Exporting model to RDF graph");
		Model modelGraph = outputDatasetJenaProvider.openDefaultModel();
		if (modelGraph.supportsTransactions()) {
			logger.info("Enabling RDF graph transactions");
			modelGraph.begin();				
		}
		modelGraph.removeAll();
		
		Bem2RdfDatasetExporter datasetExporter = new Bem2RdfDatasetExporter(dataset, conversionContext, modelGraph);
		datasetExporter.export();
		
		
		if (modelGraph.supportsTransactions()) {
			logger.info("Committing RDF graph transactions");
			modelGraph.commit();
		}
		logger.info("Exporting model to RDF graph is completed");
		
		// export model to RDF file
		if (!StringUtils.isEmptyOrNull(outputDatasetFilePath)) {
			String baseUri = datasetExporter.getUriBuilder().buildDatasetUri("");
			RdfUtils.exportJenaModelToRdfFile(modelGraph, baseUri, outputDatasetFilePath, outputFileFormat, gzipOutputFile);
		}
		
		return modelGraph;
	}
	
//	/**
//	 * Exports IFC model to a Jena model (and writes it to a file if needed)
//	 * @param outputDatasetJenaProvider
//	 * @param model
//	 * @param contextName
//	 * @param outputDatasetFilePath
//	 * @param outputFileLanguage
//	 * @throws Exception
//	 */
//	public static Model exportMetaModel(
//			AbstractJenaProvider outputMetaModelJenaProvider,
//			BemDataset dataset,
//			Bem2RdfConversionContext conversionContext,
//			String outputMetaModelFilePath,
//			RDFFormat outputFileFormat,
//			boolean gzipOutputFile) throws Exception {
//		// get default grounding rule sets
//		ComplexProcessorConfiguration groundingConfiguration = BemModelAnalyser.getDefaultGroundingRuleSets();
//		
//		// ground nodes in the model
//		BemModelAnalyser modelAnalyser = new BemModelAnalyser(dataset);			
//		modelAnalyser.groundNodes(groundingConfiguration);
//		
//		// export model to RDF graph
//		logger.info("Exporting meta model to RDF graph");
//		Model modelGraph = outputMetaModelJenaProvider.openDefaultModel();
//		if (modelGraph.supportsTransactions()) {
//			logger.info("Enabling RDF graph transactions");
//			modelGraph.begin();				
//		}
//		modelGraph.removeAll();
//		Bem2RdfExportUtil.exportMetaModelToJenaModel("http://example.org", modelGraph, dataset, conversionContext);
//		if (modelGraph.supportsTransactions()) {
//			logger.info("Committing RDF graph transactions");
//			modelGraph.commit();
//		}
//		logger.info("Exporting meta model to RDF graph is completed");
//		
//		// export model to RDF file
//		if (!StringUtils.isEmptyOrNull(outputMetaModelFilePath)) {
//			String baseUri = conversionContext.generateModelNamespaceUri(dataset.getSchema().getName());
//			RdfUtils.exportJenaModelToRdfFile(modelGraph, baseUri, outputMetaModelFilePath, outputFileFormat, gzipOutputFile);
//		}
//		
//		return modelGraph;
//	}
//	
//	
//	private void logError(Object message, Throwable t) {
//	logger.error(message, t);
//	Logger.getLogger(t.getClass()).error(message, t);
//}

	
//	protected void testSchema(BemSchema schema) {
//		for (BemEntityTypeInfo entityInfo : schema.getEntityTypeInfos()) {
//			for (BemInverseAttributeInfo inverseAttributeInfo : entityInfo.getInverseAttributeInfos()) {
//				if (inverseAttributeInfo.getCardinality().isSingle() && !inverseAttributeInfo.getCardinality().isOptional()) {
//					BemAttributeInfo outgoingAttributeInfo = inverseAttributeInfo.getOutgoingAttributeInfo();
//					if (outgoingAttributeInfo.getCardinality().isSingle()) {
//						System.out.println(String.format("%s.%s<--%s.%s", inverseAttributeInfo
//								.getDestinationEntityTypeInfo().getName(), inverseAttributeInfo.getName(), outgoingAttributeInfo
//								.getEntityTypeInfo().getName(), outgoingAttributeInfo.getName()));
//					}
//				}
//			}
//		}
//	}


}
