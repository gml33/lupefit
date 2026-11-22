package com.lupefit.feature.escaneo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.lupefit.core.ui.R;
import com.lupefit.feature.escaneo.databinding.FragmentEscanearHomeBinding;
import com.lupefit.feature.escaneo.databinding.ViewEmptyEscaneoBinding;
import com.lupefit.feature.escaneo.databinding.ViewEmptyHistorialEscaneosBinding;
import com.lupefit.feature.escaneo.viewmodel.EscanearHomeViewModel;

import com.lupefit.core.domain.model.EscaneoEntry;
import com.lupefit.feature.escaneo.databinding.ItemHistorialEscaneoBinding;

public class EscanearHomeFragment extends Fragment {

    private FragmentEscanearHomeBinding binding;
    private EscanearHomeViewModel viewModel;
    private NavController navController;

    private HistorialEscaneosAdapter adapter;
    private final java.util.List<EscaneoEntry> historialList = new java.util.ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEscanearHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new EscanearHomeViewModel.Factory(requireContext())).get(EscanearHomeViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupRecyclerView();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        // Card: Escanear Código de Barras
        binding.cardEscanearCodigoBarras.setOnClickListener(v ->
                navController.navigate(EscanearHomeFragmentDirections.Companion.actionEscanearHomeFragmentToEscanearCodigoBarrasFragment()));

        // Card: Escanear Comida IA
        binding.cardEscanearComida.setOnClickListener(v ->
                navController.navigate(EscanearHomeFragmentDirections.Companion.actionEscanearHomeFragmentToEscanearComidaFragment()));

        // Card: Historial
        binding.cardHistorialEscaneos.setOnClickListener(v ->
                navController.navigate(EscanearHomeFragmentDirections.Companion.actionEscanearHomeFragmentToHistorialEscaneosFragment()));

        // Card: Buscar Manual
        binding.cardBuscarManual.setOnClickListener(v -> {
            // Navigate to buscar alimento in feature-diario
            // TODO: Implement cross-feature navigation
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show();
        });

        // Empty state buttons
        binding.emptyState.btnFirstScan.setOnClickListener(v ->
                navController.navigate(EscanearHomeFragmentDirections.Companion.actionEscanearHomeFragmentToEscanearCodigoBarrasFragment()));

        binding.emptyHistory.btnFirstHistoryScan.setOnClickListener(v ->
                navController.navigate(EscanearHomeFragmentDirections.Companion.actionEscanearHomeFragmentToEscanearCodigoBarrasFragment()));
    }

    private void setupRecyclerView() {
        adapter = new HistorialEscaneosAdapter(historialList);
        binding.rvHistorialEscaneos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistorialEscaneos.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.historial.observe(getViewLifecycleOwner(), escaneos -> {
            if (escaneos != null) {
                historialList.clear();
                historialList.addAll(escaneos);
                adapter.notifyDataSetChanged();

                // Show/hide empty states
                boolean isEmpty = escaneos.isEmpty();
                binding.contentContainer.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                binding.emptyState.getRoot().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                binding.emptyHistory.getRoot().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.loading.observe(getViewLifecycleOwner(), loading -> {
            // TODO: Show loading indicator
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ==================== ADAPTER ====================
    private static class HistorialEscaneosAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<HistorialEscaneosAdapter.ViewHolder> {
        private final java.util.List<EscaneoEntry> items;

        HistorialEscaneosAdapter(java.util.List<EscaneoEntry> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemHistorialEscaneoBinding binding = ItemHistorialEscaneoBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EscaneoEntry escaneo = items.get(position);
            holder.binding.setEscaneo(escaneo);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final ItemHistorialEscaneoBinding binding;

            ViewHolder(ItemHistorialEscaneoBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}