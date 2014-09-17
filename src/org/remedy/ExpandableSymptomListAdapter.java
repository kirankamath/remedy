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
    private final Map<String, Set<String>> selectedItemMap;
    private final Context context;
    private final ChildrenGetter childrenGetter;
    private final boolean allowChildSelection;

    private final int BLUISH_COLOR;
    private final int LIGHTRED_COLOR;

    private static final String[] categoryOrder = new String[] {
        "Head",
        "Mind",
        "Face",
        "Nose",
        "Mouth",
        "Throat",
        "Respiratory",
        "Chest",
        "Heart",
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
            Set<String> postList, Map<String, List<String>> categoryMap,
            ChildrenGetter childrenGetter, boolean allowChildSelection,
            Map<String, Set<String>> selectedItemMap) {
        this.context = context;
        this.categoryMap = categoryMap;
        this.preList = preList;
        this.postList = postList;
        this.childrenGetter = childrenGetter;
        this.allowChildSelection = allowChildSelection;
        this.selectedItemMap = new HashMap<String, Set<String>>();
        if (selectedItemMap != null) {

            // Retain only elements that matter for this remedy.
            for (String selectedCategory : selectedItemMap.keySet()) {
                if (categoryMap.containsKey(selectedCategory)) {
                    Set<String> selectedItems = new HashSet<String>(selectedItemMap.get(selectedCategory));

                    // Only retain symptoms that are in this remedy.
                    selectedItems.retainAll(getSymptomsForCategory(selectedCategory));
                    this.selectedItemMap.put(selectedCategory, selectedItems);
                }
            }
        }

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
            categoryList.removeAll(preList);

            // Add the pre entries to the beginning of the list.
            for (String pre : preList) {
                categoryList.add(0, pre);
            }
        }
        if (postList != null) {
            categoryList.removeAll(postList);

            // Add all the post entries to the end.
            categoryList.addAll(postList);
        }

        BLUISH_COLOR = context.getResources().getColor(R.color.blueish);
        LIGHTRED_COLOR = context.getResources().getColor(R.color.lightred);
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    private List<String> getSymptomsForCategory(String category) {
        List<String> children = categoryMap.get(category);
        if (children == null) {
            if (childrenGetter != null) {

                // If not set, get it at runtime.
                children = childrenGetter.getChildren(category);
                categoryMap.put(category, children);
            } else {
                return null;
            }
        }
        return children;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String category = categoryList.get(groupPosition);
        List<String> children = getSymptomsForCategory(category);
        return children == null ? 0 : children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String category = categoryList.get(groupPosition);
        List<String> children = getSymptomsForCategory(category);
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
        String category = (String) getGroup(groupPosition);

        if (isItemSelected(category, symptom)) {
            convertView.setBackgroundColor(LIGHTRED_COLOR);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

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

        Set<String> items = selectedItemMap.get(category);
        if (items != null && items.size() > 0) {
            category += " (" + items.size() +")";
        }

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
        return allowChildSelection;
    }

    public void toggleSelection(String category, String symptom) {
        assert allowChildSelection;

        Set<String> items = selectedItemMap.get(category);
        if (items == null) {
            items = new HashSet<String>();
            selectedItemMap.put(category, items);
            items.add(symptom);
        } else {
            if (items.contains(symptom)) {
                items.remove(symptom);
            } else {
                items.add(symptom);
            }
        }
        notifyDataSetChanged();
    }

    private boolean isItemSelected(String category, String symptom) {
        Set<String> items = selectedItemMap.get(category);
        if (items == null) {
            return false;
        }
        return items.contains(symptom);
    }

    public Map<String, Set<String>> getSelectedSymptoms() {
        return selectedItemMap;
    }
}
