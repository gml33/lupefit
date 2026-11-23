package com.lupefit.feature.diario.ui;

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

import com.lupefit.core.domain.model.FoodProduct;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.feature.diario.R;
import com.lupefit.feature.diario.databinding.FragmentQuickAddBinding;
import com.lupefit.feature.diario.databinding.ItemQuickAddFoodBinding;
import com.lupefit.feature.diario.viewmodel.QuickAddViewModel;
import com.lupefit.feature.diario.viewmodel.DiarioHomeViewModel;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;
import com.lupefit.core.domain.provider.CurrentUserProvider;

public class QuickAddFragment extends Fragment {

    private FragmentQuickAddBinding binding;
    private QuickAddViewModel viewModel;
    private NavController navController;
    private long currentUserId;
    private long currentDateKey;
    private String currentMealType;

    private QuickAddAdapter freshAdapter;
    private QuickAddAdapter recentAdapter;
    private QuickAddAdapter favoriteAdapter;

    private final List<FoodProduct> freshFoods = new ArrayList<>();
    private final List<FoodProduct> recentFoods = new ArrayList<>();
    private final List<FoodProduct> favoriteFoods = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuickAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        currentUserId = getCurrentUserId();

        if (getArguments() != null) {
            currentDateKey = getArguments().getLong("dateKey", getTodayDateKey());
            currentMealType = getArguments().getString("mealType", "otro");
        }

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new QuickAddViewModel.Factory(context)).get(QuickAddViewModel.class);

        setupToolbar();
        setupRecyclerViews();
        setupObservers();
    }

    private long getCurrentUserId() {
        return ((CurrentUserProvider) requireContext().getApplicationContext()).getCurrentUserId();
    }

    private int getTodayDateKey() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.YEAR) * 10000
                + (cal.get(java.util.Calendar.MONTH) + 1) * 100
                + cal.get(java.util.Calendar.DAY_OF_MONTH);
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupRecyclerViews() {
        freshAdapter = new QuickAddAdapter(freshFoods, this::onFoodSelected);
        recentAdapter = new QuickAddAdapter(recentFoods, this::onFoodSelected);
        favoriteAdapter = new QuickAddAdapter(favoriteFoods, this::onFoodSelected);

        binding.rvFreshFoods.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFreshFoods.setAdapter(freshAdapter);

        binding.rvRecentFoods.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentFoods.setAdapter(recentAdapter);

        binding.rvFavoriteFoods.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavoriteFoods.setAdapter(favoriteAdapter);
    }

    private void setupObservers() {
        viewModel.freshFoods.observe(getViewLifecycleOwner(), foods -> {
            freshFoods.clear();
            if (foods != null) freshFoods.addAll(foods);
            freshAdapter.notifyDataSetChanged();
            updateEmptyState();
        });

        viewModel.recentFoods.observe(getViewLifecycleOwner(), foods -> {
            recentFoods.clear();
            if (foods != null) recentFoods.addAll(foods);
            recentAdapter.notifyDataSetChanged();
        });

        viewModel.favoriteFoods.observe(getViewLifecycleOwner(), foods -> {
            favoriteFoods.clear();
            if (foods != null) favoriteFoods.addAll(foods);
            favoriteAdapter.notifyDataSetChanged();
        });
    }

    private void updateEmptyState() {
        boolean allEmpty = freshFoods.isEmpty() && recentFoods.isEmpty() && favoriteFoods.isEmpty();
        binding.emptyState.getRoot().setVisibility(allEmpty ? View.VISIBLE : View.GONE);
        binding.rvFreshFoods.setVisibility(freshFoods.isEmpty() ? View.GONE : View.VISIBLE);
        binding.rvRecentFoods.setVisibility(recentFoods.isEmpty() ? View.GONE : View.VISIBLE);
        binding.rvFavoriteFoods.setVisibility(favoriteFoods.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void onFoodSelected(FoodProduct food) {
        showPorcionSheet(food);
    }

    private void showPorcionSheet(FoodProduct food) {
        EditarPorcionSheet sheet = EditarPorcionSheet.newInstance(food, currentMealType);
        sheet.setOnPorcionSelectedListener((grams, mealType) -> {
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

            DiarioHomeViewModel homeViewModel = new ViewModelProvider(
                    requireActivity(),
                    new DiarioHomeViewModel.Factory(requireContext(), currentUserId)
            ).get(DiarioHomeViewModel.class);
            homeViewModel.addEntry(entry);

            viewModel.addToRecent(food);
            Toast.makeText(requireContext(), food.getName() + " añadido", Toast.LENGTH_SHORT).show();
        });
        sheet.show(getChildFragmentManager(), "EditarPorcionSheet");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ==================== ADAPTER ====================
    private static class QuickAddAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<QuickAddAdapter.ViewHolder> {
        private final List<FoodProduct> items;
        private final OnFoodClickListener listener;

        interface OnFoodClickListener {
            void onClick(FoodProduct food);
        }

        QuickAddAdapter(List<FoodProduct> items, OnFoodClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemQuickAddFoodBinding binding = ItemQuickAddFoodBinding.inflate(
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
            final ItemQuickAddFoodBinding binding;

            ViewHolder(ItemQuickAddFoodBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
