package fi.aalto.cs.drumbeat.rdf.data;

public class RdfComparatorRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RdfComparatorRuntimeException() {
	}

	public RdfComparatorRuntimeException(String message) {
		super(message);
	}

	public RdfComparatorRuntimeException(Throwable cause) {
		super(cause);
	}

	public RdfComparatorRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RdfComparatorRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
