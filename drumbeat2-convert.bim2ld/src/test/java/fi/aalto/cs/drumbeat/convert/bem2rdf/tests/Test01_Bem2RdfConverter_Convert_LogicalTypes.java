package fi.aalto.cs.drumbeat.convert.bem2rdf.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.profiles.violations.UseOfUndeclaredDataProperty;

import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.convert.bem2rdf.impl.Bem2RdfConverterManager;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemLogicalValue;
import fi.aalto.cs.drumbeat.data.bem.schema.BemLogicalEnum;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary.XSD;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

public class Test01_Bem2RdfConverter_Convert_LogicalTypes extends Test_Base {
	
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = true;
	public static final boolean WRITE_ACTUAL_DATASETS = true;
	public static final Boolean THROW_OWL_VIOLATIONS = true;
	
	private Model jenaModel;
	private Bem2RdfConversionContext context;
	
	private ExpressSchema bemSchema;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		init();		
	}
	

	@Before
	public void setUp() throws Exception {		
		bemSchema = new ExpressSchema(TEST_SCHEMA_NAME);
		jenaModel = ModelFactory.createDefaultModel();
		
		context = new Bem2RdfConversionContext();
		context.setBuiltInOntologyNamespacePrefixFormat(BUILT_IN_ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setBuiltInOntologyNamespaceUriFormat(BUILT_IN_ONTOLOGY_NAMESPACE_URI_FORMAT);
		context.setOntologyNamespacePrefixFormat(ONTOLOGY_NAMESPACE_PREFIX_FORMAT);
		context.setOntologyNamespaceUriFormat(ONTOLOGY_NAMESPACE_URI_FORMAT);
	}
	
	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_Full() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_DL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_RL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingLogical_As_NamedIndividual_OWL2_EL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_Full() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_DL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_RL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingLogical_As_XsdBoolean_OWL2_EL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_Full() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_DL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_RL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingLogical_As_XsdString_OWL2_EL() throws Exception {
		exportLogical(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_EL);
	}

	private void exportLogical(String convertLogicalTo, OwlProfileEnum owlProfileId) throws Exception {
		startTest(1);
		
		context.getConversionParams().setParamValue(
				Bem2RdfConversionContextParams.PARAM_CONVERT_LOGICALS_TO,
				convertLogicalTo);
		
		context.setTargetOwlProfileList(new OwlProfileList(owlProfileId));
		
		Bem2RdfConverterManager converter = new Bem2RdfConverterManager(context, bemSchema);
		
		converter.exportNsPrefixes(jenaModel);
		
		converter.convertTypeInfo(jenaModel, bemSchema.BOOLEAN, true);
		converter.convertTypeInfo(jenaModel, bemSchema.LOGICAL, true);
		
		boolean validateOwl = true;		
		byte[] ontologyBuffer = writeAndCompareModel(1, jenaModel, WRITE_ACTUAL_DATASETS, COMPARE_WITH_EXPECTED_DATASETS, validateOwl);
		if (validateOwl) {
			assertNotNull(ontologyBuffer);
			TestHelper.validateOwl(ontologyBuffer, owlProfileId, Arrays.asList(UseOfUndeclaredDataProperty.class), THROW_OWL_VIOLATIONS);
		}
		
		exportLogicalValues(converter, convertLogicalTo);
	}
	
	private void exportLogicalValues(Bem2RdfConverterManager converter, String convertLogicalTo) {
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
