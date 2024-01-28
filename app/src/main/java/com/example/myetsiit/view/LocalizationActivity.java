package com.example.myetsiit.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import com.example.myetsiit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LocalizationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLocations;
    private LocationMenuAdapter locationAdapter;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private double destinationLatitude = 37.182965053779945; // predefined latitude
    private double destinationLongitude = -3.6051149021000355; // predefined longitude

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization);

        // Obtener referencia a la barra de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewLocalizacion);

        // Configurar el listener para la selección de elementos de la barra de navegación
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Verificar el ID del elemento seleccionado
                if (item.getItemId() == R.id.navigation_home) {
                    // Si es "Inicio", cambiar a HomeFragment
                    switchToMainActivity(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_profile) {
                    // Si es "Perfil", cambiar a ProfileFragment
                    switchToMainActivity(new ProfileFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_chatbot) {
                    // Si es "Localización", cambiar a LocalizationFragment
                    switchTo(new FragmentDialogFlow());
                    return true;
                }
                return false;
            }
        });

        // Configurar el RecyclerView y el Adapter
        recyclerViewLocations = findViewById(R.id.recyclerViewLocations);
        locationAdapter = new LocationMenuAdapter(getSampleLocations(), this, new LocationMenuAdapter.OnLocationClickListener() {
            @Override
            public void onLocationClick(String location) {

                // establecemos las coordenadas de destino
                if(location.equals("Rectorado")){
                    destinationLatitude= 37.18493014764161;
                    destinationLongitude = -3.601100820034889;
                }

                if(location.equals("Servicio de Becas")){
                    destinationLatitude = 37.18708940327209;
                    destinationLongitude = -3.6041243893446135;
                }

                if(location.equals("Oficina de RRII")){
                    destinationLatitude = 37.192547979097824;
                    destinationLongitude = -3.5947931777069844;
                }

                if(location.equals("Tienda UGR (Centro)")){
                    destinationLatitude = 37.175585506831005;
                    destinationLongitude = -3.5968521777079423;
                }

                if(location.equals("CAD Cartuja")){
                    destinationLatitude = 37.19061721651888;
                    destinationLongitude = -3.598880677707074;
                }

                if(location.equals("CAD Fuentenueva")){
                    destinationLatitude = 37.18260366131891;
                    destinationLongitude = -3.609130819272596;
                }

                if (location.equals("Comedores Fuentenueva")) {
                    destinationLatitude = 37.182965053779945;
                    destinationLongitude = -3.6051149021000355;
                }

                if(location.equals("Comedores Aynadamar (ETSIIT)")){
                    destinationLatitude = 37.19692720366017;
                    destinationLongitude = -3.6242796181803074;
                }

                if(location.equals("Comedores PTS")){
                    destinationLatitude = 37.14839096441252;
                    destinationLongitude = -3.6036596740017384;
                }

                if(location.equals("Comedores Cartuja")){
                    destinationLatitude = 37.19205973957023;
                    destinationLongitude = -3.597918604689388;
                }

                // Comprueba que tenemos los permisos de ubicacion y de lo contrario los pide
                if (ContextCompat.checkSelfPermission(LocalizationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LocalizationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                } else {
                    getCurrentLocation();   // Obtener ubicacion actual y en consecuencia ruta
                }
            }
        });


        recyclerViewLocations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLocations.setAdapter(locationAdapter);
    }

    // Método para cambiar a MainActivity con un fragmento específico
    private void switchToMainActivity(Fragment fragment) {
        Intent intent = new Intent(LocalizationActivity.this, MainActivity.class);
        intent.putExtra("fragmentToLoad", fragment.getClass().getSimpleName());
        startActivity(intent);
        finish();  // Finalizar LocalizationActivity
    }

    private void switchTo(FragmentActivity fragment) {
        Intent intent = new Intent(LocalizationActivity.this, MainActivity.class);
        intent.putExtra("fragmentToLoad", fragment.getClass().getSimpleName());
        startActivity(intent);
        finish();  // Finalizar LocalizationActivity
    }

    // Método de ejemplo para obtener una lista de ubicaciones de muestra
    private List<String> getSampleLocations() {
        List<String> locations = new ArrayList<>();
        locations.add("Rectorado");
        locations.add("Servicio de Becas");
        locations.add("Oficina de RRII");
        locations.add("Tienda UGR (Centro)");
        locations.add("CAD Cartuja");
        locations.add("CAD Fuentenueva");
        locations.add("Comedores Fuentenueva");
        locations.add("Comedores PTS");
        locations.add("Comedores Cartuja");
        locations.add("Comedores Aynadamar (ETSIIT)");
        // Agregar más ubicaciones según sea necesario
        return locations;
    }

    // Como se actua ante peticion de permiso de ubicacion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                getCurrentLocation();
            } else {
                // Permission denied
                // Handle the denial of permission
            }
        }
    }

    // Funcion para obtener la ubicacion actual
    // Function to obtain the current location
    protected void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("TAG-BOT", "Localizacion2");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Use the location object to get latitude, longitude, etc.
                        navigateToLocation(location.getLatitude(), location.getLongitude());
                    } else {
                        // Handle the situation where location data is not available
                    }
                }
            });
        }
    }

    // Method to navigate to a predefined location using the current location
    protected void navigateToLocation(double currentLatitude, double currentLongitude) {

        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="
                + currentLatitude + "," + currentLongitude
                + "&destination=" + destinationLatitude + "," + destinationLongitude
                + "&travelmode=driving");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        Log.d("TAG-BOT", "Localizacion3");

        try {
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Log.d("MapIntent", "No activity found to handle map intent");
            }
        } catch (Exception e) {
            Log.e("MapIntentError", "Error starting map activity", e);
        }
    }

}
