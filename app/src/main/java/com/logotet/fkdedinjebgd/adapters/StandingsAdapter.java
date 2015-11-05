package com.logotet.fkdedinjebgd.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.Tabela;
import com.logotet.dedinjeadmin.model.TabelaRow;
import com.logotet.fkdedinjebgd.R;

import java.util.ArrayList;

/**
 * Created by boban on 9/16/15.
 */
public class StandingsAdapter extends BaseAdapter {
    Activity activity;
    private LayoutInflater inflater;
    ArrayList<TabelaRow> arrayList;

    public StandingsAdapter(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<TabelaRow>();
        arrayList.addAll(Tabela.getInstance().getPlasman());
    }

    public void reload(){
        arrayList.clear();
        arrayList.addAll(Tabela.getInstance().getPlasman());
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
            convertView = inflater.inflate(R.layout.standings_row, null);

        int baseclr = parent.getResources().getColor(R.color.baseclr);
        int defaultclr = parent.getResources().getColor(R.color.screen_background);

        TextView tvMesto = (TextView) convertView.findViewById(R.id.tvMestoNaTabeli);
        TextView tvTeamName = (TextView) convertView.findViewById(R.id.tvTeamName);
        TextView tvPlayed = (TextView) convertView.findViewById(R.id.tvPlayed);
        TextView tvWin = (TextView) convertView.findViewById(R.id.tvWin);
        TextView tvDraw = (TextView) convertView.findViewById(R.id.tvDraw);
        TextView tvLose = (TextView) convertView.findViewById(R.id.tvLose);
        TextView tvGoalDiff = (TextView) convertView.findViewById(R.id.tvGoalDiff);
        TextView tvPoints = (TextView) convertView.findViewById(R.id.tvPoints);

        TabelaRow linija = (TabelaRow) getItem(position);

        tvMesto.setText(linija.getBroj() + ".");
        tvTeamName.setText(linija.getNaziv());

        tvPlayed.setText(linija.getPlayed() + "");
        tvWin.setText(linija.getWin() + "");
        tvDraw.setText(linija.getDraw() + "");
        tvLose.setText(linija.getLose() + "");
        tvGoalDiff.setText(linija.getGoaldif());
        tvPoints.setText(linija.getPoints() + "");

        if (linija.isUserTeam())
            convertView.setBackgroundColor(baseclr);
        else
            convertView.setBackgroundColor(defaultclr);
        return convertView;
    }
}
