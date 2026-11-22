package com.lupefit.feature.insights.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;

public class PatronesViewModel extends ViewModel {

    public static class PatronAlimentario {
        public final String nombre;
        public final int frecuencia;
        public final String descripcion;
        public final boolean esPositivo;

        public PatronAlimentario(String nombre, int frecuencia, String descripcion, boolean esPositivo) {
            this.nombre = nombre;
            this.frecuencia = frecuencia;
            this.descripcion = descripcion;
            this.esPositivo = esPositivo;
        }
    }

    private final MutableLiveData<List<PatronAlimentario>> _patrones = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<PatronAlimentario>> patrones = _patrones;

    private final MutableLiveData<String> _patronPrincipal = new MutableLiveData<>("—");
    public final LiveData<String> patronPrincipal = _patronPrincipal;

    private final MutableLiveData<Integer> _scoreConsistencia = new MutableLiveData<>(0);
    public final LiveData<Integer> scoreConsistencia = _scoreConsistencia;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    public PatronesViewModel() {
        cargarPatronesSimulados();
    }

    private void cargarPatronesSimulados() {
        List<PatronAlimentario> lista = new ArrayList<>();
        lista.add(new PatronAlimentario("Desayuno consistente", 85, "Desayunas 6 de 7 días", true));
        lista.add(new PatronAlimentario("Cena tardía", 60, "Cenas después de las 22:00", false));
        lista.add(new PatronAlimentario("Hidratación matutina", 90, "Vaso de agua al despertar", true));
        lista.add(new PatronAlimentario("Snack nocturno", 40, "Picoteo antes de dormir", false));
        lista.add(new PatronAlimentario("Proteína en almuerzo", 95, "Siempre incluye proteína", true));
        lista.add(new PatronAlimentario("Fines de semana flexibles", 70, "Más calorías Sáb/Dom", false));

        _patrones.setValue(lista);
        _patronPrincipal.setValue("Desayuno consistente (85%)");
        _scoreConsistencia.setValue(72);
    }

    public void recargar() {
        _cargando.setValue(true);
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            cargarPatronesSimulados();
            _cargando.postValue(false);
        }, 500);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PatronesViewModel.class)) {
                return (T) new PatronesViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}