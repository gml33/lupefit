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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickAddViewModel extends ViewModel {

    private final FoodCatalogRepository catalogRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<FoodProduct>> _freshFoods = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<FoodProduct>> freshFoods = _freshFoods;

    private final MutableLiveData<List<FoodProduct>> _recentFoods = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<FoodProduct>> recentFoods = _recentFoods;

    private final MutableLiveData<List<FoodProduct>> _favoriteFoods = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<FoodProduct>> favoriteFoods = _favoriteFoods;

    public QuickAddViewModel(FoodCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
        loadFreshFoods();
        loadRecentFoods();
        loadFavoriteFoods();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(QuickAddViewModel.class)) {
                FoodCatalogRepositoryProvider provider =
                        (FoodCatalogRepositoryProvider) context.getApplicationContext();
                FoodCatalogRepository repository = provider.getFoodCatalogRepository();
                return (T) new QuickAddViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    private void loadFreshFoods() {
        executor.execute(() -> {
            catalogRepository.getFreshFoods(foods -> {
                _freshFoods.postValue(foods != null ? foods : new ArrayList<>());
            });
        });
    }

    private void loadRecentFoods() {
        // TODO: Load from recent foods table/preferences
        _recentFoods.postValue(new ArrayList<>());
    }

    private void loadFavoriteFoods() {
        // TODO: Load from favorites table/preferences
        _favoriteFoods.postValue(new ArrayList<>());
    }

    public void addToFavorites(FoodProduct food) {
        // TODO: Save to favorites
    }

    public void addToRecent(FoodProduct food) {
        // TODO: Save to recent
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}