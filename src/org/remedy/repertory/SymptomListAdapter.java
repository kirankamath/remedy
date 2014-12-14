package org.remedy.repertory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.remedy.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class SymptomListAdapter extends BaseAdapter implements Filterable {

	private List<String> symptomList;
	private final Set<String> selectedSymptoms;
	private final Context context;
	private final Filter filter;
    private final int LIGHTRED_COLOR;

	public SymptomListAdapter(Context context,
			List<String> symptomList, Set<String> selectedSymptoms) {
		this.symptomList = new ArrayList<String>(symptomList);
		if (selectedSymptoms == null) {
		    this.selectedSymptoms = new HashSet<String>();
		} else {
		    this.selectedSymptoms = new HashSet<String>(selectedSymptoms);
		}

		this.context = context;
		this.filter = new SubstringFilter();
		LIGHTRED_COLOR = context.getResources().getColor(R.color.lightred);
	}

	public void updateData(List<String> list) {
		this.symptomList = list;
	}

	@Override
	public int getCount() {
		return symptomList.size();
	}

	@Override
	public Object getItem(int position) {
		return symptomList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
		}
		TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
		String item = (String) getItem(position);
		textView.setText(item);

		if (selectedSymptoms.contains(item)) {
			convertView.setBackgroundColor(LIGHTRED_COLOR);
		} else {
			convertView.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	public void toggleItemSelection(int position) {
		String item = (String) getItem(position);
		if (selectedSymptoms.contains(item)) {
			selectedSymptoms.remove(item);
		} else {
			selectedSymptoms.add(item);
		}
		notifyDataSetChanged();
	}

	public Set<String> getChosenList() {
		return selectedSymptoms;
	}

	private class SubstringFilter extends Filter {
		private final List<String> originalList;
		private SubstringFilter() {

			// Store a copy of the list so that we can modify the parent's list during filtering.
			originalList = new ArrayList<String>(symptomList);
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			String prefixString = prefix.toString().toUpperCase(Locale.ENGLISH);
			FilterResults results = new FilterResults();

			if (prefix == null || prefix.length() == 0) {
				// Reset the original data.
				results.count = originalList.size();
				results.values = originalList;
			} else {
				List<String> filteredList = new ArrayList<String>();
				for (String s: originalList) {
					if (s.toUpperCase(Locale.ENGLISH).contains(prefixString)) {
						filteredList.add(s);
					}
				}
				results.values = filteredList;
				results.count = filteredList.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			symptomList = (List<String>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}
