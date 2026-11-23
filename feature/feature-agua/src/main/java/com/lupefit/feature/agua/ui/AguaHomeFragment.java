package com.lupefit.feature.agua.ui;

import android.os.Bundle;
import android.text.InputType;
import android.widget.FrameLayout;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lupefit.feature.agua.R;
import com.lupefit.core.domain.model.WaterEntry;
import com.lupefit.feature.agua.databinding.FragmentAguaHomeBinding;
import com.lupefit.feature.agua.databinding.ViewEmptyAguaBinding;
import com.lupefit.feature.agua.databinding.ViewEmptyHistorialAguaBinding;
import com.lupefit.feature.agua.databinding.ItemHistorialAguaBinding;
import com.lupefit.feature.agua.viewmodel.AguaHomeViewModel;
import com.lupefit.feature.agua.ui.AguaHomeFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class AguaHomeFragment extends Fragment {

    private FragmentAguaHomeBinding binding;
    private AguaHomeViewModel viewModel;
    private NavController navController;

    private HistorialAguaAdapter adapter;
    private final List<WaterEntry> historialList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAguaHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new AguaHomeViewModel.Factory(requireContext())).get(AguaHomeViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupRecyclerView();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        // Quick add buttons
        binding.btnAdd100.setOnClickListener(v -> viewModel.addQuickWater(100));
        binding.btnAdd250.setOnClickListener(v -> viewModel.addQuickWater(250));
        binding.btnAdd500.setOnClickListener(v -> viewModel.addQuickWater(500));
        binding.btnAddCustom.setOnClickListener(v -> showAmountDialog());

        // FAB
        binding.fabAddWater.setOnClickListener(v -> viewModel.addQuickWater(250));

        // Grid cards
        binding.cardAgregarVaso.setOnClickListener(v -> viewModel.addQuickWater(250));
        binding.cardAgregarBotella.setOnClickListener(v -> viewModel.addQuickWater(500));
        binding.cardConfigurarMeta.setOnClickListener(v ->
                navController.navigate(AguaHomeFragmentDirections.Companion.actionAguaHomeFragmentToConfigurarMetaFragment()));
        binding.cardVerHistorial.setOnClickListener(v ->
                navController.navigate(AguaHomeFragmentDirections.Companion.actionAguaHomeFragmentToHistorialAguaFragment()));

        // Historial
        binding.btnVerTodoHistorial.setOnClickListener(v ->
                navController.navigate(AguaHomeFragmentDirections.Companion.actionAguaHomeFragmentToHistorialAguaFragment()));

        // Empty state
        binding.emptyState.btnFirstWater.setOnClickListener(v -> viewModel.addQuickWater(250));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadTodayData();
    }

    private void showAmountDialog() {
        TextInputLayout field = new TextInputLayout(requireContext());
        field.setHint(getString(R.string.water_amount_ml));
        TextInputEditText input = new TextInputEditText(field.getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        field.addView(input);
        int padding = (int) (24 * getResources().getDisplayMetrics().density + 0.5f);
        FrameLayout content = new FrameLayout(requireContext());
        content.setPadding(padding, 0, padding, 0);
        content.addView(field, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.water_add_custom)
                .setView(content)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.setOnShowListener(ignored -> dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    try {
                        int ml = Integer.parseInt(String.valueOf(input.getText()));
                        if (ml < 1 || ml > 10000) {
                            field.setError(getString(R.string.water_invalid_amount));
                            return;
                        }
                        viewModel.addWater(ml);
                        dialog.dismiss();
                    } catch (NumberFormatException e) {
                        field.setError(getString(R.string.water_invalid_amount));
                    }
                }));
        dialog.show();
    }

    private void setupRecyclerView() {
        adapter = new HistorialAguaAdapter(historialList);
        binding.rvRecentHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentHistory.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.loading.observe(getViewLifecycleOwner(), loading -> {
            // TODO: Show loading indicator
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.consumedStr.observe(getViewLifecycleOwner(), consumed -> {
            if (consumed != null) {
                binding.tvConsumedAmount.setText(consumed);
                binding.tvConsumedToday.setText(consumed);
            }
        });

        viewModel.dailyGoalStr.observe(getViewLifecycleOwner(), goal -> {
            if (goal != null) {
                binding.tvDailyGoal.setText(goal);
            }
        });

        viewModel.remainingStr.observe(getViewLifecycleOwner(), remaining -> {
            if (remaining != null) {
                binding.tvRemaining.setText(remaining);
            }
        });

        viewModel.progressPercent.observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                binding.circularProgress.setProgress(progress);
            }
        });

        // Historial observer
        viewModel.historial.observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                // Show only last 5 entries on home
                historialList.clear();
                int count = Math.min(5, entries.size());
                for (int i = 0; i < count; i++) {
                    historialList.add(entries.get(i));
                }
                adapter.notifyDataSetChanged();

                boolean isEmpty = entries.isEmpty();
                binding.emptyState.getRoot().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                binding.emptyHistory.getRoot().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
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
