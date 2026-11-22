package com.lupefit.app.ui.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lupefit.app.R;
import com.lupefit.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();
        setupBottomNavigation();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Load initial feature (Diario)
            navController.setGraph(R.navigation.nav_diario);
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_diario) {
                navController.setGraph(R.navigation.nav_diario);
                return true;
            } else if (itemId == R.id.nav_ejercicio) {
                navController.setGraph(R.navigation.nav_ejercicio);
                return true;
            } else if (itemId == R.id.nav_nutricion) {
                navController.setGraph(R.navigation.nav_nutricion);
                return true;
            } else if (itemId == R.id.nav_progreso) {
                navController.setGraph(R.navigation.nav_progreso);
                return true;
            } else if (itemId == R.id.nav_mas) {
                navController.setGraph(R.navigation.nav_planes);
                return true;
            }
            return false;
        });
    }
}