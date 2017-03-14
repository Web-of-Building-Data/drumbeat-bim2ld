package fi.aalto.cs.drumbeat.convert.bem2rdf;

public class RdfModelComparatorException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdfModelComparatorException() {
	}

	public RdfModelComparatorException(String message) {
		super(message);
	}

	public RdfModelComparatorException(Throwable cause) {
		super(cause);
	}

	public RdfModelComparatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public RdfModelComparatorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
