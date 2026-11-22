package com.lupefit.feature.perfil.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.sync.worker.SyncScheduler;

public class SyncAccountViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _cuentaVinculada = new MutableLiveData<>(false);
    public final LiveData<Boolean> cuentaVinculada = _cuentaVinculada;

    private final MutableLiveData<String> _emailServidor = new MutableLiveData<>("");
    public final LiveData<String> emailServidor = _emailServidor;

    private final MutableLiveData<String> _ultimaSync = new MutableLiveData<>("Nunca");
    public final LiveData<String> ultimaSync = _ultimaSync;

    private final MutableLiveData<Boolean> _syncAutomatica = new MutableLiveData<>(true);
    public final LiveData<Boolean> syncAutomatica = _syncAutomatica;

    private final MutableLiveData<Boolean> _vinculando = new MutableLiveData<>(false);
    public final LiveData<Boolean> vinculando = _vinculando;

    private final MutableLiveData<Boolean> _desvinculando = new MutableLiveData<>(false);
    public final LiveData<Boolean> desvinculando = _desvinculando;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public SyncAccountViewModel() {
        // TODO: Check if account is linked
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SyncAccountViewModel.class)) {
                return (T) new SyncAccountViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void vincularCuenta(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            _error.setValue("Email obligatorio");
            return;
        }
        if (password == null || password.length() < 6) {
            _error.setValue("Contraseña inválida");
            return;
        }

        _vinculando.setValue(true);
        _error.setValue(null);

        // TODO: Call backend auth + sync
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _vinculando.postValue(false);
            _cuentaVinculada.postValue(true);
            _emailServidor.postValue(email);
            _ultimaSync.postValue("Ahora mismo");
        }, 2000);
    }

    public void desvincularCuenta() {
        _desvinculando.setValue(true);

        // TODO: Call backend to unlink + clear local sync data
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _desvinculando.postValue(false);
            _cuentaVinculada.postValue(false);
            _emailServidor.postValue("");
            _ultimaSync.postValue("Nunca");
        }, 1000);
    }

    public void sincronizarAhora() {
        // TODO: Trigger SyncWorker
    }

    public void toggleSyncAutomatica() {
        _syncAutomatica.setValue(!(_syncAutomatica.getValue() != null && _syncAutomatica.getValue()));
        // TODO: Enable/disable periodic WorkManager
    }
}