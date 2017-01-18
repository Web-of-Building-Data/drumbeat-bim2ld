package fi.aalto.cs.drumbeat.data.bem.schema;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;
import fi.aalto.cs.drumbeat.common.string.StringUtils;

public class BemCollectionTypeInfo extends BemTypeInfo {

	public BemCollectionTypeInfo(BemSchema schema, String name) {
		super(schema, name);
	}
	
	private BemCollectionKindEnum collectionKind;
	private String itemTypeInfoName;
	private BemTypeInfo itemTypeInfo;
	private BemCardinality cardinality;

	public BemCollectionTypeInfo(BemSchema schema, String typeName,
			BemCollectionKindEnum collectionKind, BemTypeInfo itemTypeInfo) {
		super(schema, typeName);
		this.collectionKind = collectionKind;
		this.itemTypeInfo = itemTypeInfo;
		this.itemTypeInfoName = itemTypeInfo.getName();
	}

	public BemCollectionTypeInfo(BemSchema schema, String typeName,
			BemCollectionKindEnum collectionKind, String itemTypeInfoName) {
		super(schema, typeName);
		this.collectionKind = collectionKind;
		this.itemTypeInfoName = itemTypeInfoName;
	}

	public BemCollectionKindEnum getCollectionKind() {
		return collectionKind;
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
		if (itemTypeInfo == null && !StringUtils.EMPTY.equals(itemTypeInfoName)) {
			try {
				itemTypeInfo = getSchema().getTypeInfo(itemTypeInfoName);
			} catch (DrbNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return itemTypeInfo;
	}

	@Override
	public boolean isCollectionType() {
		return true;
	}

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

		BemTypeInfo itemTypeInfo = getItemTypeInfo();		
		if (itemTypeInfo == null) {
			return null; 
		}

		String superTypeName = formatCollectionTypeName(collectionKind,
				itemTypeInfo.getName(), null);

		return new BemCollectionTypeInfo(
				getSchema(), superTypeName, collectionKind, itemTypeInfo);
		
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

		BemCollectionTypeInfo superType = new BemCollectionTypeInfo(
				getSchema(), superTypeName, collectionKind, StringUtils.EMPTY);

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
