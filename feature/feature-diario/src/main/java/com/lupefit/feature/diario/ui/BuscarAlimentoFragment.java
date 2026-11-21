package com.lupefit.feature.diario.ui;

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
import com.lupefit.core.domain.model.FoodProduct;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.feature.diario.R;
import com.lupefit.feature.diario.databinding.FragmentBuscarAlimentoBinding;
import com.lupefit.feature.diario.databinding.ItemFoodSearchResultBinding;
import com.lupefit.feature.diario.viewmodel.BuscarAlimentoViewModel;
import com.lupefit.feature.diario.viewmodel.DiarioHomeViewModel;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuscarAlimentoFragment extends Fragment {

    private FragmentBuscarAlimentoBinding binding;
    private BuscarAlimentoViewModel viewModel;
    private NavController navController;
    private long currentUserId;
    private long currentDateKey;
    private String currentMealType;

    private FoodSearchAdapter adapter;
    private final List<FoodProduct> searchResults = new ArrayList<>();

    // Categorías para filtros
    private final String[] CATEGORIES = {"all", "carnes", "pescados", "huevos", "lacteos", "cereales", "frutas", "verduras", "legumbres", "frutos_secos", "aceites", "bebidas", "snacks"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuscarAlimentoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        currentUserId = getCurrentUserId();

        // Get arguments
        if (getArguments() != null) {
            currentDateKey = getArguments().getLong("dateKey", getTodayDateKey());
            currentMealType = getArguments().getString("mealType", "otro");
        }

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new BuscarAlimentoViewModel.Factory(context)).get(BuscarAlimentoViewModel.class);

        setupToolbar();
        setupCategoryChips();
        setupSearchInput();
        setupRecyclerView();
        setupObservers();
    }

    private long getCurrentUserId() {
        return 1L; // TODO: Get from UserRepository
    }

    private int getTodayDateKey() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.YEAR) * 10000
                + (cal.get(java.util.Calendar.MONTH) + 1) * 100
                + cal.get(java.util.Calendar.DAY_OF_MONTH);
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
            case "carnes": return "Carnes";
            case "pescados": return "Pescados";
            case "huevos": return "Huevos";
            case "lacteos": return "Lácteos";
            case "cereales": return "Cereales";
            case "frutas": return "Frutas";
            case "verduras": return "Verduras";
            case "legumbres": return "Legumbres";
            case "frutos_secos": return "Frutos secos";
            case "aceites": return "Aceites";
            case "bebidas": return "Bebidas";
            case "snacks": return "Snacks";
            default: return category;
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
        adapter = new FoodSearchAdapter(searchResults, this::onFoodSelected);
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

    private void onFoodSelected(FoodProduct food) {
        // Mostrar sheet para seleccionar porción y añadir al diario
        showPorcionSheet(food);
    }

    private void showPorcionSheet(FoodProduct food) {
        EditarPorcionSheet sheet = EditarPorcionSheet.newInstance(food, currentMealType);
        sheet.setOnPorcionSelectedListener((grams, mealType) -> {
            // Crear entrada en el diario
            DiarioEntry entry = new DiarioEntry();
            entry.setFoodName(food.getName());
            entry.setFoodId(String.valueOf(food.getId()));
            entry.setCantidad(grams);
            entry.setTipoComida(mealType);
            entry.setDateKey((int) currentDateKey);
            entry.setTimestamp(System.currentTimeMillis());

            FoodProduct.MacroBreakdown macros = food.calculateMacrosForGrams(grams);
            entry.setCalorias((int) macros.kcal);
            entry.setProteinas(macros.proteinG);
            entry.setCarbohidratos(macros.carbsG);
            entry.setGrasas(macros.fatG);

            // Insertar via ViewModel del Home
            DiarioHomeViewModel homeViewModel = new ViewModelProvider(
                    requireActivity(),
                    new DiarioHomeViewModel.Factory(requireContext(), currentUserId)
            ).get(DiarioHomeViewModel.class);
            homeViewModel.addEntry(entry);

            Toast.makeText(requireContext(), food.getName() + " añadido al diario", Toast.LENGTH_SHORT).show();
        });
        sheet.show(getChildFragmentManager(), "EditarPorcionSheet");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ==================== ADAPTER ====================
    private static class FoodSearchAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<FoodSearchAdapter.ViewHolder> {
        private final List<FoodProduct> items;
        private final OnFoodClickListener listener;

        interface OnFoodClickListener {
            void onClick(FoodProduct food);
        }

        FoodSearchAdapter(List<FoodProduct> items, OnFoodClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemFoodSearchResultBinding binding = ItemFoodSearchResultBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FoodProduct food = items.get(position);
            holder.binding.setFood(food);
            holder.binding.setOnClick(v -> listener.onClick(food));
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final ItemFoodSearchResultBinding binding;

            ViewHolder(ItemFoodSearchResultBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}