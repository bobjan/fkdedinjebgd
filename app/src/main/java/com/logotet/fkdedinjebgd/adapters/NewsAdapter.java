package com.logotet.fkdedinjebgd.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaPozicija;
import com.logotet.dedinjeadmin.model.BazaSaopstenja;
import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.dedinjeadmin.model.Pozicija;
import com.logotet.dedinjeadmin.model.Saopstenje;
import com.logotet.fkdedinjebgd.R;

import java.util.ArrayList;

/**
 * Created by boban on 8/30/15.
 */
public class NewsAdapter extends BaseAdapter {
    private static final String TAG = "NewsAdapter";
    Activity activity;
    ArrayList<Saopstenje> arrayList;
    private LayoutInflater inflater;

    public NewsAdapter(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<Saopstenje>();
        arrayList.addAll(BazaSaopstenja.getInstance().getVesti());
    }

    public void reload(){
        arrayList.clear();
        arrayList.addAll(BazaSaopstenja.getInstance().getVesti());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.news_row, null);

        TextView tvDatum = (TextView) convertView.findViewById(R.id.tvNewsDate);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvNewsTitle);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvNewsText);

        Saopstenje saopstenje = (Saopstenje) getItem(position);

        tvDatum.setText(saopstenje.getDatum().toString());
        tvTitle.setText(saopstenje.getNaslov());
        tvText.setText(saopstenje.getFullText());
        return convertView;
    }
}
