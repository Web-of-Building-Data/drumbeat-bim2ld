package fi.aalto.cs.drumbeat.data.bem;

public class BemAttributeNotFoundException extends BemNotFoundException {

	private static final long serialVersionUID = 1L;

	public BemAttributeNotFoundException() {
	}

	public BemAttributeNotFoundException(String message) {
		super(message);
	}

	public BemAttributeNotFoundException(Throwable cause) {
		super(cause);
	}

	public BemAttributeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemAttributeNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
