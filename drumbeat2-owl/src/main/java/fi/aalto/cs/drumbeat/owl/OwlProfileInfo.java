package fi.aalto.cs.drumbeat.owl;

import java.util.Set;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public interface OwlProfileInfo {
	
	boolean supportsStatement(Property property, RDFNode object);
	
	boolean supportsAnonymousIndividual();
	
	boolean supportsDataType(Resource type);
	
	Set<Resource> getSupportedDataTypes();

}
