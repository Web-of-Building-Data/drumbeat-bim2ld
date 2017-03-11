package fi.aalto.cs.drumbeat.convert.bem2rdf;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.common.file.FileManager;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchemaPool;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcSchemaParser;
import fi.aalto.cs.drumbeat.rdf.OwlProfileList;

public class Bem2RdfTestHelper {
	
	public static final String TEST_RESOURCES_PATH ="src/test/resources/"; 
	
	public static final double DOUBLE_DELTA = 1e-15;
	
	public static final String CONFIG_FILE_PATH = "src/test/java/ifc2rdf-config.xml";
	public static final String LOGGER_CONFIG_FILE_PATH = "src/test/java/log4j.xml";
	
	public static final String TEST_SOURCE_RESOURCES_PATH = TEST_RESOURCES_PATH + "source/";

	public static final String TEST_IFC_SCHEMAS_FILE_PATH = TEST_SOURCE_RESOURCES_PATH + "schemas";
	public static final String TEST_IFC_MODEL_FILE_PATH = TEST_SOURCE_RESOURCES_PATH + "models/sample.ifc";
	
	public static final String TEST_TARGET_RESOURCES_PATH = TEST_RESOURCES_PATH + "target/";
	
	public static final String TEST_SCHEMA_NAME = "TestSchema";

	public static final String BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT = "${Ontology.Language}";
	public static final String BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT = "http://drumbeat.cs.hut.fi/owl/${Ontology.Language}#";
	

	public static final String ONTOLOGY_NAMESPACE_PREFIX_FORMAT = "${Ontology.Name}";
	public static final String ONTOLOGY_NAMESPACE_URI_FORMAT = "http://drumbeat.cs.hut.fi/owl/${Ontology.Name}#";
	
	public static final String MODEL_NAMESPACE_PREFIX = "model";
	public static final String MODEL_NAMESPACE_URI_FORMAT = "http://architectural.drb.cs.hut.fi/collection1/model1/";
	
	public static final String MODEL_BLANK_NODE_NAMESPACE_URI_FORMAT = MODEL_NAMESPACE_URI_FORMAT + "B/";
	
	private static boolean initialized = false;
	
	public static void init() throws Exception {
		if (!initialized) {
			initialized = true;
//			DOMConfigurator.configure(LOGGER_CONFIG_FILE_PATH);			
//			ConfigurationDocument.load(CONFIG_FILE_PATH);
//			BemParserUtil.parseSchemas(TEST_IFC_SCHEMAS_FILE_PATH);
			
			BemParserUtil.getSchemaParsers().clear();
			BemParserUtil.registerSchemaParser(new IfcSchemaParser());
			List<BemSchema> schemas = BemParserUtil.parseSchemas(TEST_IFC_SCHEMAS_FILE_PATH, null, true, true);
			for (BemSchema schema : schemas) {
				BemSchemaPool.add(schema);			
			}
		}
	}
	
	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
	 * @return method name
	 */
	public static String getMethodName(final int methodCallShift)
	{
	  StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	  return ste[methodCallShift + 2].getMethodName();
	}
	
	public static String getTestFilePath(Object callingObject, int callingMethodCallShift, boolean isExpected, String extension) {
		return String.format("%s%s/%s/%s.%s",
				TEST_TARGET_RESOURCES_PATH,
				isExpected ? "expected" : "actual",
				callingObject.getClass().getSimpleName(),
				getMethodName(callingMethodCallShift + 1),
				extension);
	}
	
	public static String getExpectedTestFilePath(Object callingObject, int callingMethodCallShift, String extension) {
		return getTestFilePath(callingObject, callingMethodCallShift + 1, true, extension);
	}
	
	public static String getActualTestFilePath(Object callingObject, int callingMethodCallShift, String extension) {
		return getTestFilePath(callingObject, callingMethodCallShift + 1, false, extension);
	}

	public static Model readModel(String filePath) throws IOException {
		FileInputStream in = new FileInputStream(filePath);
		Model model = ModelFactory.createDefaultModel();
		try {
			RDFDataMgr.read(model, in, Lang.TURTLE);
		} finally {
			in.close();
		}
		return model;
	}
	
	public static void writeModel(Model model, String filePath) throws IOException {
		FileOutputStream out = FileManager.createFileOutputStream(filePath);
		RDFDataMgr.write(out, model, RDFFormat.TURTLE_PRETTY);
		out.close();
	}	
	
	public static BemSchema createTestBemSchema() {
		return BemSchemaPool.getSchema(TEST_SCHEMA_NAME);
	}
	
	public static void startTest(Object callingObject, int callingMethodCallShift) {
		System.out.printf("%n[%s::%s()]%n",
				callingObject.getClass().getSimpleName(),
				getMethodName(callingMethodCallShift + 1));		
	}
	
	public static void compareWithExpectedResult(
			Object callingObject, 
			int callingMethodCallShift,
			Model actualModel,
			boolean readExpectedModel,
			boolean writeActualModel,
			OwlProfileList targetOwlProfiles) throws Exception {
		
		String actualModelFilePath = Bem2RdfTestHelper.getTestFilePath(callingObject, callingMethodCallShift + 1, false, "txt");
		String expectedModelFilePath = Bem2RdfTestHelper.getTestFilePath(callingObject, callingMethodCallShift + 1, true, "txt");
		
		if (writeActualModel) {
			System.out.println("Writing Jena model: " + actualModelFilePath);
			Bem2RdfTestHelper.writeModel(actualModel, actualModelFilePath);
			Bem2RdfOwlValidator.validateOwl(actualModelFilePath, targetOwlProfiles);
		}
		
		
		if (readExpectedModel) {
			Model expectedModel = Bem2RdfTestHelper.readModel(expectedModelFilePath);
			RdfAsserter rdfAsserter = new RdfAsserter(r -> r.isAnon());
			rdfAsserter.assertEquals(expectedModel, actualModel);
		} else {
			String reminderMessage = String.format("Reminder: Compare manually files '%s' and '%s'", expectedModelFilePath, actualModelFilePath);
			System.out.println(reminderMessage);
//			throw new NotImplementedException(reminderMessage);
		}
		
	}
	

}
