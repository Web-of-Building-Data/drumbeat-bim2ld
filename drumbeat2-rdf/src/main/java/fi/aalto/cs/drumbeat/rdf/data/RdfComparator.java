package fi.aalto.cs.drumbeat.rdf.data;

import java.util.*;
import org.apache.jena.rdf.model.*;

import fi.aalto.cs.drumbeat.common.collections.Pair;

public class RdfComparator {
	
	public static class RdfNodeComparatorCache {
		
		private static final Map<Model, RdfNodeComparatorCache> cacheMap = new HashMap<>();
		
		private final Map<Pair<Resource, Resource>, Integer> cache;
		
		public RdfNodeComparatorCache() {
			cache = new HashMap<>();
		}
		
		public static RdfNodeComparatorCache getInstance(Model model) {
			RdfNodeComparatorCache cache = cacheMap.get(model);
			if (cache == null) {
				cache = new RdfNodeComparatorCache();
				cacheMap.put(model, cache);
			}
			return cache;
		}
		
		public void clear() {
			cache.clear();
		}
		
		private int compareAnonId(Resource r1, Resource r2) {
			return r1.getId().toString().compareTo(r2.getId().toString());
		}
		
		private void internalPut(Resource r1, Resource r2, int comparisonResult) {
//			System.out.printf("Putting comparison result: %s, %s, %d%n", r1, r2, comparisonResult);
			cache.put(new Pair<Resource, Resource>(r1, r2), comparisonResult);			
		}
		
		public void put(Resource r1, Resource r2, int comparisonResult) {
			assert(r1.isAnon() && r2.isAnon());
			if (compareAnonId(r1, r2) <= 0) {
				internalPut(r1, r2, comparisonResult);
			} else {
				internalPut(r2, r1, -comparisonResult);
			}
		}
		
		private Integer internalGet(Resource r1, Resource r2) {
			Integer result = cache.get(new Pair<Resource, Resource>(r1, r2));
//			System.out.printf("Getting comparison result: %s, %s, %d%n", r1, r2, result);
			return result;
		}
		
		public Integer get(Resource r1, Resource r2) {
			assert(r1.isAnon() && r2.isAnon());
			if (compareAnonId(r1, r2) <= 0) {
				return internalGet(r1, r2);
			} else {
				Integer result = internalGet(r2, r1);
				if (result != null) {
					result = -result;
				}
				return result;
			}
		}
		
	}
	
	
	
//	public static final Comparator<Literal> LITERAL_COMPARATOR = new LiteralComparator();
//	public static final Comparator<RDFNode> NODE_COMPARATOR_NOT_IGNORING_ = new RdfNodeComparator(false);
//	public static final Comparator<RDFNode> NODE_COMPARATOR_BY_IDS_AND_CHILDREN = new RdfNodeComparator(true);
	
	public static class LiteralComparator implements Comparator<Literal> {
		
		public LiteralComparator() {			
		}

		@Override
		public int compare(Literal o1, Literal o2) {
			
			int result;
			if ((result = o1.getDatatypeURI().compareTo(o2.getDatatypeURI())) != 0) {
				return result;
			}
			
			return o1.getValue().toString().compareTo(o2.getValue().toString());
		}
		
	}
	
	public static class RdfNodeComparator implements Comparator<RDFNode> {
		
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;
		
		public RdfNodeComparator(boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		public static RdfNodeComparator createSubjectNodeComparator(boolean compareAnonIds) {
			return new RdfNodeComparator(compareAnonIds, 0);
		}
		
		public static RdfNodeComparator createPredicateNodeComparator() {
			return new RdfNodeComparator(false, 0);
		}

		@Override
		public int compare(RDFNode o1, RDFNode o2) {

			RdfNodeTypeEnum type1 = RdfNodeTypeEnum.getType(o1);
			RdfNodeTypeEnum type2 = RdfNodeTypeEnum.getType(o2);
			Integer result;
			if ((result = type1.compareTo(type2)) != 0) {
				return result;			
			}
			
			if (type1.equals(RdfNodeTypeEnum.Literal)) {
				final Comparator<Literal> literalComparator = new LiteralComparator();
				return literalComparator.compare(o1.asLiteral(), o2.asLiteral());
			} else if (type1.equals(RdfNodeTypeEnum.Uri)) {
				return o1.asResource().getURI().compareTo(o2.asResource().getURI());
			} else {
				assert(o1.isAnon() && o2.isAnon());
				
				Resource r1 = o1.asResource();
				Resource r2 = o2.asResource();
				
				boolean sameModel = r1.getModel().equals(r2.getModel());
				
				if (sameModel) {
					
					if (compareAnonIds) {
						return r1.getId().toString().compareTo(r2.getId().toString());						
					} else if (r1.equals(r2)) {
						return 0;
					} else {
						
						result = RdfNodeComparatorCache.getInstance(r1.getModel()).get(r1, r2);
						if (result != null) {
							return result;
						}
						
					}
				}
				
				if (depthOfAnonPropertiesToCompare > 0) {
					final StmtIterator properties1 = o1.asResource().listProperties();
					final StmtIterator properties2 = o2.asResource().listProperties();

					final Comparator<StmtIterator> stmtIteratorComparator = new StmtIteratorComparator(false, compareAnonIds, depthOfAnonPropertiesToCompare);					
					if ((result = stmtIteratorComparator.compare(properties1, properties2)) != 0) {
						
						if (sameModel) {
							RdfNodeComparatorCache.getInstance(r1.getModel()).put(r1, r2, result);
						}
						return result;
					}
				}
				
				if (sameModel) {
					RdfNodeComparatorCache.getInstance(r1.getModel()).put(r1, r2, 0);
				}
				return 0;				
			}
		}
		
	}
	
	public static class StatementComparator implements Comparator<Statement> {

		private final boolean compareStatementSubjects;
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;

		public StatementComparator(boolean compareStatementSubjects, boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.compareStatementSubjects = compareStatementSubjects;
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		@Override
		public int compare(Statement o1, Statement o2) {
			
			int result;
			if (compareStatementSubjects) {
				if ((result = RdfNodeComparator.createSubjectNodeComparator(compareAnonIds).compare(o1.getSubject(), o2.getSubject())) != 0) {
					return result;
				}
			}
			
			if ((result = RdfNodeComparator.createPredicateNodeComparator().compare(o1.getPredicate(), o2.getPredicate())) != 0) {
				return result;
			}

			RdfNodeComparator objectNodeComparator = new RdfNodeComparator(compareAnonIds, depthOfAnonPropertiesToCompare);
			return objectNodeComparator.compare(o1.getObject(), o2.getObject());
		}
		
	}
	
	
	public static class StatementListComparator implements Comparator<List<Statement>> {

		private final boolean compareStatementSubjects;
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;

		public StatementListComparator(boolean compareStatementSubjects, boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.compareStatementSubjects = compareStatementSubjects;
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		@Override
		public int compare(List<Statement> o1, List<Statement> o2) {
			
			final StatementComparator statementComparator =
					new RdfComparator.StatementComparator(compareStatementSubjects, compareAnonIds, depthOfAnonPropertiesToCompare);
			
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
	
		

	
	public static class StmtIteratorComparator implements Comparator<StmtIterator> {

		private final boolean compareStatementSubjects;
		private final boolean compareAnonIds;
		private final int depthOfAnonPropertiesToCompare;

		public StmtIteratorComparator(boolean compareStatementSubjects, boolean compareAnonIds, int depthOfAnonPropertiesToCompare) {
			this.compareStatementSubjects = compareStatementSubjects;
			this.compareAnonIds = compareAnonIds;
			this.depthOfAnonPropertiesToCompare = depthOfAnonPropertiesToCompare;
		}
		
		@Override
		public int compare(StmtIterator o1, StmtIterator o2) {
			
			final StatementListComparator statementListComparator = new StatementListComparator(compareStatementSubjects, compareAnonIds, depthOfAnonPropertiesToCompare);
			
			List<Statement> l1 = o1.toList();
			List<Statement> l2 = o2.toList();
			
			return statementListComparator.compare(l1, l2);
		}
		
	}	
	
	
}
