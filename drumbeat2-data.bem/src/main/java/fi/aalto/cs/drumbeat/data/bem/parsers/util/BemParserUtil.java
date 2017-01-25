package fi.aalto.cs.drumbeat.data.bem.parsers.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.parsers.*;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class BemParserUtil {
	
	private static final Logger logger = Logger.getLogger(BemParserUtil.class);	
	private static final List<BemSchemaParser> schemaParsers = new ArrayList<>();
	private static final List<BemDatasetParser> datasetParsers = new ArrayList<>();
	
	
	public static List<BemSchemaParser> getSchemaParsers() {
		return schemaParsers;
	}	

	public static void registerSchemaParser(BemSchemaParser parser) {
		schemaParsers.add(parser);
	}
	
	public static void unregisterSchemaParser(BemSchemaParser parser) {
		schemaParsers.remove(parser);
	}
	
	public static List<BemSchema> parseSchemas(String filePath, FilenameFilter filter, boolean checkFileType, boolean skipWrongFileTypes) throws IOException, BemException {
		File file = new File(filePath);
		
		final List<BemSchema> schemas = new ArrayList<>();
		
		if (file.isDirectory()) {
			logger.info(String.format("Parsing schemas in folder '%s'", filePath));
			
			for (File schemaFile: file.listFiles(filter)) {
				try {
					BemSchema schema = parseSchema(schemaFile.getPath(), checkFileType);
					schemas.add(schema);
				} catch (BemUnsupportedDataTypeException e) {
					if (!skipWrongFileTypes) {
						throw e;
					}
				}
			}
			
			logger.info(String.format("Parsing schemas in folder '%s' has been completed successfully", filePath));
			
		} else {
			final BemSchema schema = parseSchema(filePath, checkFileType);
			schemas.add(schema);
		}
		return schemas;
	}
	
	public static BemSchema parseSchema(String filePath, boolean checkFileType) throws IOException, BemException {
		logger.info(String.format("Parsing schema '%s' started", filePath));
		
		FileInputStream input = new FileInputStream(filePath);
		String fileExtension = FilenameUtils.getExtension(filePath);
		
		for (BemSchemaParser schemaParser : schemaParsers) {
			try {
				BemSchema schema = schemaParser.parse(input, fileExtension, checkFileType);
				logger.info(String.format("Parsing schema '%s' has been completed successfully", filePath));
				return schema;
			} catch (BemUnsupportedDataTypeException e) {				
			}
		}
		
		throw new BemUnsupportedDataTypeException(filePath);
	}	
	
	public static List<BemDatasetParser> getDatasetParsers() {
		return datasetParsers;
	}	

	public static void registerDatasetParser(BemDatasetParser parser) {
		datasetParsers.add(parser);
	}
	
	public static void unregisterDatasetParser(BemDatasetParser parser) {
		datasetParsers.remove(parser);
	}
	
	public static List<BemDataset> parseDatasets(String filePath, FilenameFilter filter, boolean checkFileType, boolean skipWrongFileTypes) throws IOException, BemException {
		File file = new File(filePath);
		
		final List<BemDataset> datasets = new ArrayList<>();
		
		if (file.isDirectory()) {
			logger.info(String.format("Parsing datasets in folder '%s'", filePath));
			
			for (String datasetFilePath : file.list(filter)) {
				try {
					BemDataset dataset = parseDataset(datasetFilePath, checkFileType);
					datasets.add(dataset);
				} catch (BemUnsupportedDataTypeException e) {
					if (!skipWrongFileTypes) {
						throw e;
					}
				}
			}
			
			logger.info(String.format("Parsing datasets in folder '%s' has been completed successfully", filePath));
			
		} else {
			final BemDataset dataset = parseDataset(filePath, checkFileType);
			datasets.add(dataset);
		}
		return datasets;
	}
	
	public static BemDataset parseDataset(String filePath, boolean checkFileType) throws IOException, BemException {
		logger.info(String.format("Parsing dataset '%s' started", filePath));
		
		FileInputStream input = new FileInputStream(filePath);
		String fileExtension = FilenameUtils.getExtension(filePath);
		
		for (BemDatasetParser datasetParser : datasetParsers) {
			try {
				BemDataset dataset = datasetParser.parse(input, fileExtension, checkFileType);
				logger.info(String.format("Parsing dataset '%s' has been completed successfully", filePath));
				return dataset;
			} catch (BemUnsupportedDataTypeException e) {				
			}
		}
		
		throw new BemUnsupportedDataTypeException(filePath);
	}	
	
	
}
