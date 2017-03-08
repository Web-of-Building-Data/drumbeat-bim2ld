package fi.aalto.cs.drumbeat.rdf.data;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.jena.rdf.model.*;

import fi.aalto.cs.drumbeat.common.digest.ByteArray;
import fi.aalto.cs.drumbeat.common.digest.MessageDigestManager;

public class RdfChecksumCalculator {
	
	private final byte RDF_NODE_TYPE_LITERAL = 1;
	private final byte RDF_NODE_TYPE_URI = 2;
	private final byte RDF_NODE_TYPE_BLANK_NODE = 3;
	
	private RdfNodeTypeChecker nodeTypeChecker;
	private final Map<Model, Map<Resource, ByteArray>> checksumCacheMap;
	private final int checksumLength;
	public final ByteArray defaultChecksum;
	
//	public RdfChecksumCalculator() {
//		this((RdfNodeTypeChecker)null);
//	}

	public RdfChecksumCalculator(Function<Resource, Boolean> localResourceChecker) {
		this(new RdfNodeTypeChecker(localResourceChecker));
	}

	public RdfChecksumCalculator(RdfNodeTypeChecker nodeTypeChecker) {
		setNodeTypeChecker(nodeTypeChecker);
		checksumCacheMap = new HashMap<>();
		checksumLength = getDigest().getDigestLength();
		defaultChecksum = new ByteArray(checksumLength);
	}
	
	public MessageDigest getDigest() {
		return MessageDigestManager.getMessageDigest();
	}
	
	public RdfNodeTypeChecker getNodeTypeChecker() {
		return nodeTypeChecker;
	}
	
	public void setNodeTypeChecker(RdfNodeTypeChecker nodeTypeChecker) {
		this.nodeTypeChecker = nodeTypeChecker != null ? nodeTypeChecker : new RdfNodeTypeChecker();
	}
	
	public void clearCache() {
		for (Map<Resource, ByteArray> checksumCache : checksumCacheMap.values()) {
			checksumCache.clear();
		}
		checksumCacheMap.clear();
	}
	
	public void clearCache(Model model) {
		Map<Resource, ByteArray> checksumCache = checksumCacheMap.remove(model);
		if (checksumCache != null) {
			checksumCache.clear();
		}
	}

	public ByteArray getChecksum(Resource resource) throws RdfCircularChecksumException {
		
		if (!nodeTypeChecker.isLocalResource(resource)) {
			throw new IllegalArgumentException("Resource is not local: " + resource);
		}
		
		Map<Resource, ByteArray> checksumCache = getChecksumCache(resource.getModel());
		return getChecksum(resource, checksumCache);
	}
	
	public ByteArray getChecksum(Statement statement, boolean includeSubject) throws RdfCircularChecksumException {
		Map<Resource, ByteArray> checksumCache = getChecksumCache(statement.getSubject().getModel());
		return new ByteArray(getChecksum(statement, includeSubject, checksumCache));		
	}
	
	private byte[] getChecksum(Statement statement, boolean includeSubject, Map<Resource, ByteArray> checksumCache) throws RdfCircularChecksumException {		
		MessageDigest digest = getDigest();
		
		if (includeSubject) {
			updateDigest(digest, statement.getSubject(), checksumCache);
		}		
		updateDigest(digest, statement.getPredicate(), checksumCache);
		updateDigest(digest, statement.getObject(), checksumCache);
		
		return digest.digest();
	}
	
	
	private ByteArray getChecksum(Resource resource, Map<Resource, ByteArray> checksumCache) throws RdfCircularChecksumException {
		ByteArray resultChecksum = checksumCache.get(resource);
		if (resultChecksum != null) {
			if (resultChecksum != defaultChecksum) {
				return resultChecksum;
			}
			throw new RdfCircularChecksumException("Circular digest");
		}			
			
		checksumCache.put(resource, defaultChecksum);
		
		resultChecksum = new ByteArray(checksumLength);
		
		StmtIterator it = resource.listProperties();
		while (it.hasNext()) {
			Statement statement = it.next();
			byte[] checksum = getChecksum(statement, false, checksumCache);
			resultChecksum.xor(checksum);
		}
		
		checksumCache.put(resource, resultChecksum);
		return resultChecksum;
	}
	
	private Map<Resource, ByteArray> getChecksumCache(Model model) {
		Map<Resource, ByteArray> checksumCache = checksumCacheMap.get(model);
		if (checksumCache == null) {
			// TODO: change to ConcurrentHashMap
			checksumCache = new HashMap<>();
			checksumCacheMap.put(model, checksumCache);
		}
		return checksumCache;
	}
	

	private void updateDigest(MessageDigest digest, RDFNode node, Map<Resource, ByteArray> checksumCache) throws RdfCircularChecksumException {
		if (node.isLiteral()) {
			digest.update(RDF_NODE_TYPE_LITERAL);
			digest.update(StringUtils.getBytesUtf8(node.toString()));			
		} else {
			assert(node.isResource());
			Resource resource = node.asResource();
			if (!nodeTypeChecker.isLocalResource(resource)) {
				digest.update(RDF_NODE_TYPE_URI);
				digest.update(StringUtils.getBytesUtf8(resource.getURI().toString()));
			} else {
				digest.update(RDF_NODE_TYPE_BLANK_NODE);
				ByteArray checksum = getChecksum(resource, checksumCache); 
				digest.update(checksum.getArray());
			}
		}
	}
	

}
