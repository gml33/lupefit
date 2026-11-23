package com.lupefit.feature.diario.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.feature.diario.R;
import com.lupefit.core.ui.databinding.CardMacroSummaryBinding;
import com.lupefit.feature.agua.viewmodel.AguaHomeViewModel;
import com.lupefit.feature.diario.databinding.FragmentDiarioHomeBinding;
import com.lupefit.feature.diario.databinding.ViewEmptyDiarioBinding;
import com.lupefit.feature.diario.viewmodel.DiarioHomeViewModel;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;
import com.lupefit.core.domain.engine.voice.VoiceTranscriptionEngine;
import com.lupefit.core.domain.provider.CurrentUserProvider;

public class DiarioHomeFragment extends Fragment {

    private FragmentDiarioHomeBinding binding;
    private AguaHomeViewModel water;
    private CardMacroSummaryBinding macroBinding;
    private ViewEmptyDiarioBinding emptyBinding;
    private DiarioHomeViewModel viewModel;
    private NavController navController;
    private long currentUserId;
    private int currentDateKey;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiarioHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        currentUserId = getCurrentUserId();
        currentDateKey = getTodayDateKey();

        // Los layouts incluidos con <layout> generan sus propios bindings
        macroBinding = binding.macroSummary;
        emptyBinding = binding.emptyState;

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new DiarioHomeViewModel.Factory(context, currentUserId)).get(DiarioHomeViewModel.class);

        setupToolbar();
        binding.tvDateHeader.setText(new SimpleDateFormat("EEEE, d 'de' MMMM", new Locale("es", "AR"))
                .format(new Date()));
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
        water = new ViewModelProvider(this, new AguaHomeViewModel.Factory(requireContext()))
                .get(AguaHomeViewModel.class);
        water.consumedStr.observe(getViewLifecycleOwner(), ignored -> updateWater());
        water.dailyGoalStr.observe(getViewLifecycleOwner(), ignored -> updateWater());
        water.progressPercent.observe(getViewLifecycleOwner(), progress -> {
            binding.waterProgress.progressWater.setMax(100);
            binding.waterProgress.progressWater.setProgress(progress);
        });
        binding.waterProgress.btnQuickWater.setOnClickListener(v -> water.addQuickWater(250));
        binding.waterProgress.btnAddWater.setOnClickListener(v ->
                navController.navigate(Uri.parse("lupefit://hidratacion")));

        viewModel.loadDiarioDelDia();
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
        binding.toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_add_food) {
                navigateToBuscarAlimento();
                return true;
            } else if (itemId == R.id.action_quick_add) {
                navigateToQuickAdd();
                return true;
            } else if (itemId == R.id.action_scan_barcode) {
                navigateToEscaneo();
                return true;
            } else if (itemId == R.id.action_voice_log) {
                showDiarioVozSheet();
                return true;
            }
            return false;
        });
    }


    private void setupRecyclerView() {
        binding.recyclerDiario.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerDiario.setAdapter(new DiarioEntryAdapter(this::editPortion));
    }

    private void editPortion(DiarioEntry entry) {
        TextInputLayout field = new TextInputLayout(requireContext());
        field.setHint(getString(R.string.diario_portion_grams));
        TextInputEditText input = new TextInputEditText(field.getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf(entry.getCantidad()));
        field.addView(input);
        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(entry.getFoodName())
                .setView(field)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.diario_delete_entry, (d, i) -> new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(R.string.diario_delete_confirm)
                        .setPositiveButton(android.R.string.ok, (confirm, option) -> viewModel.deleteEntry(entry))
                        .setNegativeButton(android.R.string.cancel, null)
                        .show())
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    double grams;
                    try {
                        grams = Double.parseDouble(String.valueOf(input.getText()).replace(',', '.'));
                    } catch (NumberFormatException e) {
                        grams = 0;
                    }
                    if (!Double.isFinite(grams) || grams <= 0 || grams > 10000 || entry.getCantidad() <= 0) {
                        field.setError(getString(R.string.diario_portion_invalid));
                        return;
                    }
                    double ratio = grams / entry.getCantidad();
                    entry.setCantidad(grams);
                    entry.setCalorias((int) Math.round(entry.getCalorias() * ratio));
                    entry.setProteinas(entry.getProteinas() * ratio);
                    entry.setCarbohidratos(entry.getCarbohidratos() * ratio);
                    entry.setGrasas(entry.getGrasas() * ratio);
                    entry.setFibra(entry.getFibra() * ratio);
                    entry.setAzucar(entry.getAzucar() * ratio);
                    entry.setSodio(entry.getSodio() * ratio);
                    entry.setEstimatedCostSnapshot(entry.getEstimatedCostSnapshot() * ratio);
                    entry.setSynced(false);
                    entry.setSyncAction(entry.getServerId() == null ? "CREATE" : "UPDATE");
                    entry.setLastModified(System.currentTimeMillis());
                    viewModel.updateEntry(entry);
                    dialog.dismiss();
                }));
        dialog.show();
    }

    private void setupObservers() {
        viewModel.getDiarioEntries().observe(getViewLifecycleOwner(), entries -> {
            // Update adapter with entries
            if (binding.recyclerDiario.getAdapter() instanceof DiarioEntryAdapter) {
                ((DiarioEntryAdapter) binding.recyclerDiario.getAdapter()).setEntries(entries);
            }
            emptyBinding.getRoot().setVisibility(entries.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getTotales().observe(getViewLifecycleOwner(), totales -> {
            updateMacroSummary(totales);
        });
    }

    private void updateMacroSummary(MacroTotals totales) {
        if (totales == null) return;

        macroBinding.tvTotalCaloriesValue.setText(String.valueOf(totales.getCalorias()));

        macroBinding.tvProteinValue.setText(String.format(Locale.getDefault(), "%.1f g", totales.getProteinas()));
        macroBinding.tvCarbsValue.setText(String.format(Locale.getDefault(), "%.1f g", totales.getCarbohidratos()));
        macroBinding.tvFatValue.setText(String.format(Locale.getDefault(), "%.1f g", totales.getGrasas()));

        // Calculate percentages for progress bars (based on typical macro distribution)
        int totalMacroGrams = (int) (totales.getProteinas() + totales.getCarbohidratos() + totales.getGrasas());
        if (totalMacroGrams > 0) {
            macroBinding.pbProtein.setProgress((int) (totales.getProteinas() * 100 / totalMacroGrams));
            macroBinding.pbCarbs.setProgress((int) (totales.getCarbohidratos() * 100 / totalMacroGrams));
            macroBinding.pbFat.setProgress((int) (totales.getGrasas() * 100 / totalMacroGrams));
        }
    }

    private void setupClickListeners() {
        binding.fabAddFood.setOnClickListener(v -> navigateToBuscarAlimento());

        binding.btnAddBreakfast.setOnClickListener(v -> navigateToMeal("desayuno"));
        binding.btnAddLunch.setOnClickListener(v -> navigateToMeal("almuerzo"));
        binding.btnAddSnack.setOnClickListener(v -> navigateToMeal("merienda"));
        binding.btnAddDinner.setOnClickListener(v -> navigateToMeal("cena"));

        emptyBinding.btnFirstFood.setOnClickListener(v -> navigateToBuscarAlimento());
    }

    private void navigateToMeal(String mealType) {
        Bundle args = new Bundle();
        args.putInt("dateKey", currentDateKey);
        args.putString("mealType", mealType);
        navController.navigate(R.id.action_diarioHomeFragment_to_buscarAlimentoFragment, args);
    }

    private void navigateToBuscarAlimento() {
        Bundle args = new Bundle();
        args.putInt("dateKey", currentDateKey);
        args.putString("mealType", "otro");
        navController.navigate(R.id.action_diarioHomeFragment_to_buscarAlimentoFragment, args);
    }

    private void navigateToQuickAdd() {
        Bundle args = new Bundle();
        args.putInt("dateKey", currentDateKey);
        args.putString("mealType", "otro");
        navController.navigate(R.id.action_diarioHomeFragment_to_quickAddFragment, args);
    }

    private void navigateToEscaneo() {
        // Use the existing EscaneoCodigoBarrasFragment from app module
        navController.navigate(R.id.action_diarioHomeFragment_to_escanearCodigoBarrasFragment);
    }

    private void showDiarioVozSheet() {
        DiarioVozSheet sheet = new DiarioVozSheet();
        sheet.setOnFoodsLoggedListener(items -> {
            // Handle logged foods from voice - add each to diary
            for (VoiceTranscriptionEngine.ParsedFoodItem item : items) {
                DiarioEntry entry = new DiarioEntry();
                entry.setFoodName(item.getFoodName());
                entry.setFoodId(""); // No ID for voice-parsed items
                entry.setCantidad(item.getGrams());
                entry.setTipoComida("otro");
                entry.setDateKey(currentDateKey);
                entry.setTimestamp(System.currentTimeMillis());

                entry.setCalorias((int) item.getMacros().kcal);
                entry.setProteinas(item.getMacros().proteinG);
                entry.setCarbohidratos(item.getMacros().carbsG);
                entry.setGrasas(item.getMacros().fatG);

                viewModel.addEntry(entry);
            }
            Toast.makeText(requireContext(), items.size() + " alimento(s) registrado(s) por voz", Toast.LENGTH_SHORT).show();
        });
        sheet.show(getChildFragmentManager(), "DiarioVozSheet");
    }

    private void updateWater() {
        binding.waterProgress.tvWaterProgress.setText(getString(
                R.string.diario_water_progress, water.consumedStr.getValue(), water.dailyGoalStr.getValue()));
    }

    @Override
    public void onResume() {
        super.onResume();
        water.loadTodayData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        macroBinding = null;
        emptyBinding = null;
    }
}
