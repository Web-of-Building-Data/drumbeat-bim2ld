package fi.aalto.cs.drumbeat.owl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class OwlProfileList extends ArrayList<OwlProfile> implements OwlProfileInfo {
	
	private static final long serialVersionUID = 1L;
	
	public OwlProfileList() {		
	}

	public OwlProfileList(String... owlProfileNames) {
		for (String owlProfileName : owlProfileNames) {
			OwlProfileEnum owlProfileId = OwlProfileEnum.valueOf(owlProfileName.trim());
			add(new OwlProfile(owlProfileId));
		}		
	}
	
	public OwlProfileList(OwlProfileEnum... owlProfileIds) {
		for (OwlProfileEnum owlProfileId : owlProfileIds) {
			add(new OwlProfile(owlProfileId));
		}		
	}

	public OwlProfileList(OwlProfile... owlProfiles) {
		for (OwlProfile owlProfile : owlProfiles) {
			add(owlProfile);
		}		
	}

	public List<OwlProfileEnum> getOwlProfileIds() {
		return this.stream().map(OwlProfile::getOwlProfileId).collect(Collectors.toList());
	}
	
	@Override
	public boolean supportsStatement(Property property, RDFNode object) {
		for (OwlProfile owlProfile : this) {
			if (!owlProfile.supportsStatement(property, object)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean supportsAnonymousIndividual() {
		for (OwlProfile owlProfile : this) {
			if (!owlProfile.supportsAnonymousIndividual()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean supportsDataType(Resource type) {
		for (OwlProfile owlProfile : this) {
			if (!owlProfile.supportsDataType(type)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<Resource> getSupportedDataTypes() {
		Set<Resource> supportedDataTypes = new HashSet<>(this.get(0).getSupportedDataTypes());
		for (int i = 1; i < this.size(); ++i) {
			supportedDataTypes.retainAll(get(i).getSupportedDataTypes());
		}
		return supportedDataTypes;
	}	
	
	
	public Resource getFirstSupportedDatatype(Collection<Resource> datatypes) {
		
		for (Resource type : datatypes) {
			boolean isSupported = true;
			for (OwlProfile profile : this) {
				if (!profile.supportsDataType(type)) {
					isSupported = false;
				}
			}
			if (isSupported) {
				return type;
			}
		}
		
		return null;		
	}

}
