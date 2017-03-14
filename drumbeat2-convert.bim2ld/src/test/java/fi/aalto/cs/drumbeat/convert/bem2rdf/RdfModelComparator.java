package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainer;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerBuilder;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerPrinter;
import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool;

public class RdfModelComparator {
	
	private static final Logger logger = Logger.getLogger(RdfModelComparator.class); 
	
	private final RdfComparatorPool comparatorPool;
	private RdfMsgContainer lastExpectedMsgContainer;
	private RdfMsgContainer lastActualMsgContainer;
	private Stack<Pair<Object, Object>> lastComparisonDifferences;
	
	public RdfModelComparator(RdfComparatorPool comparatorPool) {
		this.comparatorPool = comparatorPool;
	}
	
	public RdfModelComparator(Function<Resource, Boolean> localResourceChecker) {
		this.comparatorPool = new RdfComparatorPool(localResourceChecker);
	}
	
	public int compare(String modelPath1, Model model2) throws RdfModelComparatorException, IOException {
		Model model1 = TestHelper.readJenaModel(modelPath1);
		return compare(model1, model2);
	}	

	public int compare(Model model1, Model model2) throws RdfModelComparatorException {
		
		lastExpectedMsgContainer = null;
		lastActualMsgContainer = null;
		lastComparisonDifferences = null;
		
		try {
			lastExpectedMsgContainer = RdfMsgContainerBuilder.build(model1, comparatorPool);
			lastActualMsgContainer = RdfMsgContainerBuilder.build(model2, comparatorPool);
			
			lastComparisonDifferences = new Stack<>();		
			int result = lastExpectedMsgContainer.compareTo(lastActualMsgContainer, lastComparisonDifferences);		
			if (result != 0) {
				Map<String, String> nsPrefixMap = new HashMap<>();
				nsPrefixMap.putAll(model1.getNsPrefixMap());
				nsPrefixMap.putAll(model2.getNsPrefixMap());
				printDifferences(nsPrefixMap, lastComparisonDifferences);
			}
			return result;
//			Assert.assertEquals(expectedEquals, result == 0);
			
		} catch (RdfChecksumException e) {
			throw new RdfModelComparatorException(e);			
		}
	}
	
	private void printDifferences(Map<String, String> nsPrefixMap, Stack<Pair<Object, Object>> differences) throws RdfModelComparatorException {
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
			throw new RdfModelComparatorException(e);
		}
	}
	
	public RdfMsgContainer getLastExpectedMsgContainer() {
		return lastExpectedMsgContainer;
	}
	
	public RdfMsgContainer getLastActualMsgContainer() {
		return lastActualMsgContainer;
	}
	
	public Stack<Pair<Object, Object>> getLastAssertionDifferences() {
		return lastComparisonDifferences;
	}
		
}
