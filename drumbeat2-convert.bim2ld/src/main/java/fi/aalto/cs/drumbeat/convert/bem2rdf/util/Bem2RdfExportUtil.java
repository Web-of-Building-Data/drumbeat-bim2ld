package fi.aalto.cs.drumbeat.convert.bem2rdf.util;


import org.apache.log4j.Logger;

import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.common.config.document.ConfigurationParserException;
import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;


/**
 * Contains main entry points for exporting IFC schemas and IFC models to RDF. 
 * @author Nam Vu
 *
 */
public class Bem2RdfExportUtil {
	
	private static final Logger logger = Logger.getRootLogger();
	
	private static Bem2RdfConversionContext defaultContext = null;
	
	
	
	/**
	 * Gets the default IFC-to-RDF conversion context loaded from the configuration file.
	 * 
	 * @return the default {@link Bem2RdfConversionContext} object
	 *  
	 * @throws ConfigurationParserException
	 */
	public static Bem2RdfConversionContext getDefaultConversionContext() throws ConfigurationParserException {
//		if (defaultContext == null) {
//			ConfigurationDocument configurationDocument = ConfigurationDocument.getInstance();
//			defaultContext = Bem2RdfConversionContextLoader.loadFromConfigurationDocument(configurationDocument, null); 
//		}
		return defaultContext;
	}
	
	
	
	/**
	 * Exports an IFC schema to Jena model using the default IFC-to-RDF conversion context.
	 * 
	 * @param jenaModel a Jena {@link Model} (the target).
	 * @param schema an {@link BemSchema} (the source).
	 * 
	 * @throws Exception
	 */
	public static void exportSchemaToJenaModel(Model jenaModel, BemSchema schema) throws Exception {
		exportSchemaToJenaModel(jenaModel, schema, (Bem2RdfConversionContext)null);
	}


	/**
	 * Exports an IFC schema to Jena model using a specified IFC-to-RDF conversion context.
	 * 
	 * @param jenaModel a Jena {@link Model} (the target).
	 * @param schema an {@link BemSchema} (the source).
	 * @param context an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
	 * 
	 * @throws Exception
	 */
	public static void exportSchemaToJenaModel(Model jenaModel, BemSchema schema, Bem2RdfConversionContext context) throws Exception {
		
		if (context == null) {
			context = getDefaultConversionContext();
		}
		
		logger.info("Exporting schema to Jena");
		try {
			
			new Bem2RdfSchemaExporter(schema, context, jenaModel).export();
			
			logger.info("Exporting schema has been completed successfully");
			
		} catch (Exception e) {
			logger.error("Error exporting schema", e);
			throw e;
		}
	}
	
////	/**
////	 * Exports an IFC schema to Jena model using a specified IFC-to-RDF conversion context.
////	 * 
////	 * @param jenaModel a Jena {@link Model} (the target).
////	 * @param schema an {@link BemSchema} (the source).
////	 * @param contextName an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
////	 * 
////	 * @throws Exception
////	 */
////	public static void exportSchemaToJenaModel(Model jenaModel, BemSchema schema, String contextName) throws Exception {
////		Bem2RdfConversionContext context = Bem2RdfConversionContextLoader.loadFromConfigurationFile(contextName);
////		exportSchemaToJenaModel(jenaModel, schema, context);
////	}
//	
//	
//	/**
//	 * Exports an IFC model to Jena model using the default IFC-to-RDF conversion context.
//	 * 
//	 * @param jenaModel a Jena {@link Model} (the target).
//	 * @param model an {@link BemDataset} (the source).
//	 * 
//	 * @throws Exception
//	 */
//	public static void exportModelToJenaModel(Model jenaModel, BemDataset model) throws Exception {
//		exportModelToJenaModel(jenaModel, model, (Bem2RdfConversionContext)null);
//	}
//
//
//	/**
//	 * Exports an IFC schema to Jena model using a specified IFC-to-RDF conversion context.
//	 * 
//	 * @param jenaModel a Jena {@link Model} (the target).
//	 * @param model an {@link BemDataset} (the source).
//	 * @param context an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
//	 * 
//	 * @throws Exception
//	 */
//	public static void exportModelToJenaModel(Model jenaModel, BemDataset model, Bem2RdfConversionContext context) throws Exception {
//		if (context == null) {
//			context = getDefaultConversionContext();
//		}
//
//		logger.info("Exporting model to Jena");
//		try {
//			new Bem2RdfModelExporter(model, context, jenaModel).export();
//			
//			logger.info("Exporting model has been completed successfully");
//			
//		} catch (Exception e) {
//			logger.error("Error exporting model", e);
//			throw e;
//		}		
//	}
//	
////	/**
////	 * Exports an IFC schema to Jena model using a specified IFC-to-RDF conversion context.
////	 * 
////	 * @param jenaModel a Jena {@link Model} (the target).
////	 * @param model an {@link BemDataset} (the source).
////	 * @param contextName an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
////	 * 
////	 * @throws Exception
////	 */
////	public static void exportModelToJenaModel(Model jenaModel, BemDataset model, String contextName) throws Exception {
////		Bem2RdfConversionContext context = Bem2RdfConversionContextLoader.loadFromDefaultConfigurationFile(contextName);
////		exportModelToJenaModel(jenaModel, model, context);
////	}
//
//	/**
//	 * Exports an IFC model to Jena model using the default IFC-to-RDF conversion context.
//	 * 
//	 * @param jenaModel a Jena {@link Model} (the target).
//	 * @param model an {@link BemDataset} (the source).
//	 * 
//	 * @throws Exception
//	 */
//	public static void exportMetaModelToJenaModel(String metaDataSetUri, Model jenaModel, BemDataset model) throws Exception {
//		exportMetaModelToJenaModel(metaDataSetUri, jenaModel, model, (Bem2RdfConversionContext)null);
//	}
//
//
//	/**
//	 * Exports an IFC schema to Jena model using a specified IFC-to-RDF conversion context.
//	 * 
//	 * @param jenaModel a Jena {@link Model} (the target).
//	 * @param model an {@link BemDataset} (the source).
//	 * @param context an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
//	 * 
//	 * @throws Exception
//	 */
//	public static void exportMetaModelToJenaModel(String metaDataSetUri, Model jenaModel, BemDataset model, Bem2RdfConversionContext context) throws Exception {
//		if (context == null) {
//			context = getDefaultConversionContext();
//		}
//
//		logger.info("Exporting model to Jena");
//		try {
//			new Bem2RdfMetaModelExporter(metaDataSetUri, model, context, jenaModel).export();
//			
//			logger.info("Exporting model has been completed successfully");
//			
//		} catch (Exception e) {
//			logger.error("Error exporting model", e);
//			throw e;
//		}		
//	}
//	
////	/**
////	 * Exports an IFC schema to Jena model using a specified IFC-to-RDF conversion context.
////	 * 
////	 * @param jenaModel a Jena {@link Model} (the target).
////	 * @param model an {@link BemDataset} (the source).
////	 * @param contextName an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
////	 * 
////	 * @throws Exception
////	 */
////	public static void exportMetaModelToJenaModel(String metaDataSetUri, Model jenaModel, BemDataset model, String contextName) throws Exception {
////		Bem2RdfConversionContext context = Bem2RdfConversionContextLoader.loadFromDefaultConfigurationFile(contextName);
////		exportMetaModelToJenaModel(metaDataSetUri, jenaModel, model, context);
////	}
}
