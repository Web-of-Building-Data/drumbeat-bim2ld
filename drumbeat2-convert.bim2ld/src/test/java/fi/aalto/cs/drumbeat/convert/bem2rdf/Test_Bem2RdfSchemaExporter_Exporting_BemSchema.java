package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.convert.bem2rdf.RdfAsserter.ModelAsserter;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public class Test_Bem2RdfSchemaExporter_Exporting_BemSchema {
	
	private static BemSchema bemSchema;
	private Model jenaModel;
	private Bem2RdfConversionContext context;
	private Bem2RdfConverter converter;
	private Bem2RdfSchemaExporter schemaExporter;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Bem2RdfTestHelper.init();
		bemSchema = Bem2RdfTestHelper.getTestBemSchema();
	}
	

	@Before
	public void setUp() throws Exception {		
		context = new Bem2RdfConversionContext();
		context.getConversionParams().setParamValue(Bem2RdfConversionContextParams.PARAM_IGNORE_BUILT_IN_TYPES, true);
		context.getConversionParams().setParamValue(Bem2RdfConversionContextParams.PARAM_IGNORE_NON_BUILT_IN_TYPES, false);
		converter = new Bem2RdfConverter(context, bemSchema);
		
		jenaModel = ModelFactory.createDefaultModel();		
		Bem2RdfTestHelper.setNsPrefixes(jenaModel, converter);

		schemaExporter = new Bem2RdfSchemaExporter(bemSchema, context, jenaModel);
	}
	
	private void compareWithExpectedResult(Model actualModel) throws IOException {
		
		boolean readExpectedModel = true;
		if (readExpectedModel) {
			String testFilePath = Bem2RdfTestHelper.getTestFilePath(this, 1, true, "txt");
			Model expectedModel = Bem2RdfTestHelper.readModel(testFilePath);

			ModelAsserter asserter = new ModelAsserter();
			asserter.assertEquals(expectedModel, actualModel);
		} else {
			String actualFilePath = Bem2RdfTestHelper.getTestFilePath(this, 1, false, "txt");
			Bem2RdfTestHelper.writeModel(jenaModel, actualFilePath);			
			throw new NotImplementedException("TODO: Compare with expected result");
		}
	}
	
	

	@Test
	public void test_export_ExpressSchema() throws IOException, BemException {
		
		Model model = schemaExporter.export();
		compareWithExpectedResult(model);		
		
	}	
	
	

}
