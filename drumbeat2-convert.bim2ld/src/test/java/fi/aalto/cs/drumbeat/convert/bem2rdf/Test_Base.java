package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.util.List;
import java.util.function.Function;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;

import fi.aalto.cs.drumbeat.common.config.document.ConfigurationDocument;
import fi.aalto.cs.drumbeat.common.meta.MetaClassUtils;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchemaPool;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcSchemaParser;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool;

public class Test_Base {

	public static final boolean ALL_TESTS_WRITE_ACTUAL_DATASETS = true;
	public static final boolean ALL_TESTS_COMPARE_WITH_EXPECTED_DATASETS = true;

	public static final String TEST_RESOURCES_FOLDER_PATH ="src/test/resources/"; 
	public static final String TEST_CONFIG_FOLDER_PATH = TEST_RESOURCES_FOLDER_PATH + "config/"; 
	
	public static final double DOUBLE_DELTA = 1e-15;
	
	public static final String CONFIG_FILE_PATH = TEST_CONFIG_FOLDER_PATH + "ifc2ld.xml";
	public static final String LOGGER_CONFIG_FILE_PATH = TEST_CONFIG_FOLDER_PATH + "log4j.xml";
	
	public static final String TEST_SOURCE_RESOURCES_PATH = TEST_RESOURCES_FOLDER_PATH + "source/";

	public static final String TEST_IFC_SCHEMAS_FILE_PATH = TEST_SOURCE_RESOURCES_PATH + "schemas";
	public static final String TEST_IFC_MODEL_FILE_PATH = TEST_SOURCE_RESOURCES_PATH + "models/sample.ifc";
	
	public static final String TEST_TARGET_RESOURCES_PATH = TEST_RESOURCES_FOLDER_PATH + "target/";
	
	public static final String TEST_SCHEMA_NAME = "TestSchema";

	public static final String BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT = "${Ontology.Language}";
	public static final String BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT = "http://drumbeat.cs.hut.fi/owl/${Ontology.Language}#";
	

	public static final String ONTOLOGY_NAMESPACE_PREFIX_FORMAT = "${Ontology.Name}";
	public static final String ONTOLOGY_NAMESPACE_URI_FORMAT = "http://drumbeat.cs.hut.fi/owl/${Ontology.Name}#";
	
	public static final String DATASET_NAMESPACE_PREFIX = "model";
	public static final String DATASET_NAMESPACE_URI_FORMAT = "http://architectural.drb.cs.hut.fi/collection1/model1/";
	
	public static final String DATASET_BLANK_NODE_NAMESPACE_PREFIX_FORMAT = "model_b";
	public static final String DATASET_BLANK_NODE_NAMESPACE_URI_FORMAT = DATASET_NAMESPACE_URI_FORMAT + "B/";
	
	private static boolean initialized = false;
	
	@BeforeClass
	public synchronized static void init() throws Exception {
		if (!initialized) {
			initialized = true;
			DOMConfigurator.configure(LOGGER_CONFIG_FILE_PATH);			
			ConfigurationDocument.load(CONFIG_FILE_PATH);
				
			BemParserUtil.getSchemaParsers().clear();
			BemParserUtil.registerSchemaParser(new IfcSchemaParser());
			List<BemSchema> schemas = BemParserUtil.parseSchemas(TEST_IFC_SCHEMAS_FILE_PATH, null, true, true);
			if (!schemas.isEmpty()) {
				for (BemSchema schema : schemas) {
					System.out.println("Adding bemSchema: " + schema.getName());
					BemSchemaPool.add(schema);			
				}
			} else {
				throw new IllegalArgumentException("No schemas found in: " + TEST_IFC_SCHEMAS_FILE_PATH);
			}
		}
	}
	
	public static String getTestFilePath(boolean isExpected, String className, String methodName, String extension) {
		return String.format("%s%s/%s/%s.%s",
				TEST_TARGET_RESOURCES_PATH,
				isExpected ? "expected" : "actual",
				className,
				methodName,
				extension);
	}

	private String getTestFilePath(int callingMethodCallShift, boolean isExpected, String extension) {
		return getTestFilePath(isExpected, getClass().getSimpleName(), MetaClassUtils.getCallingMethodName(callingMethodCallShift + 1), extension);
	}
	
	protected String getExpectedTestFilePath(int callingMethodCallShift, String extension) {
		return getTestFilePath(callingMethodCallShift + 1, true, extension);
	}
	
	protected String getActualTestFilePath(int callingMethodCallShift, String extension) {
		return getTestFilePath(callingMethodCallShift + 1, false, extension);
	}

//	public static BemSchema createTestBemSchema() {
//		return BemSchemaPool.getSchema(TEST_SCHEMA_NAME);
//	}
	
	protected void startTest(int callingMethodCallShift) {
		System.out.printf("%n[%s::%s()]%n",
				getClass().getSimpleName(),
				MetaClassUtils.getCallingMethodName(callingMethodCallShift + 1));		
	}
	
	protected byte[] writeAndCompareModel(
			int callingMethodCallShift,
			Model actualModel,
			boolean writeActualModel,
			boolean compareWithExpectedModel,
			boolean createBuffer) throws Exception
	{
		
		writeActualModel &= ALL_TESTS_WRITE_ACTUAL_DATASETS;
		compareWithExpectedModel &= ALL_TESTS_COMPARE_WITH_EXPECTED_DATASETS;

		String actualModelFilePath = getActualTestFilePath(callingMethodCallShift + 1, "ttl");
		String expectedModelFilePath = getExpectedTestFilePath(callingMethodCallShift + 1, "ttl");
		
		byte[] buffer = null;
		
		if (writeActualModel) {
			System.out.printf("Writing Jena model: %s (%,d triples)%n", actualModelFilePath, actualModel.size());
			buffer = TestHelper.writeJenaModel(actualModel, actualModelFilePath, createBuffer);
		}
		
		
		if (compareWithExpectedModel) {
			Model expectedModel = TestHelper.readJenaModel(expectedModelFilePath);
			Function<Resource, Boolean> localResourceChecker = r -> r.isAnon() || r.getURI().startsWith(DATASET_BLANK_NODE_NAMESPACE_URI_FORMAT);
			RdfComparatorPool comparatorPool = new RdfComparatorPool(localResourceChecker);
			
			RdfModelComparator rdfModelComparator = new RdfModelComparator(comparatorPool);
			int result = rdfModelComparator.compare(expectedModel, actualModel);
			
			if (result != 0 && rdfModelComparator.getLastAssertionDifferences() != null) {
			
				TestHelper.printRdfMsgContainer(
						expectedModel,
						rdfModelComparator.getLastExpectedMsgContainer(),
						comparatorPool,
						expectedModelFilePath.replaceAll("ttl", "ttl.msg"));
				
				TestHelper.printRdfMsgContainer(
						actualModel,
						rdfModelComparator.getLastActualMsgContainer(),
						comparatorPool,
						actualModelFilePath.replaceAll("ttl", "ttl.msg"));
				
			}
			
			Assert.assertTrue("Actual and expected models are different", result == 0);
			
		} else {
			String reminderMessage = String.format("Reminder: To compare files '%s' and '%s'", expectedModelFilePath, actualModelFilePath);
			System.err.println(reminderMessage);
//			throw new NotImplementedException(reminderMessage);
		}
		
		return buffer;
	}
	

}
