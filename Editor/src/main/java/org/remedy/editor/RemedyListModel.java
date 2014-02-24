package org.remedy.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractListModel;

public class RemedyListModel extends AbstractListModel<String> {

	private static final long serialVersionUID = 2587502852879516447L;

	private final Map<String, Remedy> remedyMap;

	public RemedyListModel(Map<String, Remedy> remedyMap) {
		this.remedyMap = remedyMap;
	}

	@Override
	public String getElementAt(int index) {
		Set<String> keys = remedyMap.keySet();
		List<String> sortedKeys = new ArrayList<>(keys);
		Collections.sort(sortedKeys);
		return sortedKeys.get(index);
	}

	@Override
	public int getSize() {
		return remedyMap.keySet().size();
	}

	public String get(int index) {
		return getElementAt(index);
	}

	public void update() {
		fireContentsChanged(this, 0, getSize());
	}
}
