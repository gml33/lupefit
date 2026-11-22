package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.sync.worker.SyncScheduler;
import com.lupefit.core.sync.worker.SyncWorker;

public class SyncViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _syncAuto = new MutableLiveData<>(true);
    public final LiveData<Boolean> syncAuto = _syncAuto;

    private final MutableLiveData<Boolean> _syncSoloWifi = new MutableLiveData<>(true);
    public final LiveData<Boolean> syncSoloWifi = _syncSoloWifi;

    private final MutableLiveData<Boolean> _syncCargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> syncCargando = _syncCargando;

    private final MutableLiveData<String> _ultimaSync = new MutableLiveData<>("Nunca");
    public final LiveData<String> ultimaSync = _ultimaSync;

    private final MutableLiveData<Integer> _pendientesCount = new MutableLiveData<>(0);
    public final LiveData<Integer> pendientesCount = _pendientesCount;

    private final MutableLiveData<String> _estadoSync = new MutableLiveData<>("Idle");
    public final LiveData<String> estadoSync = _estadoSync;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public SyncViewModel() {
        // TODO: Load sync status
        cargarEstadoSync();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SyncViewModel.class)) {
                return (T) new SyncViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void cargarEstadoSync() {
        // TODO: Load from SyncScheduler / WorkManager
        _ultimaSync.setValue("Hace 5 minutos");
        _pendientesCount.setValue(0);
        _estadoSync.setValue("Sincronizado");
    }

    public void sincronizarAhora() {
        _syncCargando.setValue(true);
        _estadoSync.setValue("Sincronizando...");

        // TODO: Trigger WorkManager sync
        // SyncWorker.enqueueSync(context);

        // Simulate async
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _syncCargando.postValue(false);
            _estadoSync.postValue("Sincronizado");
            _ultimaSync.postValue("Ahora mismo");
            _pendientesCount.postValue(0);
        }, 3000);
    }

    public void toggleSyncAuto() {
        _syncAuto.setValue(!(_syncAuto.getValue() != null && _syncAuto.getValue()));
        // TODO: Enable/disable periodic WorkManager
    }

    public void toggleSoloWifi() {
        _syncSoloWifi.setValue(!(_syncSoloWifi.getValue() != null && _syncSoloWifi.getValue()));
    }
}