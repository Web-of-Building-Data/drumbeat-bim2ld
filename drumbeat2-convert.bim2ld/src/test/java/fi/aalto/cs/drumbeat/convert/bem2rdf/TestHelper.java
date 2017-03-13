package fi.aalto.cs.drumbeat.convert.bem2rdf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
//import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

import fi.aalto.cs.drumbeat.common.file.FileManager;
import fi.aalto.cs.drumbeat.data.bem.BemException;
import fi.aalto.cs.drumbeat.data.bem.dataset.BemDataset;
import fi.aalto.cs.drumbeat.data.bem.parsers.util.BemParserUtil;
import fi.aalto.cs.drumbeat.data.ifc.parsers.IfcDatasetParser;
import fi.aalto.cs.drumbeat.data.step.dataset.StepDataset;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.owlapi.OwlApiUtils;

public class TestHelper {
	
//	private static Logger logger = Logger.getLogger(TestHelper.class); 
	
	public static Model readJenaModel(String filePath) throws IOException {
		FileInputStream in = new FileInputStream(filePath);
		Model model = ModelFactory.createDefaultModel();
		try {
			RDFDataMgr.read(model, in, Lang.TURTLE);
		} finally {
			in.close();
		}
		return model;
	}
	
	public static StringWriter writeJenaModel(Model model, String filePath, StringWriter stringWriter) throws IOException {
		
		if (stringWriter != null) {
			try {
				RDFDataMgr.write(stringWriter, model, RDFFormat.TURTLE_PRETTY);
			} finally {
				stringWriter.close();
			}
			
			FileWriter fileWriter = FileManager.createFileWriter(filePath);
			
			try {
				fileWriter.write(stringWriter.toString());
			} finally {
				fileWriter.close();
			}
			
			return stringWriter;
			
		} else {

			FileOutputStream out = FileManager.createFileOutputStream(filePath);
			try {
				RDFDataMgr.write(out, model, RDFFormat.TURTLE_PRETTY);
			} finally {
				out.close();
			}
			
			return null;
			
		}
		
		
	}
	
	
	public static void validateOwl(StringBuffer inputBuffer, OwlProfileEnum owlProfileId, List<Class<?>> ignorredViolationClasses, boolean throwExceptionIfViolated) throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer.toString().getBytes());
		validateOwl(inputStream, owlProfileId, ignorredViolationClasses, throwExceptionIfViolated);
	}
	

	public static void validateOwl(InputStream inputStream, OwlProfileEnum owlProfileId, List<Class<?>> ignorredViolationClasses, boolean throwExceptionIfViolated) throws Exception {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputStream);
		OWLProfileReport report = OwlApiUtils.checkOntology(ontology, owlProfileId, ignorredViolationClasses);
		
		int numberOfViolations = report.getViolations().size(); 
		
		String message = String.format("Violations against %s: %d%n%s", owlProfileId, numberOfViolations, report.toString());

		if (numberOfViolations > 0) {
			System.err.println(message);
//			logger.warn(message);
			if (throwExceptionIfViolated) {
				throw new OwlProfileViolationException(message);
			}
		} else {
			System.out.println(message);
//			logger.debug(message);
		}		
		
		
//		for (OWLProfile owlProfile : owlProfiles) {		
//			
//			OWLProfileReport report = owlProfile.checkOntology(ontology);
//			
//			StringBuilder sb = new StringBuilder();
//			
//			int numberOfViolations = 0;
//			for (OWLProfileViolation violation : report.getViolations()) {
//				if (ignorredViolationClasses != null) {
//					boolean ignored = false;
//					
//					for (Class<?> ignorredViolationClass : ignorredViolationClasses) {
//						if (ignorredViolationClass.isAssignableFrom(violation.getClass())) {
//							ignored = true;
//							break;
//						}
//					}
//					
//					if (!ignored) {					
//						++numberOfViolations;
//						sb.append(String.format("%s: %s%n\tSolution: %s%n", violation.getClass().getSimpleName(), violation, violation.repair()));
//					}
//				}
//			}
//			
//			String message = String.format("Violations against %s: %d%n%s", owlProfile.getName(), numberOfViolations, sb);
//			
//			if (numberOfViolations > 0) {
//				System.err.println(message);
//				logger.warn(message);				
//			} else {
//				System.out.println(message);
//				logger.debug(message);
//			}			
//		}		
		
		
//		OWLDataFactory factory = manager.getOWLDataFactory();

		// OWLOntology expressOntology =
		// manager.loadOntologyFromOntologyDocument(expressOntologyFile);
		// OWLImportsDeclaration importDeclaraton =
		// factory.getOWLImportsDeclaration(IRI.create(EXPRESS_IRI));

		// IRI expressOntologyIRI =
		// IRI.create("file:///C:/DRUM/!github/drumbeat/Ifc2Rdf/software/drumbeat-ifc2ld-1.0.0/drumbeat-ifc.convert.ifc2ld.cli/output/EXPRESS.ttl");
		// //IRI.create(expressOntologyFile.toURI().toURL().toExternalForm());
		// System.out.println(expressOntologyIRI);

		// manager.getIRIMappers().add(new
		// SimpleIRIMapper(IRI.create(EXPRESS_IRI), expressOntologyIRI));
		
//		IRI ontologyIri = IRI.create(new File(ontologyFilePath));

//		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyIri);
		
		// ChangeApplied changeApplied = manager.applyChange(new
		// AddImport(ifcOntology, importDeclaraton));
		// System.out.println(changeApplied);

		//
		// OWLImportsDeclaration importDeclaraton =
		// factory.get

	}
	
	public static BemDataset loadDataset(String filePath) throws IOException, BemException 
	{
		BemParserUtil.registerDatasetParser(new IfcDatasetParser());		
		BemDataset dataset = BemParserUtil.parseDataset(filePath, true);
		assertNotNull(dataset);
		return dataset;
	}


	
	

}
