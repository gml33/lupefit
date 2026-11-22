package com.lupefit.feature.insights.ui;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.core.ui.R;
import com.lupefit.feature.insights.databinding.FragmentPatronesBinding;
import com.lupefit.feature.insights.databinding.ItemPatronAlimentarioBinding;
import com.lupefit.feature.insights.viewmodel.PatronesViewModel;

public class PatronesFragment extends Fragment {

    private FragmentPatronesBinding binding;
    private PatronesViewModel viewModel;
    private NavController navController;
    private PatronesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPatronesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new PatronesViewModel.Factory(requireContext())).get(PatronesViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setTitle(getString(R.string.patrones));
    }

    private void setupRecyclerView() {
        adapter = new PatronesAdapter();
        binding.rvPatrones.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPatrones.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.btnRecargar.setOnClickListener(v -> viewModel.recargar());
    }

    private void setupObservers() {
        viewModel.cargando.observe(getViewLifecycleOwner(), cargando -> {
            binding.progressBar.setVisibility(cargando != null && cargando ? View.VISIBLE : View.GONE);
            binding.btnRecargar.setEnabled(cargando == null || !cargando);
        });

        viewModel.patrones.observe(getViewLifecycleOwner(), patrones -> {
            if (patrones != null) {
                adapter.submitList(new java.util.ArrayList<>(patrones));
            }
        });

        viewModel.patronPrincipal.observe(getViewLifecycleOwner(), principal -> {
            if (principal != null) {
                binding.tvPatronPrincipal.setText(principal);
            }
        });

        viewModel.scoreConsistencia.observe(getViewLifecycleOwner(), score -> {
            if (score != null) {
                binding.progressConsistencia.setProgress(score);
                binding.tvScoreConsistencia.setText(score + "%");
            }
        });
    }

    private static class PatronesAdapter extends androidx.recyclerview.widget.ListAdapter<PatronesViewModel.PatronAlimentario, PatronesAdapter.PatronViewHolder> {

        public PatronesAdapter() {
            super(new androidx.recyclerview.widget.DiffUtil.ItemCallback<PatronesViewModel.PatronAlimentario>() {
                @Override
                public boolean areItemsTheSame(@NonNull PatronesViewModel.PatronAlimentario oldItem, @NonNull PatronesViewModel.PatronAlimentario newItem) {
                    return oldItem.nombre.equals(newItem.nombre);
                }

                @Override
                public boolean areContentsTheSame(@NonNull PatronesViewModel.PatronAlimentario oldItem, @NonNull PatronesViewModel.PatronAlimentario newItem) {
                    return oldItem.frecuencia == newItem.frecuencia && oldItem.esPositivo == newItem.esPositivo;
                }
            });
        }

        @NonNull
        @Override
        public PatronViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemPatronAlimentarioBinding itemBinding = ItemPatronAlimentarioBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new PatronViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull PatronViewHolder holder, int position) {
            PatronesViewModel.PatronAlimentario patron = getItem(position);
            holder.bind(patron);
        }

        static class PatronViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            private final ItemPatronAlimentarioBinding binding;

            public PatronViewHolder(@NonNull ItemPatronAlimentarioBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(PatronesViewModel.PatronAlimentario patron) {
                binding.tvNombrePatron.setText(patron.nombre);
                binding.tvFrecuencia.setText(patron.frecuencia + "%");
                binding.tvDescripcionPatron.setText(patron.descripcion);
                binding.progressFrecuencia.setProgress(patron.frecuencia);

                int bgColor = patron.esPositivo
                        ? binding.getRoot().getContext().getColor(R.color.status_success)
                        : binding.getRoot().getContext().getColor(R.color.status_error);
                binding.cardPatron.setCardBackgroundColor(bgColor);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}