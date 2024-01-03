package com.example.aplicacion31;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.aplicacion31.ui.home.HomeFragment;
import com.example.aplicacion31.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.aplicacion31.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.Activity;
import java.util.Arrays;
import java.util.List;
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
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Initialize BlurredFragment
        blurredFragment = new BlurredFragment();

        // Setup toggle button
        setupToggleButton();

        // Initialize ViewPager and TabLayout
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Create lists of news titles and image resources
        List<String> newsTitles = Arrays.asList("Noticia 1", "Noticia 2");
        List<Integer> newsImageResources = Arrays.asList(R.drawable.botoninicio, R.drawable.noticia_horarioverano);

        // Initialize NewsPagerAdapter with the lists of news titles and image resources
        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), newsTitles, newsImageResources);

        // Set the adapter to the ViewPager
        viewPager.setAdapter(newsPagerAdapter);

        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            boolean showNews = true;

            if(item.getItemId() == R.id.navigation_home){
                selectedFragment = new HomeFragment();
            }else if (item.getItemId() == R.id.navigation_profile){
                selectedFragment = new ProfileFragment();
                showNews = false; // No mostrar las noticias en el perfil
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, selectedFragment).commit();
            }

            // Mostrar u ocultar las noticias
            viewPager.setVisibility(showNews ? View.VISIBLE : View.GONE);
            tabLayout.setVisibility(showNews ? View.VISIBLE : View.GONE); // Ocultar también el TabLayout

            return true;
        });
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

