package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;

public class Test_Bem2RdfSchemaExporter_Exporting_ExpressSchema {
	
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

		context.setBuiltInOntologyNamespacePrefixFormat(Bem2RdfTestHelper.BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setBuiltInOntologyNamespaceUriFormat(Bem2RdfTestHelper.BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT);
		
		context.setOntologyNamespacePrefixFormat(Bem2RdfTestHelper.ONTOLOGY_NAMESPACE_PREFIX_FORMAT);		
		context.setOntologyNamespaceUriFormat(Bem2RdfTestHelper.ONTOLOGY_NAMESPACE_URI_FORMAT);
		
		context.getConversionParams().setParamValue(Bem2RdfConversionContextParams.PARAM_IGNORE_NON_BUILT_IN_TYPES, true);
		
		converter = new Bem2RdfConverter(context, bemSchema);
		
		jenaModel = ModelFactory.createDefaultModel();		
		Bem2RdfTestHelper.setNsPrefixes(jenaModel, converter);

		schemaExporter = new Bem2RdfSchemaExporter(bemSchema, context, jenaModel);
	}
	
	private void compareWithExpectedResult(Model actualModel) throws IOException, RdfChecksumException {
		
		boolean readExpectedModel = true;
		boolean writeActualModel = true;
		
		String actualModelFilePath = Bem2RdfTestHelper.getTestFilePath(this, 1, false, "txt");
		String expectedModelFilePath = Bem2RdfTestHelper.getTestFilePath(this, 1, true, "txt");
		
		if (writeActualModel) {
			Bem2RdfTestHelper.writeModel(jenaModel, actualModelFilePath);
		}
		
		
		if (readExpectedModel) {
			Model expectedModel = Bem2RdfTestHelper.readModel(expectedModelFilePath);
			RdfAsserter rdfAsserter = new RdfAsserter(r -> r.isAnon());
			rdfAsserter.assertEquals(expectedModel, actualModel);
		} else {
			throw new NotImplementedException(
					String.format("Reminder: Compare manually files %s and %s", expectedModelFilePath, actualModelFilePath));
		}
	}
	
	@Test
	public void test_export_ExpressSchema() throws IOException, BemException, RdfChecksumException {
		
		Model model = schemaExporter.export();
		compareWithExpectedResult(model);		
		
	}	
	
	

}
