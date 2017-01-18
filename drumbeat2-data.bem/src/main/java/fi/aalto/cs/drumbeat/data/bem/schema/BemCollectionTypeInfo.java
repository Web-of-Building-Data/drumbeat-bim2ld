package fi.aalto.cs.drumbeat.data.bem.schema;

public class BemCollectionTypeInfo extends BemTypeInfo {

	private BemCollectionKindEnum collectionKind;
	private BemTypeInfo itemTypeInfo;
	private BemCardinality cardinality;

	public BemCollectionTypeInfo(BemSchema schema, String name) {
		super(schema, name);
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
		
		return String.format("%s_%s", itemTypeInfoName, collectionKind);		
		
//		if (itemTypeInfoName != null && cardinality != null) {
//			return String.format("%s_%s_%s_%s_%s",
//					collectionKind,
//					cardinality.getMinCardinality(),
//					cardinality.getMaxCardinality() == BemCardinality.UNBOUNDED ? "UNBOUNDED" : cardinality.getMaxCardinality(),
//					BemVocabulary.ExpressFormat.OF,
//					itemTypeInfoName); 
//		} else if (cardinality != null) {
//			return String.format("%s_%s_%s",
//					collectionKind,
//					cardinality.getMinCardinality(),
//					cardinality.getMaxCardinality() == BemCardinality.UNBOUNDED ? "UNBOUNDED" : cardinality.getMaxCardinality()); 
//		} else { // itemTypeInfoName
//			return String.format("%s_%s_%s",
//					collectionKind,
//					BemVocabulary.ExpressFormat.OF,
//					itemTypeInfoName);
//		}

	}

}
