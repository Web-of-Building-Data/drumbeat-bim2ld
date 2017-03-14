package fi.aalto.cs.drumbeat.data.bem.schema;

/**
 * A pair of two integers: min and max
 * 
 * @author Nam Vu
 *
 */
public class BemCardinality {

	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int UNBOUNDED = Integer.MAX_VALUE;
	
	public static final int LIST_MIN_INDEX = 0;
	
	private final int minCardinality;
	private final int maxCardinality;
	private final int minIndex;
	private final int maxIndex;
	private final boolean isArrayIndex;
	
	public BemCardinality(int left, int right, boolean isArrayIndex) {
		this.isArrayIndex = isArrayIndex;
		if (isArrayIndex) {
			minIndex = left;
			maxIndex = right;
			minCardinality = right - left + 1;
			maxCardinality = minCardinality;
		} else {
			minCardinality = left;
			maxCardinality = right;
			minIndex = LIST_MIN_INDEX;
			maxIndex = minIndex + maxCardinality - 1;
		}
	}
	
	public boolean isArrayIndex() {
		return isArrayIndex;
	}
	
	public boolean isCardinalityFixed() {
		return minCardinality == maxCardinality;
	}
	
	public boolean isMultiple() {
		return maxCardinality > ONE;
	}
	
	public boolean isSingle() {
		return maxCardinality <= ONE;
	}	
	
	public int getMinCardinality() {
		return minCardinality;
	}
	
	public int getMaxCardinality() {
		return maxCardinality;
	}
	
	public int getMinIndex() {
		return minIndex;
	}
	
	public int getMaxIndex() {
		return maxIndex;
	}
	
	@Override
	public String toString() {
		if (isArrayIndex) {
			return String.format("[%s:%s]", minIndex, maxIndex);			
		} else {
			return String.format("[%s:%s]", minCardinality, maxCardinality == UNBOUNDED ? "?" : maxCardinality);
		}
	}
	
}
