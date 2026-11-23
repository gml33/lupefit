package com.lupefit.feature.nutricion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.repository.DiarioRepository;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MacrosDetalleViewModel extends ViewModel {

    private final SharedPreferences prefs;
    private final Context context;

    public final MutableLiveData<Boolean> netCarbsMode;
    public final LiveData<List<com.lupefit.feature.nutricion.model.MacroDetail>> macroDetails;
    public final LiveData<List<com.lupefit.feature.nutricion.model.MicroDetail>> microDetails;

    public MacrosDetalleViewModel(DiarioRepository diarioRepository, Context context) {
        this.context = context.getApplicationContext();
        prefs = context.getSharedPreferences("nutricion_metas", Context.MODE_PRIVATE);
        netCarbsMode = new MutableLiveData<>(prefs.getBoolean("net_carbs_mode", false));
        LiveData<List<DiarioEntry>> entries = diarioRepository.getEntriesForDate(getTodayDateKey());
        macroDetails = Transformations.map(entries, this::buildMacroDetails);
        microDetails = Transformations.map(entries, this::buildMicroDetails);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MacrosDetalleViewModel.class)) {
                DiarioRepositoryProvider provider =
                        (DiarioRepositoryProvider) context.getApplicationContext();
                DiarioRepository repository = provider.getDiarioRepository();
                return (T) new MacrosDetalleViewModel(repository, context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    private List<com.lupefit.feature.nutricion.model.MacroDetail> buildMacroDetails(List<DiarioEntry> entries) {
        MacroTotals totales = new MacroTotals();
        double fibra = 0;
        if (entries != null) {
            for (DiarioEntry entry : entries) {
                totales.setCalorias(totales.getCalorias() + entry.getCalorias());
                totales.setProteinas(totales.getProteinas() + entry.getProteinas());
                totales.setCarbohidratos(totales.getCarbohidratos() + entry.getCarbohidratos());
                totales.setGrasas(totales.getGrasas() + entry.getGrasas());
                fibra += entry.getFibra();
            }
        }
        List<com.lupefit.feature.nutricion.model.MacroDetail> details = new ArrayList<>();

        // Get goals from SharedPreferences (use the prefs already stored in this class)
        int goalCalorias = prefs.getInt("meta_calorias", 2000);
        float goalProteinas = prefs.getFloat("meta_proteinas", 150f);
        float goalCarbohidratos = prefs.getFloat("meta_carbohidratos", 250f);
        float goalGrasas = prefs.getFloat("meta_grasas", 70f);

        boolean netCarbs = netCarbsMode.getValue() != null && netCarbsMode.getValue();

        // Calorías
        int caloriasPct = goalCalorias > 0 ? (int) (totales.getCalorias() * 100f / goalCalorias) : 0;
        details.add(new com.lupefit.feature.nutricion.model.MacroDetail(
                "Calorías",
                "Energía total consumida",
                String.valueOf(totales.getCalorias()),
                "kcal",
                caloriasPct + "%",
                context.getColor(com.lupefit.core.ui.R.color.md_theme_primary)
        ));

        // Proteínas
        int proteinasPct = goalProteinas > 0 ? (int) (totales.getProteinas() * 100f / goalProteinas) : 0;
        details.add(new com.lupefit.feature.nutricion.model.MacroDetail(
                "Proteínas",
                "Construcción y reparación muscular",
                String.format("%.1f", totales.getProteinas()),
                "g",
                proteinasPct + "%",
                context.getColor(com.lupefit.core.ui.R.color.macro_protein)
        ));

        // Carbohidratos
        float carbs = netCarbs ? (float) Math.max(0, totales.getCarbohidratos() - fibra) : (float) totales.getCarbohidratos();
        int carbsPct = goalCarbohidratos > 0 ? (int) (carbs * 100f / goalCarbohidratos) : 0;
        details.add(new com.lupefit.feature.nutricion.model.MacroDetail(
                netCarbs ? "Net Carbs" : "Carbohidratos",
                netCarbs ? "Carbohidratos netos (sin fibra)" : "Carbohidratos totales",
                String.format("%.1f", carbs),
                "g",
                carbsPct + "%",
                context.getColor(com.lupefit.core.ui.R.color.macro_carbs)
        ));

        // Grasas
        int grasasPct = goalGrasas > 0 ? (int) (totales.getGrasas() * 100f / goalGrasas) : 0;
        details.add(new com.lupefit.feature.nutricion.model.MacroDetail(
                "Grasas",
                "Energía y funciones hormonales",
                String.format("%.1f", totales.getGrasas()),
                "g",
                grasasPct + "%",
                context.getColor(com.lupefit.core.ui.R.color.macro_fat)
        ));

        return details;
    }

    private List<com.lupefit.feature.nutricion.model.MicroDetail> buildMicroDetails(List<DiarioEntry> entries) {
        double fibra = 0;
        double azucar = 0;
        double sodio = 0;
        if (entries != null) {
            for (DiarioEntry entry : entries) {
                fibra += entry.getFibra();
                azucar += entry.getAzucar();
                sodio += entry.getSodio();
            }
        }
        float goalFibra = prefs.getFloat("meta_fibra", 25f);
        String fibraPct = goalFibra > 0 ? Math.round(fibra * 100 / goalFibra) + "%" : "";
        return java.util.Arrays.asList(
                new com.lupefit.feature.nutricion.model.MicroDetail(
                        "Fibra", "Fibra registrada", String.format("%.1f", fibra), "g", fibraPct),
                new com.lupefit.feature.nutricion.model.MicroDetail(
                        "Azúcar", "Azúcares totales", String.format("%.1f", azucar), "g", ""),
                new com.lupefit.feature.nutricion.model.MicroDetail(
                        "Sodio", "Sodio registrado", String.format("%.0f", sodio), "mg", ""));
    }

    private int getTodayDateKey() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) * 10000
                + (cal.get(Calendar.MONTH) + 1) * 100
                + cal.get(Calendar.DAY_OF_MONTH);
    }

}
