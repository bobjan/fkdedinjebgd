package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.logotet.dedinjeadmin.model.BazaStadiona;
import com.logotet.dedinjeadmin.model.Stadion;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.util.BJDatum;

public class NextMatchActivity extends AppCompatActivity {
    TextView tvDanDatum;
    TextView tvZakazanoVreme;
    TextView tvDomaciTim;
    TextView tvGostujuciTim;
    TextView tvStadionName;
    ImageView ivGoogleMaps;

    Stadion stadion;
    Intent mapsActivity;

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


        Utakmica utakmica = Utakmica.getInstance();

        BJDatum datum = utakmica.getDatum();
        int indexDana = datum.getDayOfWeek() - 2;
        if (indexDana < 0)
            indexDana += 7;
        tvDanDatum.setText(BJDatum.getNazivDana(indexDana) + " " + datum.toString());
        tvZakazanoVreme.setText(utakmica.getPlaniranoVremePocetka().toString().substring(0, 5));

        tvDomaciTim.setText(utakmica.getHomeTeamName());
        tvGostujuciTim.setText(utakmica.getAwayTeamName());
        stadion = BazaStadiona.getInstance().getStadion(utakmica.getStadionId());

        tvStadionName.setText(stadion.getNaziv());

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
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("B28574CD0A9CA2F7FCCFF26090B8385C").addTestDevice("E41AFA768EE9855050236B1E36F530EF").build();

        mAdView.loadAd(adRequest);
    }
}
