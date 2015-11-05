package com.logotet.fkdedinjebgd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.BazaSaopstenja;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.fkdedinjebgd.adapters.NewsAdapter;

import java.io.IOException;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = "NewsActivity";
    private NewsAdapter newsAdapter;
    Handler handler;

    ListView lvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        lvNews = (ListView) findViewById(R.id.lvNews);
        newsAdapter = new NewsAdapter(this);
        lvNews.setAdapter(newsAdapter);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
                if (msg.what == 1) {
                    newsAdapter.reload();
                    newsAdapter.notifyDataSetChanged();
                }
            }
        };
        preuzmiVesti();
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
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            case R.id.action_management:
                startActivity(new Intent(this, ManagementActivity.class));
                return true;
            case R.id.action_fixtures:
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void preuzmiVesti() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher;
                    BazaSaopstenja.getInstance().getVesti().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETSAOPSTENJA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    BazaSaopstenja.getInstance().sortiraj();
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }
}
