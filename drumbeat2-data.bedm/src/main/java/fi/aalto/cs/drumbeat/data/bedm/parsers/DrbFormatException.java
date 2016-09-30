package fi.aalto.cs.drumbeat.data.bedm.parsers;

import fi.aalto.cs.drumbeat.common.DrbException;

public class DrbFormatException extends DrbParserException {

	private static final long serialVersionUID = 1L;

	public DrbFormatException(long lineNumber) {
	}

	public DrbFormatException(long lineNumber, String message) {
		super(formatMessage(lineNumber, message));
	}

	public DrbFormatException(long lineNumber, Throwable cause) {
		super(cause);
	}

	public DrbFormatException(long lineNumber, String message, Throwable cause) {
		super(formatMessage(lineNumber, message), cause);
	}

	public DrbFormatException(long lineNumber, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(formatMessage(lineNumber, message), cause, enableSuppression, writableStackTrace);
	}
	
	private static String formatMessage(long lineNumber, String message) {
		return String.format("%s [LINE %d]", message, lineNumber);
	}
	

}
