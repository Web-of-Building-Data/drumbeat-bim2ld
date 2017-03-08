package fi.aalto.cs.drumbeat.rdf.data.msg;

import java.util.*;

import org.apache.jena.ext.com.google.common.collect.Multimap;
import org.apache.jena.ext.com.google.common.collect.Ordering;
import org.apache.jena.ext.com.google.common.collect.TreeMultimap;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.common.collections.SortedList;
import fi.aalto.cs.drumbeat.common.digest.ByteArray;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumCalculator;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool;
import fi.aalto.cs.drumbeat.rdf.data.RdfNodeTypeChecker;
import fi.aalto.cs.drumbeat.rdf.data.RdfNodeTypeEnum;


public class RdfMsgContainer implements Iterable<RdfMsg>, Comparable<RdfMsgContainer> {
	
	private static final Logger logger = Logger.getLogger(RdfMsgContainer.class);	
	
	private final RdfComparatorPool comparatorPool;

	private SortedList<RdfMsg> classDefinitionTripleMsgs;	
	private SortedList<RdfMsg> singleTripleMsgs;
	private Multimap<ByteArray, RdfMsg> singleTreeMsgs;
	private Multimap<ByteArray, RdfMsg> multiTreeMsgs;
	
	public RdfMsgContainer(RdfComparatorPool comparatorPool) {
		this.comparatorPool = comparatorPool;

		classDefinitionTripleMsgs = new SortedList<>();
		singleTripleMsgs = new SortedList<>();
		singleTreeMsgs = TreeMultimap.create(Ordering.natural(), Ordering.natural());
		multiTreeMsgs = TreeMultimap.create(Ordering.natural(), Ordering.natural());
	}
	
	public RdfComparatorPool getComparatorPool() {
		return comparatorPool;
	}
	
	public RdfNodeTypeChecker getNodeTypeChecker() {
		return comparatorPool.getNodeTypeChecker();
	}
	
	public RdfChecksumCalculator getChecksumCalculator() {
		return comparatorPool.getChecksumCalculator();
	}

	public boolean isTerminated(RDFNode node) {
		RdfNodeTypeEnum type = getNodeTypeChecker().getNodeType(node);
		return !type.isBlankNode();
	}


	public int getTotalSize() {
		return classDefinitionTripleMsgs.size() + singleTripleMsgs.size() + singleTreeMsgs.size() + multiTreeMsgs.size();
	}
	
	public Collection<RdfMsg> getAllMsgsByType(RdfMsgType type) {
		if (type == RdfMsgType.ClassDefinitionTripleMsg) {
			return getClassDefinitionTripleMsgs();
		} else if (type == RdfMsgType.SingleTripleMsg) {
			return getSingleTripleMsgs();
		} else if (type == RdfMsgType.SingleTreeMsg) {
			return singleTreeMsgs.values();
		} else {
			return multiTreeMsgs.values();
		}
	}
	
	public Collection<RdfMsg> getClassDefinitionTripleMsgs() {
		return classDefinitionTripleMsgs;
	}

	public Collection<RdfMsg> getSingleTripleMsgs() {
		return singleTripleMsgs;
	}

	public Multimap<ByteArray, RdfMsg> getSingleTreeMsgs() {
		return singleTreeMsgs;
	}
	
	public Multimap<ByteArray, RdfMsg> getMultiTreeMsgs() {
		return multiTreeMsgs;
	}
	
	@Override
	public Iterator<RdfMsg> iterator() {
		return new RdfMsgContainerIterator();
	}
	
	public class RdfMsgContainerIterator implements Iterator<RdfMsg> {
		
		private Iterator<RdfMsg> currentIterator;
		private RdfMsgType nextIteratorType;
		
		public RdfMsgContainerIterator() {
			nextIteratorType = RdfMsgType.ClassDefinitionTripleMsg;
		}
		
		@Override
		public boolean hasNext() {
			if (currentIterator != null && currentIterator.hasNext()) {
				return true;
			}
			
			while (nextIteratorType != null) {
				if (nextIteratorType == RdfMsgType.ClassDefinitionTripleMsg) {
					
					currentIterator = getClassDefinitionTripleMsgs().iterator();
					nextIteratorType = RdfMsgType.SingleTripleMsg;
					
				} else if (nextIteratorType == RdfMsgType.SingleTripleMsg) {
					
					currentIterator = getSingleTripleMsgs().iterator();
					nextIteratorType = RdfMsgType.SingleTreeMsg;
					
				} else if (nextIteratorType == RdfMsgType.SingleTreeMsg) {
					
					currentIterator = getSingleTreeMsgs().values().iterator();
					nextIteratorType = RdfMsgType.MultiTreeMsg;
					
				}  else if (nextIteratorType == RdfMsgType.MultiTreeMsg) {
					
					currentIterator = getMultiTreeMsgs().values().iterator();
					nextIteratorType = null;
				}
				
				if (currentIterator.hasNext()) {
					return true;
				}
				
			}
			return false;
		}

		@Override
		public RdfMsg next() {
			if (currentIterator != null) {
				return currentIterator.next();
			} else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			currentIterator.remove();
		}
		
	}


	public void addMsg(RdfMsg msg) throws RdfChecksumException {
		switch (msg.getType()) {
		case ClassDefinitionTripleMsg:
			classDefinitionTripleMsgs.add(msg);
			break;
		case SingleTripleMsg:
			singleTripleMsgs.add(msg);
			break;
		case SingleTreeMsg:
			ByteArray checksum = msg.getChecksum();
			if (singleTreeMsgs.put(checksum, msg)) {
				logger.warn(String.format("More than one MSG has the same checksum: " + singleTreeMsgs.get(checksum).toArray()));
			}
			break;
		case MultiTreeMsg:
			checksum = msg.getChecksum();
			if (multiTreeMsgs.put(checksum, msg)) {
				logger.warn(String.format("More than one MSG has the same checksum: " + multiTreeMsgs.get(checksum).toArray()));
			}			
		default:
			throw new IllegalArgumentException("Invalid MSG type: " + msg.getType());
		}
	}
	
	
	@Override
	public int compareTo(RdfMsgContainer other) {
		return compareTo(other, null);
	}	
	
	public int compareTo(RdfMsgContainer other, Stack<Pair<Object, Object>> differences) {
		Iterator<RdfMsg> it1 = this.iterator();
		Iterator<RdfMsg> it2 = other.iterator();
		
		while (it1.hasNext()) {
			if (it2.hasNext()) {
				RdfMsg msg1 = it1.next();
				RdfMsg msg2 = it2.next();
				int result = msg1.compareTo(msg2, differences);
				if (result != 0) {
					if (differences != null) {
						differences.push(new Pair<>(msg1, msg2));
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
	


}
