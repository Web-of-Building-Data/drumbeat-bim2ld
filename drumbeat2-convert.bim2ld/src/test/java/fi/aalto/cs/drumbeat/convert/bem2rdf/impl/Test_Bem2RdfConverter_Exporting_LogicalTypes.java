package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemLogicalValue;
import fi.aalto.cs.drumbeat.data.bem.schema.BemLogicalEnum;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;
import fi.aalto.cs.drumbeat.rdf.OwlProfileEnum;
import fi.aalto.cs.drumbeat.rdf.OwlProfileList;
import fi.aalto.cs.drumbeat.rdf.RdfVocabulary.XSD;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

public class Test_Bem2RdfConverter_Exporting_LogicalTypes {
	
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = true;
	
	private Model jenaModel;
	private Bem2RdfConversionContext context;
	
	private ExpressSchema bemSchema;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Bem2RdfTestHelper.init();		
	}
	

	@Before
	public void setUp() throws Exception {		
		bemSchema = new ExpressSchema(Bem2RdfTestHelper.TEST_SCHEMA_NAME);
		jenaModel = ModelFactory.createDefaultModel();
		
		context = new Bem2RdfConversionContext();
		context.setBuiltInOntologyNamespacePrefixFormat(Bem2RdfTestHelper.BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setBuiltInOntologyNamespaceUriFormat(Bem2RdfTestHelper.BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT);
		context.setOntologyNamespacePrefixFormat(Bem2RdfTestHelper.ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setOntologyNamespaceUriFormat(Bem2RdfTestHelper.ONTOLOGY_NAMESPACE_URI_FORMAT);
	}
	
	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_Full() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_DL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_RL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_EL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_Full() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_DL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_RL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_EL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_Full() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_DL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_RL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_EL() throws Exception {
		test_exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_EL);
	}

	private void test_exportLogical(String convertLogicalTo, OwlProfileEnum... owlProfiles) throws Exception {
		Bem2RdfTestHelper.startTest(this, 1);
		
		context.getConversionParams().setParamValue(
				Bem2RdfConversionContextParams.PARAM_CONVERT_LOGICALS_TO,
				convertLogicalTo);
		
		context.setTargetOwlProfileList(new OwlProfileList(owlProfiles));
		
		Bem2RdfConverterManager converter = new Bem2RdfConverterManager(context, bemSchema);
		
		converter.exportNsPrefixes(jenaModel);
		
		converter.convertTypeInfo(jenaModel, bemSchema.BOOLEAN, true);
		converter.convertTypeInfo(jenaModel, bemSchema.LOGICAL, true);
		
		Bem2RdfTestHelper.compareWithExpectedResult(this, 1, jenaModel, COMPARE_WITH_EXPECTED_DATASETS, true, context.getTargetOwlProfileList());
		
		test_exportingLogicalValues(converter, convertLogicalTo);
	}
	
	private void test_exportingLogicalValues(Bem2RdfConverterManager converter, String convertLogicalTo) {
		for (BemLogicalEnum logicalValue : BemLogicalEnum.values()) {			
			RDFNode logicalNode = converter.convertSimpleValue(jenaModel, new BemLogicalValue(logicalValue), bemSchema.LOGICAL);
			switch (convertLogicalTo) {
			case Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL:
				assertTrue(logicalNode.isURIResource());
				assertEquals(logicalValue.name(), logicalNode.asResource().getLocalName());
				break;
			case Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN:
			case Bem2RdfConversionContextParams.VALUE_XSD_STRING:
			default:
				assertTrue(logicalNode.isLiteral());
				assertEquals(convertLogicalTo, logicalNode.asLiteral().getDatatypeURI().replaceAll(XSD.BASE_URI, XSD.BASE_PREFIX + ":"));
				break;
			}
		}		
	}

	

}
