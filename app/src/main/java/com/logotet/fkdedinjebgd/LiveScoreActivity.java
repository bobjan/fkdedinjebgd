package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logotet.dedinjeadmin.AllStatic;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaStadiona;
import com.logotet.dedinjeadmin.model.BazaTimova;
import com.logotet.dedinjeadmin.model.Stadion;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.fkdedinjebgd.adapters.ClientEventsAdapter;
import com.logotet.fkdedinjebgd.adapters.ClientIgracAdapter;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LiveScoreActivity extends AppCompatActivity {
    private static final String TAG = "LiveScoreActivity";
    private boolean showSastav;
    private boolean showEvents;

    private ClientEventsAdapter eventsAdapter;
    private ClientIgracAdapter sastavAdapter;

    ProgressBar pbar;
    Button btnShowSastav;
    Button btnShowEvents;

    LinearLayout llEvents;
    LinearLayout llSastav;
    ListView lvClientEvents;
    ListView lvClientIgrac;

    TextView tvCurrentScore;
    TextView tvCurrentMinute;
    TextView tvHomeTeam;
    TextView tvAwayTeam;

    TextView txtDatum;
    TextView txtStadion;
    ImageView ivGmaps;

    Stadion stadion;
    Intent mapsActivity;
    Handler handler;

    private final long INTERVAL = 15L * 1000; // 15 sekundi
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_score);
        btnShowSastav = (Button) findViewById(R.id.btnShowSastav);
        btnShowEvents = (Button) findViewById(R.id.btnShowEvents);
        llEvents = (LinearLayout) findViewById(R.id.llEvents);
        llSastav = (LinearLayout) findViewById(R.id.llSastav);


        lvClientEvents = (ListView) findViewById(R.id.lvClientEvents);
        lvClientIgrac = (ListView) findViewById(R.id.lvClientIgrac);

        eventsAdapter = new ClientEventsAdapter(this);
        sastavAdapter = new ClientIgracAdapter(this);

        lvClientEvents.setAdapter(eventsAdapter);
        lvClientIgrac.setAdapter(sastavAdapter);

        pbar = (ProgressBar) findViewById(R.id.pbarLivescore);
        pbar.setVisibility(View.VISIBLE);

        tvCurrentScore = (TextView) findViewById(R.id.tvCurrentScore);
        tvCurrentMinute = (TextView) findViewById(R.id.tvCurrentMinute);
        tvHomeTeam = (TextView) findViewById(R.id.tvHomeTeamName);
        tvAwayTeam = (TextView) findViewById(R.id.tvAwayTeamName);
        txtDatum = (TextView) findViewById(R.id.txtDatum);
        txtStadion = (TextView) findViewById(R.id.txtStadion);

        ivGmaps = (ImageView) findViewById(R.id.ivgmaps);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    refreshAll();
                    pbar.setVisibility(View.GONE);
                    eventsAdapter.notifyDataSetChanged();
                    sastavAdapter.notifyDataSetChanged();
                }
            }
        };
        preuzmiMatch();

        showSastav = true;
        showEvents = true;

        btnShowSastav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSastav = showSastav ? false : true;
                sastavAdapter.notifyDataSetChanged();
                if (showSastav)
                    llSastav.setVisibility(View.VISIBLE);
                else
                    llSastav.setVisibility(View.GONE);
            }
        });

        btnShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEvents = showEvents ? false : true;
                if (showEvents)
                    llEvents.setVisibility(View.VISIBLE);
                else
                    llEvents.setVisibility(View.GONE);
            }
        });

        mapsActivity = new Intent(this, MapsActivity.class);
        txtStadion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.putExtra("latitude", stadion.getLatitude());
                mapsActivity.putExtra("longitude", stadion.getLongitude());
                mapsActivity.putExtra("stadion", stadion.getNaziv());
                startActivity(mapsActivity);
            }
        });
        ivGmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.putExtra("latitude", stadion.getLatitude());
                mapsActivity.putExtra("longitude", stadion.getLongitude());
                mapsActivity.putExtra("stadion", stadion.getNaziv());
                startActivity(mapsActivity);
            }
        });

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pbar.setVisibility(View.GONE);
                preuzmiMatch();
            }
        }, INTERVAL, INTERVAL);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAll();
    }

    @Override
    protected void onPause() {
        if (timer != null) {
            timer.cancel();
        }
        super.onPause();
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
                startActivity(new Intent(this, StandingsActivity.class));
                return true;
            case R.id.action_livescore:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshAll() {
        Utakmica utakmica = Utakmica.getInstance();
        utakmica.odrediMinutazu();
        tvCurrentScore.setText(utakmica.getCurrentRezulat());
        tvCurrentMinute.setText(utakmica.getCurrentMinutIgre());
        tvHomeTeam.setText(utakmica.getHomeTeamName());
        tvAwayTeam.setText(utakmica.getAwayTeamName());

        txtDatum.setText(utakmica.getDatum().toString());

        stadion = BazaStadiona.getInstance().getStadion(utakmica.getStadionId());
        if (stadion != null)
            txtStadion.setText(stadion.getNaziv());
        else
            txtStadion.setText(stadion.getNaziv());
    }


    private void preuzmiMatch() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.SERVERTIME, AllStatic.HTTPHOST, null);
                    catcher.catchData();

                    if(BazaStadiona.getInstance().getTereni().size() == 0) {
                        catcher = new HttpCatcher(RequestPreparator.GETSTADION, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                    }
                    if(BazaTimova.getInstance().getProtivnici().size() == 0) {
                        catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                    }

                    BazaIgraca.getInstance().getSquad().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETEKIPA, AllStatic.HTTPHOST, null);
                    catcher.catchData();

                    catcher = new HttpCatcher(RequestPreparator.GETLIVEMATCH, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    if (!Utakmica.getInstance().getDatum().greaterThanToday()) {
                        catcher = new HttpCatcher(RequestPreparator.GETSASTAV, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                    }

                    catcher = new HttpCatcher(RequestPreparator.ALLEVENTS, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }

}
