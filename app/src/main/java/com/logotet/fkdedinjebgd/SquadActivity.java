package com.logotet.fkdedinjebgd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.fkdedinjebgd.adapters.IgracAdapter;

import java.util.ArrayList;

public class SquadActivity extends AppCompatActivity {
    ListView lvSquad;
    private IgracAdapter igracAdapter;
    ArrayList<Igrac> ekipa;
    Handler handler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_squad);
        lvSquad = (ListView) findViewById(R.id.lvSquad);
        igracAdapter = new IgracAdapter(this);
        lvSquad.setAdapter(igracAdapter);

        handler = new Handler();

        ekipa = BazaIgraca.getInstance().getSquad();
        for (int idx = 0; idx < ekipa.size(); idx++) {
            Igrac igrac = ekipa.get(idx);
            if (!igrac.isImageLoaded()) {
                loadImage(igrac);
            }
        }
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
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_HASH")
                .build();
        mAdView.loadAd(adRequest);


    }

    private void loadImage(final Igrac igrac) {
        Thread thread = new ImageLoader(igrac, handler, igracAdapter);
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
        }
        return super.onOptionsItemSelected(item);
    }


}
