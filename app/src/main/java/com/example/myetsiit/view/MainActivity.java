package com.example.myetsiit.view;

import android.app.ActivityOptions;
import android.content.Intent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myetsiit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ImageButton mainButton;
    private boolean isBlurEnabled = false;
    private boolean isHome = true;
    private Bitmap blurredBitmap;  // Declarar aquí

    private View rootView;
    private Drawable originalBackground;
    private BottomNavigationView bottomNavigationView;

    private ActivityOptions animacion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");

        if (fragmentToLoad != null) {
            // Cargar el fragmento correspondiente en el contenedor principal
            Fragment fragment;
            if (fragmentToLoad.equals(ProfileFragment.class.getSimpleName())) {
                fragment = new ProfileFragment();
                loadFragment(fragment);
            }else if(fragmentToLoad.equals(HomeFragment.class.getSimpleName())){
                fragment = new HomeFragment();  // Cargar HomeFragment por defecto
                loadFragment(fragment);
            }else{
                FragmentActivity fragment_activity = new FragmentDialogFlow();
                loadFragment_Activity(fragment_activity.getClass());
            }

        } else {
            // Si no hay fragmento especificado, cargar el fragmento por defecto
            //loadFragment(new HomeFragment());
        }

        rootView = getWindow().getDecorView().getRootView();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mainButton = findViewById(R.id.mainButton);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibratePhone();

                if (isBlurEnabled) {
                    // Desactivar el efecto blur
                    isBlurEnabled = false;
                    mainButton.setImageResource(R.drawable.botoninicio);
                    mainButton.setScaleX(1f); // Puedes ajustar este valor según tus necesidades
                    mainButton.setScaleY(1f); // Puedes ajustar este valor según tus necesidades

                    // Restaurar la vista raíz a su forma originala
                    rootView.setBackground(originalBackground);

                    // Restaurar el fragmento inicial
                    if (isHome) {
                        loadFragment(new HomeFragment());
                    } else {
                        loadFragment(new ProfileFragment());
                    }
                } else {
                    // Activar el efecto blur
                    isBlurEnabled = true;
                    mainButton.setImageResource(R.drawable.botoncerrargris);
                    // Reducir el tamaño del botón cerrar gris
                    mainButton.setScaleX(0.6f); // Puedes ajustar este valor según tus necesidades
                    mainButton.setScaleY(0.6f); // Puedes ajustar este valor según tus necesidades


                    // Crear el efecto blur
                    // 1. Capturar el contenido de la vista raíz
                    View rootView = getWindow().getDecorView().getRootView();
                    rootView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
                    rootView.setDrawingCacheEnabled(false);

                    // 2. Crear el efecto blur
                    RenderScript renderScript = RenderScript.create(getApplicationContext());
                    Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation output = Allocation.createTyped(renderScript, input.getType());
                    ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
                    scriptIntrinsicBlur.setRadius(25f);
                    scriptIntrinsicBlur.setInput(input);
                    scriptIntrinsicBlur.forEach(output);
                    output.copyTo(bitmap);

                    // 3. Establecer el efecto blur en la vista raíz
                    blurredBitmap = bitmap;
                    originalBackground = rootView.getBackground(); // Guardar el fondo original
                    rootView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

                    // 4. Cargar el fragmento de menú
                    loadFragment(new MenuFragment());
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // habilitamos solo la navegación cuando el blur está desactivado
                if (!isBlurEnabled) {
                    if (item.getItemId() == R.id.navigation_home) {
                        loadFragment(new HomeFragment());
                        isHome = true;
                        return true;
                    } else if (item.getItemId() == R.id.navigation_profile) {
                        loadFragment(new ProfileFragment());
                        isHome = false;
                        return true;
                    } else if(item.getItemId() == R.id.navigation_chatbot) {
                        Intent chat = new Intent(getApplicationContext(), FragmentDialogFlow.class);
                        animacion = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.fade_in, R.anim.fade_out);
                        startActivity(chat, animacion.toBundle());
                    }
                }
                return false;
            }
        });

        // Establecer el fragmento inicial al cargar la actividad
        loadFragment(new HomeFragment());

    }

    public boolean getBlurEnability() {
        return isBlurEnabled;
    }

    private void loadFragment(Fragment fragment) {
        // Cargar el fragmento en el contenedor principal
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);  // Agregar transacción a la pila para que el botón de retroceso funcione
        fragmentTransaction.commit();
    }
    private void loadFragment_Activity(Class<? extends FragmentActivity> activityClass) {
        // Iniciar la actividad
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }


    private void vibratePhone() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For newer versions of Android
                vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated in API 26
                vibrator.vibrate(150);
            }
        }
    }


}
