package org.remedy;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class RemedyListActivity extends Activity {

    private ListView remedyList;
    private EditText inputSearch;
    private ArrayAdapter<String> remedyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedy_list);

        String[] remedyStrList = new String[] {"abies canadensis",
                "abies nigra", "abrotanum", "absinthium"};
        remedyList = (ListView) findViewById(R.id.remedy_list);
        inputSearch = (EditText) findViewById(R.id.remedy_list_inputSearch);

        remedyListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, remedyStrList);
        remedyList.setAdapter(remedyListAdapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RemedyListActivity.this.remedyListAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
