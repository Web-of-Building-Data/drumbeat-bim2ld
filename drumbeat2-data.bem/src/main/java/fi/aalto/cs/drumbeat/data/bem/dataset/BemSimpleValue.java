package fi.aalto.cs.drumbeat.data.bem.dataset;

public abstract class BemSimpleValue extends BemValue {
	
	private final Object value;
	
	public BemSimpleValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}	

}
