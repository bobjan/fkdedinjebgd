package com.logotet.fkdedinjebgd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.AllStatic;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.AppHeaderData;
import com.logotet.dedinjeadmin.model.Klub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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


       ivMainPhoto = (ImageView) findViewById(R.id.ivMainPhoto);

        handler = new Handler();

        preuzmiSliku("frontimage.png");

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

    private void preuzmiSliku(final String imgName){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(AllStatic.HTTPHOST + "/images/" + imgName);
                    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        InputStream in = urlConnection.getInputStream();
                        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            throw new IOException(urlConnection.getResponseMessage() +  ": with " + imgName);
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
}
