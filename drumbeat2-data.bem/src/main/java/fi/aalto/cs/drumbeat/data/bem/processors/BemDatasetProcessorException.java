package fi.aalto.cs.drumbeat.data.bem.processors;

import fi.aalto.cs.drumbeat.data.bem.BemException;

public class BemDatasetProcessorException extends BemException {

	private static final long serialVersionUID = 1L;

	public BemDatasetProcessorException() {
	}

	public BemDatasetProcessorException(String message) {
		super(message);
	}

	public BemDatasetProcessorException(Throwable cause) {
		super(cause);
	}

	public BemDatasetProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemDatasetProcessorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
