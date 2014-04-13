package org.remedy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private static final String DETAILS = "Details";
    private static final String DOSAGE = "Dosage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_list_layout);

        Intent intent = getIntent();
        String remedyName = (String)intent.getSerializableExtra(REMEDY_NAME);

        // Set a nicer title up.
        setTitle("Details for " + remedyName);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandable_symptom_list);
        RemedyDAO remedyDAO = new RemedyDAO();

        // XXX(kkamath): Make this async.
        Remedy remedy = remedyDAO.getRemedyDetails(this, remedyName);

        Map<String, Set<String>> inputMap = remedy.getSymptoms();
        HashMap<String, List<String>> categoryMap = new HashMap<String, List<String>>();

        // Convert the value from Set to a List.
        Set<String> categorySet = inputMap.keySet();
        for (String category: categorySet) {
            List<String> symptomList = new ArrayList<String>(inputMap.get(category));
            categoryMap.put(category, symptomList);
        }
        List<String> detailsList = new ArrayList<String>();
        detailsList.add(remedy.getDetails());
        categoryMap.put(DETAILS, detailsList);

        List<String> dosageList = new ArrayList<String>();
        dosageList.add(remedy.getDosage());
        categoryMap.put(DOSAGE, dosageList);

        RemedyDetailsListAdapter adapter = new RemedyDetailsListAdapter(this,
                new HashSet<String>(Arrays.asList(DETAILS)),
                new HashSet<String>(Arrays.asList(DOSAGE)),
                categoryMap);
        listView.setAdapter(adapter);

        if (adapter.getGroupCount() > 0) {
            listView.expandGroup(0);
        }
    }
}
