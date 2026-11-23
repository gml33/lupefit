package com.lupefit.app.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.lupefit.app.R;
import com.lupefit.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        NavController navController = host.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        int startDestination = NavGraph.Companion.findStartDestination(navController.getGraph()).getId();
        // Switching tabs discards the current stack, including screens opened through Más.
        NavOptions tabOptions = new NavOptions.Builder()
                .setPopUpTo(startDestination, false)
                .setLaunchSingleTop(true)
                .build();
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_mas) {
                navController.navigate(R.id.masDialog);
                return false;
            }
            navController.navigate(item.getItemId(), null, tabOptions);
            return true;
        });
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            for (int graphId : MasBottomSheet.DESTINATIONS) {
                if (destination.getParent() != null && destination.getParent().getId() == graphId) {
                    binding.bottomNavigation.getMenu().findItem(R.id.nav_mas).setChecked(true);
                    break;
                }
            }
        });
        binding.bottomNavigation.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.nav_mas) {
                navController.navigate(R.id.masDialog);
            }
        });
    }
}
