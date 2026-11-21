package com.lupefit.feature.nutricion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.prefs.Preferences;

public class MetasViewModel extends ViewModel {

    private static final String PREFS_NAME = "nutricion_metas";
    private static final String KEY_CALORIAS = "meta_calorias";
    private static final String KEY_PROTEINAS = "meta_proteinas";
    private static final String KEY_CARBOHIDRATOS = "meta_carbohidratos";
    private static final String KEY_GRASAS = "meta_grasas";
    private static final String KEY_FIBRA = "meta_fibra";
    private static final String KEY_AGUA = "meta_agua";
    private static final String KEY_NET_CARBS = "net_carbs_mode";

    private final SharedPreferences prefs;

    public final MutableLiveData<Integer> metaCalorias = new MutableLiveData<>(2000);
    public final MutableLiveData<Float> metaProteinas = new MutableLiveData<>(150f);
    public final MutableLiveData<Float> metaCarbohidratos = new MutableLiveData<>(250f);
    public final MutableLiveData<Float> metaGrasas = new MutableLiveData<>(70f);
    public final MutableLiveData<Float> metaFibra = new MutableLiveData<>(25f);
    public final MutableLiveData<Integer> metaAgua = new MutableLiveData<>(8);
    public final MutableLiveData<Boolean> netCarbsMode = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    public MetasViewModel(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadMetas();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MetasViewModel.class)) {
                return (T) new MetasViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    private void loadMetas() {
        metaCalorias.setValue(prefs.getInt(KEY_CALORIAS, 2000));
        metaProteinas.setValue(prefs.getFloat(KEY_PROTEINAS, 150f));
        metaCarbohidratos.setValue(prefs.getFloat(KEY_CARBOHIDRATOS, 250f));
        metaGrasas.setValue(prefs.getFloat(KEY_GRASAS, 70f));
        metaFibra.setValue(prefs.getFloat(KEY_FIBRA, 25f));
        metaAgua.setValue(prefs.getInt(KEY_AGUA, 8));
        netCarbsMode.setValue(prefs.getBoolean(KEY_NET_CARBS, false));
    }

    public void saveMetas() {
        prefs.edit()
                .putInt(KEY_CALORIAS, metaCalorias.getValue() != null ? metaCalorias.getValue() : 2000)
                .putFloat(KEY_PROTEINAS, metaProteinas.getValue() != null ? metaProteinas.getValue() : 150f)
                .putFloat(KEY_CARBOHIDRATOS, metaCarbohidratos.getValue() != null ? metaCarbohidratos.getValue() : 250f)
                .putFloat(KEY_GRASAS, metaGrasas.getValue() != null ? metaGrasas.getValue() : 70f)
                .putFloat(KEY_FIBRA, metaFibra.getValue() != null ? metaFibra.getValue() : 25f)
                .putInt(KEY_AGUA, metaAgua.getValue() != null ? metaAgua.getValue() : 8)
                .putBoolean(KEY_NET_CARBS, netCarbsMode.getValue() != null && netCarbsMode.getValue())
                .apply();
        _saved.setValue(true);
    }
}