package com.lupefit.feature.agua.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.core.ui.R;
import com.lupefit.core.domain.model.WaterEntry;
import com.lupefit.feature.agua.databinding.FragmentHistorialAguaBinding;
import com.lupefit.feature.agua.databinding.ItemHistorialAguaBinding;
import com.lupefit.feature.agua.databinding.ViewEmptyHistorialAguaBinding;
import com.lupefit.feature.agua.viewmodel.HistorialAguaViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistorialAguaFragment extends Fragment {

    private FragmentHistorialAguaBinding binding;
    private HistorialAguaViewModel viewModel;

    private HistorialAguaAdapter adapter;
    private final List<WaterEntry> historialList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistorialAguaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new HistorialAguaViewModel.Factory(requireContext())).get(HistorialAguaViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new HistorialAguaAdapter(historialList);
        binding.rvHistorialAgua.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistorialAgua.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.historial.observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                historialList.clear();
                historialList.addAll(entries);
                adapter.notifyDataSetChanged();

                boolean isEmpty = entries.isEmpty();
                binding.rvHistorialAgua.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                binding.emptyState.getRoot().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
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
    private static class HistorialAguaAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<HistorialAguaAdapter.ViewHolder> {
        private final List<WaterEntry> items;

        HistorialAguaAdapter(List<WaterEntry> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemHistorialAguaBinding binding = ItemHistorialAguaBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            WaterEntry entry = items.get(position);
            holder.binding.setRegistro(entry);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final ItemHistorialAguaBinding binding;

            ViewHolder(ItemHistorialAguaBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}