package com.lupefit.feature.ejercicio.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.lupefit.core.domain.model.Exercise;
import com.lupefit.feature.ejercicio.R;
import com.lupefit.feature.ejercicio.databinding.FragmentBuscarEjercicioBinding;
import com.lupefit.feature.ejercicio.databinding.ItemEjercicioSearchResultBinding;
import com.lupefit.feature.ejercicio.viewmodel.BuscarEjercicioViewModel;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuscarEjercicioFragment extends Fragment {

    private FragmentBuscarEjercicioBinding binding;
    private BuscarEjercicioViewModel viewModel;
    private NavController navController;

    private ExerciseSearchAdapter adapter;
    private final List<Exercise> searchResults = new ArrayList<>();

    // Categorías para filtros
    private final String[] CATEGORIES = {"all", "FUERZA", "OLIMPICO", "GYMNASTICS", "CARDIO", "STRONGMAN", "CROSSFIT"};
    private final String[] PATTERNS = {"all", "SQUAT", "HINGE", "PUSH_HORIZONTAL", "PUSH_VERTICAL", "PULL_HORIZONTAL", "PULL_VERTICAL", "CARRY", "CORE", "LOCOMOTION"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuscarEjercicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new BuscarEjercicioViewModel.Factory(context)).get(BuscarEjercicioViewModel.class);

        setupToolbar();
        setupCategoryChips();
        setupPatternChips();
        setupSearchInput();
        setupRecyclerView();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_clear_search) {
                binding.etSearch.setText("");
                viewModel.clearSearch();
                return true;
            }
            return false;
        });
    }

    private void setupCategoryChips() {
        // Add "Todas" chip first
        Chip chipAll = createChip(getString(R.string.buscar_todas), "all", true);
        binding.chipGroupCategorias.addView(chipAll);

        // Add category chips
        for (int i = 1; i < CATEGORIES.length; i++) {
            String category = CATEGORIES[i];
            String label = getCategoryLabel(category);
            Chip chip = createChip(label, category, false);
            binding.chipGroupCategorias.addView(chip);
        }

        binding.chipGroupCategorias.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                String category = "all";
                for (int id : checkedIds) {
                    Chip chip = group.findViewById(id);
                    if (chip != null && chip.isChecked()) {
                        category = (String) chip.getTag();
                        break;
                    }
                }
                viewModel.setCategory(category);
            }
        });
    }

    private void setupPatternChips() {
        // Add "Todos" chip first
        Chip chipAll = createChip(getString(R.string.buscar_todas), "all", true);
        binding.chipGroupPatrones.addView(chipAll);

        // Add pattern chips
        for (int i = 1; i < PATTERNS.length; i++) {
            String pattern = PATTERNS[i];
            String label = getPatternLabel(pattern);
            Chip chip = createChip(label, pattern, false);
            binding.chipGroupPatrones.addView(chip);
        }

        binding.chipGroupPatrones.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                String pattern = "all";
                for (int id : checkedIds) {
                    Chip chip = group.findViewById(id);
                    if (chip != null && chip.isChecked()) {
                        pattern = (String) chip.getTag();
                        break;
                    }
                }
                viewModel.setPattern(pattern);
            }
        });
    }

    private Chip createChip(String label, String category, boolean checked) {
        Chip chip = new Chip(requireContext());
        chip.setText(label);
        chip.setTag(category);
        chip.setCheckable(true);
        chip.setChecked(checked);
        chip.setChipBackgroundColorResource(com.lupefit.core.ui.R.color.chip_background_color);
        chip.setTextColor(ContextCompat.getColor(requireContext(), com.lupefit.core.ui.R.color.chip_filter_text_color));
        return chip;
    }

    private String getCategoryLabel(String category) {
        switch (category) {
            case "FUERZA": return getString(R.string.cat_fuerza);
            case "OLIMPICO": return getString(R.string.cat_olimpico);
            case "GYMNASTICS": return getString(R.string.cat_gymnastics);
            case "CARDIO": return getString(R.string.cat_cardio);
            case "STRONGMAN": return getString(R.string.cat_strongman);
            case "CROSSFIT": return getString(R.string.cat_crossfit);
            default: return category;
        }
    }

    private String getPatternLabel(String pattern) {
        switch (pattern) {
            case "SQUAT": return getString(R.string.patron_squat);
            case "HINGE": return getString(R.string.patron_hinge);
            case "PUSH_HORIZONTAL": return getString(R.string.patron_push_h);
            case "PUSH_VERTICAL": return getString(R.string.patron_push_v);
            case "PULL_HORIZONTAL": return getString(R.string.patron_pull_h);
            case "PULL_VERTICAL": return getString(R.string.patron_pull_v);
            case "CARRY": return getString(R.string.patron_carry);
            case "CORE": return getString(R.string.patron_core);
            case "LOCOMOTION": return getString(R.string.patron_locomotion);
            default: return pattern;
        }
    }

    private void setupSearchInput() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.search(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        adapter = new ExerciseSearchAdapter(searchResults, this::onExerciseSelected);
        binding.rvResultados.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvResultados.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.searchResults.observe(getViewLifecycleOwner(), results -> {
            searchResults.clear();
            if (results != null) {
                searchResults.addAll(results);
            }
            adapter.notifyDataSetChanged();

            binding.emptyState.getRoot().setVisibility(searchResults.isEmpty() ? View.VISIBLE : View.GONE);
            binding.rvResultados.setVisibility(searchResults.isEmpty() ? View.GONE : View.VISIBLE);
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onExerciseSelected(Exercise exercise) {
        Toast.makeText(requireContext(), exercise.getName() + " - " + exercise.getCategory(), Toast.LENGTH_SHORT).show();
        // TODO: Navigate to exercise detail or add to routine
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ==================== ADAPTER ====================
    private static class ExerciseSearchAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<ExerciseSearchAdapter.ViewHolder> {
        private final List<Exercise> items;
        private final OnExerciseClickListener listener;

        interface OnExerciseClickListener {
            void onClick(Exercise exercise);
        }

        ExerciseSearchAdapter(List<Exercise> items, OnExerciseClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemEjercicioSearchResultBinding binding = ItemEjercicioSearchResultBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Exercise exercise = items.get(position);
            holder.binding.setExercise(exercise);
            holder.binding.setOnClick(v -> listener.onClick(exercise));
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final ItemEjercicioSearchResultBinding binding;

            ViewHolder(ItemEjercicioSearchResultBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}