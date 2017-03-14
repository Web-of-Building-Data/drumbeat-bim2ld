package fi.aalto.cs.drumbeat.data.ifc.processors;

import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;

import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.data.bem.BemNotFoundException;
import fi.aalto.cs.drumbeat.data.bem.dataset.*;
import fi.aalto.cs.drumbeat.data.bem.processors.*;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.data.ifc.IfcVocabulary;
import fi.aalto.cs.drumbeat.data.ifc.utils.IfcGuidCompressor;


/**
 * Processor that sets entity name by its global IDs (if any).
 * 
 *  Sample syntax:
 *  
 *		<processor name="SetNameByGlobalId" enabled="true">
 *			<class>fi.aalto.cs.drumbeat.ifc.util.grounding.SetNameByGlobalId</class>
 *			<params>
 *				<param name="entityNamePattern" value="GUID_$Entity.GlobalId$" />
 *				<param name="encoderType" value="None" />
 *			</params>
 *		</processor>
 *  
 * @author vuhoan1
 *
 */
public class SetNameByGlobalId extends BemDatasetProcessor {
	
	public static final String VARIABLE_NAME_ENTITY_LONG_GUID = Matcher.quoteReplacement("Entity.LongGuid");
	public static final String VARIABLE_NAME_ENTITY_SHORT_GUID = Matcher.quoteReplacement("Entity.ShortGuid");
	
	private static BemAttributeInfo globalIdAttributeInfo;
	
	private String entityNamePattern;

	private BemEntityTypeInfo ifcRootEntityTypeInfo;

	public SetNameByGlobalId(BemDatasetProcessorManager processorManager, Properties properties) {
		super(processorManager, properties);		
	}

	@Override
	protected void initialize() throws BemDatasetProcessorException {		
		entityNamePattern = getProperties().getProperty(PARAM_ENTITY_NAME_PATTERN);
		
		try {
			BemSchema schema = getProcessorManager().getSchema();
			ifcRootEntityTypeInfo = schema.getEntityTypeInfo(IfcVocabulary.IfcTypes.IFC_ROOT);
			
			globalIdAttributeInfo = ifcRootEntityTypeInfo.getAttributeInfo(IfcVocabulary.IfcAttributes.GLOBAL_ID);
		} catch (BemNotFoundException e) {
		}
		assert(globalIdAttributeInfo != null);
	}

	@Override
	protected boolean process(BemEntity entity) throws BemDatasetProcessorException {
		if (entity.isInstanceOf(ifcRootEntityTypeInfo)) {			
			BemPrimitiveValue guidValue = (BemPrimitiveValue)entity.getAttributeMap().getAny(globalIdAttributeInfo);
			
			String shortGuid = (String)guidValue.getValue();
			String longGuid = IfcGuidCompressor.uncompressGuidString(shortGuid);
			
			HashMap<String, String> variableMap = new HashMap<>();
			variableMap.put(VARIABLE_NAME_ENTITY_SHORT_GUID, shortGuid);
			variableMap.put(VARIABLE_NAME_ENTITY_LONG_GUID, longGuid);
			
			String rawName = StringUtils.substituteVariables(entityNamePattern, variableMap);
			
//			entity.setGlobalId(rawName);
			trySetEntityName(entity, rawName);
			return true;
		}
		
		return false;
	}

	@Override
	public InputTypeEnum getInputType() {
		return InputTypeEnum.UngroundedEntity;
	}

	@Override
	protected boolean checkNameDuplication() {
		return false;
	}

	@Override
	protected boolean allowNameDuplication() {
		return false;
	}

}
