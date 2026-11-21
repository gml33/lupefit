package com.lupefit.feature.diario.viewmodel;

import android.content.Context;
import android.text.TextUtils;

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

public class BuscarAlimentoViewModel extends ViewModel {

    private final FoodCatalogRepository catalogRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<FoodProduct>> _searchResults = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<FoodProduct>> searchResults = _searchResults;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private String lastQuery = "";
    private String currentCategory = "all";

    public BuscarAlimentoViewModel(FoodCatalogRepository catalogRepository) {
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
            if (modelClass.isAssignableFrom(BuscarAlimentoViewModel.class)) {
                FoodCatalogRepositoryProvider provider =
                        (FoodCatalogRepositoryProvider) context.getApplicationContext();
                FoodCatalogRepository repository = provider.getFoodCatalogRepository();
                return (T) new BuscarAlimentoViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void search(String query) {
        if (TextUtils.isEmpty(query) || query.equals(lastQuery)) return;
        lastQuery = query;
        _isLoading.setValue(true);
        _error.setValue(null);

        executor.execute(() -> {
            catalogRepository.searchByName(query, 50, (products, error) -> {
                if (error != null) {
                    _isLoading.postValue(false);
                    _error.postValue(error);
                    return;
                }
                List<FoodProduct> filtered = filterByCategory(products);
                _searchResults.postValue(filtered);
                _isLoading.postValue(false);
            });
        });
    }

    public void setCategory(String category) {
        if (category.equals(currentCategory)) return;
        currentCategory = category;
        if (!TextUtils.isEmpty(lastQuery)) {
            search(lastQuery);
        }
    }

    private List<FoodProduct> filterByCategory(List<FoodProduct> products) {
        if ("all".equals(currentCategory)) return products;
        List<FoodProduct> filtered = new ArrayList<>();
        for (FoodProduct p : products) {
            // Simple category matching - can be improved with proper category field
            if (p.getName().toLowerCase().contains(currentCategory.toLowerCase()) ||
                    (p.getBrand() != null && p.getBrand().toLowerCase().contains(currentCategory.toLowerCase()))) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    public void clearSearch() {
        lastQuery = "";
        _searchResults.postValue(new ArrayList<>());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}