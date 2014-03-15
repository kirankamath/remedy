package org.remedy.db;

import java.util.ArrayList;
import java.util.List;

public class RemedyDAO {

    public Remedy getRemedyDetails(String remedyName) {
        List<String> symptoms = new ArrayList<String>();
        symptoms.add("First symptom");
        Remedy remedy = new Remedy(remedyName, "Details...", symptoms, "dosage ...");
        return remedy;
    }
}
