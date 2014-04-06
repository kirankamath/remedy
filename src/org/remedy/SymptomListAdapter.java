package org.remedy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SymptomListAdapter extends BaseExpandableListAdapter {

    private final List<String> categoryList;
    private final Map<String, List<String>> categoryMap;
    private final Context context;

    private static final String[] categoryOrder = new String[] {
        "Head",
        "Mind",
        "Face",
        "Nose",
        "Mouth",
        "Throat",
        "Respiratory",
        "Chest",
        "Back",
        "Stomach",
        "Abdomen",
        "Urine",
        "Genetalia",
        "Skin",
        "Modalities",
        "Sexual",
        "Sleep",
        "Female",
        "Extremeties",
        "Fever",
    };

    public SymptomListAdapter(Context context, Map<String, Set<String>> inputMap) {
        this.context = context;

        this.categoryMap = new HashMap<String, List<String>>();

        // Convert the value from Set to a List.
        Set<String> categorySet = inputMap.keySet();
        for (String category: categorySet) {
            List<String> symptomList = new ArrayList<String>(inputMap.get(category));
            this.categoryMap.put(category, symptomList);
        }
        categoryList = new ArrayList<String>();

        // Walk thru the ordered list. If found, add it.
        for (String category: categoryOrder) {
            if (categorySet.contains(category)) {
                // Remove it from the set so that we can add the rest at the end.
                categorySet.remove(category);
                categoryList.add(category);
            }
        }

        // Add any remaining entries.
        categoryList.addAll(categorySet);
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String category = categoryList.get(groupPosition);
        List<String> children = categoryMap.get(category);
        return children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String category = categoryList.get(groupPosition);
        List<String> children = categoryMap.get(category);
        return children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.remedy_details_symptom_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.remedy_details_symptom_item_text);
        String symptom = (String)getChild(groupPosition, childPosition);
        item.setText(symptom);
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.remedy_details_category_group_item, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.remedy_details_group_item_text);
        item.setTypeface(null, Typeface.BOLD);
        String category = (String)getGroup(groupPosition);
        item.setText(category);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
