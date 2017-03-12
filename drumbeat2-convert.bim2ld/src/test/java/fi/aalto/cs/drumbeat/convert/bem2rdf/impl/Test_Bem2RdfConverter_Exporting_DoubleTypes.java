package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.profiles.violations.UseOfUndeclaredDataProperty;

import fi.aalto.cs.drumbeat.convert.bem2rdf.*;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.schema.BemValueKindEnum;
import fi.aalto.cs.drumbeat.data.step.schema.ExpressSchema;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

public class Test_Bem2RdfConverter_Exporting_DoubleTypes extends Test_Base {
	
	public static final boolean WRITE_ACTUAL_DATASETS = true;
	public static final boolean COMPARE_WITH_EXPECTED_DATASETS = false;
	
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
	public void test_exportingDouble_As_AutoMostSupported_OWL2_Full() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingDouble_As_AutoMostSupported_OWL2_DL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostSupported_OWL2_RL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostSupported_OWL2_EL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_Full() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_DL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_RL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingDouble_As_AutoMostEfficient_OWL2_EL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT, OwlProfileEnum.OWL2_EL);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_Full() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_Full);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_DL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_DL);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_RL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_RL);
	}

	@Test
	public void test_exportingDouble_As_XsdString_OWL2_EL() throws Exception {
		exportDouble(Bem2RdfConversionContextParams.VALUE_XSD_STRING, OwlProfileEnum.OWL2_EL);
	}

	private void exportDouble(String convertDoubleTo, OwlProfileEnum owlProfileId) throws Exception {
		startTest(1);
		
		context.getConversionParams().setParamValue(
				Bem2RdfConversionContextParams.PARAM_CONVERT_DOUBLES_TO,
				convertDoubleTo);
		
		context.setTargetOwlProfileList(new OwlProfileList(owlProfileId));
		
		Bem2RdfConverterManager converter = new Bem2RdfConverterManager(context, bemSchema);
		
		converter.exportNsPrefixes(jenaModel);
		
		Resource realTypeResource = converter.convertTypeInfo(jenaModel, bemSchema.REAL, true);
		converter.convertTypeInfo(jenaModel, bemSchema.NUMBER, true);
		
		StringBuffer modelStringBuffer = writeAndCompareModel(1, jenaModel, WRITE_ACTUAL_DATASETS, COMPARE_WITH_EXPECTED_DATASETS);
		if (modelStringBuffer != null) {
			TestHelper.validateOwl(modelStringBuffer, owlProfileId, Arrays.asList(UseOfUndeclaredDataProperty.class));
		}
		
		exportingDoubleValues(converter, convertDoubleTo, realTypeResource);
	}
	
	private void exportingDoubleValues(Bem2RdfConverterManager converter, String convertDoubleTo, Resource realTypeResource) {
		String baseTypeForDouble = convertDoubleTo;
		
		switch (convertDoubleTo) {
		case Bem2RdfConversionContextParams.VALUE_AUTO_MOST_EFFICIENT:
			baseTypeForDouble = XSD.xdouble.getURI();
			break;
		case Bem2RdfConversionContextParams.VALUE_AUTO_MOST_SUPPORTED:
			baseTypeForDouble = XSD.decimal.getURI();
			break;
		}
		
		baseTypeForDouble = baseTypeForDouble.replaceAll(OwlVocabulary.XSD.BASE_URI, OwlVocabulary.XSD.BASE_PREFIX + ":");

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
