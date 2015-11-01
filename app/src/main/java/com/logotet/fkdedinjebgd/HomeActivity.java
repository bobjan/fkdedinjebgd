package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.AppHeaderData;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {
    ImageView ivMainPhoto;
    TextView tvPunNaziv;
    TextView tvDatumOsnivanja;
    TextView tvMesto;
    TextView tvEmail;
    TextView tvWeb;
    TextView tvPib;
    TextView tvMatbroj;
    TextView tvTekrac;
    TextView tvAdresa;

    Handler handler;
    byte[] bitmapBytes;

    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvPunNaziv = (TextView) findViewById(R.id.tvPunNaziv);
        tvDatumOsnivanja = (TextView) findViewById(R.id.tvDatumOsnivanja);
        tvMesto = (TextView) findViewById(R.id.tvMesto);
        tvAdresa = (TextView) findViewById(R.id.tvAdresa);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvWeb = (TextView) findViewById(R.id.tvWeb);
        tvPib = (TextView) findViewById(R.id.tvPib);
        tvMatbroj = (TextView) findViewById(R.id.tvMatbroj);
        tvTekrac = (TextView) findViewById(R.id.tvTekrac);

        pbar = (ProgressBar) findViewById(R.id.pbarHome);

        pbar.setVisibility(View.VISIBLE);
        ivMainPhoto = (ImageView) findViewById(R.id.ivMainPhoto);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
                if (msg.what == 1) {
                    tvPunNaziv.setText(AppHeaderData.getInstance().getUserTeamName());
                    Klub klub = Klub.getInstance();
                    tvDatumOsnivanja.setText(klub.getDatumOsnivanja().toString());
                    tvMesto.setText(klub.getMesto());
                    tvAdresa.setText(klub.getAdresa());
                    tvEmail.setText(klub.getEmail());
                    tvWeb.setText(klub.getWeb());
                    tvPib.setText(klub.getPib());
                    tvMatbroj.setText(klub.getMatbroj());
                    tvTekrac.setText(klub.getTekrac());
                    pbar.setVisibility(View.GONE);
                }
            }
        };
        preuzmiKlub();


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
                startActivity(new Intent(this, LiveScoreActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preuzmiSliku(final String imgName) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(AllStatic.HTTPHOST + "/images/" + imgName);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        InputStream in = urlConnection.getInputStream();
                        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            throw new IOException(urlConnection.getResponseMessage() + ": with " + imgName);
                        }
                        int bytesRead = 0;
                        byte[] buffer = new byte[1024];
                        while ((bytesRead = in.read(buffer)) > 0) {
                            out.write(buffer, 0, bytesRead);
                        }
                        out.close();
                        bitmapBytes = out.toByteArray();
                    } finally {
                        urlConnection.disconnect();
                    }
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            ivMainPhoto.setImageBitmap(bitmap);
//                            ivMainPhoto.invalidate();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void preuzmiKlub() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Klub.getInstance().getRukovodstvo().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.GETRUKOVODSTVO, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    handler.sendEmptyMessage(1);
                    preuzmiSliku(Klub.getInstance().getFrontimage());
                } catch (IOException e) {
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }


}
