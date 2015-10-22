package com.logotet.fkdedinjebgd;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.logotet.fkdedinjebgd.adapters.PortretAdapter;

public class PortretActivity extends FragmentActivity {
    private static final String TAG = "PortretActivity";

    int vrsta;
    int currentidx;

    private ViewPager vpPortretViewPager;

    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portret);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vrsta = extras.getInt("vrsta");
            currentidx = extras.getInt("currentidx");
        }

        vpPortretViewPager = (ViewPager) findViewById(R.id.vpPortretViewPager);
        pagerAdapter = new PortretAdapter(vrsta, getSupportFragmentManager());
        vpPortretViewPager.setAdapter(pagerAdapter);
        vpPortretViewPager.setCurrentItem(currentidx);

        vpPortretViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });

    }
}
