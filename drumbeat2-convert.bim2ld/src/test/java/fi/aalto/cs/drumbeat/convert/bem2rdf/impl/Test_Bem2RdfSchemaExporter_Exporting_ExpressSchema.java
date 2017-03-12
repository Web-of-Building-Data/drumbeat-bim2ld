package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.profiles.violations.UseOfUndeclaredDataProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContextParams;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfSchemaExporter;
import fi.aalto.cs.drumbeat.convert.bem2rdf.TestHelper;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Test_Base;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class Test_Bem2RdfSchemaExporter_Exporting_ExpressSchema extends Test_Base {
	
	public static final boolean WRITE_ACTUAL_DATASETS = true;
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = true;

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
				Bem2RdfConversionContextParams.PARAM_IGNORE_BUILT_IN_TYPES,
				Boolean.FALSE);

	}
	
	@Test
	public void test_exportExpressSchema_OWL_2_FULL() throws Exception {
		export_ExpressSchema(OwlProfileEnum.OWL2_Full);
	}
	
	private void export_ExpressSchema(OwlProfileEnum owlProfileId) throws Exception {
		
		startTest(1);
		
		context.setTargetOwlProfileList(new OwlProfileList(owlProfileId));
		
		Bem2RdfSchemaExporter schemaExporter = new Bem2RdfSchemaExporter(bemSchema, context, jenaModel);
		jenaModel = schemaExporter.export();
		
		StringBuffer modelStringBuffer = writeAndCompareModel(1, jenaModel, WRITE_ACTUAL_DATASETS, COMPARE_WITH_EXPECTED_DATASETS);
		if (modelStringBuffer != null) {
			TestHelper.validateOwl(modelStringBuffer, owlProfileId, Arrays.asList(UseOfUndeclaredDataProperty.class));
		}
		
	}	
	
	

}
