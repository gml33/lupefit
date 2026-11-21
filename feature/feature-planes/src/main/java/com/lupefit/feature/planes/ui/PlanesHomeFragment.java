package com.lupefit.feature.planes.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lupefit.core.ui.R;
import com.lupefit.feature.planes.databinding.FragmentPlanesHomeBinding;
import com.lupefit.feature.planes.viewmodel.PlanesHomeViewModel;

public class PlanesHomeFragment extends Fragment {

    private FragmentPlanesHomeBinding binding;
    private PlanesHomeViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanesHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(PlanesHomeViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setTitle("@string/planes_title");
    }

    private void setupClickListeners() {
        // TODO: Add click listeners for cards/buttons
    }

    private void setupObservers() {
        // TODO: Observe ViewModel LiveData
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
