package org.remedy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class Remedy {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((symptoms == null) ? 0 : symptoms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Remedy other = (Remedy) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (symptoms == null) {
			if (other.symptoms != null) {
				return false;
			}
		} else if (!symptoms.equals(other.symptoms)) {
			return false;
		}
		return true;
	}

	private String name;
	private String details;
	private String dosage;
	private final Map<String, Set<String>> symptoms;

	public Remedy() {
	    symptoms = new HashMap<String, Set<String>>();
	}

	public Remedy(String name) {
		this.name = name;
		symptoms = new HashMap<String, Set<String>>();
	}

	public Map<String, Set<String>> getSymptoms() {
		return symptoms;
	}

	public void addSymptom(String category, String symptom) {
	    Set<String> currentSymptoms = symptoms.get(category);
		if (currentSymptoms == null) {
		    currentSymptoms = new HashSet<String>();
		    symptoms.put(category, currentSymptoms);
		}
		currentSymptoms.add(symptom);
	}

	public String getName() {
		return name;
	}

	@Override
    public String toString() {
		return name;
	}

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
