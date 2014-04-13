package org.remedy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class AllSymptomsFragment extends Fragment {

    private FragmentActivity _activity;
    private Context _context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.symptom_list_layout, container, false);

        ExpandableListView listView = (ExpandableListView)rootView.findViewById(
                R.id.expandable_symptom_list);

        HashMap<String, List<String>> categoryMap = new HashMap<String, List<String>>();

        categoryMap.put("Test", Arrays.asList("One", "Two"));

        ExpandableSymptomListAdapter adapter = new ExpandableSymptomListAdapter(_activity,
                null, null, categoryMap);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _activity = (FragmentActivity) activity;
        _context = _activity;
    }

}
