package fi.aalto.cs.drumbeat.bim2ld.config;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import fi.aalto.cs.drumbeat.common.config.document.ConfigurationDocument;
import fi.aalto.cs.drumbeat.common.config.document.ConfigurationParserException;
import fi.aalto.cs.drumbeat.common.params.DrbParamNotFoundException;
import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConversionContext;
import fi.aalto.cs.drumbeat.convert.bem2rdf.util.config.Bem2RdfConversionContextLoader;

public class Test_Bem2RdfConversionContextLoader extends Test_Base {

	public Test_Bem2RdfConversionContextLoader() {
	}
	
	@Test
	public void test_loadConfig1_Correct() throws DrbParamNotFoundException, ConfigurationParserException {
		Map<String, Bem2RdfConversionContext> conversionContexts = loadContexts(CONFIG_FILE_PATH);
		assertNotNull(conversionContexts);
	}
	
	private Map<String, Bem2RdfConversionContext> loadContexts(String filePath) throws DrbParamNotFoundException, ConfigurationParserException {
		
		startTest(1);
		
		System.out.println("Loading config file: " + filePath);
		
		ConfigurationDocument configurationDocument = ConfigurationDocument.load(filePath);
		return Bem2RdfConversionContextLoader.loadConversionContexts(configurationDocument);
	}

}
