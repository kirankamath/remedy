package org.remedy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RemedyListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedy_list);

        String[] remedyStrList = new String[] {"abies canadensis",
                "abies nigra", "abrotanum", "absinthium"};
        ListView remedyList = (ListView) findViewById(R.id.remedy_list);
        ListAdapter listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, remedyStrList);
        remedyList.setAdapter(listAdapter);
    }
}
