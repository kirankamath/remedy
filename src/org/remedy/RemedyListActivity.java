package org.remedy;

import java.util.List;

import org.remedy.db.RemedyDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class RemedyListActivity extends Activity {

    private static final int SHOW_REMEDY_DETAILS_REQUEST = 10;

    private ListView remedyList;
    private EditText inputSearch;
    private ArrayAdapter<String> remedyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedy_list);
        setTitle("List of remedies");

        RemedyDAO remedyDao = new RemedyDAO();
        List<String> remedyStrList = remedyDao.getRemedyNames(this);
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

        remedyList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RemedyListActivity.this, OneRemedyActivity.class);
                intent.putExtra(OneRemedyActivity.REMEDY_NAME, remedyListAdapter.getItem(position));
                startActivityForResult(intent, SHOW_REMEDY_DETAILS_REQUEST);
            }
        });
    }
}
