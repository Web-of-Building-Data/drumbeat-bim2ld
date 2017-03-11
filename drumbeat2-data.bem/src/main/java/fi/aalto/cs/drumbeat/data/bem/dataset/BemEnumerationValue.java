package fi.aalto.cs.drumbeat.data.bem.dataset;

public class BemEnumerationValue extends BemSimpleValue {

	public BemEnumerationValue(Object value) {
		super(value);
	}
	
	@Override
	public String getValue() {
		return (String)super.getValue();
	}
	
}
