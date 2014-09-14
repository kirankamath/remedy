package org.remedy;

import org.remedy.db.updater.DataFetcher;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class ListenerActivity extends Activity implements DataFetcher.DataFetchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener);

        Uri fileUri = getIntent().getData();
        Toast.makeText(this, "File " + fileUri + " being processed. Uri ", Toast.LENGTH_LONG).show();
        new DataFetcher(this, fileUri.toString()).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listener, menu);
        return true;
    }

    @Override
    public void fetchComplete(String url) {
        Toast.makeText(this, "File " + url + " downloaded successfully", Toast.LENGTH_LONG).show();
    }
}
