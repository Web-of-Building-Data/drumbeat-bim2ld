package fi.aalto.cs.drumbeat.rdf.data;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import fi.aalto.cs.drumbeat.common.collections.SortedList;

public class RdfConverterUtils {
	
//	/**
//	 * Converts a {@link StmtIterator} to a {@link List}
//	 * Created to use instead of stmtIterator.toList()
//	 *  
//	 * @param stmtIterator
//	 * @param sorted
//	 * @return
//	 */
//	public static List<Statement> toList(StmtIterator stmtIterator, boolean sorted) {
//		
//		List<Statement> statementList;
//		if (sorted) {
//			Comparator<Statement>  comparator = new RdfComparator.StatementComparator(ignoreStatementSubjects, ignoreAnonIds, ignoreAnonProperties)
//			statementList = new SortedList<>(RdfComparator.StatementComparator);
//		} else {
//			statementList = new LinkedList<>();
//		}
//	}


}
