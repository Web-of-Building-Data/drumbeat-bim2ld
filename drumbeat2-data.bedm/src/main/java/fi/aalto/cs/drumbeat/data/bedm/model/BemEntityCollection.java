package fi.aalto.cs.drumbeat.data.bem.model;

import java.util.Collection;

public class BemEntityCollection extends BemCollectionValue<BemEntity> {

	public BemEntityCollection() {
	}

	public BemEntityCollection(Collection<BemEntity> values) {
		super(values);
	}

	@Override
	public Boolean isLiteralType() {
		return false;
	}

}
