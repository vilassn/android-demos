package com.wavesignal.volumemute;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class MyForegroundService extends Service {
    public static final String TAG = "MyForegroundService";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, MainActivityService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        //do heavy work on a background thread
        new Thread(() -> {
            int counter = 0;
            while (true) {
                try {
                    Log.e(TAG, "Foreground service is running.............." + counter++);

                    // volume operations
//                    Log.e(TAG, "calling changeVolume()........direction: ADJUST_SAME");
//                    changeVolume(AudioManager.ADJUST_SAME);
//                    Thread.sleep(5000);

                    Log.e(TAG, "calling changeVolume()........direction: ADJUST_LOWER");
                    changeVolume(AudioManager.ADJUST_LOWER);
                    Thread.sleep(5000);

//                    Log.e(TAG, "calling changeVolume()........direction: ADJUST_RAISE");
//                    changeVolume(AudioManager.ADJUST_RAISE);
//                    Thread.sleep(5000);


                    // mute operations
                    Log.e(TAG, "calling changeVolume()........direction: ADJUST_MUTE");
                    changeVolume(AudioManager.ADJUST_MUTE);
                    Thread.sleep(5000);

                    int pid = getPidByPackageName("com.wavesignal.volumemute");
                    Log.e(TAG, "MyPID:........" + pid);
//
//                    Log.e(TAG, "calling changeVolume()........direction: ADJUST_UNMUTE");
//                    changeVolume(AudioManager.ADJUST_UNMUTE);
//                    Thread.sleep(5000);

//                    Log.e(TAG, "calling changeVolume()........direction: ADJUST_TOGGLE_MUTE");
//                    changeVolume(AudioManager.ADJUST_TOGGLE_MUTE);
//                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //stopSelf();
        return START_NOT_STICKY;
    }

    private void changeVolume(int direction) {
        Log.e(TAG, "in changeVolume().............. direction: " + direction);
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, AudioManager.FLAG_SHOW_UI);

        //mHandler.post(() -> Toast.makeText(getApplicationContext(), "Direction: " + direction, Toast.LENGTH_SHORT).show());
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,
                "Foreground Service Channel", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    public int getPidByPackageName(String packageName) {
        int tPid = -1;
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            String tPackageName = appProcess.processName;
            String tMainPackageName = appProcess.pkgList[0];
            Log.d(TAG, "PackageName: " + tPackageName + "  pid: " + tPid + " importance:" + appProcess.importance + " tMainPackageName:" + tMainPackageName);
            try {
                if (packageName != null && tPackageName != null) {
                    if (tPackageName.contains(packageName)) {
                        return appProcess.pid;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Error>> :" + e.toString());
            }
        }
        return tPid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
