package fi.aalto.cs.drumbeat.owl.owlapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.profiles.*;

import fi.aalto.cs.drumbeat.owl.OwlProfile;
import fi.aalto.cs.drumbeat.owl.OwlProfileEnum;
import fi.aalto.cs.drumbeat.owl.OwlProfileList;

public class OwlApiUtils {

	public OwlApiUtils() {
	}
	
	public static OWLProfile toOwlApiProfile(OwlProfileEnum owlProfileId) {
		if (!OwlProfileEnum.OWL2.contains(owlProfileId)) {
			throw new IllegalArgumentException("Only OWL 2 profiles are supported");
		}
		
		switch (owlProfileId) {
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
			throw new IllegalArgumentException("Unknown OWL profile: " + owlProfileId);
		}		
	}
	
	public static OWLProfile toOwlApiProfile(OwlProfile owlProfile) {
		return toOwlApiProfile(owlProfile.getOwlProfileId());
	}
	
	public static List<OWLProfile> toOWLProfiles(OwlProfileList owlProfiles) {
		List<OWLProfile> resultList = new ArrayList<>(owlProfiles.size());
		for (OwlProfile owlProfile : owlProfiles) {
			resultList.add(toOwlApiProfile(owlProfile));
		}
		return resultList;
	}	

	public static OWLProfileReport filterOwlReport(OWLProfileReport report, List<Class<?>> ignorredViolationClasses) {
		
		if (ignorredViolationClasses != null) {
			Iterator<OWLProfileViolation> it = report.getViolations().iterator();
			while (it.hasNext()) {
				OWLProfileViolation violation = it.next();
				for (Class<?> ignorredViolationClass : ignorredViolationClasses) {
					if (ignorredViolationClass.isAssignableFrom(violation.getClass())) {
						it.remove();
						break;
					}
				}
			}
		}
		
		return report;
		
	}
	
	public static OWLProfileReport checkOntology(OWLOntology ontology, OwlProfileEnum owlProfileId, List<Class<?>> ignorredViolationClasses) {
		OWLProfile owlApiProfile = toOwlApiProfile(owlProfileId); 
		
		OWLProfileReport report = owlApiProfile.checkOntology(ontology);
		return filterOwlReport(report, ignorredViolationClasses);
	}

	public static OWLProfileReport checkOntology(OWLOntology ontology, OwlProfile owlProfile, List<Class<?>> ignorredViolationClasses) {
		OWLProfile owlApiProfile = toOwlApiProfile(owlProfile); 
		
		OWLProfileReport report = owlApiProfile.checkOntology(ontology);
		return filterOwlReport(report, ignorredViolationClasses);
	}
	
	public static Map<OwlProfile, OWLProfileReport> checkOntology(OWLOntology ontology, List<OwlProfile> owlProfileList, List<Class<?>> ignorredViolationClasses) {
		Map<OwlProfile, OWLProfileReport> reports = new TreeMap<>();
		for (OwlProfile owlProfile : owlProfileList) {
			OWLProfileReport report = checkOntology(ontology, owlProfile, ignorredViolationClasses);
			reports.put(owlProfile, report);
		}
		return reports;
	}
	

}
