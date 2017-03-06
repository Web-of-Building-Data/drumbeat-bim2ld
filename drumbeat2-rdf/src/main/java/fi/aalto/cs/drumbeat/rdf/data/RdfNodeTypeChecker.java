package fi.aalto.cs.drumbeat.rdf.data;

import java.util.function.Function;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class RdfNodeTypeChecker {
	
	private Function<Resource, Boolean> localResourceChecker;
	
	public RdfNodeTypeChecker() {
		this(null);
	}
	
	public RdfNodeTypeChecker(Function<Resource, Boolean> localResourceChecker) {
		setLocalResourceChecker(localResourceChecker);
	}
	
	public RdfNodeTypeEnum getNodeType(RDFNode node) {
		if (node.isLiteral()) {
			return RdfNodeTypeEnum.Literal;
		} 
		
		if (localResourceChecker.apply(node.asResource())) {
			return RdfNodeTypeEnum.BlankNode;
		} else {
			return RdfNodeTypeEnum.Uri;
		}
	}
	
	public boolean isLocalResource(RDFNode node) {
		return node.isResource() && localResourceChecker.apply((Resource)node);
	}

	public Function<Resource, Boolean> getLocalResourceChecker() {
		return localResourceChecker;
	}

	public void setLocalResourceChecker(Function<Resource, Boolean> localResourceChecker) {
		this.localResourceChecker = localResourceChecker != null ? localResourceChecker : (r -> r.isAnon());
	}
	
}
