package org.remedy;

import java.util.ArrayList;
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
    public static final String SELECTED_SYMPTOMS = "Selected_Symptoms";

    private static final String DETAILS = "Details";
    private static final String DOSAGE = "Dosage";
    private static final String COMMON_NAMES = "Common names";
    private static final String RELATIONSHIP = "Relationship";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_list_layout);

        Intent intent = getIntent();
        String remedyName = (String)intent.getSerializableExtra(REMEDY_NAME);

        @SuppressWarnings("unchecked")
        Map<String, Set<String>> selectedItemMap = (Map<String, Set<String>>)
            intent.getSerializableExtra(SELECTED_SYMPTOMS);

        // Set a nicer title up.
        setTitle("Details for " + remedyName);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandable_symptom_list);

        // XXX(kkamath): Make this async.
        Remedy remedy = RemedyDAO.getRemedyDetails(this, remedyName);

        Map<String, Set<String>> inputMap = remedy.getSymptoms();
        HashMap<String, List<String>> categoryMap = new HashMap<String, List<String>>();

        // Convert the value from Set to a List.
        Set<String> categorySet = inputMap.keySet();
        for (String category: categorySet) {
            List<String> symptomList = new ArrayList<String>(inputMap.get(category));
            categoryMap.put(category, symptomList);
        }

        Set<String> preList = new HashSet<String>();
        Set<String> postList = new HashSet<String>();
        List<String> detailsList = new ArrayList<String>();
        detailsList.add(remedy.getDetails());
        categoryMap.put(DETAILS, detailsList);
        preList.add(DETAILS);

        String relationship = remedy.getRelationship();
        if (relationship != null && relationship.length() > 0) {
            List<String> relationshipList = new ArrayList<String>();
            relationshipList.add(relationship);
            categoryMap.put(RELATIONSHIP, relationshipList);
            postList.add(RELATIONSHIP);
        }

        String dosage = remedy.getDosage();
        if (dosage != null && dosage.length() > 0) {
            List<String> dosageList = new ArrayList<String>();
            dosageList.add(dosage);
            categoryMap.put(DOSAGE, dosageList);
            postList.add(DOSAGE);
        }

        String commonNames = remedy.getCommonNames();
        if (commonNames != null && commonNames.length() > 0) {
            List<String> commonNamesList = new ArrayList<String>();
            commonNamesList.add(commonNames);
            categoryMap.put(COMMON_NAMES, commonNamesList);
            preList.add(COMMON_NAMES);
        }

        ExpandableSymptomListAdapter adapter = new ExpandableSymptomListAdapter(this,
                preList,
                postList,
                categoryMap, null, false, selectedItemMap);
        listView.setAdapter(adapter);
    }
}
