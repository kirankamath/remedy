package org.remedy;

import org.remedy.db.RemedyDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class RemedyDetails extends Activity {

    public static final String REMEDY_NAME = "RemedyName";

    private ExpandableListView remedySymptomList;
    private TextView remedyDetailsHeader;
    private TextView remedyDosage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedy_details);

        Intent intent = getIntent();
        String remedyName = (String)intent.getSerializableExtra(REMEDY_NAME);
        setTitle("Details for " + remedyName);

        remedyDetailsHeader = (TextView) findViewById(R.id.remedy_details_header);
        remedySymptomList = (ExpandableListView) findViewById(R.id.remedy_symptom_list);
        remedyDosage = (TextView) findViewById(R.id.remedy_dosage_details);

        RemedyDAO remedyDAO = new RemedyDAO();

        // XXX(kkamath): Make this async.
        Remedy remedy = remedyDAO.getRemedyDetails(this, remedyName);
        remedyDetailsHeader.setText(remedy.getDetails());

        SymptomListAdapter adapter = new SymptomListAdapter(this, remedy.getSymptoms());
        remedySymptomList.setAdapter(adapter);

        remedyDosage.setText(remedy.getDosage());
    }
}
