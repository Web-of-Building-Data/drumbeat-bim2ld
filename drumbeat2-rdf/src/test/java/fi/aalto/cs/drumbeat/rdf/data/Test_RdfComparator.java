package fi.aalto.cs.drumbeat.rdf.data;

import static org.junit.Assert.*;

import java.util.Stack;
import java.util.function.Function;

import org.apache.jena.rdf.model.*;
import org.junit.Test;

import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainer;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerBuilder;
import fi.aalto.cs.drumbeat.rdf.data.msg.RdfMsgContainerPrinter;

public class Test_RdfComparator {
	
	public static final String BASE_URI = "http://example.org/";

	@Test
	public void test_compareSimpleModels() throws RdfChecksumException {
		Model[] models = new Model[] {
				ModelFactory.createDefaultModel(),
				ModelFactory.createDefaultModel(),
		};
		
		final int tripleSize = 3; 
		
		for (int i = 0; i < models.length; ++i) {
			
			Resource resource = models[i].createResource(new AnonId(String.format("%f", Math.random())));

			for (int j = 0; j < tripleSize ; ++j) {
				resource.addLiteral(models[i].createProperty(BASE_URI + "value"), i == 0 ? j : tripleSize - j - 1);						
			}
			
			assertEquals(tripleSize, models[i].size());

		}
		
		int result = compair(models[0], models[1], r -> r.isAnon());		
		assertEquals(0, result);
		
	}
	
	@Test
	public void test_compareSimpleModels_WithChecksum_Equal() throws RdfChecksumException {
		Model[] models = new Model[] {
				ModelFactory.createDefaultModel(),
				ModelFactory.createDefaultModel(),
		};
		
		final int tripleSize = 3; 
		
		Resource[] resources = new Resource[models.length];

		for (int i = 0; i < models.length; ++i) {			
			resources[i] = models[i].createResource();

			for (int j = 0; j < tripleSize ; ++j) {
				resources[i]
						.addLiteral(models[i].createProperty(BASE_URI + "value"), i == 0 ? j : tripleSize - j - 1);						
			}
			
			assertEquals(tripleSize, models[i].size());
			
			models[i]
					.createResource(BASE_URI + "node1")
					.addProperty(models[i].createProperty(BASE_URI + "hasProperty"), resources[i]);

			assertEquals(tripleSize + 1, models[i].size());
		}
		
		int result = compair(models[0], models[1], r -> r.isAnon());		
		assertEquals(0, result);
		
	}
	
	
	@Test
	public void test_compareSimpleModels_WithChecksum_NotEqual() throws RdfChecksumException {
		Model[] models = new Model[] {
				ModelFactory.createDefaultModel(),
				ModelFactory.createDefaultModel(),
		};
		
		final int tripleSize = 3; 
		
		for (int i = 0; i < models.length; ++i) {			
			Resource resource = models[i].createResource();

			for (int j = 0; j < tripleSize ; ++j) {
				resource
						.addLiteral(models[i].createProperty(BASE_URI + "value"), i == 0 ? j : tripleSize - j);						
			}
			
			assertEquals(tripleSize, models[i].size());
			
			models[i]
					.createResource(BASE_URI + "node1")
					.addProperty(models[i].createProperty(BASE_URI + "hasProperty"), resource);

			assertEquals(tripleSize + 1, models[i].size());
		}
		
		int result = compair(models[0], models[1], r -> r.isAnon());		
		assertNotEquals(0, result);
		
	}
	
	public int compair(Model model1, Model model2, Function<Resource, Boolean> localResourceChecker) throws RdfChecksumException {
		
		RdfComparatorPool comparatorPool = new RdfComparatorPool(localResourceChecker);		
		RdfMsgContainer msgContainer1 = RdfMsgContainerBuilder.build(model1, comparatorPool, false);
		RdfMsgContainer msgContainer2 = RdfMsgContainerBuilder.build(model2, comparatorPool, false);
		
		Stack<Pair<Object, Object>> differences = new Stack<>();		
		int result = msgContainer1.compareTo(msgContainer2, differences);
		
		RdfMsgContainerPrinter printer = new RdfMsgContainerPrinter(null, comparatorPool.getChecksumCalculator());
		
		if (result != 0) {
			for (Pair<Object, Object> difference : differences) {
				System.out.printf("Expected %s <%s>, but was %s <%s>%n",
						difference.getKey().getClass().getSimpleName(),
						printer.toString(difference.getKey()),
						difference.getValue().getClass().getSimpleName(),
						printer.toString(difference.getValue()));
			}
		}
		
		return result;
		
	}
	

}
