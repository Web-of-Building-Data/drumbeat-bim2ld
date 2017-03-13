package fi.aalto.cs.drumbeat.convert.bem2rdf.tests;

import static fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams.*;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfDatasetExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfSchemaExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.TestHelper;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Test_Base;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchemaPool;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class Test08_Bem2RdfSchemaExporter_Exporting_IfcDataset_sample extends Test_Base {
	
	public static final boolean WRITE_ACTUAL_DATASETS = true;
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = true;
	public static final Boolean THROW_OWL_VIOLATIONS = true;

	private static BemSchema bemSchema;
	private static BemDataset bemDataset;
	
	private Model jenaModel;
	private Bem2RdfConversionContext context;
	
	@Before
	public void setUp() throws Exception {		
		bemSchema = BemSchemaPool.getSchema("IFC2X3");
		assertNotNull(bemSchema);
		
		bemDataset = TestHelper.loadDataset(TEST_IFC_MODEL_FILE_PATH);
		
		jenaModel = ModelFactory.createDefaultModel();
		
		context = new Bem2RdfConversionContext();
		context.setBuiltInOntologyNamespacePrefixFormat(BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setBuiltInOntologyNamespaceUriFormat(BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT);
		context.setOntologyNamespacePrefixFormat(ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setOntologyNamespaceUriFormat(ONTOLOGY_NAMESPACE_URI_FORMAT);
		
		context.setDatasetNamespacePrefixFormat(DATASET_NAMESPACE_PREFIX);
		context.setDatasetNamespaceUriFormat(DATASET_NAMESPACE_URI_FORMAT);
		
		context.setDatasetBlankNodeNamespacePrefixFormat(DATASET_BLANK_NODE_NAMESPACE_PREFIX_FORMAT);		
		context.setDatasetBlankNodeNamespaceUriFormat(DATASET_BLANK_NODE_NAMESPACE_URI_FORMAT);		
		
		context.getConversionParams().setParamValue(
				PARAM_IGNORE_BUILT_IN_TYPES,
				Boolean.FALSE);
		
		context.getConversionParams().setParamValue(
				PARAM_IGNORE_NON_BUILT_IN_TYPES,
				Boolean.FALSE);
		
		context.getConversionParams().setParamValue(
				PARAM_NAME_ALL_BLANK_NODES,
				Boolean.TRUE);

	}
	
	@Test
	public void test_exportIfcDataset_DrummondList_OWL_2_FULL() throws Exception {
		export_Dataset(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_Full);
	}
	
//	@Test
//	public void test_exportIfcDataset_DrummondList_OWL_2_DL() throws Exception {
//		export_Dataset(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_DL);
//	}
//
//	@Test
//	public void test_exportIfcDataset_DrummondList_OWL_2_RL() throws Exception {
//		export_Dataset(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_RL);
//	}
//
//	@Test
//	public void test_exportIfcDataset_DrummondList_OWL_2_EL() throws Exception {
//		export_Dataset(VALUE_DRUMMOND_LIST, OwlProfileEnum.OWL2_EL);
//	}
//
//	@Test
//	public void test_exportIfcDataset_OloSimilarList_OWL_2_FULL() throws Exception {
//		export_Dataset(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_Full);
//	}
//	
//	@Test
//	public void test_exportIfcDataset_OloSimilarList_OWL_2_DL() throws Exception {
//		export_Dataset(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_DL);
//	}
//
//	@Test
//	public void test_exportIfcDataset_OloSimilarList_OWL_2_RL() throws Exception {
//		export_Dataset(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_RL);
//	}
//
//	@Test
//	public void test_exportIfcDataset_OloSimilarList_OWL_2_EL() throws Exception {
//		export_Dataset(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_EL);
//	}


	private void export_Dataset(String convertCollectionsTo, OwlProfileEnum owlProfileId) throws Exception {
		
		startTest(1);
		
		context.setTargetOwlProfileList(new OwlProfileList(owlProfileId));
		context.getConversionParams().setParamValue(
				PARAM_CONVERT_COLLECTIONS_TO,
				convertCollectionsTo);
		
		Bem2RdfDatasetExporter datasetExporter = new Bem2RdfDatasetExporter(bemDataset, context, jenaModel);
		jenaModel = datasetExporter.export();
		
		writeAndCompareModel(1, jenaModel, WRITE_ACTUAL_DATASETS, COMPARE_WITH_EXPECTED_DATASETS, false);
		
		Bem2RdfSchemaExporter schemaExporter = new Bem2RdfSchemaExporter(bemSchema, context, jenaModel);
		schemaExporter.export();

		TestHelper.validateOwl(jenaModel, owlProfileId, null, THROW_OWL_VIOLATIONS);
	}	
	
	

}
