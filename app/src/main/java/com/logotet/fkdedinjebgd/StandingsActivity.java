package com.logotet.fkdedinjebgd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.logotet.dedinjeadmin.model.AppHeaderData;
import com.logotet.dedinjeadmin.model.Tabela;
import com.logotet.fkdedinjebgd.adapters.StandingsAdapter;

public class StandingsActivity extends AppCompatActivity {
    private static final String TAG = "StandingsActivity";
    TextView tvNazivLige;
    TextView tvOdigranoKolo;

    ListView lvStandings;
    private StandingsAdapter standingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        lvStandings = (ListView) findViewById(R.id.lvStandings);
        standingsAdapter = new StandingsAdapter(this);
        lvStandings.setAdapter(standingsAdapter);

        tvNazivLige = (TextView) findViewById(R.id.tvNazivLige);
        tvOdigranoKolo = (TextView) findViewById(R.id.tvOdigranoKolo);
        lvStandings = (ListView) findViewById(R.id.lvStandings);

        tvNazivLige.setText(AppHeaderData.getInstance().getNazivLige());
        tvOdigranoKolo.setText("после " + Tabela.getInstance().getLastRound() + ". кола");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_standings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
