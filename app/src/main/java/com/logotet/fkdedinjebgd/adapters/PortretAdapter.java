package com.logotet.fkdedinjebgd.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.fkdedinjebgd.PortretFragment;


/**
 * Created by boban on 10/15/15.
 */
public class PortretAdapter extends FragmentStatePagerAdapter {
    private int vrsta;

    public PortretAdapter(int vrsta, FragmentManager fm) {
        super(fm);
        this.vrsta = vrsta;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) PortretFragment.create(vrsta, position);
    }

    @Override
    public int getCount() {
        switch (vrsta) {
            case 1:
                return BazaIgraca.getInstance().getSquad().size();
            case 2:
                return Klub.getInstance().getRukovodstvo().size();
            default:
                return 0;
        }
    }
}
