package fi.aalto.cs.drumbeat.data.bem;

public class BemEntityNotFoundException extends BemNotFoundException {

	private static final long serialVersionUID = 1L;

	public BemEntityNotFoundException() {
	}

	public BemEntityNotFoundException(String message) {
		super(message);
	}

	public BemEntityNotFoundException(Throwable cause) {
		super(cause);
	}

	public BemEntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemEntityNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
