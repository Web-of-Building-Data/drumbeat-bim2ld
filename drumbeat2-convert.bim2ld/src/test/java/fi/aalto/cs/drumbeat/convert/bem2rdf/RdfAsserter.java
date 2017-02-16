package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import fi.aalto.cs.drumbeat.rdf.data.RdfComparator;
import fi.aalto.cs.drumbeat.rdf.data.RdfNodeTypeEnum;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparator.RdfNodeComparatorCache;

public class RdfAsserter {
	
	public static int DEPTH_OF_ANON_PROPERTIES_TO_COMPARE = 10;
	
	public interface Asserter<T> {
		void assertEquals(T o1, T o2);
	}
	
	public static class LiteralAsserter implements Asserter<Literal> {

		@Override
		public void assertEquals(Literal o1, Literal o2) {			
			Assert.assertEquals(o1.getDatatypeURI(), o2.getDatatypeURI());			
			Assert.assertEquals(o1.getValue(), o2.getValue());
		}
		
	}
	
	public static class RdfNodeAsserter implements Asserter<RDFNode> {
		
		private final int currentDepth; 
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;
		
		public RdfNodeAsserter(int currentDepth, boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.currentDepth = currentDepth;
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		public static RdfNodeAsserter createSubjectNodeAsserter(boolean compareAnonIds) {
			return new RdfNodeAsserter(0, compareAnonIds, 0);
		}
		
		public static RdfNodeAsserter createPredicateNodeAsserter() {
			return new RdfNodeAsserter(0, false, 0);
		}
		
		@Override
		public void assertEquals(RDFNode o1, RDFNode o2) {

			RdfNodeTypeEnum type1 = RdfNodeTypeEnum.getType(o1);
			RdfNodeTypeEnum type2 = RdfNodeTypeEnum.getType(o2);
			Assert.assertEquals(type1, type2);
			
			if (type1.equals(RdfNodeTypeEnum.Literal)) {
				final Asserter<Literal> literalAsserter = new LiteralAsserter();
				literalAsserter.assertEquals(o1.asLiteral(), o2.asLiteral());
			} else if (type1.equals(RdfNodeTypeEnum.Uri)) {
				Assert.assertEquals(o1.asResource().getURI(), o2.asResource().getURI());
			} else {
				assert(o1.isAnon());
				
				if (compareAnonIds && o1.getModel().equals(o2.getModel())) {
					Assert.assertEquals(o1.asResource().getId().toString(), o2.asResource().getId().toString());
				}
				
				if (depthOfAnonPropertiesToCompare > 0) {
					final Asserter<StmtIterator> stmtIteratorAsserter = new StmtIteratorAsserter(currentDepth + 1, false, compareAnonIds, depthOfAnonPropertiesToCompare - 1);
					stmtIteratorAsserter.assertEquals(o1.asResource().listProperties(), o2.asResource().listProperties());
				}				
			}
		}
		
	}
	
	public static class StatementAsserter implements Asserter<Statement> {

		private final int currentDepth;
		private final boolean compareStatementSubjects;
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;

		public StatementAsserter(int currentDepth, boolean compareStatementSubjects, boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.currentDepth = currentDepth;
			this.compareStatementSubjects = compareStatementSubjects;
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		@Override
		public void assertEquals(Statement o1, Statement o2) {			
			
			System.out.printf("Comparing %n\t%s%n\t%s%n", o1, o2);

			if (!compareStatementSubjects) {
				RdfNodeAsserter.createSubjectNodeAsserter(compareAnonIds).assertEquals(o1.getSubject(), o2.getSubject());
			}
			
			RdfNodeAsserter.createPredicateNodeAsserter().assertEquals(o1.getPredicate(), o2.getPredicate());
			
			RdfNodeAsserter objectNodeAsserter = new RdfNodeAsserter(currentDepth, compareAnonIds, depthOfAnonPropertiesToCompare);
			objectNodeAsserter.assertEquals(o1.getObject(), o2.getObject());
		}
		
	}
	
	
	public static class StatementListAsserter implements Asserter<List<Statement>> {

		private final int currentDepth;
		private final boolean compareStatementSubjects;
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;

		public StatementListAsserter(int currentDepth, boolean compareStatementSubjects, boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.currentDepth = currentDepth;
			this.compareStatementSubjects = compareStatementSubjects;
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		private static void removeStatementsWithAnonSubjects(List<Statement> statementList) {			
			Iterator<Statement> it = statementList.iterator();			
			while (it.hasNext()) {
				Statement s = it.next();
				if (s.getSubject().isAnon()) {
					it.remove();
				}
			}
		}
		
		@Override
		public void assertEquals(List<Statement> o1, List<Statement> o2) {
			
			if (currentDepth == 0) {			
				removeStatementsWithAnonSubjects(o1);
				removeStatementsWithAnonSubjects(o2);
			}
			
//			final RdfComparator.StatementComparator statementComparator = new RdfComparator.StatementComparator(true, false, true);
			
			final RdfComparator.StatementComparator statementComparator =
					new RdfComparator.StatementComparator(compareStatementSubjects, compareAnonIds, depthOfAnonPropertiesToCompare);
			
			o1.sort(statementComparator);
			o2.sort(statementComparator);
			
			Iterator<Statement> it1 = o1.iterator();
			Iterator<Statement> it2 = o2.iterator();

			final StatementAsserter statementAsserter = new StatementAsserter(currentDepth, compareStatementSubjects, compareAnonIds, depthOfAnonPropertiesToCompare);

			while (it1.hasNext()) {				
				Statement s1 = it1.next();
				Statement s2 = it2.next();
				statementAsserter.assertEquals(s1, s2);				
			}
			
//			Assert.assertFalse(it2.hasNext());
		}
		
	}
		

	
	public static class StmtIteratorAsserter implements Asserter<StmtIterator> {

		private final int currentDepth;
		private final boolean compareStatementSubjects;
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;

		public StmtIteratorAsserter(int currentDepth, boolean compareStatementSubjects, boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.currentDepth = currentDepth;
			this.compareStatementSubjects = compareStatementSubjects;
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		@Override
		public void assertEquals(StmtIterator o1, StmtIterator o2) {
			
			StatementListAsserter statementListAsserter = new StatementListAsserter(currentDepth, compareStatementSubjects, compareAnonIds, depthOfAnonPropertiesToCompare);
			
			List<Statement> l1 = o1.toList();
			List<Statement> l2 = o2.toList();
			
			statementListAsserter.assertEquals(l1, l2);
		}
		
	}
	
	
	public static class ModelAsserter implements Asserter<Model> {

		public ModelAsserter() {
		}
		
		@Override
		public void assertEquals(Model o1, Model o2) {
			
			final StmtIteratorAsserter stmtIteratorAsserter = new StmtIteratorAsserter(0, true, false, DEPTH_OF_ANON_PROPERTIES_TO_COMPARE);
			RdfNodeComparatorCache.getInstance(o1).clear();
			RdfNodeComparatorCache.getInstance(o2).clear();
			
			stmtIteratorAsserter.assertEquals(o1.listStatements(), o2.listStatements());
			
		}
		
	}

	
//	public static class FullResourceAsserter implements Asserter<Resource> {
//
//		@Override
//		public void assertEquals(Resource o1, Resource o2) {
//			final Asserter<RDFNode> nodeAsserter = new RdfNodeAsserter2(false, true);
//			nodeAsserter.assertEquals(o1, o2);
//			
//			final Asserter<StmtIterator> stmtIteratorAsserter = new StmtIteratorAsserter2(false, false, true);
//			stmtIteratorAsserter.assertEquals(o1.listProperties(), o2.listProperties());
//		}
//		
//	}
//	
	
}
