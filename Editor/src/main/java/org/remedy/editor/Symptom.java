package org.remedy.editor;

class Symptom {
	private final String category;
	private final String description;

	public Symptom(String category, String description) {
		this.category = category;
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}
}