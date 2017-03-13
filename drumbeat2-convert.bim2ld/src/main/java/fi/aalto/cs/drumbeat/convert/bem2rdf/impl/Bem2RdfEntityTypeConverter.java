package fi.aalto.cs.drumbeat.convert.bem2rdf.impl;

import java.util.*;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import fi.aalto.cs.drumbeat.convert.bem2rdf.Bem2RdfConverterConfigurationException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemEntity;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemValue;
import fi.aalto.cs.drumbeat.data.bem.schema.*;
import fi.aalto.cs.drumbeat.owl.OwlVocabulary;

class Bem2RdfEntityTypeConverter {

	private final Bem2RdfConverterManager manager;
	private final boolean nameAllBlankNodes;
	private final boolean useLongAttributeName;

	public Bem2RdfEntityTypeConverter(Bem2RdfConverterManager manager) throws Bem2RdfConverterConfigurationException {
		this.manager = manager;
		this.nameAllBlankNodes = manager.contextParams.nameAllBlankNodes();
		this.useLongAttributeName = manager.contextParams.useLongAttributeName();
	}

	// *****************************************
	// Region ENTITY TYPES & VALUES
	// *****************************************

	public Resource convertEntityTypeInfo(Model jenaModel, BemEntityTypeInfo typeInfo, boolean includeDetails) {
		
		Resource typeResource = jenaModel.createResource(manager.uriBuilder.buildTypeUri(typeInfo));
		
		if (includeDetails) {
			jenaModel.add(typeResource, RDF.type, OWL.Class);
			convertEntityTypeInheritanceAndDisjointRelationships(jenaModel, typeResource, typeInfo);
			convertEntityTypeAttributes(jenaModel, typeResource, typeInfo);
			convertEntityTypeUniqueKeys(jenaModel, typeResource, typeInfo);
		}

		return typeResource;

	}
	
	private void convertEntityTypeInheritanceAndDisjointRelationships(Model jenaModel, Resource typeResource, BemEntityTypeInfo typeInfo) {
		
		//
		// OWL2 supports owl:disjointUnionOf
		// See:
		// http://www.w3.org/2007/OWL/wiki/New_Features_and_Rationale#F1:_DisjointUnion
		//
		List<BemEntityTypeInfo> disjointClasses = null;

		final boolean supportsDisjointUnionOf = manager.targetOwlProfileList.supportsStatement(OWL2.disjointUnionOf,
				OwlVocabulary.DumpData.ANY_URI_LIST);

		if (typeInfo.isAbstractSuperType() && supportsDisjointUnionOf) {
			List<BemEntityTypeInfo> allSubtypeInfos = typeInfo.getSubTypeInfos();
			if (allSubtypeInfos.size() > 1) { // OWL2.disjointUnionOf requires
												// at least two members
				List<RDFNode> allSubtypeResources = new ArrayList<>(allSubtypeInfos.size());
				for (BemEntityTypeInfo subTypeInfo : allSubtypeInfos) {
					allSubtypeResources.add(jenaModel.createResource(manager.uriBuilder.buildTypeUri(subTypeInfo)));
				}
				jenaModel.add(typeResource, OWL2.disjointUnionOf, jenaModel.createList(allSubtypeResources.iterator()));
			}
		}

		//
		// write entity info
		//
		BemEntityTypeInfo superTypeInfo = typeInfo.getSuperTypeInfo();
		if (superTypeInfo != null) {
			jenaModel.add(typeResource, RDFS.subClassOf,
					jenaModel.createResource(manager.uriBuilder.buildTypeUri(superTypeInfo)));

			if (!superTypeInfo.isAbstractSuperType() || !supportsDisjointUnionOf) {

				List<BemEntityTypeInfo> allSubtypeInfos = superTypeInfo.getSubTypeInfos();

				if (allSubtypeInfos.size() > 1) {

					int indexOfCurrentType = allSubtypeInfos.indexOf(typeInfo);

					final boolean supportDisjointWithList = manager.targetOwlProfileList
							.supportsStatement(OWL.disjointWith, OwlVocabulary.DumpData.ANY_URI_LIST);

					if (allSubtypeInfos.size() > 2 && supportDisjointWithList) {
						//
						// OWL2 allow object of property "owl:disjointWith" to
						// be rdf:list
						// All classes will be pairwise disjoint
						// See:
						// http://www.w3.org/2007/OWL/wiki/New_Features_and_Rationale#F2:_DisjointClasses
						//
						disjointClasses = allSubtypeInfos;

					} else {
						final boolean supportDisjointWithSingleClass = manager.targetOwlProfileList
								.supportsStatement(OWL.disjointWith, OwlVocabulary.DumpData.ANY_URI_1);
						// manager.context.getOwlVersion() <
						// OwlProfile.OWL_VERSION_2_0

						if (supportDisjointWithSingleClass) {

							//
							// OWL1 doesn't allow object of property
							// "owl:disjointWith" to be rdf:list
							// See:
							// http://www.w3.org/TR/owl-ref/#disjointWith-def
							//
							if (indexOfCurrentType + 1 < allSubtypeInfos.size()) {

								for (int i = indexOfCurrentType + 1; i < allSubtypeInfos.size(); ++i) {
									jenaModel.add(typeResource, OWL.disjointWith, jenaModel
											.createResource(manager.uriBuilder.buildTypeUri(allSubtypeInfos.get(i))));
								}

							}

						}

					}

				}

			}

		} else { // superTypeInfo == null
			jenaModel.add(typeResource, RDFS.subClassOf, jenaModel.createResource(
					manager.uriBuilder.buildBuiltInOntologyUri(Bem2RdfVocabulary.BuiltInOntology.Entity)));
		}
		
		
		if (disjointClasses != null) {
			//
			// OWL2 allow object of property "owl:disjointWith" to be rdf:list
			// All classes will be pairwise disjoint
			// See:
			// http://www.w3.org/2007/OWL/wiki/New_Features_and_Rationale#F2:_DisjointClasses
			//

			// adapter.exportEmptyLine();

			Resource blankNode = jenaModel.createResource();
			jenaModel.add(blankNode, RDF.type, OWL2.AllDisjointClasses);

			List<Resource> disjointClassResources = new ArrayList<>();
			for (BemTypeInfo disjointClassTypeInfo : disjointClasses) {
				disjointClassResources
						.add(jenaModel.createResource(manager.uriBuilder.buildTypeUri(disjointClassTypeInfo)));
			}

			jenaModel.add(blankNode, OWL2.members, jenaModel.createList(disjointClassResources.iterator()));
		}

		
		
	}
	
	private void convertEntityTypeAttributes(Model jenaModel, Resource typeResource, BemEntityTypeInfo typeInfo) {
		
		List<BemAttributeInfo> attributeInfos = typeInfo.getAttributeInfos(false);
		if (attributeInfos != null) {
			for (BemAttributeInfo attributeInfo : attributeInfos) {
				convertEntityTypeAttribute(jenaModel, typeResource, attributeInfo, true);
			}
		}
	}
	
	
	private Property convertEntityTypeAttribute(Model jenaModel, Resource typeResource, BemAttributeInfo attributeInfo, boolean includeDetails) {
		
		Property attributeProperty = jenaModel.createProperty(manager.uriBuilder.buildAttributeUri(attributeInfo, useLongAttributeName));
		boolean exportDomainsAndRanges = useLongAttributeName;
		
		if (includeDetails) {
			jenaModel.add(attributeProperty, RDF.type, OWL.ObjectProperty);
	
			BemTypeInfo attributeTypeInfo = attributeInfo.getValueTypeInfo();
	
			if (attributeTypeInfo instanceof BemCollectionTypeInfo) {
				BemCollectionTypeInfo collectionAttributeTypeInfo = (BemCollectionTypeInfo) attributeTypeInfo;
				boolean exportAsSingleCollection = collectionAttributeTypeInfo.isSorted();
	
				if (exportAsSingleCollection) {
					assert(collectionAttributeTypeInfo.getCollectionKind().equals(BemCollectionKindEnum.List)) : collectionAttributeTypeInfo; 
					Resource collectionTypeResource =
							manager.collectionTypeConverter.convertCollectionTypeInfo(jenaModel, collectionAttributeTypeInfo, true);

					int min = attributeInfo.isOptional() ? 0 : 1;
					int max = 1;
					manager.convertPropertyRestrictions(attributeProperty, typeResource, collectionTypeResource, true,
							min, max, jenaModel, exportDomainsAndRanges, exportDomainsAndRanges);
	
				} else {
					Resource itemTypeResource = jenaModel.createResource(
							manager.uriBuilder.buildTypeUri(collectionAttributeTypeInfo.getItemTypeInfo()));
					int min = attributeInfo.isOptional() ? 0
							: collectionAttributeTypeInfo.getCardinality().getMinCardinality();
					int max = collectionAttributeTypeInfo.getCardinality().getMaxCardinality();
					manager.convertPropertyRestrictions(attributeProperty, typeResource, itemTypeResource, true, min,
							max, jenaModel, exportDomainsAndRanges, exportDomainsAndRanges);
				}
	
			} else {
	
				Resource itemTypeResource = jenaModel
						.createResource(manager.uriBuilder.buildTypeUri(attributeTypeInfo));
				int min = attributeInfo.isOptional() ? 0 : 1;
				int max = 1;
				manager.convertPropertyRestrictions(attributeProperty, typeResource, itemTypeResource, true, min, max,
						jenaModel, exportDomainsAndRanges, exportDomainsAndRanges);
	
			}
			
		}
		
		return attributeProperty;
	}	
	
	private void convertEntityTypeUniqueKeys(Model jenaModel, Resource typeResource, BemEntityTypeInfo typeInfo) {
		List<BemUniqueKeyInfo> uniqueKeyInfos = typeInfo.getUniqueKeyInfos();

		final boolean supportHasKey = manager.targetOwlProfileList.supportsStatement(OWL2.hasKey,
				OwlVocabulary.DumpData.ANY_URI_LIST);

		if (uniqueKeyInfos != null && supportHasKey) {
			for (BemUniqueKeyInfo uniqueKeyInfo : uniqueKeyInfos) {
				List<Resource> attributeResources = new ArrayList<>();
				for (BemAttributeInfo attributeInfo : uniqueKeyInfo.getAttributeInfos()) {
					attributeResources.add(jenaModel.createResource(
							manager.uriBuilder.buildAttributeUri(attributeInfo, useLongAttributeName)));
				}
				jenaModel.add(typeResource, OWL2.hasKey, jenaModel.createList(attributeResources.iterator()));
			}
		}
	}
	
	private Resource createEntityValueResource(BemEntity entity, Model jenaModel) {
		if (entity.hasName()) {
			String localName = String.format(Bem2RdfVocabulary.Dataset.GUID_NODE_ENTITY_URI_FORMAT, entity.getName()); 
			return jenaModel.createResource(manager.uriBuilder.buildDatasetUri(localName));
		} else {
			String localName = String.format(Bem2RdfVocabulary.Dataset.BLANK_NODE_ENTITY_URI_FORMAT, entity.getLocalId());
			if (nameAllBlankNodes) {
				return jenaModel.createResource(manager.uriBuilder.buildDatasetBlankNodeUri(localName));				
			} else {
				return jenaModel.createResource(new AnonId(localName));				
			}
		}
	}
	
	

	public Resource convertEntityValue(Model jenaModel, BemEntity entity, boolean includeAttributes) {
		
		Resource entityResource = createEntityValueResource(entity, jenaModel);
		
		if (includeAttributes) {
			
			long childNodeCount = 1L;
			
			BemEntityTypeInfo entityTypeInfo = entity.getTypeInfo();
			Resource entityTypeResource = convertEntityTypeInfo(jenaModel, entityTypeInfo, false);
			entityResource.addProperty(RDF.type, jenaModel.createResource(manager.uriBuilder.buildTypeUri(entityTypeInfo)));
			
			for (Entry<BemAttributeInfo, BemValue> entry : entity.getAttributeMap().entries()) {
				BemAttributeInfo attributeInfo = entry.getKey();
				Property attributeProperty = convertEntityTypeAttribute(jenaModel, entityTypeResource, attributeInfo, false);
				
				BemValue attributeValue = entry.getValue();
				RDFNode attributeNode = manager.convertValue(
						jenaModel, attributeValue, attributeInfo.getValueTypeInfo(), entityResource, childNodeCount++, false);
				
				entityResource.addProperty(attributeProperty, attributeNode);
			}			
		}

		
		return entityResource;
	}
	

	// *****************************************
	// EndRegion ENTITY TYPES & VALUES
	// *****************************************

}
