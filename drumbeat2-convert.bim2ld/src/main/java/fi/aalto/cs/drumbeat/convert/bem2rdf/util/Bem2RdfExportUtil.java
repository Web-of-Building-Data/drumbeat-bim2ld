package fi.aalto.cs.drumbeat.convert.bem2rdf.util;


import org.apache.log4j.Logger;

import org.apache.jena.rdf.model.Model;

import fi.aalto.cs.drumbeat.common.config.document.ConfigurationDocument;
import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.convert.bem2rdf.util.config.Bem2RdfConversionContextLoader;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;


/**
 * Contains main entry points for exporting IFC schemas and IFC models to RDF. 
 *
 */
public class Bem2RdfExportUtil {
	
	private static final Logger logger = Logger.getRootLogger();
	
	/**
	 * Exports an IFC schema to Jena dataset using the default IFC-to-RDF conversion context.
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
	 * Exports an IFC schema to Jena dataset using a specified IFC-to-RDF conversion context.
	 * 
	 * @param jenaModel a Jena {@link Model} (the target).
	 * @param schema an {@link BemSchema} (the source).
	 * @param context an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
	 * 
	 * @throws Exception
	 */
	public static void exportSchemaToJenaModel(Model jenaModel, BemSchema schema, Bem2RdfConversionContext context) throws Exception {
		
		logger.info("Exporting schema to Jena");
		try {
			
			if (context == null) {
				context = Bem2RdfConversionContextLoader.loadConversionContext(
						ConfigurationDocument.getDefault(), null);
			}
			
			new Bem2RdfSchemaExporter(schema, context, jenaModel).export();
			
			logger.info("Exporting schema has been completed successfully");
			
		} catch (Exception e) {
			logger.error("Error exporting schema", e);
			throw e;
		}
	}
	
	/**
	 * Exports an IFC schema to Jena dataset using a specified IFC-to-RDF conversion context.
	 * 
	 * @param jenaModel a Jena {@link Model} (the target).
	 * @param schema an {@link BemSchema} (the source).
	 * @param contextName an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
	 * 
	 * @throws Exception
	 */
	public static void exportSchemaToJenaModel(Model jenaModel, BemSchema schema, String contextName) throws Exception {
		Bem2RdfConversionContext context = Bem2RdfConversionContextLoader.loadConversionContext(
				ConfigurationDocument.getDefault(), contextName);
		exportSchemaToJenaModel(jenaModel, schema, context);
	}
	
	
	
	/**
	 * Exports a {@link BemDataset} to Jena dataset using the default IFC-to-RDF conversion context.
	 * 
	 * @param jenaModel a Jena {@link Model} (the target).
	 * @param dataset an {@link BemDataset} (the source).
	 * 
	 * @throws Exception
	 */
	public static void exportDatasetToJenaModel(Model jenaModel, BemDataset dataset) throws Exception {
		exportDatasetToJenaModel(jenaModel, dataset, (Bem2RdfConversionContext)null);
	}


	/**
	 * Exports an IFC schema to Jena dataset using a specified IFC-to-RDF conversion context.
	 * 
	 * @param jenaModel a Jena {@link Model} (the target).
	 * @param dataset an {@link BemDataset} (the source).
	 * @param context an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
	 * 
	 * @throws Exception
	 */
	public static void exportDatasetToJenaModel(Model jenaModel, BemDataset dataset, Bem2RdfConversionContext context) throws Exception {
		if (context == null) {
			context = Bem2RdfConversionContextLoader.loadConversionContext(
					ConfigurationDocument.getDefault(), null);
		}

		logger.info("Exporting dataset to Jena");
		try {
			new Bem2RdfDatasetExporter(dataset, context, jenaModel).export();
			
			logger.info("Exporting dataset has been completed successfully");
			
		} catch (Exception e) {
			logger.error("Error exporting dataset", e);
			throw e;
		}		
	}
	
	/**
	 * Exports an IFC schema to Jena dataset using a specified IFC-to-RDF conversion context.
	 * 
	 * @param jenaModel a Jena {@link Model} (the target).
	 * @param dataset an {@link BemDataset} (the source).
	 * @param contextName an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
	 * 
	 * @throws Exception
	 */
	public static void exportDatasetToJenaModel(Model jenaModel, BemDataset dataset, String contextName) throws Exception {
		Bem2RdfConversionContext context = Bem2RdfConversionContextLoader.loadConversionContext(
				ConfigurationDocument.getDefault(), contextName);
		exportDatasetToJenaModel(jenaModel, dataset, context);
	}
	
	
//
//	/**
//	 * Exports a {@link BemDataset} to Jena dataset using the default IFC-to-RDF conversion context.
//	 * 
//	 * @param jenaModel a Jena {@link Model} (the target).
//	 * @param dataset an {@link BemDataset} (the source).
//	 * 
//	 * @throws Exception
//	 */
//	public static void exportMetaDatasetToJenaModel(String metaDataSetUri, Model jenaModel, BemDataset dataset) throws Exception {
//		exportMetaDatasetToJenaModel(metaDataSetUri, jenaModel, dataset, (Bem2RdfConversionContext)null);
//	}
//
//
//	/**
//	 * Exports an IFC schema to Jena dataset using a specified IFC-to-RDF conversion context.
//	 * 
//	 * @param jenaModel a Jena {@link Model} (the target).
//	 * @param dataset an {@link BemDataset} (the source).
//	 * @param context an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
//	 * 
//	 * @throws Exception
//	 */
//	public static void exportMetaDatasetToJenaModel(String metaDataSetUri, Model jenaModel, BemDataset dataset, Bem2RdfConversionContext context) throws Exception {
//		if (context == null) {
//			context = getDefaultConversionContext();
//		}
//
//		logger.info("Exporting dataset to Jena");
//		try {
//			new Bem2RdfMetaDatasetExporter(metaDataSetUri, dataset, context, jenaModel).export();
//			
//			logger.info("Exporting dataset has been completed successfully");
//			
//		} catch (Exception e) {
//			logger.error("Error exporting dataset", e);
//			throw e;
//		}		
//	}
//	
////	/**
////	 * Exports an IFC schema to Jena dataset using a specified IFC-to-RDF conversion context.
////	 * 
////	 * @param jenaModel a Jena {@link Model} (the target).
////	 * @param dataset an {@link BemDataset} (the source).
////	 * @param contextName an {@link Bem2RdfConversionContext} (the null param indicates to use the default context).  
////	 * 
////	 * @throws Exception
////	 */
////	public static void exportMetaDatasetToJenaModel(String metaDataSetUri, Model jenaModel, BemDataset dataset, String contextName) throws Exception {
////		Bem2RdfConversionContext context = Bem2RdfConversionContextLoader.loadFromDefaultConfigurationFile(contextName);
////		exportMetaDatasetToJenaModel(metaDataSetUri, jenaModel, dataset, context);
////	}
}
