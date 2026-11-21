package com.lupefit.feature.nutricion.ui;

import android.content.Context;
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

import com.lupefit.core.domain.model.MacroTotals;
import com.lupefit.feature.nutricion.R;
import com.lupefit.feature.nutricion.databinding.FragmentMacrosDetalleBinding;
import com.lupefit.feature.nutricion.databinding.ItemMacroDetailBinding;
import com.lupefit.feature.nutricion.databinding.ItemMicroDetailBinding;
import com.lupefit.feature.nutricion.model.MacroDetail;
import com.lupefit.feature.nutricion.model.MicroDetail;
import com.lupefit.feature.nutricion.viewmodel.MacrosDetalleViewModel;

import java.util.ArrayList;
import java.util.List;

public class MacrosDetalleFragment extends Fragment {

    private FragmentMacrosDetalleBinding binding;
    private MacrosDetalleViewModel viewModel;
    private NavController navController;

    private MacroDetailAdapter macroAdapter;
    private MicroDetailAdapter microAdapter;

    private final List<MacroDetail> macroDetails = new ArrayList<>();
    private final List<MicroDetail> microDetails = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMacrosDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new MacrosDetalleViewModel.Factory(context)).get(MacrosDetalleViewModel.class);

        setupToolbar();
        setupRecyclerViews();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupRecyclerViews() {
        macroAdapter = new MacroDetailAdapter(macroDetails);
        microAdapter = new MicroDetailAdapter(microDetails);

        binding.rvMacroDetail.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMacroDetail.setAdapter(macroAdapter);

        binding.rvMicroDetail.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMicroDetail.setAdapter(microAdapter);
    }

    private void setupObservers() {
        viewModel.macroDetails.observe(getViewLifecycleOwner(), details -> {
            macroDetails.clear();
            if (details != null) macroDetails.addAll(details);
            macroAdapter.notifyDataSetChanged();
        });

        viewModel.microDetails.observe(getViewLifecycleOwner(), details -> {
            microDetails.clear();
            if (details != null) microDetails.addAll(details);
            microAdapter.notifyDataSetChanged();
        });

        viewModel.netCarbsMode.observe(getViewLifecycleOwner(), netCarbs -> {
            binding.llNetCarbsInfo.setVisibility(netCarbs ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ==================== ADAPTERS ====================
    private static class MacroDetailAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<MacroDetailAdapter.ViewHolder> {
        private final List<MacroDetail> items;

        MacroDetailAdapter(List<MacroDetail> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMacroDetailBinding binding = ItemMacroDetailBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MacroDetail macro = items.get(position);
            holder.binding.setMacro(macro);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final ItemMacroDetailBinding binding;

            ViewHolder(ItemMacroDetailBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private static class MicroDetailAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<MicroDetailAdapter.ViewHolder> {
        private final List<MicroDetail> items;

        MicroDetailAdapter(List<MicroDetail> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMicroDetailBinding binding = ItemMicroDetailBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MicroDetail micro = items.get(position);
            holder.binding.setMicro(micro);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final ItemMicroDetailBinding binding;

            ViewHolder(ItemMicroDetailBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}