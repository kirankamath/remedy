package org.remedy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

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
                FragmentListArraySupport.ArrayListFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("selected").setIndicator("Selected symptoms"),
                FragmentListArraySupport.ArrayListFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("remedy").setIndicator("Recommendation"),
                FragmentListArraySupport.ArrayListFragment.class, null);
    }
}