package com.logotet.fkdedinjebgd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.AppHeaderData;
import com.logotet.dedinjeadmin.model.Klub;

public class HomeActivity extends AppCompatActivity {
    ImageView ivFrontImage;
    TextView tvPunNaziv;
    TextView tvMesto;
    TextView tvEmail;
    TextView tvWeb;
    TextView tvPib;
    TextView tvMatbroj;
    TextView tvTekrac;
    TextView tvAdresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvPunNaziv = (TextView) findViewById(R.id.tvPunNaziv);
        tvMesto = (TextView) findViewById(R.id.tvMesto);
        tvAdresa = (TextView) findViewById(R.id.tvAdresa);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvWeb = (TextView) findViewById(R.id.tvWeb);
        tvPib = (TextView) findViewById(R.id.tvPib);
        tvMatbroj = (TextView) findViewById(R.id.tvMatbroj);
        tvTekrac = (TextView) findViewById(R.id.tvTekrac);

        tvPunNaziv.setText(AppHeaderData.getInstance().getUserTeamName());

        Klub klub = Klub.getInstance();


        tvMesto.setText(klub.getMesto());
        tvAdresa.setText(klub.getAdresa());
        tvEmail.setText(klub.getEmail());
        tvWeb.setText(klub.getWeb());
        tvPib.setText(klub.getPib());
        tvMatbroj.setText(klub.getMatbroj());
        tvTekrac.setText(klub.getTekrac());


       ivFrontImage = (ImageView) findViewById(R.id.ivFrontImage);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
