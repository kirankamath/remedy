package org.remedy;


import org.remedy.db.Remedy;
import org.remedy.db.RemedyDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RemedyDetails extends Activity {

    public static final String REMEDY_NAME = "RemedyName";

    private ListView remedySymptomList;
    private TextView remedyDetailsHeader;
    private TextView remedyDosage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedy_details);

        remedyDetailsHeader = (TextView) findViewById(R.id.remedy_details_header);
        remedySymptomList = (ListView) findViewById(R.id.remedy_symptom_list);
        remedyDosage = (TextView) findViewById(R.id.remedy_dosage_details);

        Intent intent = getIntent();
        String remedyName = (String)intent.getSerializableExtra(REMEDY_NAME);

        RemedyDAO remedyDAO = new RemedyDAO();
        Remedy remedy = remedyDAO.getRemedyDetails(remedyName);
        remedyDetailsHeader.setText(remedy.getDetails());

        ArrayAdapter<String> remedyDetailsHeader = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, remedy.getSymptoms());
        remedySymptomList.setAdapter(remedyDetailsHeader);

        remedyDosage.setText(remedy.getDosage());
    }
}
