package fi.aalto.cs.drumbeat.ifc.common;

import fi.aalto.cs.drumbeat.common.DrbException;

public class IfcException extends DrbException {

	private static final long serialVersionUID = 1L;

	public IfcException() {
	}

	public IfcException(String arg0) {
		super(arg0);
	}

	public IfcException(Throwable arg0) {
		super(arg0);
	}

	public IfcException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IfcException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
