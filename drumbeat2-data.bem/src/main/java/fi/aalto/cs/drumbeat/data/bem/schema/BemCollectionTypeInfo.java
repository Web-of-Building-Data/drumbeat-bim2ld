package fi.aalto.cs.drumbeat.data.bem.schema;

public class BemCollectionTypeInfo extends BemTypeInfo {

	private BemCollectionKindEnum collectionKind;
	private BemTypeInfo itemTypeInfo;
	private BemCardinality cardinality;

	public BemCollectionTypeInfo(BemSchema schema, String name) {
		super(schema, name);
	}
	
	@Override
	public BemValueKindEnum getValueKind() {
		return BemValueKindEnum.COLLECTION;		
	}

	public BemCollectionKindEnum getCollectionKind() {
		return collectionKind;
	}
	
	public void setCollectionKind(BemCollectionKindEnum collectionKind) {
		this.collectionKind = collectionKind;
	}

	public boolean isSorted() {
		return collectionKind.isSorted();
	}
	
	/**
	 * @return the itemsAreUnique
	 */
	public boolean allowsRepeatedItems() {
		return collectionKind.allowsDuplicatedItems();
	}

	public BemTypeInfo getItemTypeInfo() {
		return itemTypeInfo;
	}
	
	public void setItemTypeInfo(BemTypeInfo itemTypeInfo) {
		this.itemTypeInfo = itemTypeInfo;
	}

//	@Override
//	public boolean isCollectionType() {
//		return true;
//	}

//	@Override
//	public boolean isEntityOrSelectType() {
//		return getItemTypeInfo().isEntityOrSelectType();
//	}

	/**
	 * @return the cardinality
	 */
	public BemCardinality getCardinality() {
		return cardinality;
	}

	/**
	 * @param cardinality
	 *            the cardinality to set
	 */
	public void setCardinality(BemCardinality cardinality) {
		this.cardinality = cardinality;
	}

	/**
	 * @return the superTypeInfo
	 */
	public BemCollectionTypeInfo getSuperCollectionTypeWithItemTypeAndNoCardinalities() {
		
		if (cardinality == null) {
			return null;
		}

		String superTypeName = formatCollectionTypeName(collectionKind,
				itemTypeInfo.getName(), null);

		BemCollectionTypeInfo superType = new BemCollectionTypeInfo(getSchema(), superTypeName);
		superType.setCollectionKind(collectionKind);
		superType.setItemTypeInfo(itemTypeInfo);
		return superType;
	}

	/**
	 * @return the superTypeInfo
	 */
	public BemCollectionTypeInfo getSuperCollectionTypeWithCardinalitiesAndNoItemType() {
		
		if (cardinality == null) {
			return null;
		}

		BemTypeInfo itemTypeInfo = getItemTypeInfo();		
		if (itemTypeInfo == null) {
			return null; 
		}
		
		String superTypeName = formatCollectionTypeName(collectionKind, null,
				cardinality);

		BemCollectionTypeInfo superType = new BemCollectionTypeInfo(getSchema(), superTypeName);
		superType.setCollectionKind(collectionKind);
		superType.setCardinality(cardinality);
		return superType;

	}

	public static String formatCollectionTypeName(BemCollectionKindEnum collectionKind, String itemTypeInfoName, BemCardinality cardinality) {
		
//		return String.format("%s_%s", itemTypeInfoName, collectionKind);
		
		StringBuilder typeNameBuilder = new StringBuilder(collectionKind.name());
		
		if (cardinality != null) {
			int left = cardinality.isArrayIndex() ? cardinality.getMinIndex() : cardinality.getMinCardinality();
			int right = cardinality.isArrayIndex() ? cardinality.getMaxIndex() : cardinality.getMaxCardinality();
			
			typeNameBuilder.append(String.format(
					"_%s_%s",
					left,
					right != BemCardinality.UNBOUNDED ? right : "UNBOUNDED"));
		}
		
		if (itemTypeInfoName != null) {
			typeNameBuilder.append(String.format(
					"_OF_%s",
					itemTypeInfoName));			
		}
		
		return typeNameBuilder.toString();

	}

}
