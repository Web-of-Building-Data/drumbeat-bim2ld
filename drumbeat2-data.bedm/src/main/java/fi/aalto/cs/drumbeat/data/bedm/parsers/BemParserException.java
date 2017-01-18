package fi.aalto.cs.drumbeat.data.bem.parsers;

import fi.aalto.cs.drumbeat.common.DrbException;

public class BemParserException extends DrbException {

	private static final long serialVersionUID = 1L;

	public BemParserException() {
	}

	public BemParserException(String message) {
		super(message);
	}

	public BemParserException(Throwable cause) {
		super(cause);
	}

	public BemParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public BemParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
