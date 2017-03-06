package fi.aalto.cs.drumbeat.rdf.data.msg;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.codec.digest.DigestUtils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.common.digest.ByteArray;
import fi.aalto.cs.drumbeat.common.digest.MessageDigestManager;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool.*;


public class RdfTree extends TreeMap<Statement, RdfTree> implements Comparable<RdfTree> {
	
	private static final long serialVersionUID = 1L;

	private static final byte BYTE_START_PREDICATE = (byte)0;
	private static final byte BYTE_END_PREDICATE = (byte)0xFF;
	
	private final Resource headNode;
	private final RdfMsgContainer msgContainer;
	private RdfTreeTypeEnum type;
	private RdfMsg msg;

	public RdfTree(Resource headNode, RdfMsgContainer msgContainer) {
		super(msgContainer.getComparatorPool().createStatementComparator(false));
		this.headNode = headNode;
		this.msgContainer = msgContainer;
	}
	
	public RdfMsgContainer getMsgContainer() {
		return msgContainer;
	}
	
	public RdfTreeTypeEnum getType() {
		return type;
	}
	
	public void setType(RdfTreeTypeEnum type) {
		this.type = type;
	}

	public boolean isTopTree() {
		return type == RdfTreeTypeEnum.Top;
	}
	
	public boolean isSharedTree() {
		return type == RdfTreeTypeEnum.SharedSub;
	}
	
	public RdfMsg getMsg() {
		return msg;
	}
	
	public void setMsg(RdfMsg msg) {
		this.msg = msg;
	}
	
	public boolean isSingle() {
		if (size() == 1) {
			Statement statement = this.firstKey();
			
			return getMsgContainer().isTerminated(statement.getSubject()) &&
					getMsgContainer().isTerminated(statement.getObject());			
		}
		return false;
	}

	public int getDepth() {
		int maxDepth = 0;
		for (RdfTree subMsg : values()) {
			if (subMsg != null) {
				int depth = subMsg.getDepth();
				if (maxDepth < depth) {
					maxDepth = depth;
				}
			}
		}
		return maxDepth + 1;
	}
	
	public int getSizeInTriples() {
		return (int)toModel().size();
//		Set<RdfSharedTree> subSharedTrees = new TreeSet<>();
//		int totalSize = getSizeInTriplesWithoutSubSharedTrees(subSharedTrees );
//		for (RdfSharedTree subSharedTree : subSharedTrees) {
//			totalSize += subSharedTree.getSizeInTriplesWithoutSubSharedTrees();
//		}
//		return totalSize;
	}
	
	public int getSizeInTriplesWithoutSubSharedTrees() {

		int totalSize = size();
		
		for (RdfTree subTree : values()) {
			if (subTree != null && !subTree.isSharedTree()) {
				totalSize += subTree.getSizeInTriplesWithoutSubSharedTrees();
			}
		}
		return totalSize;
	}	
	
	@Override
	public RdfTree put(Statement statement, RdfTree subTreeMsg) {
		return super.put(statement, subTreeMsg);
	}

//	/**
//	 * @return the isShared
//	 */
//	public boolean isShared() {
//		return isShared;
//	}
//
//	/**
//	 * @param isShared the isShared to set
//	 */
//	public void setShared(boolean isShared) {
//		this.isShared = isShared;
//	}

	@Override
	public int compareTo(RdfTree other) {
		return compareTo(other, null);
	}
	
	public int compareTo(RdfTree other, Stack<Pair<Object, Object>> differences) {
		final RdfNodeComparator nodeComparator = msgContainer.getComparatorPool().createNodeComparator();
		
		int result = nodeComparator.compare(headNode, other.headNode);
		if (result != 0) {
			if (differences != null) {
				differences.add(new Pair<>(headNode, other.headNode));
			}
			return result;
		}
		
		Iterator<Statement> it1 = this.keySet().iterator();
		Iterator<Statement> it2 = other.keySet().iterator();
		
		final RdfStatementComparator statementComparator = msgContainer.getComparatorPool().createStatementComparator(false);

		while (it1.hasNext()) {
			if (it2.hasNext()) {
				Statement statement1 = it1.next();
				Statement statement2 = it2.next();
				result = statementComparator.compare(statement1, statement2);
				if (result != 0) {
					if (differences != null) {
						differences.add(new Pair<>(statement1, statement2));
					}
					return result;
				}
			} else {
				if (differences != null) {
					differences.add(new Pair<>(it1.next(), null));
				}
				return 1;
			}
		}
		
		if (it2.hasNext()) {
			if (differences != null) {
				differences.add(new Pair<>(null, it2.next()));
			}
			return -1;
		}
		
		return 0;			
	}
	
	
	
	public Statement getHeadStatement() {
		return firstKey();
	}
	
	public Resource getHeadNode() {
		return headNode;
	}
	
	public ByteArray getChecksum() {
		
		MessageDigest messageDigest = MessageDigestManager.getMessageDigest();
		
		Resource subject = getHeadNode();
		if (subject.isURIResource()) {
			messageDigest.update(subject.getURI().getBytes());
		}

		Property currentPredicate = null;
		ByteArray xorOjectChecksum = null;
		
		for (Entry<Statement, RdfTree> entry : entrySet()) {
			Statement statement = entry.getKey();	
			Property predicate = statement.getPredicate();
			RDFNode object = statement.getObject();
			RdfTree subMsg = entry.getValue();			
			
			if (predicate != currentPredicate) {
				//
				// handle the end of the previous predicate
				//
				if (currentPredicate != null) {
					assert (xorOjectChecksum != null) : "Expected: (xorOjectChecksum != null)";
					messageDigest.update(BYTE_START_PREDICATE);
					messageDigest.update(currentPredicate.getURI().getBytes());
					messageDigest.update(xorOjectChecksum.array);
					messageDigest.update(BYTE_END_PREDICATE);
				}
				
				// 
				// handle the start of the new predicate
				//
				currentPredicate = predicate;
				xorOjectChecksum = null;
			}
			
			ByteArray newObjectChecksum; 

			if (subMsg == null) {
				assert(!object.isAnon()) : String.format("Expected: !object.getRdfNodeType().isBlankNode(), RdfProperty = %s", statement);
				newObjectChecksum = new ByteArray(DigestUtils.md5(object.toString()));				
			} else {
				newObjectChecksum = subMsg.getChecksum();
			}
			
			if (xorOjectChecksum == null) {
				xorOjectChecksum = newObjectChecksum;
			} else {
				xorOjectChecksum.xor(newObjectChecksum);
			}
		}
		
		if (currentPredicate != null) {
			assert (xorOjectChecksum != null) : "Expected: (xorOjectChecksum != null)";
			messageDigest.update(BYTE_START_PREDICATE);
			messageDigest.update(currentPredicate.getURI().getBytes());
			messageDigest.update(xorOjectChecksum.array);
			messageDigest.update(BYTE_END_PREDICATE);
		}
			
		return new ByteArray(messageDigest.digest());
	}
	
	public Model toModel() {
		Model model = ModelFactory.createDefaultModel();
		addAllToModel(model);
		return model;
	}	
	
	public void addAllToModel(Model model) {
		for (Entry<Statement, RdfTree> entry : this.entrySet()) {
			Statement statement = entry.getKey();
			model.add(statement);
			RdfTree subTree = entry.getValue();
			if (subTree != null) {
				subTree.addAllToModel(model);
			}
		}
	}

	public boolean contains(Statement statement) {
		if (this.containsKey(statement)) {
			return true;
		}
		
		for (RdfTree subTree : values()) {
			if (subTree != null && subTree.contains(statement)) {
				return true;
			}
		}
		
		return false;
	}

}
