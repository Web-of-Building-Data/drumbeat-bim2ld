package fi.aalto.cs.drumbeat.data.bem.processors;

public class BemNameConflictException extends BemDatasetProcessorException {

	private static final long serialVersionUID = 1L;

	public BemNameConflictException() {
	}

	public BemNameConflictException(String message) {
		super(message);
	}

	public BemNameConflictException(Throwable cause) {
		super(cause);
	}

	public BemNameConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemNameConflictException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
