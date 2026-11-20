package com.lupefit.app.ui.escaneo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.lupefit.app.R;
import com.lupefit.app.data.local.entity.FoodProduct;
import com.lupefit.app.databinding.ItemMacroMiniBinding;
import com.lupefit.app.databinding.SheetResultadoEscaneoBinding;

/**
 * BottomSheet para mostrar resultado del escaneo y seleccionar cantidad.
 */
public class ResultadoEscaneoSheet extends BottomSheetDialogFragment {

    private static final String TAG = "ResultadoEscaneoSheet";
    private static final String ARG_PRODUCT = "product";

    private SheetResultadoEscaneoBinding binding;
    private FoodProduct product;
    private OnProductAddedListener listener;

    public interface OnProductAddedListener {
        void onProductAdded(FoodProduct product, double grams);
    }

    public static ResultadoEscaneoSheet newInstance(FoodProduct product) {
        ResultadoEscaneoSheet sheet = new ResultadoEscaneoSheet();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT);
        }
        setStyle(STYLE_NORMAL, com.lupefit.core.ui.R.style.Theme_LuPeFit_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SheetResultadoEscaneoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (product == null) {
            dismiss();
            return;
        }
        setupViews();
        populateData();
    }

    private void setupViews() {
        // EditText para gramos
        binding.etGrams.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCalculatedSummary();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botones +/-
        binding.btnMinus.setOnClickListener(v -> adjustGrams(-10));
        binding.btnPlus.setOnClickListener(v -> adjustGrams(10));

        // Chips de preset
        binding.chipPreset1.setOnClickListener(v -> setGrams(100));
        binding.chipPreset2.setOnClickListener(v -> setGrams(product.defaultServingGrams));
        binding.chipPreset3.setOnClickListener(v -> setGrams(200));

        // Botones acción
        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnAddToDiary.setOnClickListener(v -> {
            double grams = getCurrentGrams();
            if (listener != null) {
                listener.onProductAdded(product, grams);
            }
            dismiss();
        });
    }

    private void populateData() {
        binding.tvProductName.setText(product.name);

        // Macros por 100g
        FoodProduct.MacroBreakdown macros100 = product.calculateMacrosForGrams(100);
        setMacroView(binding.macroKcal.getRoot(), macros100.kcal, "kcal");
        setMacroView(binding.macroProtein.getRoot(), macros100.proteinG, "g");
        setMacroView(binding.macroCarbs.getRoot(), macros100.carbsG, "g");
        setMacroView(binding.macroFat.getRoot(), macros100.fatG, "g");

        // Valor por defecto: porción por defecto del producto
        double defaultGrams = product.defaultServingGrams > 0 ? product.defaultServingGrams : 100;
        setGrams(defaultGrams);
    }

    private void setMacroView(View view, double value, String unit) {
        TextView tvValue = view.findViewById(R.id.tvMacroValue);
        TextView tvLabel = view.findViewById(R.id.tvMacroLabel);
        TextView tvUnit = view.findViewById(R.id.tvMacroUnit);

        tvValue.setText(String.format("%.0f", value));
        tvUnit.setText(unit);
        // Label se setea en XML
    }

    private void setGrams(double grams) {
        binding.etGrams.setText(String.format("%.0f", grams));
    }

    private double getCurrentGrams() {
        String text = binding.etGrams.getText().toString().trim();
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 100;
        }
    }

    private void adjustGrams(double delta) {
        double current = getCurrentGrams();
        setGrams(Math.max(1, current + delta));
    }

    private void updateCalculatedSummary() {
        double grams = getCurrentGrams();
        FoodProduct.MacroBreakdown macros = product.calculateMacrosForGrams(grams);

        setMacroView(binding.calcKcal.getRoot(), macros.kcal, "kcal");
        setMacroView(binding.calcProtein.getRoot(), macros.proteinG, "g");
        setMacroView(binding.calcCarbs.getRoot(), macros.carbsG, "g");
        setMacroView(binding.calcFat.getRoot(), macros.fatG, "g");
    }

    public void setOnProductAddedListener(OnProductAddedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}