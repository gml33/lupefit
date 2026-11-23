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
import java.text.ParsePosition;
import java.util.Date;
import java.util.List;
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

    private final MutableLiveData<List<PesoEntry>> history = new MutableLiveData<>();
    public final LiveData<List<PesoEntry>> entries = history;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public PesoViewModel(ProgresoRepository repository) {
        this.repository = repository;
        dateFormat.setLenient(false);
        // Set today's date by default
        Calendar cal = Calendar.getInstance();
        fecha.setValue(dateFormat.format(cal.getTime()));
        refreshHistory();
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
            pesoValue = Float.parseFloat(pesoInput.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            _error.setValue("Peso inválido");
            return;
        }

        if (!Float.isFinite(pesoValue) || pesoValue <= 0) {
            _error.setValue("Ingresá un peso válido");
            return;
        }

        String fechaStr = fecha.getValue();
        if (fechaStr == null || fechaStr.isEmpty()) {
            _error.setValue("Seleccioná una fecha");
            return;
        }

        ParsePosition position = new ParsePosition(0);
        Date selectedDate = dateFormat.parse(fechaStr, position);
        if (selectedDate == null || position.getIndex() != fechaStr.length()) {
            _error.setValue("Ingresá una fecha válida (dd/mm/aaaa)");
            return;
        }
        Calendar selected = Calendar.getInstance();
        selected.setTime(selectedDate);
        int dateKey = selected.get(Calendar.YEAR) * 10000
                + (selected.get(Calendar.MONTH) + 1) * 100 + selected.get(Calendar.DAY_OF_MONTH);
        String note = nota.getValue();

        _saved.setValue(false);
        _error.setValue(null);

        executor.execute(() -> {
            try {
                PesoEntry entry = new PesoEntry();
                entry.setPeso(pesoValue);
                entry.setFecha(fechaStr);
                entry.setNota(note != null ? note : "");
                entry.setDateKey(dateKey);
                entry.setTimestamp(System.currentTimeMillis());

                repository.insertPeso(entry);
                _saved.postValue(true);
                loadHistory();
            } catch (Exception e) {
                _error.postValue("Error guardando: " + e.getMessage());
            }
        });
    }

    private void refreshHistory() {
        executor.execute(this::loadHistory);
    }

    private void loadHistory() {
        try {
            repository.getPesoHistory(history::postValue);
        } catch (Exception e) {
            _error.postValue("Error cargando el historial de peso");
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}