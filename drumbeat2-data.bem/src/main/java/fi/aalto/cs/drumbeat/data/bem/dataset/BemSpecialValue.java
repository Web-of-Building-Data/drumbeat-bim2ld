package fi.aalto.cs.drumbeat.data.bem.dataset;

public class BemSpecialValue extends BemValue {
	
	public static final BemSpecialValue NULL = new BemSpecialValue("NULL");	
	public static final BemSpecialValue ANY = new BemSpecialValue("ANY");
	
	
	private final String name;
	
	public BemSpecialValue(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof BemSpecialValue && name.equals(((BemSpecialValue)other).name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
