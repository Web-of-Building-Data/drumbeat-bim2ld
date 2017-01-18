package fi.aalto.cs.drumbeat.data.bem;

public class BemNotFoundException extends BemException {

	private static final long serialVersionUID = 1L;

	public BemNotFoundException() {
	}

	public BemNotFoundException(String message) {
		super(message);
	}

	public BemNotFoundException(Throwable cause) {
		super(cause);
	}

	public BemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
