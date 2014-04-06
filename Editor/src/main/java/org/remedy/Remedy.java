package org.remedy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * JSON serializable data object which holds information about a Remedy.
 */
public class Remedy {

    // Name of the remedy.
	private String name;

	// High level details of the remedy.
	private String details;

	// Recommended dosage for the remedy.
	private String dosage;

	/*
	 * List of symptoms that can be treated by this remedy.
	 *
	 * This list is organized by categories.
	 * Key in this map is the name of the category. E.g: Head/Chest.
	 * Value is a list of symptoms within that category.
	 */
	private final Map<String, Set<String>> symptoms;

	/**
	 * Constructor for the remedy object.
	 * This no argument constructor is requried when object is deserialized from JSON.
	 */
	public Remedy() {
	    symptoms = new HashMap<String, Set<String>>();
	}

	/**
	 * Constructor for the remedy object.
	 *
	 * @param name Name of the remedy.
	 */
	public Remedy(String name) {
		this.name = name;
		symptoms = new HashMap<String, Set<String>>();
	}

	/**
	 * Get the symptoms that can be treated by this remedy.
	 *
	 * @return Map of category to set of symptoms.
	 */
	public Map<String, Set<String>> getSymptoms() {
		return symptoms;
	}

	/**
	 * Add a symptom into a category.
	 *
	 * This is used by the remedy editor.
	 * @param category Category into which the symptom should be added.
	 * @param symptom Symptom which needs to be added.
	 */
	public void addSymptom(String category, String symptom) {
	    Set<String> currentSymptoms = symptoms.get(category);
		if (currentSymptoms == null) {
		    currentSymptoms = new HashSet<String>();
		    symptoms.put(category, currentSymptoms);
		}
		currentSymptoms.add(symptom);
	}

	/**
	 * Get name of the remedy.
	 *
	 * @return Name of the remedy.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get string representation of the remedy.
	 */
	@Override
    public String toString() {
		return name;
	}

	/**
	 * Get the recommended dosage for the remedy.
	 *
	 * @return Recommended dosage.
	 */
    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    /**
     * Get high level details of the remedy.
     *
     * @return High level details.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Update the high level details of the remedy.
     * Used by the remedy editor.
     *
     * @param details High level details to set.
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Method for generating a unique hashcode for this object.
     * Required since this object is placed in HashMap in some cases.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((symptoms == null) ? 0 : symptoms.hashCode());
        return result;
    }

    /**
     * Method for comparing self with another object.
     * Required since this object is placed in HashMap in some cases.
     */
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
}
