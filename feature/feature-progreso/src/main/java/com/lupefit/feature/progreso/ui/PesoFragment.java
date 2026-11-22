package com.lupefit.feature.progreso.ui;

import android.content.Context;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lupefit.core.domain.model.PesoEntry;
import com.lupefit.feature.progreso.R;
import com.lupefit.feature.progreso.databinding.FragmentPesoBinding;
import com.lupefit.feature.progreso.databinding.ItemPesoHistorialBinding;
import com.lupefit.feature.progreso.databinding.ViewEmptyPesoBinding;
import com.lupefit.feature.progreso.viewmodel.PesoViewModel;

import java.util.ArrayList;
import java.util.List;

public class PesoFragment extends Fragment {

    private FragmentPesoBinding binding;
    private PesoViewModel viewModel;
    private NavController navController;

    private PesoHistorialAdapter adapter;
    private final List<PesoEntry> historial = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPesoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new PesoViewModel.Factory(context)).get(PesoViewModel.class);

        setupToolbar();
        setupInputs();
        setupRecyclerView();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupInputs() {
        // Set today's date
        binding.etFecha.setText(new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));
    }

    private void setupRecyclerView() {
        adapter = new PesoHistorialAdapter(historial);
        binding.rvHistorialPeso.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistorialPeso.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (saved != null && saved) {
                Toast.makeText(requireContext(), "Peso guardado", Toast.LENGTH_SHORT).show();
                binding.etPeso.setText("");
                binding.etNota.setText("");
                binding.etFecha.setText(new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));
                // Refresh historial
                // TODO: Load historial from repository
            }
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
    private static class PesoHistorialAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<PesoHistorialAdapter.ViewHolder> {
        private final List<PesoEntry> items;

        PesoHistorialAdapter(List<PesoEntry> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemPesoHistorialBinding binding = ItemPesoHistorialBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PesoEntry peso = items.get(position);
            holder.binding.setPeso(peso);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final ItemPesoHistorialBinding binding;

            ViewHolder(ItemPesoHistorialBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}