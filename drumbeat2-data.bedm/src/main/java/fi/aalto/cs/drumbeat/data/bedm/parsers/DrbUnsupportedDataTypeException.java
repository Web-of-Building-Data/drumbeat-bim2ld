package fi.aalto.cs.drumbeat.data.bedm.parsers;

import fi.aalto.cs.drumbeat.common.DrbException;

public class DrbUnsupportedDataTypeException extends DrbException {

	private static final long serialVersionUID = 1L;

	public DrbUnsupportedDataTypeException() {
	}

	public DrbUnsupportedDataTypeException(String message) {
		super(message);
	}

	public DrbUnsupportedDataTypeException(Throwable cause) {
		super(cause);
	}

	public DrbUnsupportedDataTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DrbUnsupportedDataTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
