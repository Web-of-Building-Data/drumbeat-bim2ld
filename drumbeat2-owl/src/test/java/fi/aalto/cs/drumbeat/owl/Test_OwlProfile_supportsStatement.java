package fi.aalto.cs.drumbeat.owl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

import fi.aalto.cs.drumbeat.owl.OwlProfile;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;

public class Test_OwlProfile_supportsStatement {
	
	private static Model model;

	private static Resource uri1;
	private static Resource uri2;
	private static Resource uri3;
	private static RDFList uriList;
	
	
	@BeforeClass
	public static void setUpBeforeClass() {
		model = ModelFactory.createDefaultModel();
		String baseUri = "http://example.org/";
		uri1 = model.createResource(baseUri + "uri1");
		uri2 = model.createResource(baseUri + "uri2");
		uri3 = model.createResource(baseUri + "uri3");
		uriList = model.createList(new RDFNode[]{uri1, uri2, uri3});
	}
	
	
	@Before
	public void setUp() {
		model = ModelFactory.createDefaultModel();
	}

	@Test
	public void test_OwlProfile() {
		OwlProfile owlProfile = new OwlProfile(OwlProfileEnum.OWL1_Lite);
		
		assertFalse(owlProfile.isOwl2());		
		assertFalse(owlProfile.supportsStatement(OWL.complementOf, null));
		assertFalse(owlProfile.supportsStatement(OWL2.hasKey, null));
		assertFalse(owlProfile.supportsStatement(OWL.oneOf, null));
		assertFalse(owlProfile.supportsStatement(OWL.unionOf, null));		
		
		assertFalse(owlProfile.supportsStatement(OWL.disjointWith, uri1));
		assertFalse(owlProfile.supportsStatement(OWL.disjointWith, uriList));
		
		assertTrue(owlProfile.supportsStatement(RDF.type, null));
		
		assertTrue(owlProfile.supportsDataType(XSD.xstring));
		assertTrue(owlProfile.supportsDataType(XSD.integer));
		assertTrue(owlProfile.supportsDataType(XSD.decimal));
		assertTrue(owlProfile.supportsDataType(XSD.dateTime));
		assertTrue(owlProfile.supportsDataType(XSD.xdouble));
		assertTrue(owlProfile.supportsDataType(XSD.xboolean));
		
		assertFalse(owlProfile.supportsDataType(OwlVocabulary.OWL.real));	
	}

	@Test
	public void test_SupportStatement_Owl1DL() {
		OwlProfile owlProfile = new OwlProfile(OwlProfileEnum.OWL1_DL);

		assertFalse(owlProfile.isOwl2());		
		assertTrue(owlProfile.supportsStatement(OWL.complementOf, null));
		assertTrue(owlProfile.supportsStatement(OWL2.hasKey, null));
		assertTrue(owlProfile.supportsStatement(OWL.oneOf, null));
		assertTrue(owlProfile.supportsStatement(OWL.unionOf, null));		
		
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uri1));
		assertFalse(owlProfile.supportsStatement(OWL.disjointWith, uriList));
		
		assertTrue(owlProfile.supportsStatement(RDF.type, null));
		
		assertTrue(owlProfile.supportsDataType(XSD.xstring));
		assertTrue(owlProfile.supportsDataType(XSD.integer));
		assertTrue(owlProfile.supportsDataType(XSD.decimal));
		assertTrue(owlProfile.supportsDataType(XSD.dateTime));
		assertTrue(owlProfile.supportsDataType(XSD.xdouble));
		assertTrue(owlProfile.supportsDataType(XSD.xboolean));
		
		assertFalse(owlProfile.supportsDataType(OwlVocabulary.OWL.real));		
	}
	
	@Test
	public void test_SupportStatement_Owl1Full() {
		OwlProfile owlProfile = new OwlProfile(OwlProfileEnum.OWL1_Full);
		
		assertFalse(owlProfile.isOwl2());
		assertTrue(owlProfile.supportsStatement(OWL.complementOf, null));
		assertTrue(owlProfile.supportsStatement(OWL2.hasKey, null));
		assertTrue(owlProfile.supportsStatement(OWL.oneOf, null));
		assertTrue(owlProfile.supportsStatement(OWL.unionOf, null));		
		assertTrue(owlProfile.supportsStatement(RDF.type, null));		
		
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uri1));
		assertFalse(owlProfile.supportsStatement(OWL.disjointWith, uriList));
		
		assertTrue(owlProfile.supportsDataType(XSD.xstring));
		assertTrue(owlProfile.supportsDataType(XSD.integer));
		assertTrue(owlProfile.supportsDataType(XSD.decimal));
		assertTrue(owlProfile.supportsDataType(XSD.dateTime));
		assertTrue(owlProfile.supportsDataType(XSD.xdouble));
		assertTrue(owlProfile.supportsDataType(XSD.xboolean));
		
		assertFalse(owlProfile.supportsDataType(OwlVocabulary.OWL.real));
	}
	
	@Test
	public void test_SupportStatement_Owl2EL() {
		OwlProfile owlProfile = new OwlProfile(OwlProfileEnum.OWL2_EL);

		assertTrue(owlProfile.isOwl2());
		
		assertFalse(owlProfile.supportsStatement(OWL.oneOf, null));
		assertFalse(owlProfile.supportsStatement(OWL.unionOf, null));		
		assertFalse(owlProfile.supportsStatement(OWL2.hasKey, null));

		assertTrue(owlProfile.supportsStatement(OWL.complementOf, null));
		assertTrue(owlProfile.supportsStatement(RDF.type, null));		
		
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uri1));
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uriList));
		
		assertTrue(owlProfile.supportsDataType(XSD.xstring));
		assertTrue(owlProfile.supportsDataType(XSD.integer));
		assertTrue(owlProfile.supportsDataType(XSD.decimal));
		assertTrue(owlProfile.supportsDataType(XSD.dateTime));

		assertTrue(owlProfile.supportsDataType(OwlVocabulary.OWL.real));
		assertTrue(owlProfile.supportsDataType(OwlVocabulary.OWL.rational));

		assertFalse(owlProfile.supportsDataType(XSD.xdouble));
		assertFalse(owlProfile.supportsDataType(XSD.xboolean));
	}
	
	@Test
	public void test_SupportStatement_Owl2QL() {
		OwlProfile owlProfile = new OwlProfile(OwlProfileEnum.OWL2_QL);

		assertTrue(owlProfile.isOwl2());

		assertFalse(owlProfile.supportsStatement(OWL.oneOf, null));
		assertFalse(owlProfile.supportsStatement(OWL.unionOf, null));		
		assertFalse(owlProfile.supportsStatement(OWL2.hasKey, null));

		assertTrue(owlProfile.supportsStatement(OWL.complementOf, null));
		assertTrue(owlProfile.supportsStatement(RDF.type, null));		
		
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uri1));
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uriList));
		
		assertTrue(owlProfile.supportsDataType(XSD.xstring));
		assertTrue(owlProfile.supportsDataType(XSD.integer));
		assertTrue(owlProfile.supportsDataType(XSD.decimal));
		assertTrue(owlProfile.supportsDataType(XSD.dateTime));

		assertTrue(owlProfile.supportsDataType(OwlVocabulary.OWL.real));
		assertTrue(owlProfile.supportsDataType(OwlVocabulary.OWL.rational));

		assertFalse(owlProfile.supportsDataType(XSD.xdouble));
		assertFalse(owlProfile.supportsDataType(XSD.xboolean));
	}
	
	@Test
	public void test_SupportStatement_Owl2RL() {
		OwlProfile owlProfile = new OwlProfile(OwlProfileEnum.OWL2_RL);

		assertTrue(owlProfile.isOwl2());

		assertFalse(owlProfile.supportsStatement(OWL.oneOf, null));
		assertFalse(owlProfile.supportsStatement(OWL.unionOf, null));		

		assertTrue(owlProfile.supportsStatement(OWL2.hasKey, null));
		assertTrue(owlProfile.supportsStatement(OWL.complementOf, null));
		assertTrue(owlProfile.supportsStatement(RDF.type, null));		
		
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uri1));
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uriList));
		
		assertTrue(owlProfile.supportsDataType(XSD.xstring));
		assertTrue(owlProfile.supportsDataType(XSD.integer));
		assertTrue(owlProfile.supportsDataType(XSD.decimal));
		assertTrue(owlProfile.supportsDataType(XSD.dateTime));

		assertFalse(owlProfile.supportsDataType(OwlVocabulary.OWL.real));
		assertFalse(owlProfile.supportsDataType(OwlVocabulary.OWL.rational));

		assertTrue(owlProfile.supportsDataType(XSD.xdouble));
		assertTrue(owlProfile.supportsDataType(XSD.xboolean));
	}

	@Test
	public void test_SupportStatement_Owl2DL() {
		OwlProfile owlProfile = new OwlProfile(OwlProfileEnum.OWL2_DL);

		assertTrue(owlProfile.isOwl2());

		assertTrue(owlProfile.supportsStatement(OWL.complementOf, null));
		assertTrue(owlProfile.supportsStatement(OWL2.hasKey, null));
		assertTrue(owlProfile.supportsStatement(OWL.oneOf, null));
		assertTrue(owlProfile.supportsStatement(OWL.unionOf, null));		
		assertTrue(owlProfile.supportsStatement(RDF.type, null));		
		
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uri1));
		assertTrue(owlProfile.supportsStatement(OWL.disjointWith, uriList));
		
		assertTrue(owlProfile.supportsDataType(XSD.xstring));
		assertTrue(owlProfile.supportsDataType(XSD.integer));
		assertTrue(owlProfile.supportsDataType(XSD.decimal));
		assertTrue(owlProfile.supportsDataType(XSD.dateTime));

		assertTrue(owlProfile.supportsDataType(XSD.xdouble));
		assertTrue(owlProfile.supportsDataType(XSD.xboolean));
		assertTrue(owlProfile.supportsDataType(OwlVocabulary.OWL.real));
	}
	
}
