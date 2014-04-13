package org.remedy;

import java.util.HashMap;
import java.util.List;

import org.remedy.ExpandableSymptomListAdapter.ChildrenGetter;
import org.remedy.db.RemedyDAO;

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

        RemedyDAO dao = new RemedyDAO();
        List<String> categoryList = dao.getAllCategories(_context);
        HashMap<String, List<String>> categoryMap = new HashMap<String, List<String>>();

        for (String category : categoryList) {
            categoryMap.put(category, null);
        }

        ChildrenGetter getter = new ChildrenGetter() {
            @Override
            public List<String> getChildren(String parent) {
                return RemedyDAO.getAllSymptomsForCategory(_context, parent);
            }
        };

        ExpandableSymptomListAdapter adapter = new ExpandableSymptomListAdapter(_activity,
                null, null, categoryMap, getter);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _activity = (FragmentActivity) activity;
        _context = activity;
    }

}
