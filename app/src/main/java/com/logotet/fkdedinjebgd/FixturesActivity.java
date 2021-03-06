package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.Fixtures;
import com.logotet.dedinjeadmin.model.FixturesRow;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.fkdedinjebgd.adapters.FixturesAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class FixturesActivity extends AppCompatActivity {
    private static final String TAG = "FixturesActivity";

    public static final String MY_PREFS_NAME = "DedinjePrefsFile";
    public static final String PREFS_PARAM = "lastfixtures";
    private static long READ_INTERVAL = 1000L * 60 * 120; // dva sata

    TextView tvSezona;
    ListView lvFixtures;
    private FixturesAdapter fixturesAdapter;
    Handler handler;

    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures);

        lvFixtures = (ListView) findViewById(R.id.lvFixtures);
        pbar = (ProgressBar) findViewById(R.id.pbarFixtures);

        fixturesAdapter = new FixturesAdapter(this);
        tvSezona = (TextView) findViewById(R.id.tvSeason);
        pbar.setVisibility(View.VISIBLE);
        lvFixtures.setAdapter(fixturesAdapter);
//
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
                if (msg.what == 1) {
                    String sezona = Fixtures.getInstance().getSezona();
                    tvSezona.setText(sezona);
//                    raspored.clear();
//                    raspored.addAll(Fixtures.getInstance().getRaspored());
                    fixturesAdapter.reload();
                    fixturesAdapter.notifyDataSetChanged();
                    pbar.setVisibility(View.GONE);
                }
            }
        };
        preuzmiRaspored();

        AdView mAdView = (AdView) findViewById(R.id.ad2View);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B28574CD0A9CA2F7FCCFF26090B8385C")
                .addTestDevice("E41AFA768EE9855050236B1E36F530EF")
                .addTestDevice("7D6799C2FA3E8750288C0C1502069E5D")
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
            case R.id.action_news:
                startActivity(new Intent(this, NewsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void preuzmiRaspored() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isTooOld() || (!Fixtures.getInstance().isLoaded()) || (Fixtures.getInstance().getRaspored().size() == 0)) {
                        HttpCatcher catcher;
                        catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                        catcher = new HttpCatcher(RequestPreparator.GETFIXTURES, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                        rememberReadTime();
                    }
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }

    private boolean isTooOld() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        long now = System.currentTimeMillis();
        long lastRead = prefs.getLong(PREFS_PARAM, 0L);

        if (now > (lastRead + READ_INTERVAL))
            return true;
        return false;
    }
    private void rememberReadTime() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        long now = System.currentTimeMillis();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREFS_PARAM, now);
        editor.commit();
    }
}
