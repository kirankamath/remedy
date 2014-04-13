package org.remedy.repertory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

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

/**
 * Activity to select a bunch of symptoms.
 */
public class SelectSymptomActivity extends ActionBarActivity {

    private ExpandableSymptomListAdapter adapter;
    private ExpandableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_list_layout);
        setTitle("Symptoms");

        listView = (ExpandableListView)findViewById(R.id.expandable_symptom_list);
        List<String> categoryList = RemedyDAO.getAllCategories(this);
        HashMap<String, List<String>> categoryMap = new HashMap<String, List<String>>();

        for (String category : categoryList) {
            categoryMap.put(category, null);
        }

        ChildrenGetter getter = new ChildrenGetter() {
            @Override
            public List<String> getChildren(String parent) {
                return RemedyDAO.getAllSymptomsForCategory(SelectSymptomActivity.this, parent);
            }
        };

        adapter = new ExpandableSymptomListAdapter(this,
                null, null, categoryMap, getter, true);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_symptom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_symptom_next: {
                Intent intent = new Intent(this, FilteredRemedyActivity.class);
                intent.putExtra(FilteredRemedyActivity.SELECTED_SYMPTOMS,
                        (Serializable) adapter.getSelectedSymptoms());
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