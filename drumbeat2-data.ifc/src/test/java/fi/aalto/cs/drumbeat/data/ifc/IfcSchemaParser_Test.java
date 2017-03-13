package fi.aalto.cs.drumbeat.data.ifc;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.parsers.BemUnsupportedDataTypeException;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcSchemaParser;

import static org.junit.Assert.*;

public class IfcSchemaParser_Test {
	
	public static final String RESOURCES_FOLDER = "src/test/resources/";
	public static final String RESOURCES_SCHEMAS_FOLDER = RESOURCES_FOLDER + "schemas/";
	public static final String RESOURCES_DATASETS_FOLDER = RESOURCES_FOLDER + "datasets/";
	
	@Before
	public void setUp() {
		BemParserUtil.getSchemaParsers().clear();
	}
	
	@Test(expected=FileNotFoundException.class)
	public void test_parseNonExistingFile() throws BemException, IOException {
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
		test_parse(RESOURCES_SCHEMAS_FOLDER + "IFC2X3_TC1.ex");
	}

	@Test(expected=BemUnsupportedDataTypeException.class)
	public void test_parseWrongTypeFile() throws BemException, IOException {
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());
		test_parse(RESOURCES_DATASETS_FOLDER + "sample.ifc");
	}

	@Test(expected=BemUnsupportedDataTypeException.class)
	public void test_parseNoParserRegistered() throws BemException, IOException {
		BemParserUtil.parseSchema(RESOURCES_SCHEMAS_FOLDER + "IFC2X3_TC1.exp", true);
	}
	
	@Test
	public void test_parseIfc2x3() throws BemException, IOException {
		test_parse(RESOURCES_SCHEMAS_FOLDER + "IFC2X3_TC1.exp");
	}

	@Test
	public void test_parseIfc4Add1() throws BemException, IOException {
		test_parse(RESOURCES_SCHEMAS_FOLDER + "IFC4_ADD1.exp");
	}

	private void test_parse(String filePath) throws BemException, IOException 
	{
		BemParserUtil.registerSchemaParser(new IfcSchemaParser());		
		BemSchema schema = BemParserUtil.parseSchema(filePath, true);
		assertNotNull(schema);
		
		BemEntityTypeInfo type_IfcProject = (BemEntityTypeInfo)schema.getTypeInfo("IfcProject");
		assertNotNull(type_IfcProject);
		assertEquals("IfcProject", type_IfcProject.getName());
		assertEquals(BemEntityTypeInfo.class, type_IfcProject.getClass());
		
		BemTypeInfo type_IfcProject2 = schema.getTypeInfo("IFCPROJECT");
		assertEquals(type_IfcProject, type_IfcProject2);
		
		BemTypeInfo type_Integer = schema.getTypeInfo("INTEGER");
		assertNotNull(type_Integer);
		assertEquals(BemPrimitiveTypeInfo.class, type_Integer.getClass());
		
		BemTypeInfo type_IfcInteger = schema.getTypeInfo("IFCINTEGER");
		assertNotNull(type_IfcInteger);
		assertEquals(BemDefinedTypeInfo.class, type_IfcInteger.getClass());
		
		BemAttributeInfo attribute_IfcProject_Name = type_IfcProject.getAttributeInfo("NAME");
		assertNotNull(attribute_IfcProject_Name);
		assertEquals("name", attribute_IfcProject_Name.getName());
		
		assertNotNull(type_IfcProject.getInverseAttributeInfos(true));
		assertTrue(!type_IfcProject.getInverseAttributeInfos(true).isEmpty());

		BemAttributeInfo attribute_IfcProject_HasAssociations = type_IfcProject.getInverseAttributeInfo("HasAssociations");
		assertNotNull(attribute_IfcProject_HasAssociations);
		assertEquals("hasAssociations", attribute_IfcProject_HasAssociations.getName());
		
		BemTypeInfo type_IfcBoolean = schema.getTypeInfo("IfcBoolean");
		assertNotNull(type_IfcBoolean);
		assertEquals("IfcBoolean", type_IfcBoolean.getName());
		assertEquals(BemDefinedTypeInfo.class, type_IfcBoolean.getClass());
		assertEquals(BemValueKindEnum.LOGICAL, type_IfcBoolean.getValueKind());		
		
		BemTypeInfo type_BOOLEAN = ((BemDefinedTypeInfo)type_IfcBoolean).getWrappedTypeInfo();
		assertNotNull(type_BOOLEAN);
		assertEquals("BOOLEAN", type_BOOLEAN.getName());
		assertEquals(BemLogicalTypeInfo.class, type_BOOLEAN.getClass());
		assertEquals(BemValueKindEnum.LOGICAL, type_BOOLEAN.getValueKind());
		
	}
	
	

}
