package com.lupefit.feature.insights.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lupefit.feature.insights.R;
import com.lupefit.feature.insights.databinding.FragmentInsightsHomeBinding;

public class InsightsHomeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return FragmentInsightsHomeBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentInsightsHomeBinding binding = FragmentInsightsHomeBinding.bind(view);
        NavController navigation = NavHostFragment.findNavController(this);
        binding.toolbar.setNavigationOnClickListener(v -> navigation.navigateUp());
        binding.weekly.setOnClickListener(v -> navigation.navigate(R.id.action_insightsHomeFragment_to_semanalFragment));
        binding.trends.setOnClickListener(v -> navigation.navigate(R.id.action_insightsHomeFragment_to_tendenciasFragment));
        binding.predictions.setOnClickListener(v -> navigation.navigate(R.id.action_insightsHomeFragment_to_prediccionesFragment));
        binding.patterns.setOnClickListener(v -> navigation.navigate(R.id.action_insightsHomeFragment_to_patronesFragment));
    }
}
