package fi.aalto.cs.drumbeat.rdf.data.msg;

import java.util.*;

//import org.apache.log4j.Logger;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;

import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.common.digest.ByteArray;

public class RdfMsg implements Comparable<RdfMsg> {
	
	private final RdfMsgContainer msgContainer;
	
	private final SortedSet<RdfTree> topTrees;
	private final SortedMap<Resource, RdfTree> sharedTrees;
	
	public RdfMsg(RdfMsgContainer msgContainer) {
		this.msgContainer = msgContainer;
		topTrees = new TreeSet<>();
		sharedTrees = new TreeMap<>(msgContainer.getComparatorPool().createNodeComparator());
	}
	
	public RdfMsgContainer getMsgContainer() {
		return msgContainer;
	}
	
	@Override
	public int compareTo(RdfMsg other) {
		return compareTo(other, null);
	}	
	
	public int compareTo(RdfMsg other, Stack<Pair<Object, Object>> differences) {
		Iterator<RdfTree> it1 = this.topTrees.iterator();
		Iterator<RdfTree> it2 = other.topTrees.iterator();
		
		while (it1.hasNext()) {
			if (it2.hasNext()) {
				RdfTree tree1 = it1.next();
				RdfTree tree2 = it2.next();
				int result = tree1.compareTo(tree2, differences);
				if (result != 0) {
					if (differences != null) {
						differences.push(new Pair<>(tree1, tree2));
					}
					return result;
				}
			} else {
				if (differences != null) {
					differences.push(new Pair<>(it1.next(), null));
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
	
	public void addTopTree(RdfTree tree) {
		topTrees.add(tree);
	}
	
	public void addSharedTree(RdfTree tree) {
		sharedTrees.put(tree.getHeadNode(), tree);
	}
	
	public SortedSet<RdfTree> getTopTrees() {
		return topTrees;
	}
	
	public SortedMap<Resource, RdfTree> getSharedTrees() {
		return sharedTrees;
	}

	public ByteArray getChecksum() {
		
		if (topTrees.size() == 1) {
			
			return topTrees.first().getChecksum();
			
		} else {
		
			ByteArray msgChecksum = null;
			
			for (RdfTree tree : topTrees) {
				ByteArray moleculeChecksum = tree.getChecksum(); 
				if (msgChecksum == null) {
					msgChecksum = moleculeChecksum;
				} else {
					msgChecksum.xor(moleculeChecksum);
				}
			}
			
			return msgChecksum;
		}
	}

	public void mergeWith(RdfMsg msg2) {
		topTrees.addAll(msg2.topTrees);
		if (msg2.sharedTrees != null) {
			getSharedTrees().putAll(msg2.sharedTrees);
		}
	}

	public int getSizeInTriples() {
		
		int totalSize = 0;
		
		for (RdfTree tree : topTrees) {
			totalSize += tree.getSizeInTriplesWithoutSubSharedTrees();
		}
		
		if (sharedTrees != null) {
			for (RdfTree subSharedTree : sharedTrees.values()) {
				totalSize += subSharedTree.getSizeInTriplesWithoutSubSharedTrees();
			}
		}
		
		return totalSize;
	}
	
	public RdfMsgType getType() {
		if (topTrees.size() == 1) {
			if (topTrees.first().isSingle()) {
				Statement statement = topTrees.first().firstEntry().getKey();
				if (statement.getPredicate().equals(RDF.type)) {
					return RdfMsgType.ClassDefinitionTripleMsg;
				} else {
					return RdfMsgType.SingleTripleMsg;
				}
			} else {
				return RdfMsgType.SingleTreeMsg;
			}
		} else {
			assert(!sharedTrees.isEmpty());
			return RdfMsgType.MultiTreeMsg;
		}
	}
	
	public Model toModel() {
		Model model = ModelFactory.createDefaultModel();
		
		for (RdfTree tree : topTrees) {
			tree.addAllToModel(model);
		}
		
		return model;
	}
	
	public boolean contains(Statement statement) {
		for (RdfTree tree : topTrees) {
			if (tree.contains(statement)) {
				return true;
			}
		}
		return false;
	}
	
	public int getMaxDepth() {
		int maxDepth = 0;
		for (RdfTree tree : topTrees) {
			int depth = tree.getDepth();
			if (maxDepth < depth) {
				maxDepth = depth;
			}
		}
		return maxDepth;
	}
	
}


