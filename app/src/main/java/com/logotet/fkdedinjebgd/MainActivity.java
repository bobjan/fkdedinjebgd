package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String MY_PREFS_NAME = "DedinjePrefsFile";

    Button btnHome;
    Button btnManagement;
    Button btnSquad;
    Button btnFixtures;
    Button btnStandings;
    Button btnLivescore;

    Switch switchSound;
    boolean soundflag;

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

        switchSound = (Switch) findViewById(R.id.swSound);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                switchSound.setShowText(true);
        } else{
            // do something for phones running an SDK before lollipop
        }

        switchSound.setChecked(getOldSoundPref());

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

        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundflag = isChecked;
                setNewSoundPref(soundflag);

            }
        });

        Intent serviceIntent = new Intent(this, MatchService.class);
        startService(serviceIntent);
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
            case R.id.action_news:
                startActivity(new Intent(this, NewsActivity.class));
                return true;       }
        return super.onOptionsItemSelected(item);
    }

    private boolean getOldSoundPref() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        soundflag = prefs.getBoolean("soundflag", true);
        return soundflag;
    }

    private void setNewSoundPref(boolean flag) {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("soundflag", flag);
        editor.commit();
    }
}
