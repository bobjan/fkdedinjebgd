package com.logotet.fkdedinjebgd.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.logotet.dedinjeadmin.model.BazaSaopstenja;

import java.util.ArrayList;

/**
 * Created by boban on 8/30/15.
 */
public class SaopstenjeAdapter extends BaseAdapter {
    Activity activity;
    ArrayList arrayList;

    public SaopstenjeAdapter(Activity activity) {
        this.activity = activity;
//        arrayList = BazaSaopstenja.getInstance().
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
        return null;
    }
}
