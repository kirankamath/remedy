package org.remedy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;

/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments, using FragmentTabHost.
 */
public class RepertoryActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repertory);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("symptoms").setIndicator("Symptoms"),
                ArrayListFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("selected").setIndicator("Selected symptoms"),
                ArrayListFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("remedy").setIndicator("Remedy list"),
                ArrayListFragment.class, null);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            TextView view = (TextView)mTabHost.getTabWidget()
                    .getChildAt(i).findViewById(android.R.id.title);
            view.setAllCaps(false);
        }

    }
}