package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ExportarViewModel extends ViewModel {

    public enum FormatoExportacion {
        CSV, JSON
    }

    public enum TipoDato {
        TODO, DIARIO, EJERCICIO, PROGRESO, RECETAS, AGUA, GLUCOSA, MOOD, NOTAS
    }

    private final MutableLiveData<FormatoExportacion> _formato = new MutableLiveData<>(FormatoExportacion.CSV);
    public final LiveData<FormatoExportacion> formato = _formato;

    private final MutableLiveData<TipoDato> _tipoDato = new MutableLiveData<>(TipoDato.TODO);
    public final LiveData<TipoDato> tipoDato = _tipoDato;

    private final MutableLiveData<Boolean> _incluirImagenes = new MutableLiveData<>(false);
    public final LiveData<Boolean> incluirImagenes = _incluirImagenes;

    private final MutableLiveData<String> _fechaInicio = new MutableLiveData<>("");
    public final LiveData<String> fechaInicio = _fechaInicio;

    private final MutableLiveData<String> _fechaFin = new MutableLiveData<>("");
    public final LiveData<String> fechaFin = _fechaFin;

    private final MutableLiveData<Boolean> _exportando = new MutableLiveData<>(false);
    public final LiveData<Boolean> exportando = _exportando;

    private final MutableLiveData<String> _progreso = new MutableLiveData<>("");
    public final LiveData<String> progreso = _progreso;

    private final MutableLiveData<String> _archivoGenerado = new MutableLiveData<>("");
    public final LiveData<String> archivoGenerado = _archivoGenerado;

    public final LiveData<Boolean> archivoGeneradoVisible = androidx.lifecycle.Transformations.map(_archivoGenerado, s -> s != null && !s.isEmpty());

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public ExportarViewModel() {
        // TODO: Set default dates
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ExportarViewModel.class)) {
                return (T) new ExportarViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void exportar() {
        _exportando.setValue(true);
        _progreso.setValue("Preparando datos...");
        _archivoGenerado.setValue("");
        _error.setValue(null);

        // TODO: Implement actual export logic
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _exportando.postValue(false);
            _progreso.postValue("Completado");
            _archivoGenerado.postValue("lupefit_export_" + System.currentTimeMillis() + "." + (_formato.getValue() == FormatoExportacion.CSV ? "csv" : "json"));
        }, 2000);
    }

    public void setFormato(FormatoExportacion formato) {
        _formato.setValue(formato);
    }

    public void setTipoDato(TipoDato tipo) {
        _tipoDato.setValue(tipo);
    }

    public void toggleImagenes() {
        _incluirImagenes.setValue(!(_incluirImagenes.getValue() != null && _incluirImagenes.getValue()));
    }
}