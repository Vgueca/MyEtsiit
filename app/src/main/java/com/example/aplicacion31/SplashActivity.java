package com.example.aplicacion31;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        int SPLASH_DISPLAY_LENGTH = 3000; // Duration in milliseconds (3000ms = 3s)

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Comprueba si ya hemos mostrado la intro
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                if (settings.getBoolean("my_first_time", true)) {
                    // Si es la primera vez, lanza la actividad de introducción
                    Intent introIntent = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(introIntent);
                } else {
                    // Si no es la primera vez, lanza la actividad principal
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
