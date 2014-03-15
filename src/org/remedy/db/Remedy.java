package org.remedy.db;

import java.util.List;

public class Remedy {

    private final String name;
    private final String details;
    private final List<String> symptoms;
    private final String dosage;

    public Remedy(String name, String details, List<String> symptoms, String dosage) {
        this.name = name;
        this.details = details;
        this.symptoms = symptoms;
        this.dosage = dosage;
    }

    public String getDetails() {
        return details;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public String getDosage() {
        return dosage;
    }

    public String getName() {
        return name;
    }
}
