package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import com.logotet.dedinjeadmin.model.AppHeaderData;
import com.logotet.dedinjeadmin.model.Tabela;
import com.logotet.dedinjeadmin.model.TabelaRow;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.fkdedinjebgd.adapters.StandingsAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class StandingsActivity extends AppCompatActivity {
    private static final String TAG = "StandingsActivity";

    public static final String MY_PREFS_NAME = "DedinjePrefsFile";
    public static final String PREFS_PARAM = "laststandings";
    private static long READ_INTERVAL = 1000L * 60 * 120; // dva sata


    TextView tvNazivLige;
    TextView tvOdigranoKolo;

    ListView lvStandings;
    private StandingsAdapter standingsAdapter;
    ArrayList<TabelaRow> tabela;

    Handler handler;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);


        lvStandings = (ListView) findViewById(R.id.lvStandings);
        standingsAdapter = new StandingsAdapter(this);
        lvStandings.setAdapter(standingsAdapter);


        tvNazivLige = (TextView) findViewById(R.id.tvNazivLige);
        tvOdigranoKolo = (TextView) findViewById(R.id.tvOdigranoKolo);
        lvStandings = (ListView) findViewById(R.id.lvStandings);
        pbar = (ProgressBar) findViewById(R.id.pbarStandings);
        pbar.setVisibility(View.VISIBLE);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    tvNazivLige.setText(AppHeaderData.getInstance().getNazivLige());
                    tvOdigranoKolo.setText("после " + Tabela.getInstance().getLastRound() + ". кола");
                    standingsAdapter.reload();
//                    tabela.clear();
//                    tabela.addAll(Tabela.getInstance().getPlasman());
                    standingsAdapter.notifyDataSetChanged();
                    pbar.setVisibility(View.GONE);
                }
                if (msg.what == 0) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
            }
        };

        preuzmiTabelu();

        AdView mAdView = (AdView) findViewById(R.id.ad1View);
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
                startActivity(new Intent(this, FixturesActivity.class));
                return true;
            case R.id.action_squad:
                startActivity(new Intent(this, SquadActivity.class));
                return true;
            case R.id.action_standings:
                return true;
            case R.id.action_livescore:
                startActivity(new Intent(this, LiveScoreActivity.class));
                return true;
            case R.id.action_news:
                startActivity(new Intent(this, NewsActivity.class));
                return true;        }
        return super.onOptionsItemSelected(item);
    }

    private void preuzmiTabelu() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isTooOld() || (!Tabela.getInstance().isLoaded()) || (Tabela.getInstance().getPlasman().size() == 0)) {
                        Tabela.getInstance().getPlasman().clear();
                        HttpCatcher catcher;
                        catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                        catcher = new HttpCatcher(RequestPreparator.GETTABELA, AllStatic.HTTPHOST, null);
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
