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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.reflect.Type;

public class QuickAddViewModel extends ViewModel {

    private final FoodCatalogRepository catalogRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.content.SharedPreferences preferences;
    private final Gson gson = new Gson();
    private static final Type FOOD_LIST_TYPE = new TypeToken<List<FoodProduct>>() {}.getType();

    private final MutableLiveData<List<FoodProduct>> _freshFoods = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<FoodProduct>> freshFoods = _freshFoods;

    private final MutableLiveData<List<FoodProduct>> _recentFoods = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<FoodProduct>> recentFoods = _recentFoods;

    private final MutableLiveData<List<FoodProduct>> _favoriteFoods = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<FoodProduct>> favoriteFoods = _favoriteFoods;

    public QuickAddViewModel(FoodCatalogRepository catalogRepository, Context context) {
        this.catalogRepository = catalogRepository;
        preferences = context.getApplicationContext().getSharedPreferences("lupefit_quick_add", Context.MODE_PRIVATE);
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
                return (T) new QuickAddViewModel(repository, context);
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
        _recentFoods.postValue(readList("recent_foods"));
    }

    private void loadFavoriteFoods() {
        _favoriteFoods.postValue(readList("favorite_foods"));
    }

    public void addToFavorites(FoodProduct food) {
        saveToList("favorite_foods", food, _favoriteFoods);
    }

    public void addToRecent(FoodProduct food) {
        saveToList("recent_foods", food, _recentFoods);
    }

    private List<FoodProduct> readList(String key) {
        String json = preferences.getString(key, "[]");
        List<FoodProduct> values = gson.fromJson(json, FOOD_LIST_TYPE);
        return values == null ? new ArrayList<>() : values;
    }

    private void saveToList(String key, FoodProduct food, MutableLiveData<List<FoodProduct>> target) {
        if (food == null) return;
        executor.execute(() -> {
            List<FoodProduct> values = readList(key);
            values.removeIf(existing -> String.valueOf(existing.getId()).equals(String.valueOf(food.getId())));
            values.add(0, food);
            if (values.size() > 10) values = new ArrayList<>(values.subList(0, 10));
            preferences.edit().putString(key, gson.toJson(values, FOOD_LIST_TYPE)).apply();
            target.postValue(values);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
