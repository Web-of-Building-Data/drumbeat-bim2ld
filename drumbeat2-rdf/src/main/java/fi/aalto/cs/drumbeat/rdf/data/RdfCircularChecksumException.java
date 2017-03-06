package fi.aalto.cs.drumbeat.rdf.data;

public class RdfCircularChecksumException extends RdfChecksumException {

	private static final long serialVersionUID = 1L;

	public RdfCircularChecksumException() {
	}

	public RdfCircularChecksumException(String arg0) {
		super(arg0);
	}

	public RdfCircularChecksumException(Throwable arg0) {
		super(arg0);
	}

	public RdfCircularChecksumException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RdfCircularChecksumException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
