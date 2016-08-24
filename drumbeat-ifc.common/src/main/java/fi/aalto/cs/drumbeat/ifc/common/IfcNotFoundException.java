package fi.aalto.cs.drumbeat.ifc.common;

import fi.aalto.cs.drumbeat.ifc.common.IfcException;

public class DrbNotFoundException extends IfcException {

	private static final long serialVersionUID = 1L;
	
	public DrbNotFoundException() {
	}

	public DrbNotFoundException(String arg0) {
		super(arg0);
	}

	public DrbNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public DrbNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DrbNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}	

}
