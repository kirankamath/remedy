package org.remedy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RemedyActivity extends Activity {
    public RemedyActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Setup listeners for buttons.
        Button listButton = (Button) findViewById(R.id.remedy_list_button);
        listButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemedyActivity.this, RemedyListActivity.class);
                startActivity(intent);
            }
        });

        Button repertoryButton = (Button) findViewById(R.id.repertory_button);
        repertoryButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemedyActivity.this, RepertoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
