package com.logotet.fkdedinjebgd;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;
import com.logotet.util.BJTime;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class GoalService extends Service {
    private static final String TAG = "GoalService";
    public static final String MY_PREFS_NAME = "DedinjePrefsFile";
    private Utakmica utakmica;

    private final long INTERVAL = 15L * 1000; // 15 sekundi
    private Timer timer;

    private static final int NOSOUND = 0;
    private static final int GOALSOUND = 1;
    private static final int BOOSOUND = 2;
    private int whatsound;

    private SoundPool soundPool;
    private int booSoundID;
    private int goalSoundID;
    boolean loadedSounds;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;

    private MediaPlayer mediaPlayer;

    public GoalService() {
        timer = new Timer();
        loadedSounds = false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        audioManager = (AudioManager) this.getApplicationContext().getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
                loadedSounds = true;
            }
        });
        goalSoundID = soundPool.load(this.getApplicationContext(), R.raw.shortgoal, 1);
        booSoundID = soundPool.load(this.getApplicationContext(), R.raw.boo, 1);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIVEMATCH, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.ALLEVENTS, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    utakmica = Utakmica.getInstance();
//                    Log.w(TAG, " urtakmica fetched");
                    if (utakmica != null) {
                        if (!utakmica.getDatum().lessThanToday()) {
                            utakmica.odrediMinutazu();
                            if (utakmica.uToku() && !utakmica.isFinished()) {
                                if (!isOldResult())
                                    createNotification();
                            }
                            if (utakmica.isFinished())
                                stopSelf();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0L, INTERVAL);
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isOldResult() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String rezultat = utakmica.getCurrentRezulat();
        String oldRezultat = prefs.getString("currentscore", "0:0");
        whatsound = NOSOUND;

        if (rezultat.equals(oldRezultat))
            return true;

        try {
            StringTokenizer st = new StringTokenizer(oldRezultat, ":");
            int oldHomegoal = Integer.parseInt(st.nextToken());
            int oldAwaygoal = Integer.parseInt(st.nextToken());

            st = new StringTokenizer(rezultat, ":");
            int newHomegoal = Integer.parseInt(st.nextToken());
            int newAwaygoal = Integer.parseInt(st.nextToken());

            if (utakmica.isUserTeamDomacin()) {
                if (newHomegoal > oldHomegoal)
                    whatsound = GOALSOUND;
                else if (newAwaygoal > oldAwaygoal)
                    whatsound = BOOSOUND;
            } else {
                if (newHomegoal > oldHomegoal)
                    whatsound = BOOSOUND;
                else if (newAwaygoal > oldAwaygoal)
                    whatsound = GOALSOUND;
            }
        } catch (NumberFormatException nfe) {
        } catch (Exception e) {
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("currentscore", rezultat);
        editor.commit();
        return false;
    }

    private boolean getOldSoundPref() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean soundflag = prefs.getBoolean("soundflag", true);
        return soundflag;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("currentscore", "0:0");
        editor.commit();
        super.onDestroy();
//        Log.w(TAG, "service  destroyed");
    }


    public void createNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.notifikacija);
        notificationBuilder.setContentTitle("Гооооол!");
        notificationBuilder.setContentText(utakmica.getHomeTeamName() + "-" + utakmica.getAwayTeamName() + " " + utakmica.getCurrentRezulat());
        notificationBuilder.setAutoCancel(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, LiveScoreActivity.class);

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

        BJTime sada = new BJTime();
        BJTime jutro = new BJTime("09:00");
        BJTime vece = new BJTime("22:00");


            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);

        if (getOldSoundPref()) {
            if (sada.getSeconds() > jutro.getSeconds())
                if (sada.getSeconds() < vece.getSeconds()) {
                    if (whatsound == GOALSOUND) {
                        if(loadedSounds)
                            soundPool.play(goalSoundID,volume,volume,1,0,1f);
                    }else
                    if (whatsound == BOOSOUND) {
                        if(loadedSounds)
                            soundPool.play(booSoundID,volume,volume,1,0,1f);
                    }
                }
        }
    }

}
