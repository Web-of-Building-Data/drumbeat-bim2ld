package fi.aalto.cs.drumbeat.data.ifc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemUnsupportedDataTypeException;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcDatasetParser;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcSchemaParser;
import fi.aalto.cs.drumbeat.data.step.parsers.StepDatasetParser;

import static org.junit.Assert.*;

public class IfcDatasetParser_Test {
	
	public static final String RESOURCES_FOLDER = "src/test/resources/";
	
	@BeforeClass
    public static void runOnceBeforeClass() throws IOException, BemException {
		BemParserUtil.getSchemaParsers().clear();
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
		List<BemSchema> schemas = BemParserUtil.parseSchemas(RESOURCES_FOLDER, null, true, true);
		for (BemSchema schema : schemas) {
			BemSchemaPool.addSchema(schema);			
		}
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
	public void test_parseSampleIfc() throws BemException, BemNotFoundException, IOException {
		test_parse(RESOURCES_FOLDER + "sample.ifc");
	}
//
//	@Test
//	public void test_parseIfc4Add1() throws BemException, BemNotFoundException, IOException {
//		test_parse("IFC4_ADD1.exp");
//	}

	private void test_parse(String filePath) throws BemException, BemNotFoundException, IOException 
	{
		BemParserUtil.registerDatasetParser(new IfcDatasetParser());
		
		BemDataset dataset = BemParserUtil.parseDataset(filePath, true);
	}
	
	

}
