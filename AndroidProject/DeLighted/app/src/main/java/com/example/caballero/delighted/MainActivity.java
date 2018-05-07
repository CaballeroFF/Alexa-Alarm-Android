package com.example.caballero.delighted;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.TabLayout;
import android.view.Window;


public class MainActivity extends AppCompatActivity {

    private SectionPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mSectionsPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        Log.d(TAG, "onCreate: Starting");

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabOneFragment(), "TAB1");
        adapter.addFragment(new TabTwoFragment(), "Clock");
        adapter.addFragment(new TabThreeFragment(), "TAB3");
        adapter.addFragment(new TabFourFragment(),"TAB4");
        viewPager.setAdapter(adapter);
    }

}
