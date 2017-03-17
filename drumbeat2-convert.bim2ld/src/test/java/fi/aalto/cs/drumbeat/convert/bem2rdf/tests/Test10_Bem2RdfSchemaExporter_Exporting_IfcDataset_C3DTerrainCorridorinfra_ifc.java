package fi.aalto.cs.drumbeat.convert.bem2rdf.tests;

import static fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams.*;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfDatasetExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.TestHelper;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Test_Base;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class Test10_Bem2RdfSchemaExporter_Exporting_IfcDataset_C3DTerrainCorridorinfra_ifc extends Test_Base {
	
	public static final boolean WRITE_ACTUAL_DATASETS = true;
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = true;
	public static final Boolean THROW_OWL_VIOLATIONS = true;

	private static BemSchema bemSchema;
	private static BemDataset bemDataset;
	
	private Model jenaModel;
	private Bem2RdfConversionContext context;
	
	@Before
	public void setUp() throws Exception {
		try {
			bemDataset = TestHelper.loadDataset("c:\\DRUM\\!git_local\\DRUM\\Test\\Models\\KICT\\C3D-Terrain-Corridor-infra.ifc");
		} catch (FileNotFoundException e) {
			return;
		}
		
		assertNotNull(bemDataset);
		
		bemSchema = bemDataset.getSchema();
		assertNotNull(bemSchema);
		
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
	public void test_exportIfcDataset_OloSimilarList_OWL_2_FULL() throws Exception {
		export_Dataset(VALUE_OLO_SIMILAR_LIST, OwlProfileEnum.OWL2_Full);
	}	

	private void export_Dataset(String convertCollectionsTo, OwlProfileEnum owlProfileId) throws Exception {
		
		startTest(1);
		
		if (bemDataset != null) {
		
			context.setLimitingOwlProfileList(new OwlProfileList(owlProfileId));
			context.getConversionParams().setParamValue(
					PARAM_CONVERT_COLLECTIONS_TO,
					convertCollectionsTo);
			
			System.out.printf("[%s] Exporting dataset (%d entities) to Jena model", Calendar.getInstance(), bemDataset.getAllEntities().size());
			
			Bem2RdfDatasetExporter datasetExporter = new Bem2RdfDatasetExporter(bemDataset, context, jenaModel);
			jenaModel = datasetExporter.export();
			
			System.out.printf("[%s] Finnished exporting model: %d", Calendar.getInstance(), jenaModel.size());
			
//			writeAndCompareModel(1, jenaModel, WRITE_ACTUAL_DATASETS, COMPARE_WITH_EXPECTED_DATASETS, false);
//			
//			Bem2RdfSchemaExporter schemaExporter = new Bem2RdfSchemaExporter(bemSchema, context, jenaModel);
//			schemaExporter.export();
//	
//			TestHelper.validateOwl(jenaModel, owlProfileId, null, THROW_OWL_VIOLATIONS);
			
		} else {
			
			System.err.println("Test case is skipped: Dataset is empty");
			
		}
	}	
	
	

}
