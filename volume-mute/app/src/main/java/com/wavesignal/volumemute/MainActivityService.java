package com.wavesignal.volumemute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivityService extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    Button btnStartFgService, btnStopFgService;
    Button btnStartBgService, btnStopBgService;

    boolean fgRunning = false;
    boolean bgRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_service);

        btnStartFgService = findViewById(R.id.btnStartFgService);
        btnStopFgService = findViewById(R.id.btnStopFgService);
        btnStartFgService.setOnClickListener(v -> startFgService());
        btnStopFgService.setOnClickListener(v -> stopFgService());

        btnStartBgService = findViewById(R.id.btnStartBgService);
        btnStopBgService = findViewById(R.id.btnStopBgService);
        btnStartBgService.setOnClickListener(v -> startBgService());
        btnStopBgService.setOnClickListener(v -> stopBgService());
    }

    public void startFgService() {
        if (!fgRunning) {
            Intent serviceIntent = new Intent(this, MyForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            ContextCompat.startForegroundService(this, serviceIntent);
            fgRunning = true;
        } else {
            Log.d(TAG, "Foreground service is already running");
        }
    }

    public void stopFgService() {
        if (fgRunning) {
            Intent serviceIntent = new Intent(this, MyForegroundService.class);
            stopService(serviceIntent);
            fgRunning = false;
        } else {
            Log.d(TAG, "Foreground service is not running");
        }
    }

    public void startBgService() {
        if (!bgRunning) {
            Intent serviceIntent = new Intent(this, MyBackgroundService.class);
            serviceIntent.putExtra("inputExtra", "Background Service Example in Android");
            startService(serviceIntent);
            bgRunning = true;
        } else {
            Log.d(TAG, "Background service is already running");
        }
    }

    public void stopBgService() {
        if (bgRunning) {
            Intent serviceIntent = new Intent(this, MyBackgroundService.class);
            stopService(serviceIntent);
            bgRunning = false;
        } else {
            Log.d(TAG, "Background service is not running");
        }
    }
}