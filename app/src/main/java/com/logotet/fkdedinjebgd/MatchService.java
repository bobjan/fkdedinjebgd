package com.logotet.fkdedinjebgd;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.logotet.dedinjeadmin.AllStatic;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.BazaStadiona;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.util.BJDatum;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MatchService extends Service {
    private static final String TAG = "MatchService";
    public static final String MY_PREFS_NAME = "DedinjePrefsFile";
   private Utakmica utakmica;

//    private final int INTERVAL = 120 * 60 * 1000; // dva sata
//    private final int INTERVAL = 120 * 60 * 1000; // dva sata
    private final int INTERVAL = 5 * 60 * 1000; // 5 minuta
    private Timer timer;


    public MatchService() {
       timer = new Timer();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.w(TAG, "service started");

        // Execute an action after period time
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                    try {
                        HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIVEMATCH, AllStatic.HTTPHOST, null);
                        catcher.catchData();

                        BazaStadiona.getInstance().getTereni().clear();
                        catcher = new HttpCatcher(RequestPreparator.GETSTADION, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                        catcher = new HttpCatcher(RequestPreparator.ALLEVENTS, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                        utakmica = Utakmica.getInstance();
                        if(utakmica != null){
//                            Log.w(TAG, " utakmica != null");
                            if(!utakmica.getDatum().lessThanToday()){
//                                Log.w(TAG, " utakmica.datum  != today");
                                // obraditi dogadjaje
                                 pokreniGoalSevice();
                                if(!utakmica.uToku() && !utakmica.isFinished()){
//                                    Log.w(TAG, " utakmica.utoku.notfinished ");
                                    if(!isAlreadyNotified())
                                            createNotification();
                                }
                            }
                        }
                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }, 0, INTERVAL);

        return super.onStartCommand(intent, flags, startId);
    }

    private void pokreniGoalSevice() {
        Intent serviceIntent = new Intent(this, GoalService.class);
        startService(serviceIntent);

    }

    private boolean isAlreadyNotified() {
//        Log.w(TAG, " isAlready .. test");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String nextMatch = utakmica.getAllInOne();
        String matchNotified = prefs.getString("nextmatch", " ");

        if(nextMatch.equals(matchNotified))
            return true;

//        Log.w(TAG, " isAlready .. to be false");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nextmatch", nextMatch);
        editor.commit();
       return false;
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
//        Log.w(TAG, "service  destroyed");
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void createNotification(){
        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.notifikacija);
        notificationBuilder.setContentTitle("Нова утакмица");
        notificationBuilder.setContentText(utakmica.getDatum().toString() + " *** " + utakmica.getHomeTeamName() + "-" + utakmica.getAwayTeamName());
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NextMatchActivity.class);

// The stack builder object will contain an artificial back stack for the started Activity.
// This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NextMatchActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        notificationManager.notify(0, notificationBuilder.build());
    }

}
