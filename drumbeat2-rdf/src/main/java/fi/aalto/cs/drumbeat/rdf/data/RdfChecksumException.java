package fi.aalto.cs.drumbeat.rdf.data;

import fi.aalto.cs.drumbeat.rdf.RdfException;

public class RdfChecksumException extends RdfException {

	private static final long serialVersionUID = 1L;

	public RdfChecksumException() {
	}

	public RdfChecksumException(String arg0) {
		super(arg0);
	}

	public RdfChecksumException(Throwable arg0) {
		super(arg0);
	}

	public RdfChecksumException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RdfChecksumException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
