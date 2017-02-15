package fi.aalto.cs.drumbeat.data.ifc;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemPrimitiveValue;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcDatasetParser;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcSchemaParser;
import fi.aalto.cs.drumbeat.data.step.dataset.StepDataset;

import static org.junit.Assert.*;

public class IfcDatasetParser_Test {
	
	public static final String RESOURCES_FOLDER = "src/test/resources/";
	public static final String RESOURCES_SCHEMAS_FOLDER = RESOURCES_FOLDER + "schemas/";
	public static final String RESOURCES_DATASETS_FOLDER = RESOURCES_FOLDER + "datasets/";
	
	@BeforeClass
    public static void runOnceBeforeClass() throws IOException, BemException {
		BemParserUtil.getSchemaParsers().clear();
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
		List<BemSchema> schemas = BemParserUtil.parseSchemas(RESOURCES_SCHEMAS_FOLDER, null, true, true);
		BemSchemaPool.addAll(schemas);
    }
	
	@Before
	public void runBeforeTestMethod() {		
		BemParserUtil.getDatasetParsers().clear();
	}
	
//	@Test(expected=FileNotFoundException.class)
//	public void test_parseNonExistingFile() throws BemException, IOException {
//		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
//		BemParserUtil.parseSchema("../resources/IFC/IFC2X3_TC1.ex", true);
//	}
//
//	@Test(expected=BemUnsupportedDataTypeException.class)
//	public void test_parseWrongTypeFile() throws BemException, IOException {
//		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
//		BemParserUtil.parseSchema("../resources/IFC/sample.ifc", true);
//	}
//
//	@Test(expected=BemUnsupportedDataTypeException.class)
//	public void test_parseNoParserRegistered() throws BemException, IOException {
//		BemParserUtil.parseSchema("../resources/IFC/IFC2X3_TC1.exp", true);
//	}
//	
	@Test
	public void test_parseSampleIfc() throws BemException, IOException {
		StepDataset dataset = test_parse(RESOURCES_DATASETS_FOLDER + "sample.ifc");

		BemEntity projectEntity = dataset.getAnyEntityByType(IfcVocabulary.IfcTypes.IFC_PROJECT);
		assertNotNull(projectEntity);
		assertTrue(projectEntity.getTypeInfo().getName().equals(IfcVocabulary.IfcTypes.IFC_PROJECT));
		
		BemValue guidValue = projectEntity.getAttributeMap().getAny(IfcVocabulary.IfcAttributes.GLOBAL_ID);
		assertNotNull(guidValue);
		assertTrue(guidValue instanceof BemPrimitiveValue);
		assertEquals("0YvctVUKr0kugbFTf53O9L", ((BemPrimitiveValue)guidValue).getValue());
		
		List<BemValue> representationContextAttributes = projectEntity.getAttributeMap().getAll("RepresentationContexts");
		assertNotNull(representationContextAttributes);
		assertEquals(1, representationContextAttributes.size());
		BemValue representationContext = representationContextAttributes.get(0);
		assertNotNull(representationContext);
		assertTrue("Expected: representationContext instanceof BemEntity: " + representationContext.getClass(), representationContext instanceof BemEntity);
		
		BemEntityTypeInfo geometricRepresentationContextType = dataset.getSchema().getEntityTypeInfo("IFCGEOMETRICREPRESENTATIONCONTEXT");
		assertNotNull(geometricRepresentationContextType);
		assertEquals(geometricRepresentationContextType, ((BemEntity)representationContext).getTypeInfo());
		
		assertEquals(1, projectEntity.getIncomingAttributeMap().size());
		BemEntity decomposesEntity = (BemEntity)projectEntity.getIncomingAttributeMap().getAny("Decomposes");
		assertNull(decomposesEntity);		
		
		BemEntity relAggregatesEntity = (BemEntity)projectEntity.getIncomingAttributeMap().getAny("IsDecomposedBy");
		assertNotNull(relAggregatesEntity);		

		BemEntityTypeInfo relDecomposesEntityTypeInfo = dataset.getSchema().getEntityTypeInfo("IfcRelDecomposes"); 
		assertTrue(relAggregatesEntity.isInstanceOf(relDecomposesEntityTypeInfo));
		
		List<BemValue> siteEntities = relAggregatesEntity.getAttributeMap().getAll("relatedObjects");
		assertEquals(1, siteEntities.size());
		
		BemEntity siteEntity = (BemEntity)siteEntities.get(0);
		assertEquals("Default Site", ((BemPrimitiveValue)siteEntity.getAttributeMap().getAny("name")).getValue());		
		
	}
	
	
//
//	@Test
//	public void test_parseIfc4Add1() throws BemException, BemNotFoundException, IOException {
//		test_parse("IFC4_ADD1.exp");
//	}

	private StepDataset test_parse(String filePath) throws BemException, IOException 
	{
		BemParserUtil.registerDatasetParser(new IfcDatasetParser());
		
		BemDataset dataset = BemParserUtil.parseDataset(filePath, true);
		assertNotNull(dataset);
		assertTrue(dataset instanceof StepDataset);
		return (StepDataset)dataset;
	}

}
