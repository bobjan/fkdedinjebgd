package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.logotet.dedinjeadmin.model.Fixtures;
import com.logotet.fkdedinjebgd.adapters.FixturesAdapter;

public class FixturesActivity extends AppCompatActivity {
    private static final String TAG = "FixturesActivity";
    TextView tvSezona;
    ListView lvFixtures;
    private FixturesAdapter fixturesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures);


        String sezona = Fixtures.getInstance().getSezona();

        lvFixtures = (ListView) findViewById(R.id.lvFixtures);
        fixturesAdapter = new FixturesAdapter(this);
        lvFixtures.setAdapter(fixturesAdapter);
        tvSezona = (TextView) findViewById(R.id.tvSeason);
//
        tvSezona.setText(sezona);
        AdView mAdView = (AdView) findViewById(R.id.ad2View);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_HASH")
                .build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            case R.id.action_management:
                startActivity(new Intent(this, ManagementActivity.class));
                return true;
            case R.id.action_fixtures:
                return true;
            case R.id.action_squad:
                startActivity(new Intent(this, SquadActivity.class));
                return true;
            case R.id.action_standings:
                startActivity(new Intent(this, StandingsActivity.class));
                return true;
            case R.id.action_livescore:
                startActivity(new Intent(this, LiveScoreActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
