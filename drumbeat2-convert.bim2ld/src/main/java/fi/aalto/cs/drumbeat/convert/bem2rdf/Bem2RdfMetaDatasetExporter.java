//package fi.aalto.cs.drumbeat.convert.bem2rdf;
//
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.vocabulary.OWL;
//import org.apache.jena.vocabulary.RDF;
//import org.apache.jena.vocabulary.RDFS;
//import org.apache.jena.vocabulary.XSD;
//
//import fi.aalto.cs.drumbeat.data.bem.BemException;
//import fi.aalto.cs.drumbeat.data.bem.dataset.*;
//import fi.aalto.cs.drumbeat.data.bem.schema.*;
//import fi.aalto.cs.drumbeat.ifc.convert.ifc2ld.Ifc2RdfExporterBase;
//import fi.aalto.cs.drumbeat.rdf.OwlProfileList;
//import fi.aalto.cs.drumbeat.rdf.OwlVocabulary;
//
//
//public class Bem2RdfMetaDatasetExporter extends Ifc2RdfExporterBase {
//	
//	private String metaDataSetUri;
//	private BemSchema ifcSchema;
////	private BemDataset bemDataset;
//	private BemDataset metaModel;
//	
//	private Bem2RdfConversionContext context;
//	private OwlProfileList owlProfileList;
//	
//	public Bem2RdfMetaDatasetExporter(String metaDataSetUri, BemDataset bemDataset, Bem2RdfConversionContext context, Model jenaModel) {
//		super(context, jenaModel);
//		
//		this.metaDataSetUri = metaDataSetUri;
//		this.metaModel = bemDataset.getMetaModel();
//		this.ifcSchema = bemDataset.getSchema();
//		this.context = context;
//		this.owlProfileList = context.getOwlProfileList();
//		
//
//		String modelNamespacePrefix = context.getModelNamespacePrefix();
//		String modelNamespaceUri = String.format(context.getModelNamespaceUriFormat(), ifcSchema.getVersion(), context.getName());
//		
//		//String ontologyNamespaceUri = String.format(context.getOntologyNamespaceUriFormat(), ifcSchema.getVersion(), context.getName());
//		String ontologyNamespaceUri = Bem2RdfVocabulary.IFC.getBaseUri(ifcSchema.getVersion());
//		
//		super.setOntologyNamespaceUri(ontologyNamespaceUri);
//		super.setModelNamespacePrefix(modelNamespacePrefix);
//		super.setModelNamespaceUri(modelNamespaceUri);		
//	}
//	                                                                                                                    
//	public Model export() throws BemException {
//		
//		//
//		// write header and prefixes
//		//
//		//adapter.startExport();		
//		
//		jenaModel.setNsPrefix(OwlVocabulary.OWL.BASE_PREFIX, OWL.getURI());
//		jenaModel.setNsPrefix(OwlVocabulary.RDF.BASE_PREFIX, RDF.getURI());
//		jenaModel.setNsPrefix(OwlVocabulary.RDFS.BASE_PREFIX, RDFS.getURI());
//		jenaModel.setNsPrefix(OwlVocabulary.XSD.BASE_PREFIX, XSD.getURI());	
//		jenaModel.setNsPrefix(OwlVocabulary.VOID.BASE_PREFIX, OwlVocabulary.VOID.BASE_URI);
//		jenaModel.setNsPrefix(OwlVocabulary.DCTERMS.BASE_PREFIX, OwlVocabulary.DCTERMS.BASE_URI);
//		
//		jenaModel.setNsPrefix(Bem2RdfVocabulary.EXPRESS.BASE_PREFIX, Bem2RdfVocabulary.EXPRESS.getBaseUri());		
//		jenaModel.setNsPrefix(Bem2RdfVocabulary.STEP.BASE_PREFIX,Bem2RdfVocabulary.STEP.getBaseUri());
//
//		
//		//adapter.exportEmptyLine();
//		
//		Resource dataSetResource = super.createUriResource(metaDataSetUri);
//
//		jenaModel.setNsPrefix(getModelNamespacePrefix(), getModelNamespaceUri());
//		//adapter.exportEmptyLine();
//
//		jenaModel.add(dataSetResource, RDF.type, OwlVocabulary.VOID.DataSet);
//
//		String conversionParamsString = context.getConversionParams().toString();
////				.replaceFirst("\\[", "[\r\n\t\t\t ")
////				.replaceFirst("\\]", "\r\n\t\t]")
////				.replaceAll(",", "\r\n\t\t\t");		
//		conversionParamsString = String.format("OWL profile: %s.\r\n\t\tConversion options: %s",
//				owlProfileList.getOwlProfileIds(),
//				conversionParamsString); 
//		jenaModel.add(dataSetResource, OwlVocabulary.DCTERMS.description, jenaModel.createTypedLiteral(conversionParamsString));		
//
//		IfcStepFileDescription stepFileDescription = metaModel.getFileDescription();
//		stepFileDescription.getDescriptions().forEach(x ->
//			jenaModel.add(dataSetResource, OwlVocabulary.DCTERMS.description, jenaModel.createTypedLiteral(x))
//		);
//		
//		IfcStepFileName stepFileName = metaModel.getFileName();		
//		jenaModel.add(dataSetResource, OwlVocabulary.DCTERMS.title, jenaModel.createTypedLiteral(stepFileName.getName()));		
//
//		stepFileName.getAuthors().forEach(x ->
//			jenaModel.add(dataSetResource, OwlVocabulary.DCTERMS.creator, jenaModel.createTypedLiteral(x))
//		);
//		
//		stepFileName.getOrganizations().forEach(x ->
//			jenaModel.add(dataSetResource, OwlVocabulary.DCTERMS.publisher, jenaModel.createTypedLiteral(x))
//		);
//		
//		jenaModel.add(dataSetResource, OwlVocabulary.DCTERMS.created, jenaModel.createTypedLiteral(stepFileName.getTimeStamp()));	
//		
//		
//		jenaModel.add(dataSetResource, OwlVocabulary.DCTERMS.hasVersion, jenaModel.createTypedLiteral(stepFileName.getPreprocessorVersion(), XSD.date.toString()));
//		
//		Resource fileDescriptionResource = jenaModel.createResource();
//		Resource fileNameResource = jenaModel.createResource();
//		Resource fileSchemaResource = jenaModel.createResource();
//		
//		jenaModel.add(dataSetResource, Bem2RdfVocabulary.STEP.fileDescription, fileDescriptionResource);
//		jenaModel.add(dataSetResource, Bem2RdfVocabulary.STEP.fileName, fileNameResource);
//		jenaModel.add(dataSetResource, Bem2RdfVocabulary.STEP.fileSchema, fileSchemaResource);
//		
//		stepFileDescription.getDescriptions().forEach(x ->
//			jenaModel.add(fileDescriptionResource, Bem2RdfVocabulary.STEP.FileDescription.description, jenaModel.createTypedLiteral(x))
//		);
//		jenaModel.add(fileDescriptionResource, Bem2RdfVocabulary.STEP.FileDescription.implementation_level, jenaModel.createTypedLiteral(stepFileDescription.getImplementationLevel()));
//		
//		jenaModel.add(fileNameResource, Bem2RdfVocabulary.STEP.FileName.name, jenaModel.createTypedLiteral(stepFileName.getName()));
//		jenaModel.add(fileNameResource, Bem2RdfVocabulary.STEP.FileName.time_stamp, jenaModel.createTypedLiteral(stepFileName.getTimeStamp()));
//		stepFileName.getAuthors().forEach(x ->
//			jenaModel.add(fileNameResource, Bem2RdfVocabulary.STEP.FileName.author, jenaModel.createTypedLiteral(x))
//		);
//
//		stepFileName.getOrganizations().forEach(x ->
//			jenaModel.add(fileNameResource, Bem2RdfVocabulary.STEP.FileName.organization, jenaModel.createTypedLiteral(x))
//		);
//		
//		jenaModel.add(fileNameResource, Bem2RdfVocabulary.STEP.FileName.preprocessor_version, jenaModel.createTypedLiteral(stepFileName.getPreprocessorVersion()));
//		jenaModel.add(fileNameResource, Bem2RdfVocabulary.STEP.FileName.originating_system, jenaModel.createTypedLiteral(stepFileName.getOriginatingSystem()));
//		jenaModel.add(fileNameResource, Bem2RdfVocabulary.STEP.FileName.authorization, jenaModel.createTypedLiteral(stepFileName.getAuthorization()));
//		
//		metaModel.getFileSchema().getSchemas().forEach(x ->
//			jenaModel.add(fileSchemaResource, Bem2RdfVocabulary.STEP.FileSchema.schema_identifiers, jenaModel.createTypedLiteral(x))
//		);
//		
//		//		IfcEntity ownerHistory = bemDataset.getFirstEntityByType(IfcVocabulary.TypeNames.IFC_OWNER_HISTORY);
////		if (ownerHistory != null) {
////			
////		}
//		
//		//adapter.endExport();
//		
//		return jenaModel;
//	}
//	
//}
