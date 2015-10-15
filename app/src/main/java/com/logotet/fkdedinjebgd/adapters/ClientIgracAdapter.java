package com.logotet.fkdedinjebgd.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.fkdedinjebgd.R;

import java.util.ArrayList;

/**
 * Created by boban on 8/30/15.
 */
public class ClientIgracAdapter extends BaseAdapter {
    Activity activity;

    private LayoutInflater inflater;

    ArrayList arrayList;

    public ClientIgracAdapter(Activity activity) {
        this.activity = activity;
        arrayList = BazaIgraca.getInstance().getuProtokolu();
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
            convertView = inflater.inflate(R.layout.clientsastav_row, null);

        TextView tvIgrac = (TextView) convertView.findViewById(R.id.tvcIgracUProtokolu);
        TextView tvBrojNaDresu = (TextView) convertView.findViewById(R.id.tvcBrojNaDresu);

        int clrKlupa = parent.getResources().getColor(R.color.grey);
        int clrStarter = parent.getResources().getColor(R.color.screen_font);

        Igrac igrac = (Igrac) getItem(position);

        tvIgrac.setText(igrac.getNaziv());
        tvBrojNaDresu.setText(igrac.getBrojNaDresu() + "");

        tvIgrac.setTextColor(Color.WHITE);

        if (igrac.getBrojNaDresu() < 12) {
            tvBrojNaDresu.setTextColor(clrStarter);
            tvIgrac.setTextColor(clrStarter);
        } else {
            tvBrojNaDresu.setTextColor(clrKlupa);
            tvIgrac.setTextColor(clrKlupa);
        }
        return convertView;
    }
}
