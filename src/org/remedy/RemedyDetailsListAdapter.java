package org.remedy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * A list adapter to show remedy details in an expandable view.
 */
public class RemedyDetailsListAdapter extends BaseExpandableListAdapter {

    private final List<String> categoryList;
    private final Map<String, List<String>> categoryMap;
    private final Context context;

    private static final String DETAILS = "Details";
    private static final String DOSAGE = "Dosage";

    private final int BLUISH_COLOR;

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

    /**
     * A custom adapter for showing symptoms of a remedy.
     *
     * @param context Context to use for generating views.
     * @param remedy Remedy which should be displayed.
     */
    public RemedyDetailsListAdapter(Context context, Remedy remedy) {
        this.context = context;

        categoryMap = new HashMap<String, List<String>>();

        Map<String, Set<String>> inputMap = remedy.getSymptoms();

        // Convert the value from Set to a List.
        Set<String> categorySet = inputMap.keySet();
        for (String category: categorySet) {
            List<String> symptomList = new ArrayList<String>(inputMap.get(category));
            categoryMap.put(category, symptomList);
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

        // Add Details as the first entry after sort.
        categoryList.add(0, DETAILS);
        List<String> detailsList = new ArrayList<String>();
        detailsList.add(remedy.getDetails());
        categoryMap.put(DETAILS, detailsList);

        // Add Dosage as the last entry.
        categoryList.add(DOSAGE);
        List<String> dosageList = new ArrayList<String>();
        dosageList.add(remedy.getDosage());
        categoryMap.put(DOSAGE, dosageList);

        BLUISH_COLOR = context.getResources().getColor(R.color.blueish);
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
        String category = (String)getGroup(groupPosition);
        item.setText(category);
        if (category.equals(DETAILS) || category.equals(DOSAGE)) {
            // Give a different color for special categories.
            convertView.setBackgroundColor(Color.GRAY);
        } else {
            convertView.setBackgroundColor(BLUISH_COLOR);
        }
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
