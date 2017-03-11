package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.schema.BemValueKindEnum;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;
import fi.aalto.cs.drumbeat.rdf.OwlProfileEnum;
import fi.aalto.cs.drumbeat.rdf.OwlProfileList;
import fi.aalto.cs.drumbeat.rdf.RdfVocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

public class Test_Bem2RdfConverter_Exporting_DoubleTypes {
	
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = false;
	
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
	public void test_exportingDouble_As_AutoMostSupported_OWL2_Full() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingDouble_As_AutoMostSupported_OWL2_DL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostSupported_OWL2_RL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostSupported_OWL2_EL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_Full() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_DL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_RL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_EL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_Full() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_DL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_RL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_EL() throws Exception {
		test_exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_EL);
	}

	private void test_exportDouble(String convertDoubleTo, OwlProfileEnum... owlProfiles) throws Exception {
		Bem2RdfTestHelper.startTest(this, 1);
		
		context.getConversionParams().setParamValue(
				Bem2RdfConversionContextParams.PARAM_CONVERT_DOUBLES_TO,
				convertDoubleTo);
		
		context.setTargetOwlProfileList(new OwlProfileList(owlProfiles));
		
		Bem2RdfConverterManager converter = new Bem2RdfConverterManager(context, bemSchema);
		
		converter.exportNsPrefixes(jenaModel);
		
		Resource realTypeResource = converter.convertTypeInfo(jenaModel, bemSchema.REAL, true);
		converter.convertTypeInfo(jenaModel, bemSchema.NUMBER, true);
		
		Bem2RdfTestHelper.compareWithExpectedResult(this, 1, jenaModel, COMPARE_WITH_EXPECTED_DATASETS, true, context.getTargetOwlProfileList());
		
		test_exportingDoubleValues(converter, convertDoubleTo, realTypeResource);
	}
	
	private void test_exportingDoubleValues(Bem2RdfConverterManager converter, String convertDoubleTo, Resource realTypeResource) {
		String baseTypeForDouble = convertDoubleTo;
		
		switch (convertDoubleTo) {
		case Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT:
			baseTypeForDouble = XSD.xdouble.getURI();
			break;
		case Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED:
			baseTypeForDouble = XSD.decimal.getURI();
			break;
		}
		
		baseTypeForDouble = baseTypeForDouble.replaceAll(RdfVocabulary.XSD.BASE_URI, RdfVocabulary.XSD.BASE_PREFIX + ":");

		double[] values = new double[]{0.0, 0.005456287731e-3};
		for (double doubleValue : values) {
			RDFNode doubleNode = converter.convertSimpleValue(jenaModel, new BemPrimitiveValue(doubleValue, BemValueKindEnum.REAL), bemSchema.REAL);			
			assertTrue(doubleNode.isResource());
			StmtIterator properties = doubleNode.asResource().listProperties();
			while (properties.hasNext()) {
				Statement statement = properties.next();
				Property property = statement.getPredicate();
				
				if (property.equals(RDF.type)) {
					RDFNode object = statement.getObject();
					assertEquals(realTypeResource.getURI(), object.asResource().getURI());
				} else {
					property.getLocalName().equals("hasReal");
					assertEquals(doubleValue, statement.getObject().asLiteral().getDouble(), 0.1);
				}
			}
		}		
	}

	

}
