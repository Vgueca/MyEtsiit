package com.example.myetsiit.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myetsiit.R;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_TIMEOUT = 3000; // 3 segundos

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Simula algún proceso que puede llevar tiempo (por ejemplo, carga de datos)
        // En este caso, simplemente esperamos un tiempo antes de pasar a la siguiente actividad
        new Handler().postDelayed(() -> {
            // Oculta el loader
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            // Inicia la RegisterActivity
            iniciarActividadRegistro();
        }, SPLASH_TIMEOUT);
    }

    private void iniciarActividadRegistro() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
