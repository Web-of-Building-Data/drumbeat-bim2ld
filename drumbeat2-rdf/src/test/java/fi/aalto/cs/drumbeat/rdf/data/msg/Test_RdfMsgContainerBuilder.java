package fi.aalto.cs.drumbeat.rdf.data.msg;

import static org.junit.Assert.assertEquals;

import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.Test;

import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;

public class Test_RdfMsgContainerBuilder {
	
	public static final String BASE_URI = "http://example.org/";

	public Test_RdfMsgContainerBuilder() {
	}
	
	@Test
	public void testBuilder() throws RdfChecksumException {
		Model[] models = new Model[] {
				ModelFactory.createDefaultModel(),
				ModelFactory.createDefaultModel(),
		};
		
		final int tripleSize = 3; 
		
		Resource[] resources = new Resource[models.length];

		for (int i = 0; i < models.length; ++i) {
			
			resources[i] = models[i].createResource(new AnonId(String.format("%f", Math.random())));

			for (int j = 0; j < tripleSize ; ++j) {
				resources[i].addLiteral(models[i].createProperty(BASE_URI + "value"), i == 0 ? j : tripleSize - j - 1);						
			}
			
			assertEquals(tripleSize, models[i].size());

		}		
		
		for (int i = 0; i < models.length; ++i) {
			RdfMsgContainerBuilder.build(models[i], r -> r.isAnon());
		}
		
	}

}
