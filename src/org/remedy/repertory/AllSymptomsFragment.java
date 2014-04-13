package org.remedy.repertory;

import java.util.HashMap;
import java.util.List;

import org.remedy.ExpandableSymptomListAdapter;
import org.remedy.ExpandableSymptomListAdapter.ChildrenGetter;
import org.remedy.R;
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
import android.widget.ExpandableListView.OnChildClickListener;

public class AllSymptomsFragment extends Fragment {

    private FragmentActivity _activity;
    private Context _context;
    private SymptomAddRemoveInterface _symptomAddRemove;

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

        final ExpandableSymptomListAdapter adapter = new ExpandableSymptomListAdapter(_activity,
                null, null, categoryMap, getter, true);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {
                String category = (String)adapter.getGroup(groupPosition);
                String symptom = (String)adapter.getChild(groupPosition, childPosition);

                if (adapter.isItemSelected(groupPosition, childPosition)) {

                    // Already selected. Unselect it.
                    _symptomAddRemove.symptomRemoved(category, symptom);
                } else {
                    _symptomAddRemove.symptomAdded(category, symptom);
                }

                adapter.toggleSelection(groupPosition, childPosition);
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _activity = (FragmentActivity) activity;
        _context = activity;
        _symptomAddRemove = (SymptomAddRemoveInterface) activity;
    }
}
