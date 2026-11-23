package com.lupefit.feature.nutricion.ui;

import android.content.Context;
import android.net.Uri;
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

import com.lupefit.core.domain.model.MacroTotals;
import com.lupefit.core.ui.databinding.CardMacroSummaryBinding;
import com.lupefit.feature.nutricion.R;
import com.lupefit.feature.nutricion.databinding.FragmentNutricionHomeBinding;
import com.lupefit.feature.nutricion.databinding.ViewEmptyNutricionBinding;
import com.lupefit.feature.nutricion.viewmodel.NutricionHomeViewModel;

public class NutricionHomeFragment extends Fragment {

    private FragmentNutricionHomeBinding binding;
    private CardMacroSummaryBinding macroBinding;
    private ViewEmptyNutricionBinding emptyBinding;
    private NutricionHomeViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNutricionHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new NutricionHomeViewModel.Factory(context)).get(NutricionHomeViewModel.class);

        // Los layouts incluidos con <layout> generan sus propios bindings
        macroBinding = binding.macroSummary;
        emptyBinding = binding.emptyState;

        setupToolbar();
        setupClickListeners();
        setupObservers();

        viewModel.refresh();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        binding.cardMetas.setOnClickListener(v -> navController.navigate(R.id.action_nutricionHomeFragment_to_metasFragment));
        binding.btnVerMetas.setOnClickListener(v -> navController.navigate(R.id.action_nutricionHomeFragment_to_metasFragment));
        binding.btnVerDetalle.setOnClickListener(v -> navController.navigate(R.id.action_nutricionHomeFragment_to_macrosDetalleFragment));
        binding.btnVerGraficos.setOnClickListener(v -> navController.navigate(R.id.graficosNutricionFragment));
        binding.btnAgregarAlimento.setOnClickListener(v ->
                navController.navigate(Uri.parse("lupefit://diario/buscar-alimento")));
        emptyBinding.btnIrDiario.setOnClickListener(v ->
                navController.navigate(Uri.parse("lupefit://diario")));
    }

    private void setupObservers() {
        viewModel.totales.observe(getViewLifecycleOwner(), this::updateMacroSummary);

        viewModel.hasData.observe(getViewLifecycleOwner(), hasData -> {
            binding.contentContainer.setVisibility(hasData ? View.VISIBLE : View.GONE);
            emptyBinding.getRoot().setVisibility(hasData ? View.GONE : View.VISIBLE);
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), loading -> {
            // Show/hide loading
        });
    }

    private void updateMacroSummary(MacroTotals totales) {
        if (totales == null) return;

        macroBinding.tvTotalCaloriesValue.setText(String.valueOf(totales.getCalorias()));
        macroBinding.tvProteinValue.setText(totales.getProteinas() + "g");
        macroBinding.tvCarbsValue.setText(totales.getCarbohidratos() + "g");
        macroBinding.tvFatValue.setText(totales.getGrasas() + "g");

        int totalMacroGrams = (int) (totales.getProteinas() + totales.getCarbohidratos() + totales.getGrasas());
        if (totalMacroGrams > 0) {
            macroBinding.pbProtein.setProgress((int) (totales.getProteinas() * 100 / totalMacroGrams));
            macroBinding.pbCarbs.setProgress((int) (totales.getCarbohidratos() * 100 / totalMacroGrams));
            macroBinding.pbFat.setProgress((int) (totales.getGrasas() * 100 / totalMacroGrams));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        macroBinding = null;
        emptyBinding = null;
    }
}