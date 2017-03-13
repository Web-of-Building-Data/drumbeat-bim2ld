package fi.aalto.cs.drumbeat.rdf.utils.print;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.*;

import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumCalculator;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;

public class RdfPrinter {
	
	private final RdfChecksumCalculator checksumCalculator;
	private final Map<String, String> nsPrefixMap;
	
	public RdfPrinter() {
		this(null, null);
	}
	
	public RdfPrinter(Map<String, String> nsPrefixMap) {
		this(nsPrefixMap, null);
	}
	
	public RdfPrinter(Map<String, String> nsPrefixMap, RdfChecksumCalculator checksumCalculator) {
		this.nsPrefixMap = nsPrefixMap;
		this.checksumCalculator = checksumCalculator;
	}
	
	public String toString(Resource resource) throws RdfChecksumException {
		if (checksumCalculator != null) {
			if (checksumCalculator.getNodeTypeChecker().isLocalResource(resource)) {
				return String.format("_:%s", checksumCalculator.getChecksum(resource.asResource()).toBase64String());
			}
		} else {
			// checksumCalculator == null
			if (resource.isAnon()) {
				return resource.asResource().getId().toString();
			}
		}
		
		String uri = ((Resource)resource).getURI();
		if (nsPrefixMap != null) {
			for (Entry<String, String> nsPrefixEntry : nsPrefixMap.entrySet()) {
				if (uri.startsWith(nsPrefixEntry.getValue())) {
					return String.format(
							"%s:%s",
							nsPrefixEntry.getKey(),
							uri.substring(nsPrefixEntry.getValue().length()));
				}
			}
		}
		return String.format("<%s>", uri);		
	}
	
	public String toString(RDFNode node) throws RdfChecksumException {
		if (node.isLiteral()) {
			return String.format("\"%s\"", node.asLiteral().getLexicalForm());
		} else {
			return toString(node.asResource());
		}
	}
	
	public String toString(Statement statement) throws RdfChecksumException {
		return String.format("[%s, %s, %s]",
				toString(statement.getSubject()),
				toString(statement.getPredicate()),
				toString(statement.getObject()));
	}
	
}
