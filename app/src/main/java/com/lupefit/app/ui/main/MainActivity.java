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

            // Configure AppBarConfiguration with top-level destinations
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.diarioNavHostFragment,
                    R.id.ejercicioNavHostFragment,
                    R.id.nutricionNavHostFragment,
                    R.id.progresoNavHostFragment,
                    R.id.masNavHostFragment
            ).build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_diario) {
                navController.navigate(R.id.action_global_diario);
                return true;
            } else if (itemId == R.id.nav_ejercicio) {
                navController.navigate(R.id.action_global_ejercicio);
                return true;
            } else if (itemId == R.id.nav_nutricion) {
                navController.navigate(R.id.action_global_nutricion);
                return true;
            } else if (itemId == R.id.nav_progreso) {
                navController.navigate(R.id.action_global_progreso);
                return true;
            } else if (itemId == R.id.nav_mas) {
                // Show bottom sheet with more options
                showMasBottomSheet();
                return true;
            }
            return false;
        });
    }

    private void showMasBottomSheet() {
        MasBottomSheet sheet = new MasBottomSheet();
        sheet.show(getSupportFragmentManager(), "MasBottomSheet");
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}