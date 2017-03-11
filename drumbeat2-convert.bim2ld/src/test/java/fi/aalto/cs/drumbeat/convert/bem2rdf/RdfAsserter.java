package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;
import java.util.Stack;
import java.util.function.Function;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.junit.Assert;

import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainer;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerBuilder;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerPrinter;
import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.convert.bem2rdf.impl.RdfAsserterException;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool;

public class RdfAsserter {
	
	private final RdfComparatorPool comparatorPool;
	
	public RdfAsserter(RdfComparatorPool comparatorPool) {
		this.comparatorPool = comparatorPool;
	}
	
	public RdfAsserter(Function<Resource, Boolean> localResourceChecker) {
		this.comparatorPool = new RdfComparatorPool(localResourceChecker);
	}
	
	public void assertEquals(String modelPath1, Model model2) throws RdfAsserterException, IOException {
		Model model1 = Bem2RdfTestHelper.readModel(modelPath1);
		internalAssertEquals(model1, model2, true);
	}	

	public void assertEquals(Model model1, Model model2) throws RdfAsserterException {
		internalAssertEquals(model1, model2, true);
	}
	
	public void assertNotEquals(Model model1, Model model2) throws RdfAsserterException {
		internalAssertEquals(model1, model2, false);
	}
	
	private void internalAssertEquals(Model model1, Model model2, boolean expectedEquals) throws RdfAsserterException {
		try {
			RdfMsgContainer msgContainer1 = RdfMsgContainerBuilder.build(model1, comparatorPool);
			RdfMsgContainer msgContainer2 = RdfMsgContainerBuilder.build(model2, comparatorPool);
			
			Stack<Pair<Object, Object>> differences = new Stack<>();		
			int result = msgContainer1.compareTo(msgContainer2, differences);		
			if (expectedEquals && (result != 0)) {
				printDifferences(differences);
			}
			
			Assert.assertEquals(expectedEquals, result == 0);
		} catch (RdfChecksumException e) {
			throw new RdfAsserterException(e);			
		}
	}
	
	private void printDifferences(Stack<Pair<Object, Object>> differences) throws RdfAsserterException {
		RdfMsgContainerPrinter printer = new RdfMsgContainerPrinter(null, comparatorPool.getChecksumCalculator());
		try {
			for (Pair<Object, Object> difference : differences) {
				System.out.printf("Expected %s <%s>, but was %s <%s>%n",
						difference.getKey().getClass().getSimpleName(),
						printer.toString(difference.getKey()),
						difference.getValue().getClass().getSimpleName(),
						printer.toString(difference.getValue()));
			}
		} catch (RdfChecksumException e) {
			throw new RdfAsserterException(e);
		}
	}
		
}
