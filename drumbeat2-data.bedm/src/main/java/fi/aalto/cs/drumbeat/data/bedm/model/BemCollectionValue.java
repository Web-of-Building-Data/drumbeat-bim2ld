package fi.aalto.cs.drumbeat.data.bem.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.aalto.cs.drumbeat.common.collections.IteratorEqualChecker;
import fi.aalto.cs.drumbeat.common.string.StringUtils;

public abstract class BemCollectionValue<V extends BemNonCollectionValue> extends BemValue {
	
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
	public String toString() {
		return StringUtils.collectionToString(
				values,
				StringUtils.OPENING_ROUND_BRACKET,
				StringUtils.CLOSING_ROUND_BRACKET,
				null,
				StringUtils.COMMA);
	}	
	
	
	@Override
	public boolean isNullOrAny() {
		return false;
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
