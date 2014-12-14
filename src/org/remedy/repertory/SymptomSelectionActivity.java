package org.remedy.repertory;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.remedy.R;
import org.remedy.db.RemedyDAO;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class SymptomSelectionActivity extends Activity {

	public static final String CATEGORY_NAME = "category";
	public static final String SELECTED_SYMPTOMS = "selectedSymptoms";

	private SymptomListAdapter symptomListAdapter;
	private String categoryName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_symptom_selection);

		// We don't want the soft keyboard to show up by default.
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Intent intent = getIntent();
        categoryName = (String)intent.getSerializableExtra(CATEGORY_NAME);
        Set<String> selectedSymptoms = (Set<String>) intent.getSerializableExtra(SELECTED_SYMPTOMS);

        List<String> symptomList = RemedyDAO.getAllSymptomsForCategory(this, categoryName);
		symptomListAdapter = new SymptomListAdapter(this, symptomList, selectedSymptoms);
		ListView symptomListView = (ListView) findViewById(R.id.symptom_list);
		symptomListView.setAdapter(symptomListAdapter);
		updateTitle();

		// Monitor any data changes in the listAdapter so that we can update the title.
		symptomListAdapter.registerDataSetObserver(new DataSetObserver() {

			@Override
			public void onChanged() {
				super.onChanged();
				updateTitle();
			}

			@Override
			public void onInvalidated() {
				super.onInvalidated();
				updateTitle();
			}
		});

		symptomListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				symptomListAdapter.toggleItemSelection(position);
			}
		});

		/*
		 * If the text in the search box changes, apply the filtering criteria so
		 * that list is updated correctly.
		 */
		EditText inputSearch = (EditText) findViewById(R.id.symptom_list_inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                symptomListAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
	}

	/**
	 * Update the title for this activity.
	 * Depending on what is chosen by the user and what is currently shown to the
	 * user, different title will be displayed.
	 */
	private void updateTitle() {
		setTitle("Chosen: " + symptomListAdapter.getChosenList().size()  + ", showing: " + symptomListAdapter.getCount());
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.symptom_selection, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.symptom_selection_ok: {
                Intent intent = new Intent();
                intent.putExtra(CATEGORY_NAME, categoryName);
                intent.putExtra(SELECTED_SYMPTOMS, (Serializable)symptomListAdapter.getChosenList());
                setResult(RESULT_OK, intent);
                finish();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
