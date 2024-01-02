package com.example.aplicacion31;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.aplicacion31.databinding.ActivityMainBinding;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.Activity;





public class MainActivity extends AppCompatActivity implements BlurredFragment.OnFragmentInteractionListener {

    private ActivityMainBinding binding;
    private BlurredFragment blurredFragment;
    private boolean isBlurredLayoutVisible = false;

    private ImageButton buttonToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Initialize BlurredFragment
        blurredFragment = new BlurredFragment();

        // Setup toggle button
        setupToggleButton();

    }

    private void setupToggleButton() {
        buttonToggle = findViewById(R.id.buttonToggle);
        buttonToggle.setOnClickListener(v -> toggleBlurredLayout());
    }

    private void toggleBlurredLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isBlurredLayoutVisible) {
            transaction.remove(blurredFragment);
            buttonToggle.setVisibility(View.VISIBLE); // Show the button again
            isBlurredLayoutVisible = false;
        } else {
            transaction.add(R.id.container, blurredFragment); // Replace 'container' with your layout ID
            buttonToggle.setVisibility(View.GONE); // Hide the button
            isBlurredLayoutVisible = true;
        }
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction() {
        toggleBlurredLayout();
    }
}

