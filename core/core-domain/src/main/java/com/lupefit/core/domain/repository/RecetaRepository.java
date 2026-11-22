package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.Receta;

import java.util.List;

public interface RecetaRepository {
    void insertReceta(Receta receta);
    void updateReceta(Receta receta);
    void deleteReceta(long id);
    List<Receta> getAllRecetas();
    Receta getRecetaById(long id);
    List<Receta> searchRecetas(String query);
    List<Receta> getRecetasByCategory(String categoria);
    List<Receta> getFavoriteRecetas();
}