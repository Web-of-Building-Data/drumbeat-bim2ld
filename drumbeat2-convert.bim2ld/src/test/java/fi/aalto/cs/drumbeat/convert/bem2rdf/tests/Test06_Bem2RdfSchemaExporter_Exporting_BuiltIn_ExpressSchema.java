package fi.aalto.cs.drumbeat.convert.bem2rdf.tests;

import static fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams.*;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfSchemaExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.TestHelper;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Test_Base;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class Test06_Bem2RdfSchemaExporter_Exporting_BuiltIn_ExpressSchema extends Test_Base {
	
	public static final boolean WRITE_ACTUAL_DATASETS = true;
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = true;
	public static final Boolean THROW_OWL_VIOLATIONS = true;

	private static BemSchema bemSchema;
	private Model jenaModel;
	private Bem2RdfConversionContext context;
	
	@Before
	public void setUp() throws Exception {		
		bemSchema = new ExpressSchema(TEST_SCHEMA_NAME);
		jenaModel = ModelFactory.createDefaultModel();
		
		context = new Bem2RdfConversionContext();
		context.setBuiltInOntologyNamespacePrefixFormat(BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setBuiltInOntologyNamespaceUriFormat(BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT);
//		context.setOntologyNamespacePrefixFormat(ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
//		context.setOntologyNamespaceUriFormat(ONTOLOGY_NAMESPACE_URI_FORMAT);
		
		context.getConversionParams().setParamValue(
				PARAM_IGNORE_BUILT_IN_TYPES,
				Boolean.FALSE);

		context.getConversionParams().setParamValue(
				PARAM_IGNORE_NON_BUILT_IN_TYPES,
				Boolean.TRUE);

	}
	
	@Test
	public void test_exportExpressSchema_DrummondList_OWL_2_FULL() throws Exception {
		export_ExpressSchema(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_Full);
	}
	
	@Test
	public void test_exportExpressSchema_DrummondList_OWL_2_DL() throws Exception {
		export_ExpressSchema(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportExpressSchema_DrummondList_OWL_2_RL() throws Exception {
		export_ExpressSchema(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportExpressSchema_DrummondList_OWL_2_EL() throws Exception {
		export_ExpressSchema(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportExpressSchema_OloSimilarList_OWL_2_FULL() throws Exception {
		export_ExpressSchema(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_Full);
	}
	
	@Test
	public void test_exportExpressSchema_OloSimilarList_OWL_2_DL() throws Exception {
		export_ExpressSchema(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportExpressSchema_OloSimilarList_OWL_2_RL() throws Exception {
		export_ExpressSchema(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportExpressSchema_OloSimilarList_OWL_2_EL() throws Exception {
		export_ExpressSchema(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_EL);
	}


	private void export_ExpressSchema(String convertCollectionsTo, OwlProfileEnum owlProfileId) throws Exception {
		
		startTest(1);
		
		context.setTargetOwlProfileList(new OwlProfileList(owlProfileId));
		context.getConversionParams().setParamValue(
				PARAM_CONVERT_COLLECTIONS_TO,
				convertCollectionsTo);
		
		Bem2RdfSchemaExporter schemaExporter = new Bem2RdfSchemaExporter(bemSchema, context, jenaModel);
		jenaModel = schemaExporter.export();
		
		boolean validateOwl = true;		
		byte[] ontologyBuffer = writeAndCompareModel(1, jenaModel, WRITE_ACTUAL_DATASETS, COMPARE_WITH_EXPECTED_DATASETS, validateOwl);
		if (validateOwl) {
			assertNotNull(ontologyBuffer);
			TestHelper.validateOwl(ontologyBuffer, owlProfileId, null, THROW_OWL_VIOLATIONS);
		}
		
	}	
	
	

}
