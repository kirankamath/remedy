package org.remedy.repertory;

import java.util.HashMap;
import java.util.List;

import org.remedy.ExpandableSymptomListAdapter;
import org.remedy.ExpandableSymptomListAdapter.ChildrenGetter;
import org.remedy.R;
import org.remedy.db.RemedyDAO;

import android.os.Build;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_list_layout);
        setTitle("Select symptoms");

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ExpandableListView listView = (ExpandableListView)findViewById(
                R.id.expandable_symptom_list);

        RemedyDAO dao = new RemedyDAO();
        List<String> categoryList = dao.getAllCategories(this);
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

        final ExpandableSymptomListAdapter adapter = new ExpandableSymptomListAdapter(this,
                null, null, categoryMap, getter, true);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {
                String category = (String)adapter.getGroup(groupPosition);
                String symptom = (String)adapter.getChild(groupPosition, childPosition);

                if (adapter.isItemSelected(groupPosition, childPosition)) {

                    // Already selected. Unselect it.
                    symptomRemoved(category, symptom);
                } else {
                    symptomAdded(category, symptom);
                }

                adapter.toggleSelection(groupPosition, childPosition);
                return true;
            }
        });

    }

    public void symptomAdded(String category, String symptom) {
    }

    public void symptomRemoved(String category, String symptom) {
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
                Toast.makeText(this, "Going next", Toast.LENGTH_SHORT).show();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}