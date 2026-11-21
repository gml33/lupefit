package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.FoodProduct;

import java.util.List;

public interface FoodCatalogRepository {
    void searchByName(String query, int limit, OnFoodSearchListener listener);

    void getFreshFoods(OnFreshFoodsListener listener);

    interface OnFoodSearchListener {
        void onResult(List<FoodProduct> products, String error);
    }

    interface OnFreshFoodsListener {
        void onResult(List<FoodProduct> foods);
    }
}