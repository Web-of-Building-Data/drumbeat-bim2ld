package fi.aalto.cs.drumbeat.convert.bem2rdf;

import fi.aalto.cs.drumbeat.data.bem.BemException;

public class Bem2RdfConverterException extends BemException {
	
	private static final long serialVersionUID = 1L;

	public Bem2RdfConverterException() {
	}

	public Bem2RdfConverterException(String message) {
		super(message);
	}

	public Bem2RdfConverterException(Throwable cause) {
		super(cause);
	}

	public Bem2RdfConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	public Bem2RdfConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	

}
