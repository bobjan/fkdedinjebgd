package com.logotet.fkdedinjebgd;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.fkdedinjebgd.adapters.PortretAdapter;

import java.util.ArrayList;

public class PortretActivity extends FragmentActivity {
    private static final String TAG = "PortretActivity";

    ImageView ivPortret;
    TextView tvFullName;
    TextView tvBirthYear;
    TextView tvDefaultPozicija;
    TextView tvHeight;
    TextView tvWeight;
    TextView tvNapomena;

    ArrayList arrayList;
    int vrsta;
    int currentidx;

    private static final int NUM_PAGES = 5;
    private ViewPager vpPortretViewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portret);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vrsta = extras.getInt("vrsta");
            currentidx = extras.getInt("currentidx");
        }


        // Instantiate a ViewPager and a PagerAdapter.
        vpPortretViewPager = (ViewPager) findViewById(R.id.vpPortretViewPager);

        pagerAdapter = new PortretAdapter(vrsta, getSupportFragmentManager());
        vpPortretViewPager.setAdapter(pagerAdapter);


        vpPortretViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });


//        ivPortret = (ImageView) findViewById(R.id.ivPortret);
//        tvFullName = (TextView) findViewById(R.id.tvFullName);
//        tvBirthYear = (TextView) findViewById(R.id.tvBirthYear);
//        tvDefaultPozicija = (TextView) findViewById(R.id.tvDefaultPozicija);
//        tvHeight = (TextView) findViewById(R.id.tvHeight);
//        tvWeight = (TextView) findViewById(R.id.tvWeight);
//        tvNapomena = (TextView) findViewById(R.id.tvNapomena);
//
//        ivPortret.setImageBitmap(null);
//        tvFullName.setText("");
//        tvBirthYear.setText("");
//        tvDefaultPozicija.setText("");
//        tvHeight.setText("");
//        tvWeight.setText("");
//        tvNapomena.setText("");
//
//        switch (vrsta){
//            case 1:         // igraci
//                arrayList = BazaIgraca.getInstance().getSquad();
//                Igrac igrac = (Igrac) arrayList.get(currentidx);
//                ivPortret.setImageBitmap((Bitmap) igrac.getImage());
//                tvNapomena.setText(igrac.getNapomena());
//                tvFullName.setText(igrac.getNaziv());
//                tvBirthYear.setText(igrac.getGodinaRodjenja() + ".");
//                if(igrac.getVisina() != 0)
//                    tvHeight.setText(igrac.getVisina() + "cm");
//                if(igrac.getTezina() != 0)
//                    tvWeight.setText(igrac.getTezina() + "kg");
//                tvDefaultPozicija.setText(BazaPozicija.getInstance().getPozicija(igrac.getDefaultPozicija()).getNaziv());
//
//            break;
//            case 2:     //management
//                arrayList = Klub.getInstance().getRukovodstvo();
//                Osoba osoba = (Osoba) arrayList.get(currentidx);
//                ivPortret.setImageBitmap((Bitmap) osoba.getImage());
//                tvDefaultPozicija.setText(osoba.getFunkcija());
//                tvNapomena.setText(osoba.getNapomena());
//                tvFullName.setText(osoba.getNaziv());
//                break;
//            default:
//                arrayList = new ArrayList();
//            break;
//
//        }

    }
}
