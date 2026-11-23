package com.lupefit.feature.progreso.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lupefit.core.domain.model.PesoEntry;
import com.lupefit.core.domain.repository.ProgresoRepository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgresoHomeViewModel extends ViewModel {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<PesoEntry>> weights = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Boolean> error = new MutableLiveData<>(false);

    private final MutableLiveData<List<ProgresoRepository.Measurement>> measurements = new MutableLiveData<>();

    public LiveData<List<ProgresoRepository.Measurement>> getMeasurements() {
        return measurements;
    }

    public void refreshMeasurements(ProgresoRepository repository) {
        executor.execute(() -> loadMeasurements(repository));
    }

    public void saveMeasurement(ProgresoRepository repository, String kind, double centimeters) {
        executor.execute(() -> {
            try {
                repository.saveMeasurement(kind, centimeters);
                loadMeasurements(repository);
            } catch (RuntimeException e) {
                error.postValue(true);
            }
        });
    }

    private void loadMeasurements(ProgresoRepository repository) {
        try {
            measurements.postValue(repository.listMeasurements());
        } catch (RuntimeException e) {
            error.postValue(true);
        }
    }

    public LiveData<List<PesoEntry>> getWeights() {
        return weights;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

    public void refresh(ProgresoRepository repository) {
        executor.execute(() -> {
            try {
                repository.getPesoHistory(weights::postValue);
                error.postValue(false);
            } catch (RuntimeException e) {
                error.postValue(true);
            }
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
