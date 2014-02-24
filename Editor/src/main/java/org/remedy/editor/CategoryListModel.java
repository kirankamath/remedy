package org.remedy.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractListModel;

public class CategoryListModel extends AbstractListModel<String> {

	private static final long serialVersionUID = 2587502852879516447L;

	private final Map<String, Set<String>> categoryMap;

	public CategoryListModel(Map<String, Set<String>> categoryMap) {
		this.categoryMap = categoryMap;
	}

	@Override
	public String getElementAt(int index) {
		Set<String> keys = categoryMap.keySet();
		List<String> sortedKeys = new ArrayList<>(keys);
		Collections.sort(sortedKeys);
		return sortedKeys.get(index);
	}

	@Override
	public int getSize() {
		return categoryMap.keySet().size();
	}

	public String get(int index) {
		return getElementAt(index);
	}

	public Iterable<String> getSymptoms(int index) {
		String key = get(index);
		return categoryMap.get(key);
	}

	public void addCategory(String category) {
		Set<String> symptoms = categoryMap.get(category);
		if (symptoms == null) {
			symptoms = new HashSet<>();
			categoryMap.put(category, symptoms);
		}
		fireContentsChanged(this, 0, getSize());
	}

	public void removeCategory(String category) {
		categoryMap.remove(category);
		fireContentsChanged(this, 0, getSize());
	}
}
