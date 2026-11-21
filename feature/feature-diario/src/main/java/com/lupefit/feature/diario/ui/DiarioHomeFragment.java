package com.lupefit.feature.diario.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.core.ui.R;
import com.lupefit.feature.diario.databinding.FragmentDiarioHomeBinding;
import com.lupefit.feature.diario.viewmodel.DiarioHomeViewModel;

public class DiarioHomeFragment extends Fragment {

    private FragmentDiarioHomeBinding binding;
    private DiarioHomeViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiarioHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(DiarioHomeViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();

        viewModel.loadDiarioDelDia();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_add_food) {
                navController.navigate(DiarioHomeFragmentDirections.actionDiarioHomeFragmentToBuscarAlimentoFragment());
                return true;
            } else if (itemId == R.id.action_quick_add) {
                navController.navigate(DiarioHomeFragmentDirections.actionDiarioHomeFragmentToQuickAddFragment());
                return true;
            } else if (itemId == R.id.action_scan_barcode) {
                navController.navigate(DiarioHomeFragmentDirections.actionDiarioHomeFragmentToEscanearCodigoBarrasFragment());
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
            binding.emptyState.setVisibility(entries.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getTotales().observe(getViewLifecycleOwner(), totales -> {
            binding.macroSummary.setData(totales);
        });
    }

    private void setupClickListeners() {
        binding.fabAddFood.setOnClickListener(v -> navController.navigate(DiarioHomeFragmentDirections.actionDiarioHomeFragmentToBuscarAlimentoFragment()));
    }

    private void showDiarioVozSheet() {
        DiarioVozSheet sheet = new DiarioVozSheet();
        sheet.show(getChildFragmentManager(), "DiarioVozSheet");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}