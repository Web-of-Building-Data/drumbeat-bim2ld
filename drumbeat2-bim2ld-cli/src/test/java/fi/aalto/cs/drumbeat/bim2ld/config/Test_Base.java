package fi.aalto.cs.drumbeat.bim2ld.config;

import fi.aalto.cs.drumbeat.common.meta.AnnotationUtils;

public class Test_Base {

	public static final String TEST_RESOURCES_FOLDER_PATH ="src/test/resources/"; 
	public static final String TEST_CONFIG_FOLDER_PATH = TEST_RESOURCES_FOLDER_PATH + "config/"; 

	public static final String CONFIG_FILE_PATH = TEST_CONFIG_FOLDER_PATH + "bim2ld.xml";
	public static final String LOGGER_CONFIG_FILE_PATH = TEST_CONFIG_FOLDER_PATH + "log4j.xml";

	public Test_Base() {
	}
	
	protected void startTest(int callingMethodCallShift) {
		System.out.printf("%n[%s::%s()]%n",
				getClass().getSimpleName(),
				AnnotationUtils.getCallingMethodName(callingMethodCallShift + 1));
	}
	

}
