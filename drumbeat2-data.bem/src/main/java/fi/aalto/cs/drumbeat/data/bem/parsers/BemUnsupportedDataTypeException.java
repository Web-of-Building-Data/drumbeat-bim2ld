package fi.aalto.cs.drumbeat.data.bem.parsers;

public class BemUnsupportedDataTypeException extends BemParserException {

	private static final long serialVersionUID = 1L;

	public BemUnsupportedDataTypeException() {
	}

	public BemUnsupportedDataTypeException(String message) {
		super(message);
	}

	public BemUnsupportedDataTypeException(Throwable cause) {
		super(cause);
	}

	public BemUnsupportedDataTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemUnsupportedDataTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
