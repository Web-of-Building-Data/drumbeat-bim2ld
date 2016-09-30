package fi.aalto.cs.drumbeat.data.bedm.schema;

import fi.aalto.cs.drumbeat.common.DrbNotFoundException;
import fi.aalto.cs.drumbeat.common.string.StringUtils;

public class DrbCollectionTypeInfo extends DrbTypeInfo {

	public DrbCollectionTypeInfo(DrbSchema schema, String name) {
		super(schema, name);
	}
	
	private DrbCollectionKindEnum collectionKind;
	private String itemTypeInfoName;
	private DrbTypeInfo itemTypeInfo;
	private DrbCardinality cardinality;

	public DrbCollectionTypeInfo(DrbSchema schema, String typeName,
			DrbCollectionKindEnum collectionKind, DrbTypeInfo itemTypeInfo) {
		super(schema, typeName);
		this.collectionKind = collectionKind;
		this.itemTypeInfo = itemTypeInfo;
		this.itemTypeInfoName = itemTypeInfo.getName();
	}

	public DrbCollectionTypeInfo(DrbSchema schema, String typeName,
			DrbCollectionKindEnum collectionKind, String itemTypeInfoName) {
		super(schema, typeName);
		this.collectionKind = collectionKind;
		this.itemTypeInfoName = itemTypeInfoName;
	}

	public DrbCollectionKindEnum getCollectionKind() {
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

	public DrbTypeInfo getItemTypeInfo() {
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
	public DrbCardinality getCardinality() {
		return cardinality;
	}

	/**
	 * @param cardinality
	 *            the cardinality to set
	 */
	public void setCardinality(DrbCardinality cardinality) {
		this.cardinality = cardinality;
	}

	/**
	 * @return the superTypeInfo
	 */
	public DrbCollectionTypeInfo getSuperCollectionTypeWithItemTypeAndNoCardinalities() {
		
		if (cardinality == null) {
			return null;
		}

		DrbTypeInfo itemTypeInfo = getItemTypeInfo();		
		if (itemTypeInfo == null) {
			return null; 
		}

		String superTypeName = formatCollectionTypeName(collectionKind,
				itemTypeInfo.getName(), null);

		return new DrbCollectionTypeInfo(
				getSchema(), superTypeName, collectionKind, itemTypeInfo);
		
	}

	/**
	 * @return the superTypeInfo
	 */
	public DrbCollectionTypeInfo getSuperCollectionTypeWithCardinalitiesAndNoItemType() {
		
		if (cardinality == null) {
			return null;
		}

		DrbTypeInfo itemTypeInfo = getItemTypeInfo();		
		if (itemTypeInfo == null) {
			return null; 
		}
		
		String superTypeName = formatCollectionTypeName(collectionKind, null,
				cardinality);

		DrbCollectionTypeInfo superType = new DrbCollectionTypeInfo(
				getSchema(), superTypeName, collectionKind, StringUtils.EMPTY);

		superType.setCardinality(cardinality);

		return superType;
	}

	public static String formatCollectionTypeName(DrbCollectionKindEnum collectionKind, String itemTypeInfoName, DrbCardinality cardinality) {
		
		return String.format("%s_%s", itemTypeInfoName, collectionKind);		
		
//		if (itemTypeInfoName != null && cardinality != null) {
//			return String.format("%s_%s_%s_%s_%s",
//					collectionKind,
//					cardinality.getMinCardinality(),
//					cardinality.getMaxCardinality() == DrbCardinality.UNBOUNDED ? "UNBOUNDED" : cardinality.getMaxCardinality(),
//					DrbVocabulary.ExpressFormat.OF,
//					itemTypeInfoName); 
//		} else if (cardinality != null) {
//			return String.format("%s_%s_%s",
//					collectionKind,
//					cardinality.getMinCardinality(),
//					cardinality.getMaxCardinality() == DrbCardinality.UNBOUNDED ? "UNBOUNDED" : cardinality.getMaxCardinality()); 
//		} else { // itemTypeInfoName
//			return String.format("%s_%s_%s",
//					collectionKind,
//					DrbVocabulary.ExpressFormat.OF,
//					itemTypeInfoName);
//		}

	}

}
