package fi.aalto.cs.drumbeat.bim2ld.cli;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.common.cli.DrbOption;
import fi.aalto.cs.drumbeat.common.cli.DrbOptionComparator;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//
		// Define command line options 
		//
		Options options = createCommandLineOptions();

		CommandLineParser commandParser = new PosixParser();
		
		Bem2RdfExporter exporter;
		
		try {
			CommandLine commandLine = commandParser.parse(options, args);
			
			Bem2RdfExporter.init(
				commandLine.getOptionValue(Bim2LdCommandLineOptions.LOGGER_CONFIG_FILE_SHORT),
				commandLine.getOptionValue(Bim2LdCommandLineOptions.CONFIG_FILE_SHORT));
			
			exporter = new Bem2RdfExporter(
				commandLine.getOptionValue(Bim2LdCommandLineOptions.INPUT_SCHEMA_FILE_SHORT),
				commandLine.getOptionValue(Bim2LdCommandLineOptions.INPUT_MODEL_FILE_1_SHORT),			
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_LAYER_NAME_SHORT),			
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_SCHEMA_FILE_SHORT),			
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_SCHEMA_NAME_SHORT),			
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_MODEL_FILE_1_SHORT),			
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_MODEL_NAME_1_SHORT),
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_META_MODEL_FILE_1_SHORT),			
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_META_MODEL_NAME_1_SHORT),
				commandLine.getOptionValue(Bim2LdCommandLineOptions.OUTPUT_FILE_FORMAT_SHORT, Bim2LdCommandLineOptions.OUTPUT_FILE_FORMAT_DEFAULT));
			
		} catch (Exception e) {
			
			Options helpOptions = createHelpOptions();			
			
			try {				
				commandParser.parse(helpOptions, args);				
			} catch (ParseException e2) {
				// print original error
				System.out.printf("Unexpected error: %s%n%n", e.getMessage());
			}
			
			printHelp(options, helpOptions);
			
			return;
		}
		
		try {
			exporter.run();
		} catch (Throwable e) {
			Logger logger = Logger.getRootLogger();
			if (logger != null) {
				logger.error("Unexpected error: " + e.getMessage(), e);
			} else {
				e.printStackTrace();
				while ((e = e.getCause()) != null) {
					e.printStackTrace();
				}	
			}
		}
		
	}

	private static Options createCommandLineOptions() {
		
		int index = 0;
		Option option;
		
		Options options = new Options();
		
		// option --log-properties-file|-lf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.LOGGER_CONFIG_FILE_SHORT,
				Bim2LdCommandLineOptions.LOGGER_CONFIG_FILE_LONG,
				true,
				Bim2LdCommandLineOptions.LOGGER_CONFIG_FILE_DESCRIPTION);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		option.setRequired(true);
		options.addOption(option);
		
		// option --config-file|-cf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.CONFIG_FILE_SHORT,
				Bim2LdCommandLineOptions.CONFIG_FILE_LONG,
				true,
				Bim2LdCommandLineOptions.CONFIG_FILE_DESCRIPTION);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		option.setRequired(true);
		options.addOption(option);
		
		// option --output-layer-name|-oln <name>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_LAYER_NAME_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_LAYER_NAME_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_LAYER_NAME_DESCRIPTION);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		option.setRequired(true);
		options.addOption(option);		

		// option --input-schema-file|-isf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.INPUT_SCHEMA_FILE_SHORT,
				Bim2LdCommandLineOptions.INPUT_SCHEMA_FILE_LONG,
				true,
				Bim2LdCommandLineOptions.INPUT_SCHEMA_FILE_DESCRIPTION);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		option.setRequired(true);
		options.addOption(option);
		
		// option --input-model-file|-imf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.INPUT_MODEL_FILE_1_SHORT,
				Bim2LdCommandLineOptions.INPUT_MODEL_FILE_1_LONG,
				true,
				Bim2LdCommandLineOptions.INPUT_MODEL_FILE_1_DESCRIPTION);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		option.setRequired(false);
		options.addOption(option);		
		
		OptionGroup optionGroup = new OptionGroup();
		

		// option --output-schema-file|-osf <name>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_SCHEMA_NAME_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_SCHEMA_NAME_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_SCHEMA_NAME_DESCRIPTION + Bim2LdCommandLineOptions.SUFFIX_OPTIONAL);
		option.setArgName(Bim2LdCommandLineOptions.ARG_NAME);
		optionGroup.addOption(option);
		
		// option --output-schema-file|-osf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_SCHEMA_FILE_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_SCHEMA_FILE_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_SCHEMA_FILE_DESCRIPTION + Bim2LdCommandLineOptions.SUFFIX_OPTIONAL);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		optionGroup.addOption(option);


		optionGroup.setRequired(false);		
		options.addOptionGroup(optionGroup);
		
		
		
		optionGroup = new OptionGroup();

		// option --output-model-name|-omn <name>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_MODEL_NAME_1_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_MODEL_NAME_1_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_MODEL_NAME_1_DESCRIPTION  + Bim2LdCommandLineOptions.SUFFIX_OPTIONAL);
		option.setArgName(Bim2LdCommandLineOptions.ARG_NAME);
		optionGroup.addOption(option);
		
		// option --output-model-file|-omf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_MODEL_FILE_1_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_MODEL_FILE_1_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_MODEL_FILE_1_DESCRIPTION  + Bim2LdCommandLineOptions.SUFFIX_OPTIONAL);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		optionGroup.addOption(option);		
		
		optionGroup.setRequired(false);		
		options.addOptionGroup(optionGroup);
		
		
		
		
		optionGroup = new OptionGroup();

		// option --output-meta-model-name|-ommn <name>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_META_MODEL_NAME_1_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_META_MODEL_NAME_1_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_META_MODEL_NAME_1_DESCRIPTION  + Bim2LdCommandLineOptions.SUFFIX_OPTIONAL);
		option.setArgName(Bim2LdCommandLineOptions.ARG_NAME);
		optionGroup.addOption(option);
		
		// option --output-meta-model-file|-ommf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_META_MODEL_FILE_1_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_META_MODEL_FILE_1_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_META_MODEL_FILE_1_DESCRIPTION  + Bim2LdCommandLineOptions.SUFFIX_OPTIONAL);
		option.setArgName(Bim2LdCommandLineOptions.ARG_FILE);
		optionGroup.addOption(option);
		
		optionGroup.setRequired(false);		
		options.addOptionGroup(optionGroup);
		
		

		
		// option --config-file|-cf <file>
		option = new DrbOption(
				++index,
				Bim2LdCommandLineOptions.OUTPUT_FILE_FORMAT_SHORT,
				Bim2LdCommandLineOptions.OUTPUT_FILE_FORMAT_LONG,
				true,
				Bim2LdCommandLineOptions.OUTPUT_FILE_FORMAT_DESCRIPTION);
		option.setArgName(Bim2LdCommandLineOptions.ARG_NAME);
		option.setRequired(false);
		options.addOption(option);
		return options;
	}
	
	private static Options createHelpOptions() {
		Options options = new Options();
		Option option = new Option(
				Bim2LdCommandLineOptions.HELP_SHORT,
				Bim2LdCommandLineOptions.HELP_LONG,
				false,
				Bim2LdCommandLineOptions.HELP_DESCRIPTION);
		option.setRequired(true);
		options.addOption(option);
		return options;
	}
	
	private static void printHelp(Options options, Options helpOptions) {		
		HelpFormatter helpFormatter = new HelpFormatter();
		String className = App.class.getName();
		helpFormatter.printHelp(className, helpOptions, true);
		helpFormatter.setOptionComparator(new DrbOptionComparator());
		helpFormatter.printHelp(Bim2LdCommandLineOptions.FORMATTER_WIDTH, className, null, options, null, true);
	}

	
}
