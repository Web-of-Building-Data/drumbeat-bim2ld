package fi.aalto.cs.drumbeat.data.step;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;
import fi.aalto.cs.drumbeat.data.bedm.parsers.DrbParserException;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbDefinedTypeInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbEntityTypeInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbPrimitiveTypeInfo;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbSchema;
import fi.aalto.cs.drumbeat.data.bedm.schema.DrbTypeInfo;
import fi.aalto.cs.drumbeat.data.step.parsers.ExpressSchemaParser;

public class Test_ExpressSchemaParser {
	
	@Test
	public void test_parseIfc2x3() throws FileNotFoundException, DrbParserException, DrbNotFoundException {
		test_parse("IFC2X3_TC1.exp");
	}

	@Test
	public void test_parseIfc4Add1() throws FileNotFoundException, DrbParserException, DrbNotFoundException {
		test_parse("IFC4_ADD1.exp");
	}

	private void test_parse(String fileName) throws FileNotFoundException, DrbParserException, DrbNotFoundException 
	{
		FileInputStream in = new FileInputStream("src/test/java/resources/" + fileName);
		ExpressSchemaParser parser = new ExpressSchemaParser();
		DrbSchema schema = parser.parse(in, "exp");
		assertNotNull(schema);
		
		DrbTypeInfo typeIfcProject = schema.getTypeInfo("IfcProject");
		assertNotNull(typeIfcProject);
		assertEquals("IfcProject", typeIfcProject.getName());
		assertEquals(DrbEntityTypeInfo.class, typeIfcProject.getClass());
		
		DrbTypeInfo typeIfcProject2 = schema.getTypeInfo("IFCPROJECT");
		assertEquals(typeIfcProject, typeIfcProject2);
		
		DrbTypeInfo typeInteger = schema.getTypeInfo("INTEGER");
		assertNotNull(typeInteger);
		assertEquals(DrbPrimitiveTypeInfo.class, typeInteger.getClass());
		
		DrbTypeInfo typeIfcInteger = schema.getTypeInfo("IFCINTEGER");
		assertNotNull(typeIfcInteger);
		assertEquals(DrbDefinedTypeInfo.class, typeIfcInteger.getClass());
	}
	
}
