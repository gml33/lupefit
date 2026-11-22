package com.lupefit.feature.insights.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;

public class TendenciasViewModel extends ViewModel {

    public enum Periodo {
        SEMANA("7 días"),
        MES("30 días"),
        TRIMESTRE("90 días");

        private final String label;
        Periodo(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private final MutableLiveData<Periodo> _periodoSeleccionado = new MutableLiveData<>(Periodo.SEMANA);
    public final LiveData<Periodo> periodoSeleccionado = _periodoSeleccionado;

    private final MutableLiveData<List<Entry>> _datosCalorias = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Entry>> datosCalorias = _datosCalorias;

    private final MutableLiveData<List<Entry>> _datosPeso = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Entry>> datosPeso = _datosPeso;

    private final MutableLiveData<List<Entry>> _datosProteinas = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Entry>> datosProteinas = _datosProteinas;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    public TendenciasViewModel() {
        cargarDatosSimulados(_periodoSeleccionado.getValue());
    }

    public void setPeriodo(Periodo periodo) {
        _periodoSeleccionado.setValue(periodo);
        cargarDatosSimulados(periodo);
    }

    private void cargarDatosSimulados(Periodo periodo) {
        _cargando.setValue(true);
        List<Entry> calorias = new ArrayList<>();
        List<Entry> peso = new ArrayList<>();
        List<Entry> proteinas = new ArrayList<>();

        int dias = periodo == Periodo.SEMANA ? 7 : (periodo == Periodo.MES ? 30 : 90);

        for (int i = 0; i < dias; i++) {
            calorias.add(new Entry(i, 2000 + (float) (Math.random() * 500)));
            if (i % (periodo == Periodo.SEMANA ? 1 : (periodo == Periodo.MES ? 3 : 10)) == 0) {
                peso.add(new Entry(i, 75f + (float) (Math.random() * 2)));
            }
            proteinas.add(new Entry(i, 100f + (float) (Math.random() * 50)));
        }

        _datosCalorias.setValue(calorias);
        _datosPeso.setValue(peso);
        _datosProteinas.setValue(proteinas);
        _cargando.setValue(false);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(TendenciasViewModel.class)) {
                return (T) new TendenciasViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}