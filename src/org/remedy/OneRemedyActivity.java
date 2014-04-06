package org.remedy;

import org.remedy.db.RemedyDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

/**
 * Activity for showing details of one remedy.
 */
public class OneRemedyActivity extends Activity {

    // Shared key used by the caller of this activity.
    public static final String REMEDY_NAME = "RemedyName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedy_details);

        Intent intent = getIntent();
        String remedyName = (String)intent.getSerializableExtra(REMEDY_NAME);

        // Set a nicer title up.
        setTitle("Details for " + remedyName);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.remedy_symptom_list);
        RemedyDAO remedyDAO = new RemedyDAO();

        // XXX(kkamath): Make this async.
        Remedy remedy = remedyDAO.getRemedyDetails(this, remedyName);
        RemedyDetailsListAdapter adapter = new RemedyDetailsListAdapter(this, remedy);
        listView.setAdapter(adapter);

        if (adapter.getGroupCount() > 0) {
            listView.expandGroup(0);
        }
    }
}
