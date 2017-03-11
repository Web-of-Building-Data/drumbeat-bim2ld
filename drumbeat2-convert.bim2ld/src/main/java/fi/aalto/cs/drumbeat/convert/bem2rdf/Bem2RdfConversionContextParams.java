package fi.aalto.cs.drumbeat.convert.bem2rdf;

import static fi.aalto.cs.drumbeat.common.params.StringParam.*;

import java.util.Arrays;

import fi.aalto.cs.drumbeat.common.params.*;

public class Bem2RdfConversionContextParams extends SimpleTypeParams {
	
	private static final long serialVersionUID = 1L;
	
	public static final String PARAM_IGNORE_BUILT_IN_TYPES = "IgnoreBuiltInTypes";
	public static final String PARAM_IGNORE_NON_BUILT_IN_TYPES = "IgnoreNonBuiltInTypes";
	
	public static final String PARAM_EXPORT_PROPERTY_DOMAIN_AND_RANGES_TO = "ExportPropertyDomainsAndRangesTo";
	public static final String PARAM_CONVERT_LOGICALS_TO = "ConvertLogicalsTo";
	public static final String PARAM_CONVERT_DOUBLES_TO = "ConvertDoublesTo";
	public static final String PARAM_CONVERT_ENUMS_TO = "ConvertEnumsTo";
	public static final String PARAM_EXPORT_DEBUG_INFO = "ExportDebugInfo";
	public static final String PARAM_USE_LONG_ATTRIBUTE_NAME = "UseLongAttributeName";
	public static final String PARAM_CONVERT_COLLECTIONS_TO = "ConvertCollectionsTo";
	public static final String PARAM_NAME_ALL_BLANK_NODES = "NameAllBlankNodes";
	
	public static final String VALUE_NAMED_INDIVIDUAL = "owl:NamedIndividual";
	public static final String VALUE_NAMED_INDIVIDUAL_DESCRIPTION = "owl:NamedIndividual";
	
//	public static final String VALUE_YES = StringParam.VALUE_YES;
//	public static final String VALUE_YES_DESCRIPTION = StringParam.VALUE_YES_DESCRIPTION;
	
//	public static final String VALUE_NO = StringParam.VALUE_NO;
//	public static final String VALUE_NO_DESCRIPTION = StringParam.VALUE_NO_DESCRIPTION;
	
	public static final String VALUE_XSD_STRING = "xsd:string";
	public static final String VALUE_XSD_STRING_DESCRIPTION = "xsd:string";

	public static final String VALUE_XSD_BOOLEAN = "xsd:boolean";
	public static final String VALUE_XSD_BOOLEAN_DESCRIPTION = "xsd:boolean";
	
	public static final String VALUE_XSD_DECIMAL = "xsd:decimal";
	public static final String VALUE_XSD_DOUBLE = "xsd:double";
	public static final String VALUE_OWL_REAL = "owl:real";

	public static final String VALUE_AUTO_MOST_SUPPORTED = "AutoMostSupported";
	public static final String VALUE_AUTO_MOST_SUPPORTED_DESCRIPTION = "Auto (most supported)";

	public static final String VALUE_AUTO_MOST_EFFICIENT = "AutoMostEfficient";	
	public static final String VALUE_AUTO_MOST_EFFICIENT_DESCRIPTION = "Auto (most efficient)";
	
	public static final String VALUE_DRUMMOND_LIST = "DrummondList";
	public static final String VALUE_OLO_SIMILAR_LIST = "OloSimilarList";
	
	public static final String VALUE_ATTRIBUTES_WITH_LONG_NAMES = "AttributesWithLongNames";
	

	public Bem2RdfConversionContextParams() {
		
		addParam(new BooleanParam(
				PARAM_IGNORE_BUILT_IN_TYPES,
				null,
				null,
				Arrays.asList(
						Boolean.TRUE,
						Boolean.FALSE),
				Arrays.asList(
						VALUE_YES_DESCRIPTION,
						VALUE_NO_DESCRIPTION + VALUE_DEFAULT_DESCRIPTION),
				Boolean.FALSE
				));
		
		addParam(new BooleanParam(
				PARAM_IGNORE_NON_BUILT_IN_TYPES,
				null,
				null,
				Arrays.asList(
						Boolean.TRUE,
						Boolean.FALSE),
				Arrays.asList(
						VALUE_YES_DESCRIPTION,
						VALUE_NO_DESCRIPTION + VALUE_DEFAULT_DESCRIPTION),
				Boolean.FALSE
				));
		
		addParam(new BooleanParam(
				PARAM_EXPORT_DEBUG_INFO,
				null,
				null,
				Arrays.asList(
						Boolean.TRUE,
						Boolean.FALSE),
				Arrays.asList(
						VALUE_YES_DESCRIPTION,
						VALUE_NO_DESCRIPTION + VALUE_DEFAULT_DESCRIPTION),
				Boolean.FALSE
				));
		
		addParam(
				new StringParam(
					PARAM_EXPORT_PROPERTY_DOMAIN_AND_RANGES_TO,
					null,
					null,
					Arrays.asList(VALUE_NONE, VALUE_ATTRIBUTES_WITH_LONG_NAMES),
					Arrays.asList(VALUE_NONE_DESCRIPTION, VALUE_ATTRIBUTES_WITH_LONG_NAMES),
					VALUE_ATTRIBUTES_WITH_LONG_NAMES));
		
		addParam(
				new StringParam(
					PARAM_CONVERT_LOGICALS_TO,
					null,
					null,
					Arrays.asList(
							VALUE_NAMED_INDIVIDUAL,
							VALUE_XSD_STRING,
							VALUE_XSD_BOOLEAN),
					Arrays.asList(
							VALUE_NAMED_INDIVIDUAL_DESCRIPTION +  VALUE_DEFAULT_DESCRIPTION,
							VALUE_XSD_STRING_DESCRIPTION,
							VALUE_XSD_BOOLEAN_DESCRIPTION + " (LOGICAL value `UNKNOWN` will be ignored!)"),
					VALUE_NAMED_INDIVIDUAL));
				
		addParam(
				new StringParam(
					PARAM_CONVERT_ENUMS_TO,
					null,
					null,
					Arrays.asList(
							VALUE_NAMED_INDIVIDUAL,
							VALUE_XSD_STRING),
					Arrays.asList(
							VALUE_NAMED_INDIVIDUAL_DESCRIPTION + VALUE_DEFAULT_DESCRIPTION,
							VALUE_XSD_STRING_DESCRIPTION),
					VALUE_NAMED_INDIVIDUAL));

		addParam(
				new StringParam(
					PARAM_CONVERT_DOUBLES_TO,
					null,
					null,
					Arrays.asList(
							VALUE_AUTO_MOST_SUPPORTED,
							VALUE_AUTO_MOST_EFFICIENT,
							VALUE_XSD_DECIMAL,
							VALUE_XSD_DOUBLE,
							VALUE_OWL_REAL,
							VALUE_XSD_STRING),
					Arrays.asList(
							VALUE_AUTO_MOST_SUPPORTED_DESCRIPTION + VALUE_DEFAULT_DESCRIPTION,
							VALUE_AUTO_MOST_EFFICIENT_DESCRIPTION,
							VALUE_XSD_DECIMAL + VALUE_DEFAULT_DESCRIPTION,
							VALUE_XSD_DOUBLE + " (most efficient, but not supported in OWL 2 EL/QL)",
							VALUE_OWL_REAL,
							VALUE_XSD_STRING),
					VALUE_XSD_DECIMAL));
		
		addParam(
				new StringParam(
					PARAM_CONVERT_COLLECTIONS_TO,
					null,
					null,
					Arrays.asList(
							VALUE_DRUMMOND_LIST,
							VALUE_OLO_SIMILAR_LIST),
					Arrays.asList(
							VALUE_DRUMMOND_LIST,
							VALUE_OLO_SIMILAR_LIST),
					VALUE_DRUMMOND_LIST));

		addParam(
				new BooleanParam(
					PARAM_USE_LONG_ATTRIBUTE_NAME,
					null,
					null,
					Arrays.asList(
							Boolean.TRUE,
							Boolean.FALSE),
					Arrays.asList(
							VALUE_YES_DESCRIPTION,
							VALUE_NO_DESCRIPTION + VALUE_DEFAULT_DESCRIPTION),
					Boolean.TRUE
					));
		
		addParam(
				new BooleanParam(
					PARAM_NAME_ALL_BLANK_NODES,
					null,
					null,
					Arrays.asList(
							Boolean.TRUE,
							Boolean.FALSE),
					Arrays.asList(
							VALUE_YES_DESCRIPTION,
							VALUE_NO_DESCRIPTION + VALUE_DEFAULT_DESCRIPTION),
					Boolean.FALSE));
		
	
	}
	
	public boolean useLongAttributeName() throws Bem2RdfConverterConfigurationException {
		try {
			return (Boolean)getParamValue(PARAM_USE_LONG_ATTRIBUTE_NAME);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}
	}
	
	public boolean ignoreBuiltInTypes() throws Bem2RdfConverterConfigurationException {
		try {
			return (Boolean)getParamValue(PARAM_IGNORE_BUILT_IN_TYPES);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}
	}

	public boolean ignoreNonBuiltInTypes() throws Bem2RdfConverterConfigurationException {
		try {
			return (Boolean)getParamValue(PARAM_IGNORE_NON_BUILT_IN_TYPES);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}
	}
	
	public boolean nameAllBlankNodes() throws Bem2RdfConverterConfigurationException {
		try {
			return (Boolean)getParamValue(PARAM_NAME_ALL_BLANK_NODES);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}
	}

	public String convertLogicalsTo() throws Bem2RdfConverterConfigurationException {
		try {
			return (String)getParamValue(PARAM_CONVERT_LOGICALS_TO);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}
	}

	public String convertCollectionsTo() throws Bem2RdfConverterConfigurationException {
		try {
			return (String)getParamValue(PARAM_CONVERT_COLLECTIONS_TO);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}
	}
		
	public String convertEnumsTo() throws Bem2RdfConverterConfigurationException {
		try {
			return (String)getParamValue(PARAM_CONVERT_ENUMS_TO);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}		
	}

	public String convertDoublesTo() throws Bem2RdfConverterConfigurationException {
		try {
			return (String)getParamValue(PARAM_CONVERT_DOUBLES_TO);
		} catch (DrbParamNotFoundException e) {
			throw new Bem2RdfConverterConfigurationException(e);
		}		
	}

//	public boolean convertSetAttributesAsMultipleProperties() throws Bem2RdfConverterConfigurationException {
//		String convertCollectionsTo = convertCollectionsTo();
//		switch (convertCollectionsTo) {
//		case VALUE_DRUMMOND_LIST:
//			return true;
//		case VALUE_OLO_SIMILAR_LIST:
//			return false;
//		default:
//			throw new NotImplementedException("Unknown collection type");
//		}		
//	}
}
