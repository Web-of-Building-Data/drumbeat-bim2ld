package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;
import org.junit.Assert;

import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainer;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerBuilder;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerPrinter;
import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool;

public class RdfAsserter {
	
	private static final Logger logger = Logger.getLogger(RdfAsserter.class); 
	
	private final RdfComparatorPool comparatorPool;
	
	public RdfAsserter(RdfComparatorPool comparatorPool) {
		this.comparatorPool = comparatorPool;
	}
	
	public RdfAsserter(Function<Resource, Boolean> localResourceChecker) {
		this.comparatorPool = new RdfComparatorPool(localResourceChecker);
	}
	
	public void assertEquals(String modelPath1, Model model2) throws RdfAsserterException, IOException {
		Model model1 = TestHelper.readJenaModel(modelPath1);
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
				Map<String, String> nsPrefixMap = new HashMap<>();
				nsPrefixMap.putAll(model1.getNsPrefixMap());
				nsPrefixMap.putAll(model2.getNsPrefixMap());
				printDifferences(nsPrefixMap, differences);
			}
			
			Assert.assertEquals(expectedEquals, result == 0);
		} catch (RdfChecksumException e) {
			throw new RdfAsserterException(e);			
		}
	}
	
	private void printDifferences(Map<String, String> nsPrefixMap, Stack<Pair<Object, Object>> differences) throws RdfAsserterException {
		RdfMsgContainerPrinter printer = new RdfMsgContainerPrinter(nsPrefixMap, comparatorPool.getChecksumCalculator());
		try {
			for (Pair<Object, Object> difference : differences) {
				
				Object expectedObject = difference.getKey();
				Object actualObject = difference.getValue();
				
//				String expectedObjectType;
//				String actualObjectType;
//				
//				if (expectedObject instanceof Resource) {
//					expectedObjectType = Resource.class.getSimpleName();
//					expectedObject = comparatorPool.getChecksumCalculator().getChecksum((Resource)expectedObject).toBase64String();
//				} else {
//					expectedObjectType = expectedObject.getClass().getSimpleName();
//				}
//				
//				if (actualObject instanceof Resource) {
//					actualObjectType = Resource.class.getSimpleName();
//					actualObject = comparatorPool.getChecksumCalculator().getChecksum((Resource)actualObject).toBase64String();
//				} else {
//					actualObjectType = actualObject.getClass().getSimpleName();
//				}
//				
//				
//				String message = String.format("Expected %s <%s>, but was %s <%s>",
//						expectedObjectType, expectedObject, actualObjectType, actualObject);
				
				String message = String.format("Expected %s <%s>, but was %s <%s>",
						expectedObject.getClass().getSimpleName(),
						printer.toString(expectedObject),
						actualObject.getClass().getSimpleName(),
						printer.toString(actualObject));
				
				
//				System.err.println(message);
				logger.warn(message);
			}
		} catch (RdfChecksumException e) {
			throw new RdfAsserterException(e);
		}
	}
		
}
