package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.BazaStadiona;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.model.Osoba;
import com.logotet.dedinjeadmin.model.Stadion;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.util.BJDatum;

import java.io.IOException;

public class NextMatchActivity extends AppCompatActivity {
    TextView tvDanDatum;
    TextView tvZakazanoVreme;
    TextView tvDomaciTim;
    TextView tvGostujuciTim;
    TextView tvStadionName;
    ImageView ivGoogleMaps;
    ProgressBar pbar;

    Stadion stadion;
    Intent mapsActivity;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_match);

        tvDanDatum = (TextView) findViewById(R.id.tvDanDatum);
        tvZakazanoVreme = (TextView) findViewById(R.id.tvZakazanoVreme);
        tvDomaciTim = (TextView) findViewById(R.id.tvDomaciTim);
        tvGostujuciTim = (TextView) findViewById(R.id.tvGostujuciTim);
        tvStadionName = (TextView) findViewById(R.id.tvStadionName);
        ivGoogleMaps = (ImageView) findViewById(R.id.ivgooglemaps);

        pbar = (ProgressBar) findViewById(R.id.pbarNextMatch);
        pbar.setVisibility(View.VISIBLE);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
                if(msg.what == 1){
                    Utakmica utakmica = Utakmica.getInstance();
                    stadion = BazaStadiona.getInstance().getStadion(utakmica.getStadionId());
                    BJDatum datum = utakmica.getDatum();
                    int indexDana = datum.getDayOfWeek() - 2;
                    if (indexDana < 0)
                        indexDana += 7;
                    tvDanDatum.setText(BJDatum.getNazivDana(indexDana) + " " + datum.toString());
                    tvZakazanoVreme.setText(utakmica.getPlaniranoVremePocetka().toString().substring(0, 5));

                    tvDomaciTim.setText(utakmica.getHomeTeamName());
                    tvGostujuciTim.setText(utakmica.getAwayTeamName());

                    tvStadionName.setText(stadion.getNaziv());
                    pbar.setVisibility(View.GONE);
                }
            }
        };

        preuzmiPodatke();

        mapsActivity = new Intent(this, MapsActivity.class);
        tvStadionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.putExtra("latitude", stadion.getLatitude());
                mapsActivity.putExtra("longitude", stadion.getLongitude());
                mapsActivity.putExtra("stadion", stadion.getNaziv());
                startActivity(mapsActivity);
            }
        });

        ivGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.putExtra("latitude", stadion.getLatitude());
                mapsActivity.putExtra("longitude", stadion.getLongitude());
                mapsActivity.putExtra("stadion", stadion.getNaziv());
                startActivity(mapsActivity);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.ad5View);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B28574CD0A9CA2F7FCCFF26090B8385C")
                .addTestDevice("E41AFA768EE9855050236B1E36F530EF")
                .addTestDevice("7D6799C2FA3E8750288C0C1502069E5D")
                .build();

        mAdView.loadAd(adRequest);
    }


    private void preuzmiPodatke() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETSTADION, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.GETLIVEMATCH, AllStatic.HTTPHOST, null);
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
