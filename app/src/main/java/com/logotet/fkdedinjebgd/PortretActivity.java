package com.logotet.fkdedinjebgd;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaPozicija;
import com.logotet.dedinjeadmin.model.Igrac;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.model.Osoba;

import java.util.ArrayList;

public class PortretActivity extends AppCompatActivity {
    private static final String TAG = "PortretActivity";

    ImageView ivPortret;
    TextView tvFullName;
    TextView tvBirthYear;
    TextView tvDefaultPozicija;
    TextView tvHeight;
    TextView tvWeight;
    TextView tvNapomena;


    ArrayList arrayList;
    int vrrsta;
    int currentidx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portret);



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            vrrsta = extras.getInt("vrsta");
//            arrayList = extras.getParcelableArrayList("niz");
            currentidx = extras.getInt("currentidx");
        }



        ivPortret = (ImageView) findViewById(R.id.ivPortret);
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvBirthYear = (TextView) findViewById(R.id.tvBirthYear);
        tvDefaultPozicija = (TextView) findViewById(R.id.tvDefaultPozicija);
        tvHeight = (TextView) findViewById(R.id.tvHeight);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvNapomena = (TextView) findViewById(R.id.tvNapomena);

        ivPortret.setImageBitmap(null);
        tvFullName.setText("");
        tvBirthYear.setText("");
        tvDefaultPozicija.setText("");
        tvHeight.setText("");
        tvWeight.setText("");
        tvNapomena.setText("");

        switch (vrrsta){
            case 1:         // igraci
                arrayList = BazaIgraca.getInstance().getSquad();
                Igrac igrac = (Igrac) arrayList.get(currentidx);
                ivPortret.setImageBitmap((Bitmap) igrac.getImage());
                tvNapomena.setText(igrac.getNapomena());
                tvFullName.setText(igrac.getNaziv());
                tvBirthYear.setText(igrac.getGodinaRodjenja() + ".");
                if(igrac.getVisina() != 0)
                    tvHeight.setText(igrac.getVisina() + "cm");
                if(igrac.getTezina() != 0)
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
