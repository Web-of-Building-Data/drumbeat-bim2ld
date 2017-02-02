package fi.aalto.cs.drumbeat.data.bem;

public class BemTypeNotFoundException extends BemNotFoundException {

	private static final long serialVersionUID = 1L;

	public BemTypeNotFoundException() {
	}

	public BemTypeNotFoundException(String message) {
		super(message);
	}

	public BemTypeNotFoundException(Throwable cause) {
		super(cause);
	}

	public BemTypeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemTypeNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
