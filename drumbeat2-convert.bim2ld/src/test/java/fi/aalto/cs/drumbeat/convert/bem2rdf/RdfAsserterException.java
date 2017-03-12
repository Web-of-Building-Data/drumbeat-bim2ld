package fi.aalto.cs.drumbeat.convert.bem2rdf;

public class RdfAsserterException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdfAsserterException() {
	}

	public RdfAsserterException(String message) {
		super(message);
	}

	public RdfAsserterException(Throwable cause) {
		super(cause);
	}

	public RdfAsserterException(String message, Throwable cause) {
		super(message, cause);
	}

	public RdfAsserterException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
