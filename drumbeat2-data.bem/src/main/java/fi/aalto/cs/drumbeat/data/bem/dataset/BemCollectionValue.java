package fi.aalto.cs.drumbeat.data.bem.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.aalto.cs.drumbeat.common.collections.IteratorEqualChecker;

public class BemCollectionValue<V extends BemValue> extends BemComplexValue {

	private final List<V> values;

	public BemCollectionValue() {
		values = new ArrayList<>();
	}

	public <T extends V> BemCollectionValue(Collection<T> values) {
		this();
		values.addAll(values);
	}
	
	
	public void add(V value) {
		values.add(value);
	}

	public List<V> getSingleValues() {
		return values;
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BemCollectionValue<?>) {
			return IteratorEqualChecker.areEqual(this.getSingleValues(), ((BemCollectionValue<?>) other).getSingleValues());
		} else {
			return false;
		}
	}
	

}	



