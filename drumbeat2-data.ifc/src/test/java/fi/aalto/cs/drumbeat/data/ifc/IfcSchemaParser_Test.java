package fi.aalto.cs.drumbeat.data.ifc;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemUnsupportedDataTypeException;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcSchemaParser;

import static org.junit.Assert.*;

public class IfcSchemaParser_Test {
	
	public static final String RESOURCES_FOLDER = "src/test/java/resources/";
	
	@Before
	public void setUp() {
		BemParserUtil.getSchemaParsers().clear();
	}
	
	@Test(expected=FileNotFoundException.class)
	public void test_parseNonExistingFile() throws BemException, IOException {
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
		BemParserUtil.parseSchema("../resources/IFC/IFC2X3_TC1.ex", true);
	}

	@Test(expected=BemUnsupportedDataTypeException.class)
	public void test_parseWrongTypeFile() throws BemException, IOException {
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
		BemParserUtil.parseSchema("../resources/IFC/sample.ifc", true);
	}

	@Test(expected=BemUnsupportedDataTypeException.class)
	public void test_parseNoParserRegistered() throws BemException, IOException {
		BemParserUtil.parseSchema("../resources/IFC/IFC2X3_TC1.exp", true);
	}
	
	@Test
	public void test_parseIfc2x3() throws BemException, BemNotFoundException, IOException {
		test_parse("IFC2X3_TC1.exp");
	}

	@Test
	public void test_parseIfc4Add1() throws BemException, BemNotFoundException, IOException {
		test_parse("IFC4_ADD1.exp");
	}

	private void test_parse(String fileName) throws BemException, BemNotFoundException, IOException 
	{
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());		
		BemSchema schema = BemParserUtil.parseSchema(RESOURCES_FOLDER + fileName, true);
		assertNotNull(schema);
		
		BemTypeInfo typeIfcProject = schema.getTypeInfo("IfcProject");
		assertNotNull(typeIfcProject);
		assertEquals("IfcProject", typeIfcProject.getName());
		assertEquals(BemEntityTypeInfo.class, typeIfcProject.getClass());
		
		BemTypeInfo typeIfcProject2 = schema.getTypeInfo("IFCPROJECT");
		assertEquals(typeIfcProject, typeIfcProject2);
		
		BemTypeInfo typeInteger = schema.getTypeInfo("INTEGER");
		assertNotNull(typeInteger);
		assertEquals(BemPrimitiveTypeInfo.class, typeInteger.getClass());
		
		BemTypeInfo typeIfcInteger = schema.getTypeInfo("IFCINTEGER");
		assertNotNull(typeIfcInteger);
		assertEquals(BemDefinedTypeInfo.class, typeIfcInteger.getClass());
	}
	
	

}
