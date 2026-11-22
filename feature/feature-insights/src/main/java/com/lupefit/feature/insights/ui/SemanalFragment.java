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
import com.lupefit.feature.insights.databinding.FragmentSemanalBinding;
import com.lupefit.feature.insights.databinding.ItemResumenMacroBinding;
import com.lupefit.feature.insights.viewmodel.SemanalViewModel;

public class SemanalFragment extends Fragment {

    private FragmentSemanalBinding binding;
    private SemanalViewModel viewModel;
    private NavController navController;
    private MacroAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSemanalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new SemanalViewModel.Factory(requireContext())).get(SemanalViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setTitle(getString(R.string.resumen_semanal));
    }

    private void setupRecyclerView() {
        adapter = new MacroAdapter();
        binding.rvResumenSemanal.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvResumenSemanal.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.btnRecargar.setOnClickListener(v -> viewModel.recargar());
    }

    private void setupObservers() {
        viewModel.cargando.observe(getViewLifecycleOwner(), cargando -> {
            binding.progressBar.setVisibility(cargando != null && cargando ? View.VISIBLE : View.GONE);
            binding.btnRecargar.setEnabled(cargando == null || !cargando);
        });

        viewModel.macroResumenes.observe(getViewLifecycleOwner(), macros -> {
            if (macros != null) {
                adapter.submitList(new java.util.ArrayList<>(macros));
            }
        });
    }

    private static class MacroAdapter extends androidx.recyclerview.widget.ListAdapter<SemanalViewModel.MacroResumen, MacroAdapter.MacroViewHolder> {

        public MacroAdapter() {
            super(new androidx.recyclerview.widget.DiffUtil.ItemCallback<SemanalViewModel.MacroResumen>() {
                @Override
                public boolean areItemsTheSame(@NonNull SemanalViewModel.MacroResumen oldItem, @NonNull SemanalViewModel.MacroResumen newItem) {
                    return oldItem.nombre.equals(newItem.nombre);
                }

                @Override
                public boolean areContentsTheSame(@NonNull SemanalViewModel.MacroResumen oldItem, @NonNull SemanalViewModel.MacroResumen newItem) {
                    return oldItem.valor.equals(newItem.valor) && oldItem.progreso == newItem.progreso;
                }
            });
        }

        @NonNull
        @Override
        public MacroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemResumenMacroBinding itemBinding = ItemResumenMacroBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new MacroViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull MacroViewHolder holder, int position) {
            SemanalViewModel.MacroResumen macro = getItem(position);
            holder.bind(macro);
        }

        static class MacroViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            private final ItemResumenMacroBinding binding;

            public MacroViewHolder(@NonNull ItemResumenMacroBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(SemanalViewModel.MacroResumen macro) {
                binding.tvMacroNombre.setText(macro.nombre);
                binding.tvMacroValor.setText(macro.valor);
                binding.progressMacro.setProgress(macro.progreso);
                binding.tvMacroPorcentaje.setText(macro.porcentaje);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}