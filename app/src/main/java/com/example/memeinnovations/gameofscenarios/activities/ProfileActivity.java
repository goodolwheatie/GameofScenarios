package com.example.memeinnovations.gameofscenarios.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.memeinnovations.gameofscenarios.R;
import com.example.memeinnovations.gameofscenarios.adapters.SimpleFragmentPagerAdapter;


public class ProfileActivity extends AppCompatActivity {

    private void actionBarSetup() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Profile");
    }

    private void init(){
        actionBarSetup();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new
                SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)
                findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // initialize activity
        init();
    }



}
