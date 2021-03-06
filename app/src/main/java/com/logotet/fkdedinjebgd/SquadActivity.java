package com.logotet.fkdedinjebgd;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaPozicija;
import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.fkdedinjebgd.adapters.IgracAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class SquadActivity extends AppCompatActivity {
    private static final String TAG = "SquadActivity";
    ListView lvSquad;

    public static final String MY_PREFS_NAME = "DedinjePrefsFile";
    public static final String PREFS_PARAM = "lastsquad";
    private static long READ_INTERVAL = 1000L * 60 * 120; // dva sata

    private IgracAdapter igracAdapter;
    Handler handler;
    private Context context;

    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_squad);

//        BazaPozicija.getInstance().getTimposition().clear();
//        BazaIgraca.getInstance().getSquad().clear();
//        ekipa = new ArrayList<Igrac>();

        pbar = (ProgressBar) findViewById(R.id.pbarSquad);
        lvSquad = (ListView) findViewById(R.id.lvSquad);
        igracAdapter = new IgracAdapter(this);
        lvSquad.setAdapter(igracAdapter);

        pbar.setVisibility(View.VISIBLE);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    igracAdapter.reload();
                    pbar.setVisibility(View.GONE);
                    igracAdapter.notifyDataSetChanged();
                    for (int idx = 0; idx < BazaIgraca.getInstance().getSquad().size(); idx++) {
                        Igrac igrac = BazaIgraca.getInstance().getSquad().get(idx);
                        if (!igrac.isImageLoaded()) {
                            loadImage(igrac);
                        }
                    }
                }
                if (msg.what == 2) {
                    igracAdapter.notifyDataSetChanged();
                }
                if (msg.what == 0) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
            }
        };

        preuzmiEkipu();

        lvSquad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent portret = new Intent(context, PortretActivity.class);
                portret.putExtra("vrsta", 1);
                portret.putExtra("currentidx", position);
                startActivity(portret);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.ad3View);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B28574CD0A9CA2F7FCCFF26090B8385C")
                .addTestDevice("E41AFA768EE9855050236B1E36F530EF")
                .addTestDevice("7D6799C2FA3E8750288C0C1502069E5D")
                .build();
        mAdView.loadAd(adRequest);
    }

    private void loadImage(final Igrac igrac) {
        Thread thread = new ImageLoader(igrac, handler);
        thread.start();
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
                return true;
            case R.id.action_standings:
                startActivity(new Intent(this, StandingsActivity.class));
                return true;
            case R.id.action_livescore:
                startActivity(new Intent(this, LiveScoreActivity.class));
                return true;
            case R.id.action_news:
                startActivity(new Intent(this, NewsActivity.class));
                return true;        }
        return super.onOptionsItemSelected(item);
    }


    public void preuzmiEkipu() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isTooOld() || (!BazaIgraca.getInstance().isLoaded())  || (BazaIgraca.getInstance().getSquad().size() == 0) ||
                            (!BazaPozicija.getInstance().isLoaded()) || (BazaPozicija.getInstance().getTimposition().size() == 0)) {
                        HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETPOZICIJA, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                        catcher = new HttpCatcher(RequestPreparator.GETEKIPA, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                        rememberReadTime();
                        Log.w(TAG," citano !!!!!");
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
