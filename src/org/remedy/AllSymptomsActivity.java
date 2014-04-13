package org.remedy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class AllSymptomsActivity extends Fragment {

    private FragmentActivity _activity;
    private Context _context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.symptom_list_layout, container, false);

        ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.expandable_symptom_list);

        return container;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _activity = (FragmentActivity) activity;
        _context = _activity;
    }

}
