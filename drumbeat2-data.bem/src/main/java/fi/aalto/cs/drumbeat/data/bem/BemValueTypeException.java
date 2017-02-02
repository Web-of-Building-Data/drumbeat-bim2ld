package fi.aalto.cs.drumbeat.data.bem;

public class BemValueTypeException extends BemException {

	private static final long serialVersionUID = 1L;

	public BemValueTypeException() {
	}

	public BemValueTypeException(String message) {
		super(message);
	}

	public BemValueTypeException(Throwable cause) {
		super(cause);
	}

	public BemValueTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemValueTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
