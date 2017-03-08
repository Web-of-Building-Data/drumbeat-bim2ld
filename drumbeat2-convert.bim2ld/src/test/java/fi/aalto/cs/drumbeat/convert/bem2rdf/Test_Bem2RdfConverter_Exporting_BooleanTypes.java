package fi.aalto.cs.drumbeat.convert.bem2rdf;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.XSD;

public class Test_Bem2RdfConverter_Exporting_BooleanTypes {
	
	private static final String TEST_SCHEMA_VERSION = "BemTest"; 
	private Model jenaModel;
	
	private BemSchema bemSchema;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Bem2RdfTestHelper.init();		
	}
	

	@Before
	public void setUp() throws Exception {		
		bemSchema = new BemSchema(TEST_SCHEMA_VERSION);
		jenaModel = ModelFactory.createDefaultModel();
	}

	@Test
	public void test_Getting_XsdTypeOfBoolean_As_NamedIndividual() throws Bem2RdfConverterConfigurationException {
		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
		context.getConversionParams().setParamValue(
				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
				Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL);
		
		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
		Resource xsdDataType = converter.getBaseTypeForBooleans();
		assertEquals(OWL2.NamedIndividual, xsdDataType);
		
		BemPrimitiveValue trueValue = new BemPrimitiveValue(BemLogicalEnum.TRUE, BemValueKindEnum.LOGICAL);
		
		RDFNode node = converter.convertPrimitiveValue(trueValue, jenaModel);		
		assertNotNull(node);
		assertTrue(node.isURIResource());
		assertEquals(Bem2RdfVocabulary.EXPRESS.getBaseUri() + "true", node.asResource().getURI());
		
	}

	@Test
	public void test_Getting_XsdTypeOfBoolean_As_XsdBoolean() throws Bem2RdfConverterConfigurationException {
		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
		context.getConversionParams().setParamValue(
				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
				Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN);
		
		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
		Resource xsdDataType = converter.getBaseTypeForBooleans();
		assertEquals(XSD.xboolean, xsdDataType);
	}

	@Test
	public void test_Getting_XsdTypeOfBoolean_As_XsdString() throws Bem2RdfConverterConfigurationException {
		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
		context.getConversionParams().setParamValue(
				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
				Bem2RdfConversionContextParams.VALUE_XSD_STRING);
		
		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
		Resource xsdDataType = converter.getBaseTypeForBooleans();
		assertEquals(XSD.xstring, xsdDataType);
	}
	
}
