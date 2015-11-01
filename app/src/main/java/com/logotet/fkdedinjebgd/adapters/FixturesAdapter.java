package com.logotet.fkdedinjebgd.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.AppHeaderData;
import com.logotet.dedinjeadmin.model.Fixtures;
import com.logotet.dedinjeadmin.model.FixturesRow;
import com.logotet.fkdedinjebgd.R;

import java.util.ArrayList;

/**
 * Created by boban on 9/16/15.
 */
public class FixturesAdapter extends BaseAdapter {
    Activity activity;
    private LayoutInflater inflater;
    ArrayList<FixturesRow> arrayList;


    public FixturesAdapter(Activity activity, ArrayList<FixturesRow> lista) {
        this.activity = activity;
        arrayList = lista;
//        arrayList = Fixtures.getInstance().getRaspored();
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
            convertView = inflater.inflate(R.layout.fixtures_row, null);

        TextView tvDatum = (TextView) convertView.findViewById(R.id.tvDatum);
        TextView tvTeamHome = (TextView) convertView.findViewById(R.id.tvTeamHome);
        TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore);
        TextView tvTeamAway = (TextView) convertView.findViewById(R.id.tvTeamAway);

        int baseclr = parent.getResources().getColor(R.color.baseclr);
        int defaultclr = parent.getResources().getColor(R.color.screen_background);

        FixturesRow fixtureRow = (FixturesRow) getItem(position);

        tvTeamHome.setBackgroundColor(defaultclr);
        tvTeamAway.setBackgroundColor(defaultclr);

        tvDatum.setText(fixtureRow.getDatum().toString());
        if (fixtureRow.isDomacin()) {
            if (fixtureRow.isPlayed()) {
                String tmp = fixtureRow.getWescored() + ":" + fixtureRow.getTheyscored();
                tvScore.setText(tmp);
            } else {
                tvScore.setText("");
            }
            tvTeamHome.setText(AppHeaderData.getInstance().getUserTeamName());
            tvTeamAway.setText(fixtureRow.getProtivnik());
            tvTeamHome.setBackgroundColor(baseclr);
        } else {
            if (fixtureRow.isPlayed()) {
                String tmp = fixtureRow.getTheyscored() + ":" + fixtureRow.getWescored();
                tvScore.setText(tmp);
            } else {
                tvScore.setText("");
            }
            tvTeamHome.setText(fixtureRow.getProtivnik());
            tvTeamAway.setText(AppHeaderData.getInstance().getUserTeamName());
            tvTeamAway.setBackgroundColor(baseclr);
        }
        return convertView;
    }
}
