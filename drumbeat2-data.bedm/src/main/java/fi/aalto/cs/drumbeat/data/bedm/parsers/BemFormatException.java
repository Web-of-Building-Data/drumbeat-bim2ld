package fi.aalto.cs.drumbeat.data.bem.parsers;

import fi.aalto.cs.drumbeat.common.DrbException;

public class BemFormatException extends BemParserException {

	private static final long serialVersionUID = 1L;

	public BemFormatException(long lineNumber) {
	}

	public BemFormatException(long lineNumber, String message) {
		super(formatMessage(lineNumber, message));
	}

	public BemFormatException(long lineNumber, Throwable cause) {
		super(cause);
	}

	public BemFormatException(long lineNumber, String message, Throwable cause) {
		super(formatMessage(lineNumber, message), cause);
	}

	public BemFormatException(long lineNumber, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(formatMessage(lineNumber, message), cause, enableSuppression, writableStackTrace);
	}
	
	private static String formatMessage(long lineNumber, String message) {
		return String.format("%s [LINE %d]", message, lineNumber);
	}
	

}
