package org.remedy;

import org.remedy.repertory.CategoryListActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    public MainActivity() {
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
                Intent intent = new Intent(MainActivity.this, RemedyListActivity.class);
                startActivity(intent);
            }
        });

        Button repertoryButton = (Button) findViewById(R.id.repertory_button);
        repertoryButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });

        Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle(org.remedy.R.string.about);
                String about_string = getResources().getString(org.remedy.R.string.about_contents);
                final SpannableString s =
                        new SpannableString(
                                Html.fromHtml(about_string));
                Linkify.addLinks(s, Linkify.WEB_URLS);
                TextView textView = new TextView(MainActivity.this);
                textView.setPadding(10, 10, 10, 10);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setText(s);
                dialogBuilder.setView(textView);
                dialogBuilder.show();
            }
        });
    }
}
