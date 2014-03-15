package org.remedy;

import java.util.HashSet;
import java.util.Set;

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

	private final String name;
	private String details;
	private String dosage;
	private final Set<Symptom> symptoms;

	public Remedy(String name) {
		this.name = name;
		this.symptoms = new HashSet<Symptom>();
	}

	public Iterable<Symptom> getSymptoms() {
		return symptoms;
	}

	public void addSymptom(Symptom symptom) {
		symptoms.add(symptom);
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
