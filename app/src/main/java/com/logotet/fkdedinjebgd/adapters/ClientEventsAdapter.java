package com.logotet.fkdedinjebgd.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.Dogadjaj;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.fkdedinjebgd.R;

import java.util.ArrayList;

/**
 * Created by boban on 8/30/15.
 */
public class ClientEventsAdapter extends BaseAdapter {
    private static final String TAG = "ClientEventsAdapter";

    Activity activity;

    private LayoutInflater inflater;

    private boolean userTeamHome;


    Drawable football;
    Drawable penaltygoal;
    Drawable swap;
    Drawable yellowCard;
    Drawable redCard;
    Drawable secondYellow;
    Drawable missedPenalty;


    ArrayList<Dogadjaj> arrayList;

    public ClientEventsAdapter(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<Dogadjaj>();
        arrayList.addAll(Utakmica.getInstance().getTokZaPrikaz());

        userTeamHome = Utakmica.getInstance().isUserTeamDomacin();

        football = activity.getResources().getDrawable(R.drawable.football30x30);
        swap = activity.getResources().getDrawable(R.drawable.swap30x30);
        yellowCard = activity.getResources().getDrawable(R.drawable.yellow30x30);
        secondYellow = activity.getResources().getDrawable(R.drawable.secondyellow30x30);
        redCard = activity.getResources().getDrawable(R.drawable.red30x30);
        penaltygoal = activity.getResources().getDrawable(R.drawable.penalty30x30);
        missedPenalty = activity.getResources().getDrawable(R.drawable.missedpenalty30x30);
    }


    public void reload() {
        arrayList.clear();
        arrayList.addAll(Utakmica.getInstance().getTokZaPrikaz());
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
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        int resourceType = 0;
        Dogadjaj dogadjaj = (Dogadjaj) getItem(position);

        if (userTeamHome)
            if (dogadjaj.isForDedinje() || dogadjaj.isForProtivnik())
                resourceType = 1;
        if (!userTeamHome)
            if (dogadjaj.isForDedinje() || dogadjaj.isForProtivnik())
                resourceType = 2;
        if (dogadjaj.isIzmena())
            resourceType = 3;
        if (dogadjaj.isKomentar())
            resourceType = 4;
        return resourceType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Dogadjaj dogadjaj = (Dogadjaj) getItem(position);

        if (convertView == null) {
            switch (getItemViewType(position)) {
                case 1:
                    convertView = inflater.inflate(R.layout.homeevent_row, null);
                    break;
                case 2:
                    convertView = inflater.inflate(R.layout.awayevent_row, null);
                    break;
                case 3:
                    convertView = inflater.inflate(R.layout.swapevent_row, null);
                    break;
                case 4:
                    convertView = inflater.inflate(R.layout.komentar_row, null);
                    break;
                default:
                    convertView = inflater.inflate(R.layout.komentar_row, null);
                    break;

            }
        }
        TextView tvhMinut;
        TextView tvhPlayer;
        TextView tvhScore;
        ImageView ivhFirstIcon;
        ImageView ivhSecondIcon;

        TextView tvaMinut;
        TextView tvaPlayer;
        TextView tvaScore;
        ImageView ivaFirstIcon;
        ImageView ivaSecondIcon;

        TextView tvsMinut;
        ImageView ivsFirstIcon;
        TextView tvPlayerIn;
        TextView tvPlayerOut;
        TextView tvkMinut;
        TextView tvKomentar;

        switch (getItemViewType(position)) {
            case 1:// homeevent
                tvhMinut = (TextView) convertView.findViewById(R.id.tvhEventMinut);
                tvhPlayer = (TextView) convertView.findViewById(R.id.tvhPlayer);
                tvhScore = (TextView) convertView.findViewById(R.id.tvhScore);
                ivhFirstIcon = (ImageView) convertView.findViewById(R.id.ivhFirstIcon);
                ivhSecondIcon = (ImageView) convertView.findViewById(R.id.ivhSecondIcon);
                ivhFirstIcon.setImageDrawable(null);
                ivhSecondIcon.setImageDrawable(null);
                tvhPlayer.setText("");
                tvhScore.setText("");

                if (dogadjaj.getMinutIgre() >= 0)
                    tvhMinut.setText(dogadjaj.getMinutIgre() + "'");
                else
                    tvhMinut.setText(dogadjaj.getBJTime().toString().substring(0, 5));

                if (dogadjaj.isIgracki())
                    tvhPlayer.setText(dogadjaj.getPlayerName());
                if (dogadjaj.isForDedinje()) {
                    if (dogadjaj.isGoal())
                        if (dogadjaj.isPenaltyGoal())
                            ivhFirstIcon.setImageDrawable(penaltygoal);
                        else
                            ivhFirstIcon.setImageDrawable(football);

                    if (dogadjaj.isZutiKarton())
                        ivhFirstIcon.setImageDrawable(yellowCard);
                    if (dogadjaj.isDrugiZuti())
                        ivhFirstIcon.setImageDrawable(secondYellow);
                    if (dogadjaj.isCrveniKarton())
                        ivhFirstIcon.setImageDrawable(redCard);
                    if (dogadjaj.isMissedPenalty())
                        ivhFirstIcon.setImageDrawable(missedPenalty);
                } else {
                    if (dogadjaj.isGoal())
                        if (dogadjaj.isPenaltyGoal())
                            ivhSecondIcon.setImageDrawable(penaltygoal);
                        else
                            ivhSecondIcon.setImageDrawable(football);
                    if (dogadjaj.isZutiKarton())
                        ivhSecondIcon.setImageDrawable(yellowCard);
                    if (dogadjaj.isDrugiZuti())
                        ivhSecondIcon.setImageDrawable(secondYellow);
                    if (dogadjaj.isCrveniKarton())
                        ivhSecondIcon.setImageDrawable(redCard);
                    if (dogadjaj.isMissedPenalty())
                        ivhSecondIcon.setImageDrawable(missedPenalty);
                }
                if (dogadjaj.isGoal()) {
                    int[] rez = dogadjaj.getRezultat();
                    tvhScore.setText(rez[0] + " : " + rez[1]);
                }
                break;
            case 2:
                tvaMinut = (TextView) convertView.findViewById(R.id.tvaEventMinut);
                tvaPlayer = (TextView) convertView.findViewById(R.id.tvaPlayer);
                tvaScore = (TextView) convertView.findViewById(R.id.tvaScore);
                ivaFirstIcon = (ImageView) convertView.findViewById(R.id.ivaFirstIcon);
                ivaSecondIcon = (ImageView) convertView.findViewById(R.id.ivaSecondIcon);
                ivaFirstIcon.setImageDrawable(null);
                ivaSecondIcon.setImageDrawable(null);
                tvaPlayer.setText("");
                tvaScore.setText("");

                if (dogadjaj.getMinutIgre() >= 0)
                    tvaMinut.setText(dogadjaj.getMinutIgre() + "'");
                else
                    tvaMinut.setText(dogadjaj.getBJTime().toString().substring(0, 5));

                ivaFirstIcon.setImageDrawable(null);
                ivaSecondIcon.setImageDrawable(null);
                tvaScore.setText("");

                if (dogadjaj.isIgracki())
                    tvaPlayer.setText(dogadjaj.getPlayerName());
                if (dogadjaj.isForDedinje()) {
                    if (dogadjaj.isGoal())
                        if (dogadjaj.isPenaltyGoal())
                            ivaFirstIcon.setImageDrawable(penaltygoal);
                        else
                            ivaFirstIcon.setImageDrawable(football);
                    if (dogadjaj.isZutiKarton())
                        ivaFirstIcon.setImageDrawable(yellowCard);
                    if (dogadjaj.isDrugiZuti())
                        ivaFirstIcon.setImageDrawable(secondYellow);
                    if (dogadjaj.isCrveniKarton())
                        ivaFirstIcon.setImageDrawable(redCard);
                    if (dogadjaj.isMissedPenalty())
                        ivaFirstIcon.setImageDrawable(missedPenalty);
                } else {
                    if (dogadjaj.isGoal())
                        if (dogadjaj.isPenaltyGoal())
                            ivaSecondIcon.setImageDrawable(penaltygoal);
                        else
                            ivaSecondIcon.setImageDrawable(football);
                    if (dogadjaj.isZutiKarton())
                        ivaSecondIcon.setImageDrawable(yellowCard);
                    if (dogadjaj.isDrugiZuti())
                        ivaSecondIcon.setImageDrawable(secondYellow);
                    if (dogadjaj.isCrveniKarton())
                        ivaSecondIcon.setImageDrawable(redCard);
                    if (dogadjaj.isMissedPenalty())
                        ivaSecondIcon.setImageDrawable(missedPenalty);
                }
                if (dogadjaj.isGoal()) {
                    int[] rez = dogadjaj.getRezultat();
                    tvaScore.setText(rez[1] + " : " + rez[0]);
                }
                break;
            case 3:
                tvsMinut = (TextView) convertView.findViewById(R.id.tvEventMinutSwap);
                ivsFirstIcon = (ImageView) convertView.findViewById(R.id.ivFirstIconSwap);
                tvPlayerIn = (TextView) convertView.findViewById(R.id.tvPlayerIn);
                tvPlayerOut = (TextView) convertView.findViewById(R.id.tvPlayerOut);

                if (dogadjaj.getMinutIgre() >= 0)
                    tvsMinut.setText(dogadjaj.getMinutIgre() + "'");
                else
                    tvsMinut.setText(dogadjaj.getBJTime().toString().substring(0, 5));
                tvPlayerIn.setText(dogadjaj.getPlayerInName());
                tvPlayerOut.setText(dogadjaj.getPlayerOutName());
                break;
            case 4:
                tvkMinut = (TextView) convertView.findViewById(R.id.tvKomentarMinut);
                tvKomentar = (TextView) convertView.findViewById(R.id.tvKomentar);

                if (dogadjaj.getMinutIgre() >= 0)
                    tvkMinut.setText(dogadjaj.getMinutIgre() + "'");
                else
                    tvkMinut.setText(dogadjaj.getBJTime().toString().substring(0, 5));
                tvKomentar.setText(dogadjaj.getKomentar());
                break;
            default:
                tvkMinut = (TextView) convertView.findViewById(R.id.tvKomentarMinut);
                tvKomentar = (TextView) convertView.findViewById(R.id.tvKomentar);
                tvkMinut.setText(" *");
                tvKomentar.setText("      ****   ");
                break;
        }
        return convertView;
    }
}
