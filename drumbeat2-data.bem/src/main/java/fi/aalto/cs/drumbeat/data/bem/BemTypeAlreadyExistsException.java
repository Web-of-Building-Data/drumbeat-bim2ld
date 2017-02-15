package fi.aalto.cs.drumbeat.data.bem;

public class BemTypeAlreadyExistsException extends BemException {

	private static final long serialVersionUID = 1L;

	public BemTypeAlreadyExistsException() {
	}

	public BemTypeAlreadyExistsException(String message) {
		super(message);
	}

	public BemTypeAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public BemTypeAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemTypeAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
