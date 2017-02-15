//package fi.aalto.cs.drumbeat.convert.bem2rdf;
//
//public class Test_Bem2RdfConverter_Exporting_BooleanValues {
//	
//	
//	@Test
//	public void test_Exporting_Boolean_As_NamedIndividual() {
//		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
//		context.getConversionParams().setParamValue(
//				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
//				Bem2RdfConversionContextParams.VALUE_NAMED_INDIVIDUAL);
//		
//		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
//		
//		BemPrimitiveValue value = new BemPrimitiveValue(BemLogicalEnum.TRUE, bemSchema.BOOLEAN, BemValueKindEnum.LOGICAL);		
//		RDFNode node = converter.convertPrimitiveValue(value, jenaModel);		
//		assertNotNull(node);
//		assertTrue(node.isURIResource());
//		assertEquals(Bem2RdfVocabulary.EXPRESS.getBaseUri() + "true", node.asResource().getURI());
//	}
//	
//	@Test
//	public void test_Exporting_Boolean_As_XsdBoolean_True() {
//		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
//		context.getConversionParams().setParamValue(
//				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
//				Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN);
//		
//		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
//		
//		BemPrimitiveValue value = new BemPrimitiveValue(BemLogicalEnum.TRUE, bemSchema.BOOLEAN, BemValueKindEnum.LOGICAL);		
//		RDFNode node = converter.convertPrimitiveValue(value, jenaModel);		
//		assertNotNull(node);
//		assertTrue(node.isAnon());
//		
//		Statement valueStatement = node.asResource().getProperty(Bem2RdfVocabulary.EXPRESS.hasLogical);
//		assertNotNull(valueStatement);
//		RDFNode valueObject = valueStatement.getObject();
//		
//		assertNotNull(valueObject);
//		assertTrue(valueObject.isPrimitive());
//		assertEquals(true, valueObject.asPrimitive().getBoolean());
//		assertEquals(XSD.xboolean.getURI(), valueObject.asPrimitive().getDatatypeURI());
//	}	
//
//	@Test
//	public void test_Exporting_Boolean_As_XsdBoolean_False() {
//		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
//		context.getConversionParams().setParamValue(
//				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
//				Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN);
//		
//		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
//		
//		BemPrimitiveValue value = new BemPrimitiveValue(BemLogicalEnum.FALSE, bemSchema.BOOLEAN, BemValueKindEnum.LOGICAL);		
//		RDFNode node = converter.convertPrimitiveValue(value, jenaModel);		
//		assertNotNull(node);
//		assertTrue(node.isAnon());
//		
//		Statement valueStatement = node.asResource().getProperty(Bem2RdfVocabulary.EXPRESS.hasLogical);
//		assertNotNull(valueStatement);
//		RDFNode valueObject = valueStatement.getObject();
//		
//		assertNotNull(valueObject);
//		assertTrue(valueObject.isPrimitive());
//		assertEquals(false, valueObject.asPrimitive().getBoolean());
//		assertEquals(XSD.xboolean.getURI(), valueObject.asPrimitive().getDatatypeURI());
//	}	
//
//	@Test
//	public void test_Exporting_Boolean_As_XsdBoolean_Unknown() {
//		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
//		context.getConversionParams().setParamValue(
//				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
//				Bem2RdfConversionContextParams.VALUE_XSD_BOOLEAN);
//		
//		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
//		
//		BemPrimitiveValue value = new BemPrimitiveValue(BemLogicalEnum.UNKNOWN, bemSchema.BOOLEAN, BemValueKindEnum.LOGICAL);		
//		RDFNode node = converter.convertPrimitiveValue(value, jenaModel);		
//		assertNotNull(node);
//		assertTrue(node.isAnon());
//		
//		Statement valueStatement = node.asResource().getProperty(Bem2RdfVocabulary.EXPRESS.hasLogical);
//		assertNotNull(valueStatement);
//		RDFNode valueObject = valueStatement.getObject();
//		
//		assertNotNull(valueObject);
//		assertTrue(valueObject.isPrimitive());
//		assertEquals("unknown", valueObject.asPrimitive().getString());
//		assertEquals(XSD.xstring.getURI(), valueObject.asPrimitive().getDatatypeURI());
//	}
//	
//	@Test
//	public void test_Exporting_Boolean_As_XsdString_True() {
//		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
//		context.getConversionParams().setParamValue(
//				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
//				Bem2RdfConversionContextParams.VALUE_XSD_STRING);
//		
//		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
//		
//		BemPrimitiveValue value = new BemPrimitiveValue(BemLogicalEnum.TRUE, bemSchema.BOOLEAN, BemValueKindEnum.LOGICAL);		
//		RDFNode node = converter.convertPrimitiveValue(value, jenaModel);		
//		assertNotNull(node);
//		assertTrue(node.isAnon());
//		
//		Statement valueStatement = node.asResource().getProperty(Bem2RdfVocabulary.EXPRESS.hasLogical);
//		assertNotNull(valueStatement);
//		RDFNode valueObject = valueStatement.getObject();
//		
//		assertNotNull(valueObject);
//		assertTrue(valueObject.isPrimitive());
//		assertEquals("true", valueObject.asPrimitive().getString());
//		assertEquals(XSD.xstring.getURI(), valueObject.asPrimitive().getDatatypeURI());
//	}	
//
//	@Test
//	public void test_Exporting_Boolean_As_XsdString_False() {
//		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
//		context.getConversionParams().setParamValue(
//				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
//				Bem2RdfConversionContextParams.VALUE_XSD_STRING);
//		
//		Bem2RdfUriBuilder uriBuilder = Bem2RdfUriBuilder.createUriBuilder(context, bemSchema);
//		
//		Bem2RdfConverter converter = new Bem2RdfConverter(context, uriBuilder);
//		
//		BemPrimitiveValue value = new BemPrimitiveValue(BemLogicalEnum.FALSE, BemValueKindEnum.LOGICAL);		
//		RDFNode node = converter.convertPrimitiveValue(value, jenaModel);		
//		assertNotNull(node);
//		assertTrue(node.isAnon());
//		
//		Statement valueStatement = node.asResource().getProperty(Bem2RdfVocabulary.EXPRESS.hasLogical);
//		assertNotNull(valueStatement);
//		RDFNode valueObject = valueStatement.getObject();
//		
//		assertNotNull(valueObject);
//		assertTrue(valueObject.isPrimitive());
//		assertEquals("false", valueObject.asPrimitive().getString());
//		assertEquals(XSD.xstring.getURI(), valueObject.asPrimitive().getDatatypeURI());
//	}	
//
//	@Test
//	public void test_Exporting_Boolean_As_XsdString_Unknown() {
//		Bem2RdfConversionContext context = new Bem2RdfConversionContext();
//		context.getConversionParams().setParamValue(
//				Bem2RdfConversionContextParams.PARAM_CONVERT_BOOLEANS_TO,
//				Bem2RdfConversionContextParams.VALUE_XSD_STRING);		
//		
//		Bem2RdfConverter converter = new Bem2RdfConverter(context, bemSchema);
//
//		BemPrimitiveValue value = new BemPrimitiveValue(BemLogicalEnum.UNKNOWN, bemSchema.BOOLEAN, BemValueKindEnum.LOGICAL);		
//		RDFNode node = converter.convertPrimitiveValue(value, jenaModel);		
//		assertNotNull(node);
//		assertTrue(node.isAnon());
//		
//		Statement valueStatement = node.asResource().getProperty(Bem2RdfVocabulary.EXPRESS.hasLogical);
//		assertNotNull(valueStatement);
//		RDFNode valueObject = valueStatement.getObject();
//		
//		assertNotNull(valueObject);
//		assertTrue(valueObject.isPrimitive());
//		assertEquals("unknown", valueObject.asPrimitive().getString());
//		assertEquals(XSD.xstring.getURI(), valueObject.asPrimitive().getDatatypeURI());
//	}		
//
//}
