package org.remedy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

        Intent intent = getIntent();
        String remedyName = (String)intent.getSerializableExtra(REMEDY_NAME);
        setTitle("Details for " + remedyName);

        remedyDetailsHeader = (TextView) findViewById(R.id.remedy_details_header);
        remedySymptomList = (ListView) findViewById(R.id.remedy_symptom_list);
        remedyDosage = (TextView) findViewById(R.id.remedy_dosage_details);

        RemedyDAO remedyDAO = new RemedyDAO();

        // XXX(kkamath): Make this async.
        Remedy remedy = remedyDAO.getRemedyDetails(this, remedyName);
        remedyDetailsHeader.setText(remedy.getDetails());

        List<String> symptomStrList = new ArrayList<String>();
        Map<String, Set<String>> categoryMap = new HashMap<String, Set<String>>();
        Map<String, Set<String>> symptoms = remedy.getSymptoms();
        for (String category: symptoms.keySet()) {
            Set<String> existingSymptoms = categoryMap.get(category);
            if (existingSymptoms == null) {
                existingSymptoms = new HashSet<String>();
                categoryMap.put(category, existingSymptoms);
            }
            existingSymptoms.addAll(symptoms.get(category));
        }

        Iterator<Entry<String, Set<String>>> iter = categoryMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Set<String>> entry = iter.next();
            symptomStrList.add(entry.getKey());

            for (String desc: entry.getValue()) {
                symptomStrList.add("   " + desc);
            }
        }

        ArrayAdapter<String> remedyDetailsHeader = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, symptomStrList);
        remedySymptomList.setAdapter(remedyDetailsHeader);

        remedyDosage.setText(remedy.getDosage());
    }
}
