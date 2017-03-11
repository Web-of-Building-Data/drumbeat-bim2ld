package fi.aalto.cs.drumbeat.convert.bem2rdf;

import java.io.File;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.*;

import fi.aalto.cs.drumbeat.rdf.OwlProfile;
import fi.aalto.cs.drumbeat.rdf.OwlProfileList;

public class Bem2RdfOwlValidator {
	
	private static Logger logger = Logger.getLogger(Bem2RdfOwlValidator.class);

	public Bem2RdfOwlValidator() {
	}

	public static void validateOwl(String ontologyFilePath, OWLProfile... owlProfiles) throws Exception {
		
		if (owlProfiles == null) {
			return;
		}
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
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
		
		IRI ontologyIri = IRI.create(new File(ontologyFilePath));

		OWLOntology ifcOntology = manager.loadOntologyFromOntologyDocument(ontologyIri);
		// ChangeApplied changeApplied = manager.applyChange(new
		// AddImport(ifcOntology, importDeclaraton));
		// System.out.println(changeApplied);

		//
		// OWLImportsDeclaration importDeclaraton =
		// factory.get
		
		for (OWLProfile owlProfile : owlProfiles) {		
			
			OWLProfileReport report = owlProfile.checkOntology(ifcOntology);
			
			int numberOfViolations = report.getViolations().size();
			
			@SuppressWarnings("resource")
			PrintStream out = numberOfViolations > 0 ? System.err : System.out;
			
			String message = String.format("Violations against %s: %d", owlProfile.getName(), numberOfViolations);
			
			if (numberOfViolations > 0) {
				logger.debug(message);
			} else {
				logger.warn(message);
			}
	
			out.println(message);
			report.getViolations().stream()
					// .forEach(v -> System.out.printf("%s%n\tProblem axiom:
					// %s%n\tSolution: %s%n", v.getExpression(), v.getAxiom(),
					// v.repair()));
					.forEach(v -> {
						
						String message2 = String.format("%s%n\tSolution: %s", v, v.repair());
						logger.warn(message2);
						out.println(message2);
						
					});
		}
	}
	
	public static void validateOwl(String ontologyFilePath, OwlProfileList owlProfiles) throws Exception {
		validateOwl(ontologyFilePath, getOwlProfiles(owlProfiles));
	}
	
	
	public static OWLProfile getOwlProfile(OwlProfile owlProfile) {
		switch (owlProfile.getOwlProfileId()) {
		case OWL2_EL:
			return new OWL2ELProfile();
		case OWL2_QL:
			return new OWL2QLProfile();
		case OWL2_RL:
			return new OWL2RLProfile();
		case OWL2_DL:
			return new OWL2DLProfile();
		case OWL2_Full:
			return new OWL2Profile();
		default:
			throw new IllegalArgumentException("Invalid OWL profile: " + owlProfile.getOwlProfileId());
		}
	}
	
	public static OWLProfile[] getOwlProfiles(OwlProfileList owlProfiles) {
		OWLProfile[] result = new OWLProfile[owlProfiles.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = getOwlProfile(owlProfiles.get(i));
		}
		return result;
	}
	

}
