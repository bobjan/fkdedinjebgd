package com.logotet.fkdedinjebgd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.BaseAdapter;


import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.dedinjeadmin.model.Osoba;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by boban on 10/11/15.
 */
public class ImageLoader extends Thread {
    Igrac igrac;
    Osoba osoba;
    Handler handler;
    byte[] bitmapBytes;
    boolean forIgrac;

    public ImageLoader(Igrac igrac, Handler hdl) {
        this.igrac = igrac;
        this.handler = hdl;
        forIgrac = true;
    }

    public ImageLoader(Osoba osoba, Handler hdl) {
        this.osoba = osoba;
        this.handler = hdl;
        forIgrac = false;
    }

    @Override
    public void run() {
        try {
            URL url;
            if (forIgrac)
                url = new URL(AllStatic.HTTPHOST + "/images/" + igrac.getImageFileName());
            else
                url = new URL(AllStatic.HTTPHOST + "/images/" + osoba.getImageFileName());

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = urlConnection.getInputStream();
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if (forIgrac)
                        throw new IOException(urlConnection.getResponseMessage() + ": with " + igrac.getImageFileName());
                    else
                        throw new IOException(urlConnection.getResponseMessage() + ": with " + osoba.getImageFileName());
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
            if (forIgrac) {
                    igrac.setImage(bitmap);
                    igrac.setImageLoaded(true);
            } else {
                    osoba.setImage(bitmap);
                    osoba.setImageLoaded(true);
            }

            handler.sendEmptyMessage(2);
        } catch (IOException e) {
            if (forIgrac) {
                    igrac.setImage(null);
                    igrac.setImageLoaded(false);
            } else {
                    osoba.setImage(null);
                    osoba.setImageLoaded(true);
            }
        }
    }
}
