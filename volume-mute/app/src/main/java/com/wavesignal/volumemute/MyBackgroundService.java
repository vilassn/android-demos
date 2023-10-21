package com.wavesignal.volumemute;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyBackgroundService extends Service {
    public static final String TAG = "MyBackgroundService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()..............");

        new Thread(() -> {
            int counter = 0;
            while (true) {
                try {
                    Log.e(TAG, "Background service is running.............." + counter++);

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

        return super.onStartCommand(intent, flags, startId);
    }

    private void changeVolume(int direction) {
        Log.e(TAG, "in changeVolume().............. direction: " + direction);
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, AudioManager.FLAG_PLAY_SOUND);

        //mHandler.post(() -> Toast.makeText(getApplicationContext(), "Direction: " + direction, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()..............");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()..............");
        return null;
    }
}
