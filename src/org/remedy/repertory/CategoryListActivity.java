package org.remedy.repertory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.remedy.R;
import org.remedy.db.RemedyDAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryListActivity extends Activity {

	private ListView categoryListView;
	private Map<String, Set<String>> selectedSymptomMap;
	private CategoryListAdapter categoryListAdapter;

	private static final int SELECT_SYMPTOM_REQUEST = 11;

	private static final String[] categoryOrder = new String[] {
        "Sleep",
        "Head",
        "Mind",
        "Face",
        "Eyes",
        "Ears",
        "Nose",
        "Mouth",
        "Teeth",
        "Tongue",
        "Throat",
        "Respiratory",
        "Chest",
        "Gastric",
        "Heart",
        "Neck and Back",
        "Back",
        "Back and Extremities",
        "Spine",
        "Nervous System",
        "Stomach",
        "Abdomen",
        "Bowels",
        "Urine",
        "Rectum",
        "Stool",
        "Skin",
        "Bones",
        "Extremeties",
        "Fever",
        "Female",
        "Male",
        "Sexual",
        "Sexual organs",
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);
        setTitle("Choose symptoms");

        selectedSymptomMap = new HashMap<String, Set<String>>();
		List<String> categoryListItems = RemedyDAO.getAllCategories(this);
		for (String category: categoryListItems) {
		    selectedSymptomMap.put(category, Collections.<String> emptySet());
		}

		// Maintain an ordered array for display.
		categoryListView = (ListView) findViewById(R.id.category_list);
		categoryListAdapter = new CategoryListAdapter();
		categoryListView.setAdapter(categoryListAdapter);

		categoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(CategoryListActivity.this, SymptomSelectionActivity.class);

				String category = categoryListAdapter.getCategoryAt(position);
				intent.putExtra(SymptomSelectionActivity.CATEGORY_NAME, category);
				Set<String> selectedSymptoms = selectedSymptomMap.get(category);
				intent.putExtra(SymptomSelectionActivity.SELECTED_SYMPTOMS,
				        (Serializable)selectedSymptoms);
                startActivityForResult(intent, SELECT_SYMPTOM_REQUEST);
			}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_list, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.category_list_next: {

                boolean found = false;
                for (String category : selectedSymptomMap.keySet()) {
                    Set<String> symptoms = selectedSymptomMap.get(category);
                    if (symptoms.size() > 0) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(this, "Select at least one symptom to repertory", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent(this, FilteredRemedyActivity.class);
                intent.putExtra(FilteredRemedyActivity.SELECTED_SYMPTOMS, (Serializable) selectedSymptomMap);
                startActivity(intent);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
	    if (requestCode == SELECT_SYMPTOM_REQUEST) {
            if (resultCode == RESULT_OK) {
                Set<String> selectedSymptoms = (Set<String>) result.getSerializableExtra(
                        SymptomSelectionActivity.SELECTED_SYMPTOMS);
                String categoryName = result.getStringExtra(
                        SymptomSelectionActivity.CATEGORY_NAME);
                selectedSymptomMap.put(categoryName, selectedSymptoms);
                categoryListAdapter.dataChanged();
            }
        }
	}

	public class CategoryListAdapter extends BaseAdapter {

	    private final List<String> sortedCategoryList;
	    private List<String> displayNames;

	    private CategoryListAdapter() {

	        // Maintain an ordered array for display.
	        sortedCategoryList = new ArrayList<String>();
	        Set<String> list = new HashSet<String>(selectedSymptomMap.keySet());

	        // Walk thru the ordered list. If found, add it.
	        for (String category: categoryOrder) {
	            if (list.contains(category)) {
	                // Remove it from the set so that we can add the rest at the end.
	                list.remove(category);
	                sortedCategoryList.add(category);
	            }
	        }

	        // Add any remaining entries.
	        sortedCategoryList.addAll(list);
	        updateDisplayNames();
	    }

	    private void updateDisplayNames() {
	        // For each entry, append the count at the end.
            displayNames = new ArrayList<String>();
            for (String category: sortedCategoryList) {
                Set<String> selectedSymptoms = selectedSymptomMap.get(category);
                if (selectedSymptoms.size() > 0) {
                    category += " (" + selectedSymptoms.size() + ")";

                }
                displayNames.add(category);
            }
	    }

	    @Override
	    public int getCount() {
	        return displayNames.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return displayNames.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    /**
	     * Get the original value of the category at a position.
	     * @param position Position for which the category is being requested.
	     * @return Category name.
	     */
	    public String getCategoryAt(int position) {
	        return sortedCategoryList.get(position);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
	        }
	        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
	        String item = (String) getItem(position);
	        textView.setText(item);
	        return convertView;
	    }

	    public void dataChanged() {
	        updateDisplayNames();
	        notifyDataSetChanged();
	    }
	}
}
