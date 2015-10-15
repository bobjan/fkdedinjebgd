package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.logotet.dedinjeadmin.AllStatic;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaPozicija;
import com.logotet.dedinjeadmin.model.BazaStadiona;
import com.logotet.dedinjeadmin.model.Fixtures;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.model.Tabela;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button btnHome;
    Button btnManagement;
    Button btnSquad;
    Button btnFixtures;
    Button btnStandings;
    Button btnLivescore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHome = (Button) findViewById(R.id.btnHomeAction);
        btnManagement = (Button) findViewById(R.id.btnManagement);
        btnSquad = (Button) findViewById(R.id.btnSquad);
        btnFixtures = (Button) findViewById(R.id.btnFixtures);
        btnStandings = (Button) findViewById(R.id.btnStandings);
        btnLivescore = (Button) findViewById(R.id.btnLivescore);

        preuzmiRaspored();
        preuzmiTabelu();
        preuzmiRukovodstvo();
        preuzmiEkipu();
        preuzmiNextMatch();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
                startActivity(intent);
            }
        });
        btnSquad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SquadActivity.class);
                startActivity(intent);
            }
        });
        btnFixtures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FixturesActivity.class);
                startActivity(intent);
            }
        });
        btnStandings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StandingsActivity.class);
                startActivity(intent);
            }
        });
        btnLivescore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LiveScoreActivity.class);
                startActivity(intent);
            }
        });

        Intent serviceIntent = new Intent(this, MatchService.class);
        startService(serviceIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // @todo ucitavanje
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            case R.id.action_management:
                startActivity(new Intent(this, ManagementActivity.class));
                return true;
            case R.id.action_fixtures:
                startActivity(new Intent(this, FixturesActivity.class));
                return true;
            case R.id.action_squad:
                startActivity(new Intent(this, SquadActivity.class));
                return true;
            case R.id.action_standings:
                startActivity(new Intent(this, StandingsActivity.class));
                return true;
            case R.id.action_livescore:
                startActivity(new Intent(this, LiveScoreActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preuzmiRaspored() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Fixtures.getInstance().getRaspored().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETFIXTURES, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void preuzmiNextMatch() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIVEMATCH, AllStatic.HTTPHOST, null);
                    catcher.catchData();

                    BazaStadiona.getInstance().getTereni().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETSTADION, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.SERVERTIME, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.ALLEVENTS, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void preuzmiTabelu() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    Tabela.getInstance().getPlasman().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETTABELA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void preuzmiRukovodstvo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Klub.getInstance().getRukovodstvo().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETRUKOVODSTVO, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void preuzmiEkipu() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BazaPozicija.getInstance().getTimposition().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETPOZICIJA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    BazaIgraca.getInstance().getSquad().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETEKIPA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
