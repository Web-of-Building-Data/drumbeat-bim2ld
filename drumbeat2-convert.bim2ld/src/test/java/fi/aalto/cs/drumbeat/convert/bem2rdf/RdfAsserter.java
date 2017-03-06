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
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool;

public class RdfAsserter {
	
	public static interface Asserter<T> {
		void assertEquals(T o1, T o2);
	}
	
	private final RdfComparatorPool comparatorPool;
	
	public RdfAsserter(RdfComparatorPool comparatorPool) {
		this.comparatorPool = comparatorPool;
	}
	
	public static class LiteralAsserter implements Asserter<Literal> {

		@Override
		public void assertEquals(Literal o1, Literal o2) {			
			Assert.assertEquals(o1.getDatatypeURI(), o2.getDatatypeURI());			
			Assert.assertEquals(o1.getValue(), o2.getValue());
		}
		
	}
	
	public class RdfNodeAsserter implements Asserter<RDFNode> {		
		
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
				final Asserter<StmtIterator> stmtIteratorAsserter = new StmtIteratorAsserter(false);
				stmtIteratorAsserter.assertEquals(o1.asResource().listProperties(), o2.asResource().listProperties());
			}
		}
		
	}
	
	public class StatementAsserter implements Asserter<Statement> {
		
		private final boolean compareStatementSubjects;

		public StatementAsserter(boolean compareStatementSubjects) {			
			this.compareStatementSubjects = compareStatementSubjects;
		}
		
		@Override
		public void assertEquals(Statement o1, Statement o2) {			
			
			System.out.printf("Comparing %n\t%s%n\t%s%n", o1, o2);
			
			RdfNodeAsserter nodeAsserter = new RdfNodeAsserter();

			if (!compareStatementSubjects) {
				nodeAsserter.assertEquals(o1.getSubject(), o2.getSubject());
			}
			
			nodeAsserter.assertEquals(o1.getPredicate(), o2.getPredicate());			
			nodeAsserter.assertEquals(o1.getObject(), o2.getObject());
		}
		
	}
	
	
	public class StatementListAsserter implements Asserter<List<Statement>> {

		private final boolean compareStatementSubjects;

		public StatementListAsserter(boolean compareStatementSubjects) {
			this.compareStatementSubjects = compareStatementSubjects;
		}

		@Override
		public void assertEquals(List<Statement> list1, List<Statement> list2) {
			
			List<Statement>[] lists = new List<Statement>[]{list1, list2};
			
			if (removeStatementsWithLocalSubjects) {			
				removeStatementsWithAnonSubjects(o1);
				removeStatementsWithAnonSubjects(o2);
			}
			
//			final RdfComparator.StatementComparator statementComparator = new RdfComparator.StatementComparator(true, false, true);
			
			final RdfStatementComparator statementComparator =
					comparatorPool.createStatementComparator(compareStatementSubjects);
			
			o1.sort(statementComparator);
			o2.sort(statementComparator);
			
			Iterator<Statement> it1 = o1.iterator();
			Iterator<Statement> it2 = o2.iterator();

			final StatementAsserter statementAsserter = new StatementAsserter();

			while (it1.hasNext()) {				
				Statement s1 = it1.next();
				Statement s2 = it2.next();
				statementAsserter.assertEquals(s1, s2);				
			}
			
//			Assert.assertFalse(it2.hasNext());
		}
		
	}
	
	public class ModelAsserter implements Asserter<Model> {

		@Override
		public void assertEquals(Model model1, Model model2) {			
		}
		
		private List<Statement> getStatementsWithNonLocalSubjects(List<Statement> statementList) {			
			Iterator<Statement> it = statementList.iterator();			
			while (it.hasNext()) {
				Statement s = it.next();
				if (s.getSubject().isAnon()) {
					it.remove();
				}
			}
		}
		
		
		
	}
	
}
