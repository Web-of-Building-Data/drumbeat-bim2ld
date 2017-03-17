package fi.aalto.cs.drumbeat.convert.bem2rdf.tests;

import static fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams.*;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfSchemaExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.TestHelper;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Test_Base;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchemaPool;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class Test09_Bem2RdfSchemaExporter_Exporting_IfcSchema_IFC4 extends Test_Base {
	
	public static final boolean WRITE_ACTUAL_DATASETS = true;
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = true;
	public static final Boolean THROW_OWL_VIOLATIONS = true;

	private static BemSchema bemSchema;
	private Model jenaModel;
	private Bem2RdfConversionContext context;
	
	@Before
	public void setUp() throws Exception {		
		bemSchema = BemSchemaPool.getSchema("IFC4_ADD1");
		assertNotNull(bemSchema);
		
		jenaModel = ModelFactory.createDefaultModel();
		
		context = new Bem2RdfConversionContext();
		context.setBuiltInOntologyNamespacePrefixFormat(BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setBuiltInOntologyNamespaceUriFormat(BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT);
		context.setOntologyNamespacePrefixFormat(ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setOntologyNamespaceUriFormat(ONTOLOGY_NAMESPACE_URI_FORMAT);
		
		context.getConversionParams().setParamValue(
				PARAM_IGNORE_BUILT_IN_TYPES,
				Boolean.FALSE);

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
		
		context.setLimitingOwlProfileList(new OwlProfileList(owlProfileId));
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
