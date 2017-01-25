package fi.aalto.cs.drumbeat.data.step.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemFormatException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemParserException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchemaPool;
import fi.aalto.cs.drumbeat.data.step.StepVocabulary.SpfFormat;
import fi.aalto.cs.drumbeat.data.step.dataset.StepDataset;
import fi.aalto.cs.drumbeat.data.step.dataset.meta.StepMetaDataset;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;

class SpfDatasetInternalParser {
	
	private StepDatasetBuilder builder;
	private InputStream in;
	

	SpfDatasetInternalParser(StepDatasetBuilder builder, InputStream in) {
		this.builder = builder;
		this.in = in;
	}

	BemDataset parse() throws BemParserException {
		try {			

			StepLineReader lineReader = new StepLineReader(in);

			//
			// detect the schema version info and get the schema from the schema
			// pool
			//
			StepMetaDataset metaDataset;

			String statement = lineReader.getNextStatement();
			statement = lineReader.getNextStatement();
			if (statement != null && statement.startsWith(SpfFormat.HEADER)) {
				StringBuilder headerStringBuilder = new StringBuilder(256);
				while ((statement = lineReader.getNextStatement()) != null && !statement.startsWith(SpfFormat.ENDSEC)) {
					headerStringBuilder.append(statement);
					headerStringBuilder.append(StringUtils.SEMICOLON_CHAR);
				}

				StepLineReader headerReader = new StepLineReader(
						new ByteArrayInputStream(headerStringBuilder.toString().getBytes()));
				ExpressSchema stepMetaSchema = getStepMetaSchema();
				List<BemEntity> entities = new SpfDatasetSectionParser().parseEntities(headerReader, builder, stepMetaSchema, true, true);

				metaDataset = new StepMetaDataset(stepMetaSchema);
				metaDataset.addEntities(entities);
			} else {
				throw new BemFormatException(lineReader.getCurrentLineNumber(),
						String.format("Expected '%s'", SpfFormat.HEADER));
			}

			ExpressSchema schema = null;
			List<String> schemaVersions = metaDataset.getFileSchema().getSchemas();
			if (schemaVersions != null) {
				for (String schemaVersion : schemaVersions) {
					schema = (ExpressSchema) BemSchemaPool.getSchema(schemaVersion);
					if (schema != null) {
						break;
					}
				}
			} else {
				throw new BemParserException("Undefined schema version");				
			}

			if (schema == null) {
				throw new BemNotFoundException("None of schemas " + schemaVersions + " is found");
			}

			//
			// create a new dataset
			//
			StepDataset dataset = builder.createDataset(schema);			
			dataset.setMetaDataset(metaDataset);

			//
			// parse entities
			//
			statement = lineReader.getNextStatement();

			if (!statement.equalsIgnoreCase(SpfFormat.DATA)) {
				throw new BemParserException("Expected statement: " + SpfFormat.DATA);
			}

			List<BemEntity> entities = new SpfDatasetSectionParser().parseEntities(lineReader, builder, schema, false, false);
			dataset.addEntities(entities);

			return dataset;

		} catch (BemParserException e) {
			throw e;
		} catch (Exception e) {
			throw new BemParserException(e);
		}
	}

	public static ExpressSchema getStepMetaSchema() throws BemException {
		ExpressSchemaParser schemaParser = new ExpressSchemaParser();
		return schemaParser.parse(new ByteArrayInputStream(SpfFormat.Header.SCHEMA_STRING.getBytes()),
				SpfFormat.FILE_EXTENSION_STP, false);
	}

}
