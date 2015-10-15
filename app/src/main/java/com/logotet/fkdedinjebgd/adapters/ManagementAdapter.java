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

import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.model.Osoba;
import com.logotet.fkdedinjebgd.R;

import java.util.ArrayList;

/**
 * Created by boban on 9/16/15.
 */
public class ManagementAdapter extends BaseAdapter {
    private static final String TAG = "ManagementAdapter";
    Activity activity;
    private LayoutInflater inflater;
    ArrayList arrayList;

    public ManagementAdapter(Activity activity) {
        this.activity = activity;
        arrayList = Klub.getInstance().getRukovodstvo();
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
            convertView = inflater.inflate(R.layout.manager_row, null);

        ImageView ivOsobaIcon = (ImageView) convertView.findViewById(R.id.ivManagerIcon);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvManagerName);
        TextView tvFunkcija = (TextView) convertView.findViewById(R.id.tvFunkcija);

        Osoba osoba = (Osoba) getItem(position);

        Drawable myDrawable = activity.getResources().getDrawable(R.drawable.personicon);
        Bitmap myUserIcon = ((BitmapDrawable) myDrawable).getBitmap();
        ivOsobaIcon.setImageBitmap(myUserIcon);
        if (osoba.isImageLoaded()) {
            try {
                ivOsobaIcon.setImageBitmap((Bitmap) osoba.getImage());
            } catch (ClassCastException cce) {
//                Log.w(TAG, " nesto ne VALJA!!!");
            }
        }
        tvName.setText(osoba.getNaziv());
        tvFunkcija.setText(osoba.getFunkcija());
        return convertView;
    }
}
