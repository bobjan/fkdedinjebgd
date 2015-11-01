package com.logotet.fkdedinjebgd;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.AppHeaderData;
import com.logotet.dedinjeadmin.model.BazaStadiona;
import com.logotet.dedinjeadmin.model.BazaTimova;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MatchService extends Service {
    private static final String TAG = "MatchService";
    public static final String MY_PREFS_NAME = "DedinjePrefsFile";
    private Utakmica utakmica;

    private final long INTERVAL = 180L * 60 * 1000; // 3 sata
    private Timer timer;

    public MatchService() {
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIVEMATCH, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    BazaTimova.getInstance().getProtivnici().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                    catcher.catchData();

                    BazaStadiona.getInstance().getTereni().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETSTADION, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.ALLEVENTS, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    utakmica = Utakmica.getInstance();
                    if (utakmica != null) {
                        utakmica.odrediMinutazu();
                        if (!utakmica.uToku() && !utakmica.isFinished()) {
                            if (!isAlreadyNotified())
                                createNotification();
                        }
                        if (utakmica.getDatum().isToday()) {
                            pokreniGoalSevice();
                        }
                    }
                } catch (FileNotFoundException fnfe) {
                } catch (IOException e) {
                }
            }
        }, 0L, INTERVAL);

        return super.onStartCommand(intent, flags, startId);
    }

    private void pokreniGoalSevice() {
        Intent serviceIntent = new Intent(this, GoalService.class);
        startService(serviceIntent);
    }

    private boolean isAlreadyNotified() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String nextMatch = utakmica.getAllInOne();
        String matchNotified = prefs.getString("nextmatch", " ");

        if (nextMatch.equals(matchNotified))
            return true;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nextmatch", nextMatch);
        editor.putString("currentscore", "0:0");
        editor.commit();
        return false;
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nextmatch", " ");

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void createNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.notifikacija);
        notificationBuilder.setContentTitle("Нова утакмица");
        notificationBuilder.setContentText(utakmica.getDatum().toString() + " *** " + utakmica.getHomeTeamName() + "-" + utakmica.getAwayTeamName());
        notificationBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(this, NextMatchActivity.class);

// The stack builder object will contain an artificial back stack for the started Activity.
// This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NextMatchActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        notificationManager.notify(0, notificationBuilder.build());


        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);

    }
}
