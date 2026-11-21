package com.lupefit.feature.diario.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.feature.diario.R;
import com.lupefit.feature.diario.databinding.CardMacroSummaryBinding;
import com.lupefit.feature.diario.databinding.FragmentDiarioHomeBinding;
import com.lupefit.feature.diario.databinding.ViewEmptyDiarioBinding;
import com.lupefit.feature.diario.viewmodel.DiarioHomeViewModel;
import com.lupefit.core.domain.model.MacroTotals;

public class DiarioHomeFragment extends Fragment {

    private FragmentDiarioHomeBinding binding;
    private CardMacroSummaryBinding macroBinding;
    private ViewEmptyDiarioBinding emptyBinding;
    private DiarioHomeViewModel viewModel;
    private NavController navController;
    private long currentUserId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiarioHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        currentUserId = getCurrentUserId();

        // Los layouts incluidos con <layout> generan sus propios bindings
        macroBinding = binding.macroSummary;
        emptyBinding = binding.emptyState;

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new DiarioHomeViewModel.Factory(context, currentUserId)).get(DiarioHomeViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();

        viewModel.loadDiarioDelDia();
    }

    private long getCurrentUserId() {
        // TODO: Get actual logged-in user ID from UserRepository or preferences
        // For now, return a default user ID (1)
        return 1L;
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_add_food) {
                Toast.makeText(requireContext(), "Buscar alimento - próximamente", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_quick_add) {
                Toast.makeText(requireContext(), "Añadir rápido - próximamente", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_scan_barcode) {
                Toast.makeText(requireContext(), "Escanear código - próximamente", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_voice_log) {
                showDiarioVozSheet();
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        binding.recyclerDiario.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerDiario.setAdapter(new DiarioEntryAdapter(entry -> {
            // TODO: Open edit portion sheet
        }));
    }

    private void setupObservers() {
        viewModel.getDiarioEntries().observe(getViewLifecycleOwner(), entries -> {
            // Update adapter with entries
            if (binding.recyclerDiario.getAdapter() instanceof DiarioEntryAdapter) {
                ((DiarioEntryAdapter) binding.recyclerDiario.getAdapter()).setEntries(entries);
            }
            emptyBinding.getRoot().setVisibility(entries.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getTotales().observe(getViewLifecycleOwner(), totales -> {
            updateMacroSummary(totales);
        });
    }

    private void updateMacroSummary(MacroTotals totales) {
        if (totales == null) return;

        macroBinding.tvTotalCaloriesValue.setText(String.valueOf(totales.getCalorias()));

        macroBinding.tvProteinValue.setText(totales.getProteinas() + "g");
        macroBinding.tvCarbsValue.setText(totales.getCarbohidratos() + "g");
        macroBinding.tvFatValue.setText(totales.getGrasas() + "g");

        // Calculate percentages for progress bars (based on typical macro distribution)
        int totalMacroGrams = (int) (totales.getProteinas() + totales.getCarbohidratos() + totales.getGrasas());
        if (totalMacroGrams > 0) {
            macroBinding.pbProtein.setProgress((int) (totales.getProteinas() * 100 / totalMacroGrams));
            macroBinding.pbCarbs.setProgress((int) (totales.getCarbohidratos() * 100 / totalMacroGrams));
            macroBinding.pbFat.setProgress((int) (totales.getGrasas() * 100 / totalMacroGrams));
        }
    }

    private void setupClickListeners() {
        binding.fabAddFood.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Añadir alimento - próximamente", Toast.LENGTH_SHORT).show();
        });

        emptyBinding.btnFirstFood.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Añadir primer alimento - próximamente", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDiarioVozSheet() {
        DiarioVozSheet sheet = new DiarioVozSheet();
        sheet.setOnFoodsLoggedListener(items -> {
            // TODO: Handle logged foods from voice
            Toast.makeText(requireContext(), items.size() + " alimento(s) registrado(s) por voz", Toast.LENGTH_SHORT).show();
        });
        sheet.show(getChildFragmentManager(), "DiarioVozSheet");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        macroBinding = null;
        emptyBinding = null;
    }
}