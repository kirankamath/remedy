package org.remedy.editor;

import java.util.ArrayList;
import java.util.List;

public class Remedy {
	private final String name;
	private final List<Symptom> symptoms;

	public Remedy(String name) {
		this.name = name;
		this.symptoms = new ArrayList<>();
	}

	public List<Symptom> getSymptoms() {
		return symptoms;
	}

	public void addSymptom(Symptom symptom) {
		symptoms.add(symptom);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
