package fi.aalto.cs.drumbeat.data.bem;

public class BemException extends Exception {

	private static final long serialVersionUID = 1L;

	public BemException() {
	}

	public BemException(String message) {
		super(message);
	}

	public BemException(Throwable cause) {
		super(cause);
	}

	public BemException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
