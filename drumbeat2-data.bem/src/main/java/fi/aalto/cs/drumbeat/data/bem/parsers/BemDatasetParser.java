package fi.aalto.cs.drumbeat.data.bem.parsers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import fi.aalto.cs.drumbeat.common.collections.Pair;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.processors.BemDatasetProcessor;
import fi.aalto.cs.drumbeat.data.bem.processors.BemDatasetProcessorException;
import fi.aalto.cs.drumbeat.data.bem.processors.BemDatasetProcessorManager;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public abstract class BemDatasetParser {
	
	private List<Pair<Class<? extends BemDatasetProcessor>, Properties>> processors;
	
	public BemDatasetParser() {
	}
	
	public abstract Collection<String> getSupportedFileTypes();
	
	public BemDataset parse(InputStream in, String fileType, boolean checkFileType) throws BemException {
		return parse(in, fileType, checkFileType, null);
	}

	public abstract BemDataset parse(InputStream in, String fileType, boolean checkFileType, BemSchema schema) throws BemException;
	
	public abstract BemDatasetBuilder getDatasetBuilder(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException;
	
	public boolean checkFileType(String fileType) {
		// case-sensitive checking (like in UNIX)
		return getSupportedFileTypes().contains(fileType.trim());		
	}
	
	protected void internalCheckFileType(String fileType, boolean checkFileType) throws BemUnsupportedDataTypeException {
		if (checkFileType && !checkFileType(fileType)) {
			throw new BemUnsupportedDataTypeException("Invalid file type: " + fileType);
		}
	}
	
	public List<Pair<Class<? extends BemDatasetProcessor>, Properties>> getProcessors() {
		if (processors == null) {
			processors = new ArrayList<>();
		}
		return processors;
	}
	
	public void process(BemDataset dataset) throws BemDatasetProcessorException {
		if (processors != null) {
			BemDatasetProcessorManager processorManager = new BemDatasetProcessorManager(dataset, processors);
			processorManager.process();
		}
	}
	
}
