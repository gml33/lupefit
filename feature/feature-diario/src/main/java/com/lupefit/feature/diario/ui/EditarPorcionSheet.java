package com.lupefit.feature.diario.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.lupefit.core.domain.model.FoodProduct;
import com.lupefit.feature.diario.R;
import com.lupefit.feature.diario.databinding.ItemMacroMiniBinding;
import com.lupefit.feature.diario.databinding.SheetEditarPorcionBinding;

import java.util.Arrays;
import java.util.List;

public class EditarPorcionSheet extends BottomSheetDialogFragment {

    private static final String ARG_FOOD = "food";
    private static final String ARG_MEAL_TYPE = "meal_type";

    private SheetEditarPorcionBinding binding;
    private FoodProduct food;
    private String currentMealType;
    private double currentGrams;

    public interface OnPorcionSelectedListener {
        void onPorcionSelected(double grams, String mealType);
    }

    private OnPorcionSelectedListener listener;

    public static EditarPorcionSheet newInstance(FoodProduct food, String mealType) {
        EditarPorcionSheet sheet = new EditarPorcionSheet();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FOOD, food);
        args.putString(ARG_MEAL_TYPE, mealType);
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            food = getArguments().getParcelable(ARG_FOOD);
            currentMealType = getArguments().getString(ARG_MEAL_TYPE, "otro");
        }
        setStyle(STYLE_NORMAL, com.lupefit.core.ui.R.style.Theme_LuPeFit_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SheetEditarPorcionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (food == null) {
            dismiss();
            return;
        }
        currentGrams = food.getDefaultServingGrams() > 0 ? food.getDefaultServingGrams() : 100;
        setupViews();
        populateData();
    }

    private void setupViews() {
        // Meal type chips
        String[] mealTypes = {"desayuno", "almuerzo", "merienda", "cena", "pre_entreno", "post_entreno", "otro"};
        for (String meal : mealTypes) {
            Chip chip = new Chip(requireContext());
            chip.setText(getMealLabel(meal));
            chip.setTag(meal);
            chip.setCheckable(true);
            chip.setChecked(meal.equals(currentMealType));
            chip.setChipBackgroundColorResource(com.lupefit.core.ui.R.color.chip_choice_background_color);
            chip.setTextColor(ContextCompat.getColor(requireContext(), com.lupefit.core.ui.R.color.chip_choice_text_color));
            binding.chipGroupMealTypes.addView(chip);
        }

        binding.chipGroupMealTypes.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                if (chip != null) {
                    currentMealType = (String) chip.getTag();
                }
            }
        });

        // Preset chips
        double[] presets = {50, 100, 150, 200, 250, 300};
        for (double preset : presets) {
            Chip chip = new Chip(requireContext());
            chip.setText((int) preset + "g");
            chip.setTag(preset);
            chip.setCheckable(true);
            chip.setChecked(preset == currentGrams);
            chip.setChipBackgroundColorResource(com.lupefit.core.ui.R.color.chip_background_color);
            chip.setTextColor(ContextCompat.getColor(requireContext(), com.lupefit.core.ui.R.color.chip_filter_text_color));
            binding.chipGroupPresets.addView(chip);
        }

        binding.chipGroupPresets.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                if (chip != null) {
                    setGrams((double) chip.getTag());
                }
            }
        });

        // Botones +/-
        binding.btnMinus50.setOnClickListener(v -> adjustGrams(-50));
        binding.btnMinus10.setOnClickListener(v -> adjustGrams(-10));
        binding.btnPlus10.setOnClickListener(v -> adjustGrams(10));
        binding.btnPlus50.setOnClickListener(v -> adjustGrams(50));

        // Botones acción
        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPorcionSelected(currentGrams, currentMealType);
            }
            dismiss();
        });
    }

    private void populateData() {
        binding.tvFoodName.setText(food.getName());
        setGrams(currentGrams);

        // Macros por 100g
        FoodProduct.MacroBreakdown macros100 = food.calculateMacrosForGrams(100);
        setMacroView(binding.llMacros100g.getChildAt(0), macros100.kcal, "Calorías", "kcal", com.lupefit.core.ui.R.color.macro_protein);
        setMacroView(binding.llMacros100g.getChildAt(1), macros100.proteinG, "Proteínas", "g", com.lupefit.core.ui.R.color.macro_carbs);
        setMacroView(binding.llMacros100g.getChildAt(2), macros100.carbsG, "Carbohidratos", "g", com.lupefit.core.ui.R.color.macro_fat);
        setMacroView(binding.llMacros100g.getChildAt(3), macros100.fatG, "Grasas", "g", com.lupefit.core.ui.R.color.macro_carbs);

        // Macros calculados
        updateCalculatedMacros();
    }

    private String getMealLabel(String mealType) {
        switch (mealType) {
            case "desayuno": return "Desayuno";
            case "almuerzo": return "Almuerzo";
            case "merienda": return "Merienda";
            case "cena": return "Cena";
            case "pre_entreno": return "Pre-entreno";
            case "post_entreno": return "Post-entreno";
            default: return "Otro";
        }
    }

    private void setGrams(double grams) {
        currentGrams = Math.max(1, grams);
        binding.tvGramsValue.setText((int) currentGrams + "g");
        updateCalculatedMacros();
    }

    private void adjustGrams(double delta) {
        setGrams(currentGrams + delta);
    }

    private void updateCalculatedMacros() {
        FoodProduct.MacroBreakdown macros = food.calculateMacrosForGrams(currentGrams);
        setMacroView(binding.llCalculatedMacros.getChildAt(0), macros.kcal, "Calorías", "kcal", com.lupefit.core.ui.R.color.macro_protein);
        setMacroView(binding.llCalculatedMacros.getChildAt(1), macros.proteinG, "Proteínas", "g", com.lupefit.core.ui.R.color.macro_carbs);
        setMacroView(binding.llCalculatedMacros.getChildAt(2), macros.carbsG, "Carbohidratos", "g", com.lupefit.core.ui.R.color.macro_fat);
        setMacroView(binding.llCalculatedMacros.getChildAt(3), macros.fatG, "Grasas", "g", com.lupefit.core.ui.R.color.macro_carbs);
    }

    private void setMacroView(View view, double value, String label, String unit, int colorRes) {
        ItemMacroMiniBinding binding = ItemMacroMiniBinding.bind(view);
        binding.tvMacroValue.setText(String.format("%.0f", value));
        binding.tvMacroLabel.setText(label);
        binding.tvMacroUnit.setText(unit);
        binding.tvMacroValue.setTextColor(ContextCompat.getColor(requireContext(), colorRes));
    }

    public void setOnPorcionSelectedListener(OnPorcionSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
