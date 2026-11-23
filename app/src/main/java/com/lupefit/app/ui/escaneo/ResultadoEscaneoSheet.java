package com.lupefit.app.ui.escaneo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lupefit.app.R;
import com.lupefit.app.data.local.entity.FoodProduct;
import com.lupefit.app.data.local.preferences.UserPreferencesManager;
import com.lupefit.app.domain.engine.budget.CostBudgetEngine;
import com.lupefit.app.databinding.ItemMacroMiniBinding;
import com.lupefit.app.databinding.SheetResultadoEscaneoBinding;
import com.lupefit.app.databinding.ViewOptionalPriceInputBinding;

/**
 * BottomSheet para mostrar resultado del escaneo y seleccionar cantidad.
 * Incluye bloque opcional de carga de precio de referencia.
 */
public class ResultadoEscaneoSheet extends BottomSheetDialogFragment {

    private static final String TAG = "ResultadoEscaneoSheet";
    private static final String ARG_PRODUCT = "product";

    private SheetResultadoEscaneoBinding binding;
    private ViewOptionalPriceInputBinding priceBinding;
    private FoodProduct product;
    private OnProductAddedListener listener;
    private UserPreferencesManager prefsManager;

    public interface OnProductAddedListener {
        void onProductAdded(FoodProduct product, double grams, double estimatedCost);
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
        // Inflate price input block separately for ViewBinding access
        priceBinding = ViewOptionalPriceInputBinding.bind(binding.includedPriceInput.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (product == null) {
            dismiss();
            return;
        }

        prefsManager = UserPreferencesManager.getInstance(requireContext());
        setupViews();
        populateData();
        setupPriceInputBlock();
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
            double estimatedCost = calculateEstimatedCost(grams);
            if (listener != null) {
                listener.onProductAdded(product, grams, estimatedCost);
            }
            dismiss();
        });
    }

    private void populateData() {
        binding.tvProductName.setText(product.name);

        // Macros por 100g
        FoodProduct.MacroBreakdown macros100 = product.calculateMacrosForGrams(100);
        setMacroView(binding.macroKcal.getRoot(), macros100.kcal, "Calorías", "kcal");
        setMacroView(binding.macroProtein.getRoot(), macros100.proteinG, "Proteínas", "g");
        setMacroView(binding.macroCarbs.getRoot(), macros100.carbsG, "Carbohidratos", "g");
        setMacroView(binding.macroFat.getRoot(), macros100.fatG, "Grasas", "g");

        // Valor por defecto: porción por defecto del producto
        double defaultGrams = product.defaultServingGrams > 0 ? product.defaultServingGrams : 100;
        setGrams(defaultGrams);
    }

    private void setupPriceInputBlock() {
        boolean costTrackingEnabled = prefsManager.isCostTrackingEnabled();

        // Mostrar/ocultar bloque completo según feature toggle
        if (costTrackingEnabled) {
            binding.includedPriceInput.getRoot().setVisibility(View.VISIBLE);
            binding.llCalculatedSummary.setVisibility(View.VISIBLE); // Ensure summary visible for cost
        } else {
            binding.includedPriceInput.getRoot().setVisibility(View.GONE);
            // El costo estimado en el resumen también se oculta
            binding.tvEstimatedCost.setVisibility(View.GONE);
        }

        // Configurar header expandible
        priceBinding.llPriceHeader.setOnClickListener(v -> togglePriceContent());

        // Configurar chips rápidos de cantidad
        priceBinding.chipQty100g.setOnClickListener(v -> setReferenceQuantity(100));
        priceBinding.chipQty500g.setOnClickListener(v -> setReferenceQuantity(500));
        priceBinding.chipQty1kg.setOnClickListener(v -> setReferenceQuantity(1000));
        priceBinding.chipQtyCustom.setOnClickListener(v -> {
            // Deja el foco en el EditText para ingreso manual
            priceBinding.etReferenceQuantity.requestFocus();
        });

        // Configurar botón guardar
        priceBinding.btnSavePrice.setOnClickListener(v -> savePriceReference());

        // Cargar datos existentes si hay precio
        if (product.referencePrice > 0) {
            priceBinding.etReferencePrice.setText(String.format("%.2f", product.referencePrice));
            priceBinding.etReferenceQuantity.setText(String.format("%.0f", product.referenceQuantityGrams));
            expandPriceContent();
            updatePriceStaleWarning();
        }
    }

    private void togglePriceContent() {
        boolean isExpanded = priceBinding.llPriceContent.getVisibility() == View.VISIBLE;
        if (isExpanded) {
            collapsePriceContent();
        } else {
            expandPriceContent();
        }
    }

    private void expandPriceContent() {
        priceBinding.llPriceContent.setVisibility(View.VISIBLE);
        priceBinding.ivPriceExpand.setImageResource(android.R.drawable.arrow_up_float);
    }

    private void collapsePriceContent() {
        priceBinding.llPriceContent.setVisibility(View.GONE);
        priceBinding.ivPriceExpand.setImageResource(android.R.drawable.arrow_down_float);
    }

    private void setReferenceQuantity(double grams) {
        priceBinding.etReferenceQuantity.setText(String.format("%.0f", grams));
    }

    private void savePriceReference() {
        String priceStr = priceBinding.etReferencePrice.getText().toString().trim();
        String qtyStr = priceBinding.etReferenceQuantity.getText().toString().trim();

        if (priceStr.isEmpty() || qtyStr.isEmpty()) {
            // TODO: Show error toast
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            double qty = Double.parseDouble(qtyStr);

            if (price <= 0 || qty <= 0) {
                // TODO: Show error toast
                return;
            }

            // Actualizar producto
            product.referencePrice = price;
            product.referenceQuantityGrams = qty;
            product.priceUpdatedAt = System.currentTimeMillis();

            // Actualizar DAO en background
            com.lupefit.app.MyApplication app = (com.lupefit.app.MyApplication) requireActivity().getApplication();
            app.getContainer().getFoodProductDao().insert(product);

            // Actualizar UI
            priceBinding.etReferencePrice.setText(String.format("%.2f", price));
            priceBinding.etReferenceQuantity.setText(String.format("%.0f", qty));
            updatePriceStaleWarning();
            updateCalculatedSummary();

            // TODO: Show success toast

        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing price", e);
        }
    }

    private void updatePriceStaleWarning() {
        if (product.priceUpdatedAt > 0) {
            int daysOld = CostBudgetEngine.getDaysSincePriceUpdate(product.priceUpdatedAt);
            if (daysOld >= 0 && CostBudgetEngine.isPriceOutdated(product.priceUpdatedAt)) {
                priceBinding.llPriceStaleWarning.setVisibility(View.VISIBLE);
                priceBinding.tvPriceStaleMessage.setText(
                        String.format(getString(R.string.costo_precio_desactualizado), daysOld));
            } else {
                priceBinding.llPriceStaleWarning.setVisibility(View.GONE);
            }
        } else {
            priceBinding.llPriceStaleWarning.setVisibility(View.GONE);
        }
    }

    private void setMacroView(View view, double value, String label, String unit) {
        TextView tvValue = view.findViewById(R.id.tvMacroValue);
        TextView tvLabel = view.findViewById(R.id.tvMacroLabel);
        TextView tvUnit = view.findViewById(R.id.tvMacroUnit);

        tvValue.setText(String.format("%.0f", value));
        tvLabel.setText(label);
        tvUnit.setText(unit);
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

        setMacroView(binding.calcKcal.getRoot(), macros.kcal, "Calorías", "kcal");
        setMacroView(binding.calcProtein.getRoot(), macros.proteinG, "Proteínas", "g");
        setMacroView(binding.calcCarbs.getRoot(), macros.carbsG, "Carbohidratos", "g");
        setMacroView(binding.calcFat.getRoot(), macros.fatG, "Grasas", "g");

        // Actualizar costo estimado si tracking activado
        double estimatedCost = calculateEstimatedCost(grams);
        if (prefsManager.isCostTrackingEnabled() && estimatedCost > 0) {
            binding.tvEstimatedCost.setVisibility(View.VISIBLE);
            String currencyCode = prefsManager.getCurrencyCode();
            String formatted = CostBudgetEngine.formatCost(estimatedCost, currencyCode);
            binding.tvEstimatedCost.setText(
                    String.format(getString(R.string.costo_calculado_para), String.format("%.0f", grams), formatted));
        } else {
            binding.tvEstimatedCost.setVisibility(View.GONE);
        }
    }

    private double calculateEstimatedCost(double grams) {
        return CostBudgetEngine.calculateServingCost(product, grams);
    }

    public void setOnProductAddedListener(OnProductAddedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        priceBinding = null;
    }
}
