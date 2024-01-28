package com.example.myetsiit.view;

// MenuFragment.java
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myetsiit.R;

import java.util.Objects;

public class MenuFragment extends Fragment implements SensorEventListener{

    private boolean isComedor = true;

    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private Sensor accelerometer;

    private float THRESHOLD = 5.0f;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar el SensorManager y el Sensor
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];

            if (x > THRESHOLD) { // Inclinación a la derecha
                // Accionar botón izquierdo
                isComedor = true;
                redirectToNewActivity();
            } else if (x < -THRESHOLD) { // Inclinación a la izquierda
                // Accionar botón derecho
                isComedor = false;
                redirectToNewActivity();
            } else if (y > THRESHOLD) { // Inclinación hacia el usuario
                // Accionar botón de cerrar
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Puede ignorarse en este caso
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar los botones adicionales
        configureButtons(view);
    }

    private void configureButtons(View view) {
        ImageButton comedorButton = view.findViewById(R.id.Comedor);
        ImageButton localizacionButton = view.findViewById(R.id.Localizacion);

        comedorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para dirigirse a la nueva actividad o fragmento desde el botón de Comedor
                isComedor = true;
                redirectToNewActivity();
            }
        });

        comedorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibratePhone();


                // Lógica para dirigirse a ComedorLlevarActivity en una pulsación larga
                Intent intent = new Intent(getActivity(), ComedorLlevarActivity.class);
                startActivity(intent);

                return true; // Indica que el evento de pulsación larga se ha consumido
            }
        });

        localizacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para dirigirse a la nueva actividad o fragmento desde el botón de Localización
                isComedor = false;
                redirectToNewActivity();
            }
        });


    }

    private void redirectToNewActivity() {
        // Puedes iniciar una nueva actividad aquí o reemplazar el fragmento actual con el nuevo fragmento.
        // Por ejemplo, iniciar una nueva actividad:
        if (isComedor){
            Intent intent = new Intent(getActivity(), ComedorActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), LocalizationActivity.class);
            startActivity(intent);
        }
        // Eliminar el fragmento actual del contenedor
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(this).commit();

        // Opcionalmente, también puedes agregar esto para limpiar la pila de retroceso
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void vibratePhone() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
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
