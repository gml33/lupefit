package com.lupefit.feature.recetas.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.IngredienteReceta;
import com.lupefit.core.domain.model.FoodProduct;
import com.lupefit.core.domain.model.Receta;
import com.lupefit.core.domain.provider.RecetaRepositoryProvider;
import com.lupefit.core.domain.repository.RecetaRepository;
import com.lupefit.core.domain.repository.FoodCatalogRepository;
import com.lupefit.core.domain.provider.FoodCatalogRepositoryProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrearRecetaViewModel extends ViewModel {

    private final RecetaRepository repository;
    private final FoodCatalogRepository catalog;
    private Receta edited;
    private boolean draftLoaded;
    private final MutableLiveData<Boolean> saving = new MutableLiveData<>(false);
    public final LiveData<Boolean> isSaving = saving;
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

    public CrearRecetaViewModel(RecetaRepository repository, FoodCatalogRepository catalog) {
        this.repository = repository;
        this.catalog = catalog;
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
                FoodCatalogRepository catalog = ((FoodCatalogRepositoryProvider)
                        context.getApplicationContext()).getFoodCatalogRepository();
                return (T) new CrearRecetaViewModel(repository, catalog);
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

    public void loadRecipe(Receta recipe) {
        if (recipe == null || draftLoaded) return;
        draftLoaded = true;
        if (recipe.getId() != 0) edited = recipe;
        nombre.setValue(recipe.getNombre());
        descripcion.setValue(recipe.getDescripcion());
        if (recipe.getCategoria() != null) categoria.setValue(recipe.getCategoria());
        tiempoPrepMinStr.setValue(String.valueOf(recipe.getTiempoPrepMin()));
        tiempoCoccionMinStr.setValue(String.valueOf(recipe.getTiempoCoccionMin()));
        porcionesStr.setValue(String.valueOf(recipe.getPorciones()));
        if (recipe.getDificultad() != null) dificultad.setValue(recipe.getDificultad());
        instrucciones.setValue(recipe.getInstrucciones());
        fotoUri.setValue(recipe.getFotoUri());
        List<IngredienteReceta> ingredients = new ArrayList<>();
        for (IngredienteReceta original : recipe.getIngredientes()) {
            IngredienteReceta copy = new IngredienteReceta();
            copy.setAlimentoNombre(original.getAlimentoNombre());
            copy.setUnidad(original.getUnidad());
            copy.setCantidad(original.getCantidad());
            copy.setNota(original.getNota());
            copy.setAlimentoId(original.getAlimentoId());
            copy.setOrden(original.getOrden());
            ingredients.add(copy);
        }
        _ingredientes.setValue(ingredients);
    }

    public void saveReceta() {
        if (Boolean.TRUE.equals(saving.getValue())) return;
        String recipeName = value(nombre);
        String instructions = value(instrucciones);
        List<IngredienteReceta> ingredients = _ingredientes.getValue();
        if (recipeName.isEmpty() || instructions.isEmpty() || ingredients == null || ingredients.isEmpty()) {
            _error.setValue("Completá el nombre, los ingredientes y las instrucciones.");
            return;
        }

        Receta recipe = new Receta();
        try {
            recipe.setPorciones(number(value(porcionesStr), false));
            recipe.setTiempoPrepMin(number(value(tiempoPrepMinStr), true));
            recipe.setTiempoCoccionMin(number(value(tiempoCoccionMinStr), true));
            for (IngredienteReceta source : ingredients) {
                float amount = Float.parseFloat(source.getCantidadStr().trim().replace(',', '.'));
                if (source.getAlimentoNombre() == null || source.getAlimentoNombre().trim().isEmpty()
                        || !Float.isFinite(amount) || amount <= 0) throw new NumberFormatException();
                IngredienteReceta entry = new IngredienteReceta();
                entry.setAlimentoNombre(source.getAlimentoNombre().trim());
                entry.setCantidad(amount);
                entry.setUnidad(source.getUnidad());
                entry.setOrden(recipe.getIngredientes().size());
                entry.setAlimentoId(source.getAlimentoId());
                entry.setNota(source.getNota());
                recipe.addIngrediente(entry);
            }
        } catch (NumberFormatException e) {
            _error.setValue("Revisá las porciones, los tiempos y la cantidad de cada ingrediente.");
            return;
        }

        recipe.setNombre(recipeName);
        recipe.setDescripcion(value(descripcion));
        recipe.setCategoria(value(categoria));
        recipe.setDificultad(value(dificultad));
        recipe.setInstrucciones(instructions);
        recipe.setFotoUri(value(fotoUri));
        if (edited != null) {
            recipe.setId(edited.getId());
            recipe.setTimestamp(edited.getTimestamp());
            recipe.setEsFavorita(edited.isEsFavorita());
            recipe.setEsPublica(edited.isEsPublica());
            recipe.setUserId(edited.getUserId());
            recipe.setServerId(edited.getServerId());
            recipe.setSyncAction(edited.getServerId() == null ? "CREATE" : "UPDATE");
        } else {
            recipe.setUserId(1L);
        }
        saving.setValue(true);
        _error.setValue(null);
        executor.execute(() -> {
            try {
                addNutrition(recipe);
                if (recipe.getId() == 0) repository.insertReceta(recipe);
                else repository.updateReceta(recipe);
                _saved.postValue(true);
            } catch (RuntimeException e) {
                _error.postValue("No se pudo guardar la receta.");
            } finally {
                saving.postValue(false);
            }
        });
    }

    private void addNutrition(Receta recipe) {
        double calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;
        double fiber = 0;
        for (IngredienteReceta ingredient : recipe.getIngredientes()) {
            String unit = ingredient.getUnidad();
            if (!"g".equals(unit) && !"kg".equals(unit)) {
                recipe.setCaloriasPorPorcion(-1);
                return;
            }
            // The local catalog invokes its result callback synchronously on this worker.
            List<FoodProduct> matches = new ArrayList<>();
            catalog.searchByName(ingredient.getAlimentoNombre(), 8, (foods, error) -> {
                if (foods != null) matches.addAll(foods);
            });
            FoodProduct exact = null;
            for (FoodProduct food : matches) {
                if (ingredient.getAlimentoNombre().equalsIgnoreCase(food.getName())) {
                    if (exact != null) {
                        recipe.setCaloriasPorPorcion(-1);
                        return;
                    }
                    exact = food;
                }
            }
            if (exact == null) {
                recipe.setCaloriasPorPorcion(-1);
                return;
            }
            double factor = ingredient.getCantidad() * ("kg".equals(unit) ? 10d : 0.01d);
            calories += exact.getKcalPer100g() * factor;
            protein += exact.getProteinPer100g() * factor;
            carbs += exact.getCarbsPer100g() * factor;
            fat += exact.getFatPer100g() * factor;
            fiber += exact.getFiberPer100g() * factor;
        }
        int servings = recipe.getPorciones();
        recipe.setCaloriasPorPorcion((int) Math.round(calories / servings));
        recipe.setProteinasPorPorcion((float) (protein / servings));
        recipe.setCarbohidratosPorPorcion((float) (carbs / servings));
        recipe.setGrasasPorPorcion((float) (fat / servings));
        recipe.setFibraPorPorcion((float) (fiber / servings));
    }

    private String value(LiveData<String> data) {
        String text = data.getValue();
        return text == null ? "" : text.trim();
    }

    private int number(String raw, boolean allowZero) {
        int value = allowZero && raw.isEmpty() ? 0 : Integer.parseInt(raw);
        if (value < (allowZero ? 0 : 1)) throw new NumberFormatException();
        return value;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}