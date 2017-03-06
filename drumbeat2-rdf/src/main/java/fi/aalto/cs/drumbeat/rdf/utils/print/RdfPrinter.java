package fi.aalto.cs.drumbeat.rdf.utils.print;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.*;

import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumCalculator;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;

public class RdfPrinter {
	
	private RdfChecksumCalculator checksumCalculator;
	private Map<String, String> nsPrefixMap;
	
	public RdfPrinter() {
	}
	
	public RdfPrinter(Map<String, String> nsPrefixMap) {
		this.nsPrefixMap = nsPrefixMap;		
	}
	
	public RdfPrinter(Map<String, String> nsPrefixMap, RdfChecksumCalculator checksumCalculator) {
		this(nsPrefixMap);
		this.checksumCalculator = checksumCalculator;
	}
	
	public String toString(RDFNode node) throws RdfChecksumException {
		if (node.isLiteral()) {
			return node.asLiteral().toString();
		} else if (checksumCalculator != null) {
			if (checksumCalculator.getNodeTypeChecker().isLocalResource(node)) {
				return String.format("_:%s", checksumCalculator.getChecksum(node.asResource()).toBase64String());
			}
		} else {
			// checksumCalculator == null
			if (node.isAnon()) {
				return node.asResource().getId().toString();
			}
		}
		
		String uri = ((Resource)node).getURI();
		if (nsPrefixMap != null) {
			for (Entry<String, String> nsPrefixEntry : nsPrefixMap.entrySet()) {
				if (uri.startsWith(nsPrefixEntry.getValue())) {
					return nsPrefixEntry.getKey() + uri.substring(nsPrefixEntry.getValue().length());
				}
			}
		}
		return String.format("<%s>", uri);
	}
	
	public String toString(Statement statement) throws RdfChecksumException {
		return String.format("[%s, %s, %s]",
				toString(statement.getSubject()),
				toString(statement.getPredicate()),
				toString(statement.getObject()));
	}
	
}
