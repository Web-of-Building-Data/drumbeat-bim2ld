package fi.aalto.cs.drumbeat.convert.bem2rdf;

public class DatatypeNotSupportedException extends Bem2RdfConverterConfigurationException {

	private static final long serialVersionUID = 1L;

	public DatatypeNotSupportedException() {
	}

	public DatatypeNotSupportedException(String message) {
		super(message);
	}

	public DatatypeNotSupportedException(Throwable cause) {
		super(cause);
	}

	public DatatypeNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatatypeNotSupportedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
