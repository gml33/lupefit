package com.lupefit.feature.nutricion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.repository.DiarioRepository;
import com.lupefit.core.domain.model.MacroTotals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MacrosDetalleViewModel extends ViewModel {

    private final DiarioRepository diarioRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final SharedPreferences prefs;

    public final MutableLiveData<Boolean> netCarbsMode = new MutableLiveData<>(false);

    private final MutableLiveData<MacroTotals> _totales = new MutableLiveData<>();
    public final LiveData<MacroTotals> totales = _totales;

    private final MutableLiveData<List<com.lupefit.feature.nutricion.model.MacroDetail>> _macroDetails = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<com.lupefit.feature.nutricion.model.MacroDetail>> macroDetails = _macroDetails;

    private final MutableLiveData<List<com.lupefit.feature.nutricion.model.MicroDetail>> _microDetails = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<com.lupefit.feature.nutricion.model.MicroDetail>> microDetails = _microDetails;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private int currentDateKey;

    public MacrosDetalleViewModel(DiarioRepository diarioRepository, Context context) {
        this.diarioRepository = diarioRepository;
        this.prefs = context.getSharedPreferences("nutricion_metas", Context.MODE_PRIVATE);
        currentDateKey = getTodayDateKey();
        netCarbsMode.setValue(prefs.getBoolean("net_carbs_mode", false));
        loadTotales();
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

    private void loadTotales() {
        _isLoading.setValue(true);

        executor.execute(() -> {
            LiveData<com.lupefit.core.domain.model.MacroTotals> liveData = diarioRepository.getDailyTotals(currentDateKey);
            com.lupefit.core.domain.model.MacroTotals totales = liveData.getValue();
            if (totales != null) {
                _totales.postValue(totales);
                buildMacroDetails(totales);
                buildMicroDetails();
            }
            _isLoading.postValue(false);
        });
    }

    private void buildMacroDetails(com.lupefit.core.domain.model.MacroTotals totales) {
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
                0xFF10B981
        ));

        // Proteínas
        int proteinasPct = goalProteinas > 0 ? (int) (totales.getProteinas() * 100f / goalProteinas) : 0;
        details.add(new com.lupefit.feature.nutricion.model.MacroDetail(
                "Proteínas",
                "Construcción y reparación muscular",
                String.format("%.1f", totales.getProteinas()),
                "g",
                proteinasPct + "%",
                0xFF2563EB
        ));

        // Carbohidratos
        float carbs = netCarbs ? (float) (totales.getCarbohidratos() - 0) : (float) totales.getCarbohidratos(); // TODO: subtract fiber
        int carbsPct = goalCarbohidratos > 0 ? (int) (carbs * 100f / goalCarbohidratos) : 0;
        details.add(new com.lupefit.feature.nutricion.model.MacroDetail(
                netCarbs ? "Net Carbs" : "Carbohidratos",
                netCarbs ? "Carbohidratos netos (sin fibra)" : "Carbohidratos totales",
                String.format("%.1f", carbs),
                "g",
                carbsPct + "%",
                0xFFD97706
        ));

        // Grasas
        int grasasPct = goalGrasas > 0 ? (int) (totales.getGrasas() * 100f / goalGrasas) : 0;
        details.add(new com.lupefit.feature.nutricion.model.MacroDetail(
                "Grasas",
                "Energía y funciones hormonales",
                String.format("%.1f", totales.getGrasas()),
                "g",
                grasasPct + "%",
                0xFF7C3AED
        ));

        _macroDetails.postValue(details);
    }

    private void buildMicroDetails() {
        List<com.lupefit.feature.nutricion.model.MicroDetail> details = new ArrayList<>();

        details.add(new com.lupefit.feature.nutricion.model.MicroDetail(
                "Fibra",
                "Salud digestiva",
                "0",
                "g",
                "0%"
        ));

        details.add(new com.lupefit.feature.nutricion.model.MicroDetail(
                "Azúcar",
                "Azúcares totales",
                "0",
                "g",
                "0%"
        ));

        details.add(new com.lupefit.feature.nutricion.model.MicroDetail(
                "Sodio",
                "Equilibrio de fluidos",
                "0",
                "mg",
                "0%"
        ));

        details.add(new com.lupefit.feature.nutricion.model.MicroDetail(
                "Potasio",
                "Función muscular",
                "0",
                "mg",
                "0%"
        ));

        _microDetails.postValue(details);
    }

    public void setNetCarbsMode(boolean enabled) {
        netCarbsMode.setValue(enabled);
    }

    public void refresh() {
        loadTotales();
    }

    private int getTodayDateKey() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) * 10000
                + (cal.get(Calendar.MONTH) + 1) * 100
                + cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}