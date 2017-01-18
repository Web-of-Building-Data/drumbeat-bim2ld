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
import fi.aalto.cs.drumbeat.data.bem.parsers.*;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

public class BemParserUtil {
	
	private static final Logger logger = Logger.getLogger(BemParserUtil.class);	
	private static final List<BemSchemaParser> schemaParsers = new ArrayList<>();
	
	
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
			
			for (String schemaFilePath : file.list(filter)) {
				try {
					BemSchema schema = parseSchema(schemaFilePath, checkFileType);
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

//	public static BemModel parseModel(String filePath) throws IOException, BemParserException {
//		logger.info(String.format("Parsing model in '%s'", filePath));
//		BemModel model = BemModelParser.parse(filePath);
//		logger.info("Parsing model has been completed successfully");
//		return model;
//	}
	

}
