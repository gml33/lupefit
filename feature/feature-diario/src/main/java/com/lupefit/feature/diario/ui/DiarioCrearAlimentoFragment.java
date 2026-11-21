package com.lupefit.feature.diario.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lupefit.core.domain.model.FoodProduct;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.feature.diario.R;
import com.lupefit.feature.diario.databinding.FragmentDiarioCrearAlimentoBinding;
import com.lupefit.feature.diario.viewmodel.DiarioCrearAlimentoViewModel;
import com.lupefit.feature.diario.viewmodel.DiarioHomeViewModel;

public class DiarioCrearAlimentoFragment extends Fragment {

    private FragmentDiarioCrearAlimentoBinding binding;
    private DiarioCrearAlimentoViewModel viewModel;
    private NavController navController;
    private long currentUserId;
    private long currentDateKey;
    private String currentMealType;
    private String prefilledBarcode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiarioCrearAlimentoBinding.inflate(inflater, container, false);
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
            prefilledBarcode = getArguments().getString("barcode", "");
        }

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new DiarioCrearAlimentoViewModel.Factory(context)).get(DiarioCrearAlimentoViewModel.class);

        setupToolbar();
        setupInputs();
        setupObservers();

        if (!prefilledBarcode.isEmpty()) {
            binding.etBarcode.setText(prefilledBarcode);
        }
    }

    private long getCurrentUserId() {
        return 1L;
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
            if (item.getItemId() == R.id.action_clear_form) {
                clearForm();
                return true;
            }
            return false;
        });
    }

    private void setupInputs() {
        // Text watchers para validación
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {}
        };

        binding.etNombre.addTextChangedListener(watcher);
        binding.etMarca.addTextChangedListener(watcher);
        binding.etBarcode.addTextChangedListener(watcher);
        binding.etKcal.addTextChangedListener(watcher);
        binding.etProtein.addTextChangedListener(watcher);
        binding.etCarbs.addTextChangedListener(watcher);
        binding.etFat.addTextChangedListener(watcher);
        binding.etPorcion.addTextChangedListener(watcher);
    }

    private void setupObservers() {
        viewModel.savedFood.observe(getViewLifecycleOwner(), food -> {
            if (food != null) {
                // Alimento guardado, crear entrada en el diario
                createDiarioEntry(food);
                Toast.makeText(requireContext(), food.getName() + " creado y añadido", Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            }
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        binding.etNombre.setText("");
        binding.etMarca.setText("");
        binding.etBarcode.setText("");
        binding.etKcal.setText("0");
        binding.etProtein.setText("0");
        binding.etCarbs.setText("0");
        binding.etFat.setText("0");
        binding.etPorcion.setText("100");
        binding.switchFreshFood.setChecked(false);
    }

    private void createDiarioEntry(FoodProduct food) {
        // Get values from UI
        String name = binding.etNombre.getText().toString().trim();
        String brand = binding.etMarca.getText().toString().trim();
        String barcode = binding.etBarcode.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nombre requerido", Toast.LENGTH_SHORT).show();
            return;
        }

        double grams = 100;
        try {
            grams = Double.parseDouble(binding.etPorcion.getText().toString());
        } catch (NumberFormatException ignored) {}

        DiarioEntry entry = new DiarioEntry();
        entry.setFoodName(name);
        entry.setFoodId(String.valueOf(food.getId()));
        entry.setCantidad(grams);
        entry.setTipoComida(currentMealType);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}