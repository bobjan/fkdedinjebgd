package com.logotet.fkdedinjebgd;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaPozicija;
import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.model.Osoba;

import java.util.ArrayList;

public class PortretFragment extends Fragment {
    private static final String TAG = "PortretFragment";

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


    public static final String ARG_PAGE = "page";
    private int mPageNumber;


    public static PortretFragment create(int pageNumber) {
        PortretFragment fragment = new PortretFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PortretFragment() {
        vrsta = 1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        currentidx = mPageNumber;

//        Bundle extras = getIntent().getExtras();
//        if(extras != null){
//            vrsta = extras.getInt("vrsta");
//            currentidx = extras.getInt("currentidx");
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_portret, container, false);

        ivPortret = (ImageView) rootView.findViewById(R.id.ivPortret);
        tvFullName = (TextView) rootView.findViewById(R.id.tvFullName);
        tvBirthYear = (TextView) rootView.findViewById(R.id.tvBirthYear);
        tvDefaultPozicija = (TextView) rootView.findViewById(R.id.tvDefaultPozicija);
        tvHeight = (TextView) rootView.findViewById(R.id.tvHeight);
        tvWeight = (TextView) rootView.findViewById(R.id.tvWeight);
        tvNapomena = (TextView) rootView.findViewById(R.id.tvNapomena);

        ivPortret.setImageBitmap(null);
        tvFullName.setText("");
        tvBirthYear.setText("");
        tvDefaultPozicija.setText("");
        tvHeight.setText("");
        tvWeight.setText("");
        tvNapomena.setText("");

        doSomething();
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber()
    {
        currentidx = mPageNumber;
        return mPageNumber;
    }


    private void doSomething() {
        switch (vrsta) {
            case 1:         // igraci
                arrayList = BazaIgraca.getInstance().getSquad();
                Igrac igrac = (Igrac) arrayList.get(currentidx);
                ivPortret.setImageBitmap((Bitmap) igrac.getImage());
                tvNapomena.setText(igrac.getNapomena());
                tvFullName.setText(igrac.getNaziv());
                tvBirthYear.setText(igrac.getGodinaRodjenja() + ".");
                if (igrac.getVisina() != 0)
                    tvHeight.setText(igrac.getVisina() + "cm");
                if (igrac.getTezina() != 0)
                    tvWeight.setText(igrac.getTezina() + "kg");
                tvDefaultPozicija.setText(BazaPozicija.getInstance().getPozicija(igrac.getDefaultPozicija()).getNaziv());

                break;
            case 2:     //management
                arrayList = Klub.getInstance().getRukovodstvo();
                Osoba osoba = (Osoba) arrayList.get(currentidx);
                ivPortret.setImageBitmap((Bitmap) osoba.getImage());
                tvDefaultPozicija.setText(osoba.getFunkcija());
                tvNapomena.setText(osoba.getNapomena());
                tvFullName.setText(osoba.getNaziv());
                break;
            default:
                arrayList = new ArrayList();
                break;
        }
    }

}
