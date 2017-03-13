package fi.aalto.cs.drumbeat.convert.bem2rdf;

public class OwlProfileViolationException extends Exception {

	private static final long serialVersionUID = 1L;

	public OwlProfileViolationException() {
	}

	public OwlProfileViolationException(String message) {
		super(message);
	}

	public OwlProfileViolationException(Throwable cause) {
		super(cause);
	}

	public OwlProfileViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OwlProfileViolationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
