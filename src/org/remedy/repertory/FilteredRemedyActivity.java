package org.remedy.repertory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.remedy.OneRemedyActivity;
import org.remedy.R;
import org.remedy.db.RemedyDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FilteredRemedyActivity extends Activity {

    public static final String SELECTED_SYMPTOMS = "Selected_Symptoms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_remedy);
        setTitle("Suggested remedies");

        Intent intent = getIntent();
        @SuppressWarnings("unchecked")
        final Map<String, Set<String>> categoryMap = (Map<String, Set<String>>)
                intent.getSerializableExtra(SELECTED_SYMPTOMS);
        final Map<String, Integer> scoreMap = new HashMap<String, Integer>();
        RemedyDAO dao = new RemedyDAO(this);
        for (String category : categoryMap.keySet()) {
            Set<String> symptoms = categoryMap.get(category);
            Iterator<String> iter = symptoms.iterator();
            while (iter.hasNext()) {
                List<String> remedies = dao.getRemediesForSymptom(category, iter.next());

                /*
                 * If there is only one remedy, give it ten points.
                 * If there is more than one remedy for a symptom, symptom is probably generic.
                 * So award one point each for the remedies.
                 */
                if (remedies.size() > 1) {
                    for (String remedy : remedies) {
                        awardPoints(scoreMap, remedy, 1);
                    }
                } else if (remedies.size() == 1) {
                    awardPoints(scoreMap, remedies.get(0), 10);
                }
            }

        }
        dao.close();

        List<String> finalRemedies = new ArrayList<String>(scoreMap.keySet());
        Collections.sort(finalRemedies, new Comparator<String>() {

            @Override
            public int compare(String lhs, String rhs) {
                return scoreMap.get(rhs) - scoreMap.get(lhs);
            }
        });

        ListView list = (ListView) findViewById(R.id.filtered_remedy_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, finalRemedies);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FilteredRemedyActivity.this, OneRemedyActivity.class);
                intent.putExtra(OneRemedyActivity.REMEDY_NAME, adapter.getItem(position));
                intent.putExtra(OneRemedyActivity.SELECTED_SYMPTOMS, (Serializable)categoryMap);
                startActivity(intent);
            }
        });
    }

    private void awardPoints(Map<String, Integer> scoreMap, String remedy, int points) {
        if (scoreMap.containsKey(remedy)) {
            int currentScore = scoreMap.get(remedy);
            scoreMap.put(remedy, currentScore + points);
        } else {
            scoreMap.put(remedy, points);
        }
    }
}
