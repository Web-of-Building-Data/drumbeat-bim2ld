package fi.aalto.cs.drumbeat.data.bedm.parsers;

import fi.aalto.cs.drumbeat.common.DrbException;

public class DrbParserException extends DrbException {

	private static final long serialVersionUID = 1L;

	public DrbParserException() {
	}

	public DrbParserException(String message) {
		super(message);
	}

	public DrbParserException(Throwable cause) {
		super(cause);
	}

	public DrbParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public DrbParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
