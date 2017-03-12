package fi.aalto.cs.drumbeat.owl;

import org.apache.jena.rdf.model.*;

public class OwlVocabulary {

	public static final Model DEFAULT_MODEL = ModelFactory.createDefaultModel();	
	
	public static class DumpData {	
		
		public final static Resource ANY_URI_1 = DEFAULT_MODEL.createResource("http://example.org/uri1");
		public final static Resource ANY_URI_2 = DEFAULT_MODEL.createResource("http://example.org/uri2");
		public final static Resource ANY_URI_3 = DEFAULT_MODEL.createResource("http://example.org/uri3");
		public final static RDFList ANY_URI_LIST = DEFAULT_MODEL.createList(new RDFNode[]{ANY_URI_1, ANY_URI_2, ANY_URI_3});
		
		public final static RDFNode ANY_LITERAL_1 = DEFAULT_MODEL.createLiteral("s1");
		public final static RDFNode ANY_LITERAL_2 = DEFAULT_MODEL.createLiteral("s2");
		public final static RDFNode ANY_LITERAL_3 = DEFAULT_MODEL.createLiteral("s3");
		public final static RDFNode ANY_LITERAL_LIST = DEFAULT_MODEL.createList(new RDFNode[]{ANY_LITERAL_1, ANY_LITERAL_2, ANY_LITERAL_3});
		
	}
	
	public static class OWL {
		public static final String BASE_PREFIX = "owl";
		public static final String BASE_URI = "http://www.w3.org/2002/07/owl#";
		
		public static final Property FunctionalDataProperty = DEFAULT_MODEL.createProperty(BASE_URI + "FunctionalDataProperty");

		public static final Resource rational = DEFAULT_MODEL.createResource(BASE_URI + "rational");
		public static final Resource real = DEFAULT_MODEL.createResource(BASE_URI + "real");
	}
	
	public static class RDF {
		public static final String BASE_PREFIX = "rdf";
	}

	public static class RDFS {
		public static final String BASE_PREFIX = "rdfs";
	}

	public static class XSD {
		public static final String BASE_PREFIX = "xsd";
		public static final String BASE_URI = "http://www.w3.org/2001/XMLSchema#";
		public static final Property maxExclusive = DEFAULT_MODEL.createProperty(org.apache.jena.vocabulary.XSD.getURI() + "maxExclusive");
		public static final Property minExclusive = DEFAULT_MODEL.createProperty(org.apache.jena.vocabulary.XSD.getURI() + "minExclusive");		
	}	
	
//	public static class OLO {
//		public static final String BASE_PREFIX = "olo";
//		public static final String BASE_URI = "http://purl.org/ontology/olo/core#";
//		public static final String IMPORT_URI = "http://purl.org/ontology/olo/orderedlistontology.owl"; 
//
//		public static final Resource OrderedList = DEFAULT_MODEL.createResource(BASE_URI + "OrderedList");
//		public static final Resource Slot = DEFAULT_MODEL.createResource(BASE_URI +  "Slot");
//		
//		public static final Property index = DEFAULT_MODEL.createProperty(BASE_URI +  "index");
//		public static final Property item = DEFAULT_MODEL.createProperty(BASE_URI +  "item");
//		public static final Property length = DEFAULT_MODEL.createProperty(BASE_URI +  "length");
//		public static final Property slot = DEFAULT_MODEL.createProperty(BASE_URI +  "slot");		
//	}
	
	public static class VOID {
		public static final String BASE_PREFIX = "void";
		public static final String BASE_URI = "http://rdfs.org/ns/void#";
		
		public static final Resource DataSet = DEFAULT_MODEL.createResource(BASE_URI + "DataSet");
	}
	
	public static class DCTERMS {
		public static final String BASE_PREFIX = "dcterms";
		public static final String BASE_URI = "http://purl.org/dc/terms/";
		
		public static final Property contributor  = DEFAULT_MODEL.createProperty(BASE_URI + "contributor");
		public static final Property created  = DEFAULT_MODEL.createProperty(BASE_URI + "created");
		public static final Property creator  = DEFAULT_MODEL.createProperty(BASE_URI + "creator");
		public static final Property date  = DEFAULT_MODEL.createProperty(BASE_URI + "date");
		public static final Property hasVersion  = DEFAULT_MODEL.createProperty(BASE_URI + "hasVersion");
		public static final Property issued  = DEFAULT_MODEL.createProperty(BASE_URI + "issued");
		public static final Property modified  = DEFAULT_MODEL.createProperty(BASE_URI + "modified");
		public static final Property publisher  = DEFAULT_MODEL.createProperty(BASE_URI + "publisher");
		public static final Property subject  = DEFAULT_MODEL.createProperty(BASE_URI + "subject");
		public static final Property source  = DEFAULT_MODEL.createProperty(BASE_URI + "source");
		public static final Property title  = DEFAULT_MODEL.createProperty(BASE_URI + "title");		
		public static final Property description  = DEFAULT_MODEL.createProperty(BASE_URI + "description");
		
	}
}
