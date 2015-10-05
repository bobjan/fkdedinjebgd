package com.logotet.fkdedinjebgd.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaPozicija;
import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.dedinjeadmin.model.Pozicija;
import com.logotet.fkdedinjebgd.R;

import java.util.ArrayList;

/**
 * Created by boban on 8/30/15.
 */
public class IgracAdapter extends BaseAdapter {
    Activity activity;
    ArrayList arrayList;
    private LayoutInflater inflater;

    public IgracAdapter(Activity activity) {
        this.activity = activity;
//        arrayList = BazaIgraca.getSquad();
        arrayList = BazaIgraca.getInstance().getSquad();
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
            convertView = inflater.inflate(R.layout.igrac_row, null);

        ImageView ivIgracIcon = (ImageView) convertView.findViewById(R.id.ivIgracIcon);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPozicija = (TextView) convertView.findViewById(R.id.tvPozicija);
        TextView tvVisina = (TextView) convertView.findViewById(R.id.tvVisina);
        TextView tvTezina = (TextView) convertView.findViewById(R.id.tvTezina);
        TextView tvGodiste = (TextView) convertView.findViewById(R.id.tvGodiste);

        Igrac igrac = (Igrac) getItem(position);

        String godinaRodjenja = (igrac.getGodinaRodjenja() == 0) ? "" : "(" + igrac.getGodinaRodjenja() + ")";
        int defPox = igrac.getDefaultPozicija();
        tvName.setText(igrac.getNaziv());
        tvGodiste.setText(godinaRodjenja);
        Pozicija pozicija = BazaPozicija.getInstance().getPozicija(igrac.getDefaultPozicija());
        tvPozicija.setText(pozicija.getNaziv());
        if (igrac.getVisina() != 0)
            tvVisina.setText(igrac.getVisina() + "cm");
        if (igrac.getTezina() != 0)
            tvTezina.setText(igrac.getTezina() + "kg");
        return convertView;
    }
}
