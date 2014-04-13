package org.remedy.repertory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.remedy.ExpandableSymptomListAdapter;
import org.remedy.ExpandableSymptomListAdapter.ChildrenGetter;
import org.remedy.R;
import org.remedy.db.RemedyDAO;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

/**
 * Activity to select a bunch of symptoms.
 */
public class SelectSymptomActivity extends ActionBarActivity {

    private ExpandableSymptomListAdapter adapter;
    private ExpandableListView listView;
    private static final String SELECTED_SYMPTOMS = "Selected_Symptoms";

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_list_layout);
        setTitle("Symptoms");

        Map<String, Set<String>> selectedItemMap = null;
        if (savedInstanceState != null) {
            selectedItemMap = (Map<String, Set<String>>) savedInstanceState.getSerializable(SELECTED_SYMPTOMS);
        }

        listView = (ExpandableListView)findViewById(R.id.expandable_symptom_list);
        List<String> categoryList = RemedyDAO.getAllCategories(this);
        Map<String, List<String>> categoryMap = new HashMap<String, List<String>>();

        for (String category : categoryList) {
            categoryMap.put(category, null);
        }

        ChildrenGetter getter = new ChildrenGetter() {
            @Override
            public List<String> getChildren(String parent) {
                return RemedyDAO.getAllSymptomsForCategory(SelectSymptomActivity.this, parent);
            }
        };

        adapter = new ExpandableSymptomListAdapter(this, null, null,
                categoryMap, getter, true, selectedItemMap);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {
                String category = (String) adapter.getGroup(groupPosition);
                String symptom = (String) adapter.getChild(groupPosition, childPosition);
                adapter.toggleSelection(category, symptom);
                return true;
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_SYMPTOMS, (Serializable)adapter.getSelectedSymptoms());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_symptom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_symptom_next: {
                Map<String, Set<String>> selectedItems = adapter.getSelectedSymptoms();
                boolean found = false;
                for (String category : selectedItems.keySet()) {
                    if (selectedItems.get(category).size() > 0) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(this, "Select at least one symptom to repertory", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent(this, FilteredRemedyActivity.class);
                intent.putExtra(FilteredRemedyActivity.SELECTED_SYMPTOMS, (Serializable) selectedItems);
                startActivity(intent);
                return true;
            }
            case R.id.select_symptom_minimize: {
                collapseAllCategories();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void collapseAllCategories() {
        int categoryCount = adapter.getGroupCount();
        for (int i = 0; i < categoryCount; i++) {
            listView.collapseGroup(i);
        }
    }
}