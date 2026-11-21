package com.lupefit.core.domain.provider;

import com.lupefit.core.domain.repository.FoodCatalogRepository;

public interface FoodCatalogRepositoryProvider {
    FoodCatalogRepository getFoodCatalogRepository();
}