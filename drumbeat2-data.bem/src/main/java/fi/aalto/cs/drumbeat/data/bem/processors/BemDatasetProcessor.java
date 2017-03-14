package fi.aalto.cs.drumbeat.data.bem.processors;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.common.digest.EncoderTypeEnum;
import fi.aalto.cs.drumbeat.data.bem.dataset.*;
import fi.aalto.cs.drumbeat.data.bem.schema.*;


public abstract class BemDatasetProcessor {
	
	private static final Logger logger = Logger.getLogger(BemDatasetProcessor.class);	
	
	public enum InputTypeEnum {
		UngroundedEntity,
		GroundedEntity,
		Any;
	}
	
	public static final String PARAM_ENTITY_NAME_PATTERN = "entityNamePattern";
	public static final String PARAM_ENCODER_TYPE = "encoderType";
	public static final String PARAM_MIN_NUMBER_OF_INCOMING_LINKS = "minNumberOfIncomingLinks";
	
	private BemDatasetProcessorManager processorManager;
	private String name; 
	private Properties properties;
	private EncoderTypeEnum encoderType;
	private Map<BemEntityTypeInfo, Integer> entityCountersForDebugging = null;	

	public BemDatasetProcessor(BemDatasetProcessorManager processorManager, Properties properties) {
		this.processorManager = processorManager;
		this.properties = properties;
		if (properties != null) {
			encoderType = Enum.valueOf(EncoderTypeEnum.class, properties.getProperty(PARAM_ENCODER_TYPE, EncoderTypeEnum.None.toString()));			
		} else {
			encoderType = EncoderTypeEnum.None;
		}
	}	
	
	protected abstract InputTypeEnum getInputType();
	protected abstract void initialize() throws BemDatasetProcessorException;
	protected abstract boolean process(BemEntity entity) throws BemDatasetProcessorException;	
	protected abstract boolean checkNameDuplication();
	protected abstract boolean allowNameDuplication();
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	protected void trySetEntityName(BemEntity entity, String rawName) throws BemNameConflictException {
		processorManager.trySetEntityName(entity, rawName, encoderType, checkNameDuplication(), allowNameDuplication());
	}

	public BemDatasetProcessorManager getProcessorManager() {
		return processorManager;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public void preCommit() throws BemNameConflictException {		
	}	
	
	public void preProcess() {		
		if (logger.isDebugEnabled()) {
			entityCountersForDebugging = new TreeMap<>();
		}		
	}
	
	public void postProcess() {		
		if (entityCountersForDebugging != null) {
			for (Map.Entry<BemEntityTypeInfo, Integer> counter : entityCountersForDebugging.entrySet()) {
				logger.debug(String.format("\t\tNumber of effected entities: %2$d of %1$s", counter.getKey(), counter.getValue()));
			}
		}
	}
	
	protected void addEffectedEntityInfoForDebugging(BemEntity entity) {
		if (entityCountersForDebugging != null) {
			BemEntityTypeInfo entityTypeInfo = entity.getTypeInfo();
			Integer counter = entityCountersForDebugging.get(entityTypeInfo);
			counter = counter != null ? counter + 1 : 1;
			entityCountersForDebugging.put(entityTypeInfo, counter);					
		}		
		
	}
	
}
