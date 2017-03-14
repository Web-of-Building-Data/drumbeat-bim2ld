package fi.aalto.cs.drumbeat.rdf.data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.apache.jena.rdf.model.*;

import fi.aalto.cs.drumbeat.common.digest.ByteArray;

public class RdfComparatorPool {

	private final RdfChecksumCalculator checksumCalculator;
	private final RdfNodeTypeChecker nodeTypeChecker;
	
	/**
	 * Creates a {@link RdfComparatorPool} with default {@link RdfNodeTypeChecker} 
	 */
//	public RdfComparatorPool() {
//		this((RdfNodeTypeChecker)null);
//	}
	
	public RdfComparatorPool(Function<Resource, Boolean> localResourceChecker) {
		this(new RdfNodeTypeChecker(localResourceChecker));
	}	
	
	/**
	 * Creates a {@link RdfComparatorPool} with a specified {@link RdfNodeTypeChecker}
	 * 
	 *  @param nodeTypeChecker
	 *  	RDF node type checker
	 */
	public RdfComparatorPool(RdfNodeTypeChecker nodeTypeChecker) {
		this.checksumCalculator = new RdfChecksumCalculator(nodeTypeChecker);
		this.nodeTypeChecker = checksumCalculator.getNodeTypeChecker();
	}
	
	public RdfNodeTypeChecker getNodeTypeChecker() {
		return nodeTypeChecker;
	}
	
	public RdfChecksumCalculator getChecksumCalculator() {
		return checksumCalculator;
	}
	
//	public RdfModelComparator createModelComparator() {
//		return new RdfModelComparator();
//	}
	
	public RdfStatementComparator createStatementComparator(boolean compareStatementSubjects) {
		return new RdfStatementComparator(compareStatementSubjects);
	}

	public RdfNodeComparator createNodeComparator() {
		return new RdfNodeComparator();
	}	
	
	
//	/**
//	 * RdfModelComparator 
//	 *
//	 */
//	public class RdfModelComparator implements Comparator<Model> {
//		
//		public RdfComparatorPool getComparatorPool() {
//			return RdfComparatorPool.this;
//		}		
//		
//		@Override
//		public int compare(Model model1, Model model2) {
//			
//			StmtIterator it1 = model1.listStatements();
//			StmtIterator it2 = model2.listStatements();
//			RdfStmtIteratorComparator stmtIteratorComparator = new RdfStmtIteratorComparator(true); 
//			return stmtIteratorComparator.compare(it1, it2);
//		}
//		
//	}
	
	
	/**
	 * RdfStmtIteratorComparator 
	 *
	 */
	public class RdfStmtIteratorComparator implements Comparator<StmtIterator> {

		private final boolean compareStatementSubjects;

		public RdfStmtIteratorComparator(boolean compareStatementSubjects) {
			this.compareStatementSubjects = compareStatementSubjects;
		}
		
		public RdfComparatorPool getComparatorPool() {
			return RdfComparatorPool.this;
		}		
		
		@Override
		public int compare(StmtIterator o1, StmtIterator o2) {
			
			final RdfStatementListComparator statementListComparator = new RdfStatementListComparator(compareStatementSubjects);
			
			List<Statement> l1 = o1.toList();
			List<Statement> l2 = o2.toList();
			
			return statementListComparator.compare(l1, l2);
		}
		
	}
	
	
	
	/**
	 * RdfStmtIteratorComparator
	 *
	 */
	public class RdfStatementListComparator implements Comparator<List<Statement>> {

		private final boolean compareStatementSubjects;

		public RdfStatementListComparator(boolean compareStatementSubjects) {
			this.compareStatementSubjects = compareStatementSubjects;
		}
		
		public RdfComparatorPool getComparatorPool() {
			return RdfComparatorPool.this;
		}		
		
		@Override
		public int compare(List<Statement> o1, List<Statement> o2) {
			
			final RdfStatementComparator statementComparator = new RdfStatementComparator(compareStatementSubjects);
			
			o1.sort(statementComparator);
			o2.sort(statementComparator);
			
			Iterator<Statement> it1 = o1.iterator();
			Iterator<Statement> it2 = o2.iterator();
			
			int result;
			
			while (it1.hasNext()) {
				
				if (!it2.hasNext()) {
					return 1;
				}
				
				Statement s1 = it1.next();
				Statement s2 = it2.next();

				if ((result = statementComparator.compare(s1, s2)) != 0) {
					return result;
				}				
			}
			
			if (it2.hasNext()) {
				return -1;
			}
			
			return 0;
		}
		
	}
	
	
	
	
	public class RdfStatementComparator implements Comparator<Statement> {

		private final boolean compareStatementSubjects;

		public RdfStatementComparator() {
			this(true);
		}

		public RdfStatementComparator(boolean compareStatementSubjects) {
			this.compareStatementSubjects = compareStatementSubjects;
		}
		
		public RdfComparatorPool getComparatorPool() {
			return RdfComparatorPool.this;
		}		
		
		@Override
		public int compare(Statement o1, Statement o2) {
			
			final RdfNodeComparator nodeComparator = new RdfNodeComparator();
			
			int result;
			if (compareStatementSubjects && (result = nodeComparator.compare(o1.getSubject(), o2.getSubject())) != 0) {
				return result;
			}
			
			if ((result = nodeComparator.compare(o1.getPredicate(), o2.getPredicate())) != 0) {
				return result;
			}

			return nodeComparator.compare(o1.getObject(), o2.getObject());
		}
		
	}


	public class RdfNodeComparator implements Comparator<RDFNode> {
		
		public RdfComparatorPool getComparatorPool() {
			return RdfComparatorPool.this;
		}		
		
		@Override
		public int compare(RDFNode o1, RDFNode o2) {

			RdfNodeTypeEnum type1 = nodeTypeChecker.getNodeType(o1);
			RdfNodeTypeEnum type2 = nodeTypeChecker.getNodeType(o2);
			
			Integer result;
			if ((result = type1.compareTo(type2)) != 0) {
				return result;			
			}
			
			switch (type1) {
			
			case Literal:
				final Comparator<Literal> literalComparator = new RdfLiteralComparator();
				return literalComparator.compare(o1.asLiteral(), o2.asLiteral());
				
			case Uri:
				return o1.asResource().getURI().compareTo(o2.asResource().getURI());
				
			case BlankNode:
			default:
				
				try {
					ByteArray checksum1 = checksumCalculator.getChecksum(o1.asResource());
					ByteArray checksum2 = checksumCalculator.getChecksum(o2.asResource());
					return checksum1.compareTo(checksum2);
					
				} catch (RdfChecksumException e) {
					
					throw new RuntimeException(e);
					
				}
				
			}
			
		}
		
	}
	
	public class RdfLiteralComparator implements Comparator<Literal> {
		
		public RdfComparatorPool getComparatorPool() {
			return RdfComparatorPool.this;
		}		
		
		@Override
		public int compare(Literal o1, Literal o2) {
			
			int result;
			if ((result = o1.getLexicalForm().toString().compareTo(o2.getLexicalForm().toString())) != 0) {
				return result;
			}
			
			return o1.getDatatypeURI().compareTo(o2.getDatatypeURI());
		}
		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new RdfComparatorPool(nodeTypeChecker);
	}
	
	
}
