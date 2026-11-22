package com.lupefit.feature.recetas.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.IngredienteReceta;
import com.lupefit.core.domain.model.Receta;
import com.lupefit.core.domain.provider.RecetaRepositoryProvider;
import com.lupefit.core.domain.repository.RecetaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrearRecetaViewModel extends ViewModel {

    private final RecetaRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // UI State
    public final MutableLiveData<String> nombre = new MutableLiveData<>("");
    public final MutableLiveData<String> descripcion = new MutableLiveData<>("");
    public final MutableLiveData<String> categoria = new MutableLiveData<>("");
    public final MutableLiveData<String> tiempoPrepMinStr = new MutableLiveData<>("");
    public final MutableLiveData<String> tiempoCoccionMinStr = new MutableLiveData<>("");
    public final MutableLiveData<String> porcionesStr = new MutableLiveData<>("");
    public final MutableLiveData<String> dificultad = new MutableLiveData<>("");
    public final MutableLiveData<String> instrucciones = new MutableLiveData<>("");
    public final MutableLiveData<String> fotoUri = new MutableLiveData<>("");

    // Ingredientes
    private final MutableLiveData<List<IngredienteReceta>> _ingredientes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<IngredienteReceta>> ingredientes = _ingredientes;

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public CrearRecetaViewModel(RecetaRepository repository) {
        this.repository = repository;
        // Set default values
        categoria.setValue("Almuerzo");
        dificultad.setValue("Fácil");
        porcionesStr.setValue("4");
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CrearRecetaViewModel.class)) {
                RecetaRepositoryProvider provider =
                        (RecetaRepositoryProvider) context.getApplicationContext();
                RecetaRepository repository = provider.getRecetaRepository();
                return (T) new CrearRecetaViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void addIngrediente() {
        IngredienteReceta nuevo = new IngredienteReceta();
        nuevo.setAlimentoNombre("");
        nuevo.setCantidad(0f);
        nuevo.setUnidad("g");
        nuevo.setOrden(_ingredientes.getValue() != null ? _ingredientes.getValue().size() : 0);

        List<IngredienteReceta> current = new ArrayList<>(_ingredientes.getValue() != null ? _ingredientes.getValue() : new ArrayList<>());
        current.add(nuevo);
        _ingredientes.setValue(current);
    }

    public void removeIngrediente(IngredienteReceta ingrediente) {
        List<IngredienteReceta> current = _ingredientes.getValue();
        if (current != null) {
            current.remove(ingrediente);
            // Reordenar
            for (int i = 0; i < current.size(); i++) {
                current.get(i).setOrden(i);
            }
            _ingredientes.setValue(new ArrayList<>(current));
        }
    }

    public void saveReceta() {
        // Validaciones
        String nombreStr = nombre.getValue();
        if (nombreStr == null || nombreStr.trim().isEmpty()) {
            _error.setValue("El nombre es obligatorio");
            return;
        }

        String porcionesInput = porcionesStr.getValue();
        int porciones;
        try {
            porciones = Integer.parseInt(porcionesInput != null ? porcionesInput : "1");
        } catch (NumberFormatException e) {
            porciones = 1;
        }

        if (porciones <= 0) {
            _error.setValue("Las porciones deben ser mayor a 0");
            return;
        }

        List<IngredienteReceta> ingredientesList = _ingredientes.getValue();
        if (ingredientesList == null || ingredientesList.isEmpty()) {
            _error.setValue("Agregá al menos un ingrediente");
            return;
        }

        // Validar ingredientes
        for (IngredienteReceta ing : ingredientesList) {
            if (ing.getAlimentoNombre() == null || ing.getAlimentoNombre().trim().isEmpty()) {
                _error.setValue("Todos los ingredientes deben tener un alimento");
                return;
            }
            if (ing.getCantidad() <= 0) {
                _error.setValue("La cantidad debe ser mayor a 0");
                return;
            }
        }

        String instruccionesStr = instrucciones.getValue();
        if (instruccionesStr == null || instruccionesStr.trim().isEmpty()) {
            _error.setValue("Las instrucciones son obligatorias");
            return;
        }

        _saved.setValue(false);
        _error.setValue(null);

        final int porcionesFinal = porciones;
        final String instruccionesFinal = instruccionesStr;
        final List<IngredienteReceta> ingredientesFinal = ingredientesList;

        executor.execute(() -> {
            try {
                Receta receta = new Receta();
                receta.setNombre(nombreStr.trim());
                receta.setDescripcion(descripcion.getValue() != null ? descripcion.getValue().trim() : "");
                receta.setCategoria(categoria.getValue() != null ? categoria.getValue() : "Almuerzo");
                receta.setTiempoPrepMin(Integer.parseInt(tiempoPrepMinStr.getValue() != null ? tiempoPrepMinStr.getValue() : "0"));
                receta.setTiempoCoccionMin(Integer.parseInt(tiempoCoccionMinStr.getValue() != null ? tiempoCoccionMinStr.getValue() : "0"));
                receta.setPorciones(porcionesFinal);
                receta.setDificultad(dificultad.getValue() != null ? dificultad.getValue() : "Fácil");
                receta.setInstrucciones(instruccionesFinal.trim());
                receta.setFotoUri(fotoUri.getValue() != null ? fotoUri.getValue() : "");
                receta.setIngredientes(ingredientesFinal);
                receta.setUserId(1L); // TODO: Get actual user ID
                receta.setTimestamp(System.currentTimeMillis());

                // Calcular nutrición básica (placeholder)
                receta.setCaloriasPorPorcion(350);
                receta.setProteinasPorPorcion(25f);
                receta.setCarbohidratosPorPorcion(30f);
                receta.setGrasasPorPorcion(15f);
                receta.setFibraPorPorcion(5f);

                repository.insertReceta(receta);
                _saved.postValue(true);
            } catch (Exception e) {
                _error.postValue("Error guardando: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}