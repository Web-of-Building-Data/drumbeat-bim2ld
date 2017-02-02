package fi.aalto.cs.drumbeat.data.bem;

public class BemSchemaNotFoundException extends BemNotFoundException {

	private static final long serialVersionUID = 1L;

	public BemSchemaNotFoundException() {
	}

	public BemSchemaNotFoundException(String message) {
		super(message);
	}

	public BemSchemaNotFoundException(Throwable cause) {
		super(cause);
	}

	public BemSchemaNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemSchemaNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
