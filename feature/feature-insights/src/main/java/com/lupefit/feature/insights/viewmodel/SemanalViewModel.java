package com.lupefit.feature.insights.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SemanalViewModel extends ViewModel {

    public static class MacroResumen {
        public final String nombre;
        public final String valor;
        public final int progreso; // 0-100
        public final String porcentaje;

        public MacroResumen(String nombre, String valor, int progreso, String porcentaje) {
            this.nombre = nombre;
            this.valor = valor;
            this.progreso = progreso;
            this.porcentaje = porcentaje;
        }
    }

    private final MutableLiveData<String> _resumenCalorias = new MutableLiveData<>("—");
    public final LiveData<String> resumenCalorias = _resumenCalorias;

    private final MutableLiveData<String> _resumenProteinas = new MutableLiveData<>("—");
    public final LiveData<String> resumenProteinas = _resumenProteinas;

    private final MutableLiveData<String> _resumenCarbohidratos = new MutableLiveData<>("—");
    public final LiveData<String> resumenCarbohidratos = _resumenCarbohidratos;

    private final MutableLiveData<String> _resumenGrasas = new MutableLiveData<>("—");
    public final LiveData<String> resumenGrasas = _resumenGrasas;

    private final MutableLiveData<String> _resumenAgua = new MutableLiveData<>("—");
    public final LiveData<String> resumenAgua = _resumenAgua;

    private final MutableLiveData<String> _resumenEjercicio = new MutableLiveData<>("—");
    public final LiveData<String> resumenEjercicio = _resumenEjercicio;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    private final MutableLiveData<String> _periodo = new MutableLiveData<>("Esta semana");
    public final LiveData<String> periodo = _periodo;

    private final MutableLiveData<java.util.List<MacroResumen>> _macroResumenes = new MutableLiveData<>(new java.util.ArrayList<>());
    public final LiveData<java.util.List<MacroResumen>> macroResumenes = _macroResumenes;

    public SemanalViewModel() {
        cargarDatosSimulados();
    }

    private void cargarDatosSimulados() {
        _resumenCalorias.setValue("14.200 kcal");
        _resumenProteinas.setValue("120 g");
        _resumenCarbohidratos.setValue("180 g");
        _resumenGrasas.setValue("55 g");
        _resumenAgua.setValue("14.000 ml");
        _resumenEjercicio.setValue("5 sesiones");

        java.util.List<MacroResumen> lista = new java.util.ArrayList<>();
        lista.add(new MacroResumen("Calorías", "14.200 kcal", 85, "85% de la meta semanal"));
        lista.add(new MacroResumen("Proteínas", "120 g", 92, "92% de la meta semanal"));
        lista.add(new MacroResumen("Carbohidratos", "180 g", 78, "78% de la meta semanal"));
        lista.add(new MacroResumen("Grasas", "55 g", 88, "88% de la meta semanal"));
        lista.add(new MacroResumen("Agua", "14.000 ml", 100, "Meta cumplida"));
        lista.add(new MacroResumen("Ejercicio", "5 sesiones", 71, "5 de 7 días"));
        _macroResumenes.setValue(lista);
    }

    public void setPeriodo(String periodo) {
        _periodo.setValue(periodo);
    }

    public void recargar() {
        _cargando.setValue(true);
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            cargarDatosSimulados();
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
            if (modelClass.isAssignableFrom(SemanalViewModel.class)) {
                return (T) new SemanalViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}