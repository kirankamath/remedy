package org.remedy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
public class ExpandableSymptomListAdapter extends BaseExpandableListAdapter {

    private final List<String> categoryList;
    private final Set<String> preList;
    private final Set<String> postList;
    private final Map<String, List<String>> categoryMap;
    private final Context context;
    private final ChildrenGetter childrenGetter;

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
     * Interface for querying children for a given category name.
     */
    public interface ChildrenGetter {

        /**
         * Get the list of children for this parent.
         *
         * @param parent Name of the parent.
         * @return List of children.
         */
        public List<String> getChildren(String parent);
    }

    /**
     * A custom adapter for showing symptoms of a remedy.
     *
     * @param context Context to use for generating views.
     * @param remedy Remedy which should be displayed.
     */
    public ExpandableSymptomListAdapter(Context context, Set<String> preList,
            Set<String> postList, HashMap<String, List<String>> categoryMap,
            ChildrenGetter childrenGetter) {
        this.context = context;
        this.categoryMap = categoryMap;
        this.preList = preList;
        this.postList = postList;
        this.childrenGetter = childrenGetter;

        // Need to copy since modifications will reflect into original map.
        Set<String> categorySet = new HashSet<String>(categoryMap.keySet());

        // Maintain an ordered array for display.
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

        // Remove pre and post from the categorySet since we will add it explicitly.
        if (preList != null) {
            categorySet.removeAll(preList);

            // Add the pre entries to the beginning of the list.
            for (String pre : preList) {
                categoryList.add(0, pre);
            }
        }
        if (postList != null) {
            categorySet.removeAll(postList);

            // Add all the post entries to the end.
            categoryList.addAll(postList);
        }

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
        if (children == null) {
            if (childrenGetter != null) {

                // If not set, get it at runtime.
                children = childrenGetter.getChildren(category);
                categoryMap.put(category, children);
            } else {
                return 0;
            }
        }
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
        if (children == null) {
            if (childrenGetter != null) {

                // If not set, get it at runtime.
                children = childrenGetter.getChildren(category);
                categoryMap.put(category, children);
            } else {
                return 0;
            }
        }
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
        if ((preList != null && preList.contains(category)) ||
                (postList != null && postList.contains(category))) {
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
