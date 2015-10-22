package com.logotet.fkdedinjebgd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.logotet.dedinjeadmin.AllStatic;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.Fixtures;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.model.Osoba;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.fkdedinjebgd.adapters.ManagementAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class ManagementActivity extends AppCompatActivity {
    ListView lvRukovodstvo;
    private ManagementAdapter managerAdapter;
    ArrayList<Osoba> rukovodstvo;

    Handler handler;
    private Context context;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_management);

        Klub.getInstance().getRukovodstvo().clear();

        lvRukovodstvo = (ListView) findViewById(R.id.lvManagement);
        pbar = (ProgressBar) findViewById(R.id.pbarManagement);

        managerAdapter = new ManagementAdapter(this);
        lvRukovodstvo.setAdapter(managerAdapter);
        pbar.setVisibility(View.VISIBLE);

       handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    // Toast internet error!
                }
                if(msg.what == 1){
                    rukovodstvo = Klub.getInstance().getRukovodstvo();
                    managerAdapter.notifyDataSetChanged();
                    pbar.setVisibility(View.GONE);
                    for (int idx = 0; idx < rukovodstvo.size(); idx++) {
                        Osoba osoba = rukovodstvo.get(idx);
                        if (!osoba.isImageLoaded()) {
                            loadImage(osoba);
                        }
                    }
                }
            }
        };

        preuzmiRukovodstvo();

        lvRukovodstvo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent portret = new Intent(context, PortretActivity.class);
                portret.putExtra("vrsta", 2);
                portret.putExtra("currentidx", position);
                startActivity(portret);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.ad4View);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B28574CD0A9CA2F7FCCFF26090B8385C")
                .addTestDevice("E41AFA768EE9855050236B1E36F530EF")
                .addTestDevice("7D6799C2FA3E8750288C0C1502069E5D")
                .build();

        mAdView.loadAd(adRequest);
    }

    private void loadImage(final Osoba osoba) {
        Thread thread = new ImageLoader(osoba, handler, managerAdapter);
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
                startActivity(new Intent(this, LiveScoreActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void preuzmiRukovodstvo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETRUKOVODSTVO, AllStatic.HTTPHOST, null);
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
