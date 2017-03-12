package fi.aalto.cs.drumbeat.rdf;

import org.apache.jena.riot.RDFFormat;

public class RdfVocabulary {
	
	public static String getRdfFileExtension(RDFFormat format) {
		// See: http://jena.apache.org/documentation/io/
		String formatString = format.toString().toUpperCase();		
		
		if (formatString.startsWith("RDF")) {
			if (formatString.contains("XML")) {
				// RDF/XML --> .rdf
				return "rdf";
			} else if (formatString.contains("JSON")) {
				// RDF/JSON --> .rj
				return "rj";
			} else if (formatString.contains("THRIFT")) {
				return "rt";
			}
		} else if (formatString.startsWith("TURTLE") || formatString.startsWith("TTL")) {
			return "ttl"; // similar to .n3
		} else if (formatString.startsWith("N-TRIPLES") || formatString.startsWith("NT")) {
			return "nt";
		} else if (formatString.startsWith("N-QUADS") || formatString.startsWith("NQ"))  {
			return "nq";
		} else if (formatString.startsWith("JSON")) {
			return "jsonld";
		} else if (formatString.startsWith("TRIG")) {
			return "trig";
		}
		return "rdf";
	}	
	
}
