package com.lupefit.feature.progreso.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.ProgresoRepositoryProvider;
import com.lupefit.core.domain.repository.ProgresoRepository;
import com.lupefit.core.domain.model.PesoEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PesoViewModel extends ViewModel {

    private final ProgresoRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public final MutableLiveData<String> pesoStr = new MutableLiveData<>("");
    public final MutableLiveData<String> fecha = new MutableLiveData<>("");
    public final MutableLiveData<String> nota = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public PesoViewModel(ProgresoRepository repository) {
        this.repository = repository;
        // Set today's date by default
        Calendar cal = Calendar.getInstance();
        fecha.setValue(dateFormat.format(cal.getTime()));
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PesoViewModel.class)) {
                ProgresoRepositoryProvider provider =
                        (ProgresoRepositoryProvider) context.getApplicationContext();
                ProgresoRepository repository = provider.getProgresoRepository();
                return (T) new PesoViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void savePeso() {
        String pesoInput = pesoStr.getValue();
        if (pesoInput == null || pesoInput.trim().isEmpty()) {
            _error.setValue("Ingresá un peso válido");
            return;
        }

        float pesoValue;
        try {
            pesoValue = Float.parseFloat(pesoInput.trim());
        } catch (NumberFormatException e) {
            _error.setValue("Peso inválido");
            return;
        }

        if (pesoValue <= 0) {
            _error.setValue("Ingresá un peso válido");
            return;
        }

        String fechaStr = fecha.getValue();
        if (fechaStr == null || fechaStr.isEmpty()) {
            _error.setValue("Seleccioná una fecha");
            return;
        }

        _saved.setValue(false);
        _error.setValue(null);

        executor.execute(() -> {
            try {
                PesoEntry entry = new PesoEntry();
                entry.setPeso(pesoValue);
                entry.setFecha(fechaStr);
                entry.setNota(nota.getValue() != null ? nota.getValue() : "");
                entry.setDateKey(getTodayDateKey());
                entry.setTimestamp(System.currentTimeMillis());

                repository.insertPeso(entry);
                _saved.postValue(true);
            } catch (Exception e) {
                _error.postValue("Error guardando: " + e.getMessage());
            }
        });
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