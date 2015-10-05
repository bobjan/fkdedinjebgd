package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        Utakmica utakmica = Utakmica.getInstance();

        BJDatum datum = utakmica.getDatum();
        tvDanDatum.setText(datum.getNamedMonthYear() +  " " + datum.toString());
        tvZakazanoVreme.setText(utakmica.getPlaniranoVremePocetka().toString());

        tvDomaciTim.setText(utakmica.getHomeTeamName());
        tvGostujuciTim.setText(utakmica.getAwayTeamName());
        stadion = BazaStadiona.getInstance().getStadion(utakmica.getStadionId());

        tvStadionName.setText(stadion.getNaziv());

//        mapsActivity = new Intent(this,NewMapsActivity.class);
        tvStadionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mapsActivity.putExtra("latitude", stadion.getLatitude());
//                mapsActivity.putExtra("laongitude", stadion.getLatitude());
//                startActivity(mapsActivity);
            }
        });
    }
}
