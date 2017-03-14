package fi.aalto.cs.drumbeat.data.bem.processors;

import java.lang.reflect.Constructor;
import java.util.*;

import org.apache.log4j.Logger;

import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.common.digest.EncoderTypeEnum;
import fi.aalto.cs.drumbeat.data.bem.dataset.*;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;


public class BemDatasetProcessorManager {
	
	private static final Logger logger = Logger.getLogger(BemDatasetProcessorManager.class);	
	
	private final BemDataset dataset;
	private final List<BemDatasetProcessor> processors;

	private Map<String, BemEntity> temporaryNamedEntities;
	private List<BemEntity> recentlyNamedEntities;	
	private List<BemEntity> namedEntities;

//	public BemDatasetProcessorManager(BemDataset dataset, List<BemDatasetProcessor> processors) {
//		this.dataset = dataset;
//		this.processors = new ArrayList<>(processors);
//	}
	
	public BemDatasetProcessorManager(BemDataset dataset, List<Pair<Class<? extends BemDatasetProcessor>, Properties>> processors) throws BemDatasetProcessorException {
		this.dataset = dataset;
		this.processors = new ArrayList<>(processors.size());
		
		for (Pair<Class<? extends BemDatasetProcessor>, Properties> pair : processors) {
			Class<? extends BemDatasetProcessor> processorClass = pair.getKey();
			Properties properties = pair.getValue();
			
			try {
				Constructor<? extends BemDatasetProcessor> constructor = processorClass
						.getConstructor(BemDatasetProcessorManager.class, Properties.class);
				BemDatasetProcessor processor = constructor.newInstance(this, properties);
				processor.initialize();
				this.processors.add(processor);
			} catch (Exception e) {
				throw new BemDatasetProcessorException(
						String.format("Cannot initialize instance of class '%s'.", processorClass.getName()), e);
			}
			
		}
		
	}
	

	public void process() throws BemDatasetProcessorException {
		
		int numberOfAllEntities = dataset.getAllEntities().size();
		
		namedEntities = new ArrayList<>();		
		for (BemEntity entity : dataset.getAllEntities()) {
			if (entity.hasName()) {
				namedEntities.add(entity);
			}
		}		
		
		int numberOfNamedEntities = namedEntities.size();
		
		try {
			
			for (BemDatasetProcessor processor : processors) {				
				
				reset();
				processor.preProcess();
				
				logger.debug(String.format("Running grounding processor '%s'", processor.getName()));
				if (processor.getInputType() == BemDatasetProcessor.InputTypeEnum.Any) {
					for (BemEntity entity : dataset.getAllEntities()) {
						processor.process(entity);
					}
					processor.preCommit();
					commit();
				} else if (processor.getInputType() == BemDatasetProcessor.InputTypeEnum.UngroundedEntity) {					
					for (BemEntity entity : dataset.getAllEntities()) {
						if (!entity.hasName()) {
							processor.process(entity);
						}
					}	
					processor.preCommit();
					commit();
				} else if (processor.getInputType() == BemDatasetProcessor.InputTypeEnum.GroundedEntity) {
					for (BemEntity entity : namedEntities) {
						processor.process(entity);
					}
					processor.preCommit();
					commit();
					
					int index = 0;
					
					while (index < recentlyNamedEntities.size()) {
					
						for (; index < recentlyNamedEntities.size(); ++index) {
							BemEntity entity = recentlyNamedEntities.get(index);
							processor.process(entity);
						}
						
						processor.preCommit();
						commit();						
					}
				}
				
				processor.postProcess();

				int numberOfRecentlyNamedEntities = namedEntities.size() - numberOfNamedEntities;
				logger.debug(String.format("Result: %,d (%.2f%%) entities grounded",
						numberOfRecentlyNamedEntities, 100.0f * numberOfRecentlyNamedEntities / numberOfAllEntities));				

				numberOfNamedEntities = namedEntities.size();
			}
			
			logger.debug(String.format("Totally: %,d (%.2f%%) entities grounded",
					numberOfNamedEntities, 100.0f * numberOfNamedEntities / numberOfAllEntities));				

		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw e;
		}

	}
	
	public void trySetEntityName(
			BemEntity entity,
			String rawName,
			EncoderTypeEnum encoderType,
			boolean checkNameDuplication,
			boolean allowNameDuplication) throws BemNameConflictException {
		if (entity.hasName()) {
			String message = String.format("Entity '%s' already has name '%s' (new name is '%s')", entity, entity.getRawName(), rawName);
			throw new BemNameConflictException(message);
		}
		
		String name = EncoderTypeEnum.encode(encoderType, rawName);
		
		entity.setName(name);
		if (!rawName.equals(name)) {
			entity.setRawName(rawName);
		}

		if (temporaryNamedEntities == null) {
			temporaryNamedEntities = new TreeMap<>();
		} else if (checkNameDuplication && temporaryNamedEntities.containsKey(name)) {
			BemEntity otherEntity = temporaryNamedEntities.get(name);
			if (allowNameDuplication && entity.isIdenticalTo(otherEntity)) {
				entity.setSameAs(otherEntity);
			} else {
				String message = String.format(
						"Entity '%s' (raw name is '%s') already has the same name '%s'\r\n(unnamed entity is '%s', raw name '%s')",
						otherEntity,
						otherEntity.getRawName(),
						name,
						entity,
						entity.getRawName());
				throw new BemNameConflictException(message);
			}
			return;
		} 
		
		temporaryNamedEntities.put(name, entity);
	}
	
	private void reset() {
		recentlyNamedEntities = new ArrayList<>();
	}
	
	
	private void commit() {		
	
		if (temporaryNamedEntities != null) {			
			namedEntities.addAll(temporaryNamedEntities.values());
			recentlyNamedEntities.addAll(temporaryNamedEntities.values());
			temporaryNamedEntities.clear();
		}

	}
	
	private void rollback() {
		
		if (temporaryNamedEntities != null) {			
			for (BemEntity entity : temporaryNamedEntities.values()) {
				entity.setRawName(null);
				entity.setName(null);
			}
			temporaryNamedEntities.clear();
		}
		
	}

	public BemSchema getSchema() {
		return dataset.getSchema();
	}

}
