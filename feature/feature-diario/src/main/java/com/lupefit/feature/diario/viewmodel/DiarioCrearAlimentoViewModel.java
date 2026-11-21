package com.lupefit.feature.diario.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.FoodProduct;
import com.lupefit.core.domain.provider.FoodCatalogRepositoryProvider;
import com.lupefit.core.domain.repository.FoodCatalogRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiarioCrearAlimentoViewModel extends ViewModel {

    private final FoodCatalogRepository catalogRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public final MutableLiveData<Double> kcal = new MutableLiveData<>(0.0);
    public final MutableLiveData<Double> protein = new MutableLiveData<>(0.0);
    public final MutableLiveData<Double> carbs = new MutableLiveData<>(0.0);
    public final MutableLiveData<Double> fat = new MutableLiveData<>(0.0);
    public final MutableLiveData<Double> defaultServingGrams = new MutableLiveData<>(100.0);
    public final MutableLiveData<Boolean> isFreshFood = new MutableLiveData<>(false);

    private final MutableLiveData<FoodProduct> _savedFood = new MutableLiveData<>();
    public final LiveData<FoodProduct> savedFood = _savedFood;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private final MutableLiveData<Boolean> _isSaving = new MutableLiveData<>(false);
    public final LiveData<Boolean> isSaving = _isSaving;

    public DiarioCrearAlimentoViewModel(FoodCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DiarioCrearAlimentoViewModel.class)) {
                FoodCatalogRepositoryProvider provider =
                        (FoodCatalogRepositoryProvider) context.getApplicationContext();
                FoodCatalogRepository repository = provider.getFoodCatalogRepository();
                return (T) new DiarioCrearAlimentoViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void saveFood() {
        _isSaving.setValue(true);
        _error.setValue(null);

        executor.execute(() -> {
            try {
                FoodProduct food = new FoodProduct();
                food.setName(""); // Will be set from UI
                food.setBrand(""); // Will be set from UI
                food.setKcalPer100g(kcal.getValue() != null ? kcal.getValue() : 0);
                food.setProteinPer100g(protein.getValue() != null ? protein.getValue() : 0);
                food.setCarbsPer100g(carbs.getValue() != null ? carbs.getValue() : 0);
                food.setFatPer100g(fat.getValue() != null ? fat.getValue() : 0);
                food.setDefaultServingGrams(defaultServingGrams.getValue() != null ? defaultServingGrams.getValue() : 100);
                food.setFreshFood(isFreshFood.getValue() != null && isFreshFood.getValue());
                food.setLastUpdated(System.currentTimeMillis());

                // Insert into Room via repository
                catalogRepository.searchByName(food.getName(), 1, (products, error) -> {
                    // For now, just return the created food
                    _savedFood.postValue(food);
                    _isSaving.postValue(false);
                });

            } catch (Exception e) {
                _error.postValue("Error guardando: " + e.getMessage());
                _isSaving.postValue(false);
            }
        });
    }

    public void setFoodData(String name, String brand, String barcode) {
        // This would be called from fragment to set the data before saving
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}