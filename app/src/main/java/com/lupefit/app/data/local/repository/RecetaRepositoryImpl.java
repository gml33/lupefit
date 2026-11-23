package com.lupefit.app.data.local.repository;

import android.content.Context;

import com.lupefit.app.data.local.AppDatabase;
import com.lupefit.app.data.local.dao.RecetaDao;
import com.lupefit.app.data.local.entity.RecetaEntity;
import com.lupefit.core.domain.model.IngredienteReceta;
import com.lupefit.core.domain.model.Receta;
import com.lupefit.core.domain.repository.RecetaRepository;

import java.util.ArrayList;
import java.util.List;

public class RecetaRepositoryImpl implements RecetaRepository {

    private final RecetaDao recetaDao;

    public RecetaRepositoryImpl(Context context) {
        this.recetaDao = AppDatabase.getInstance(context).recetaDao();
    }

    @Override
    public void insertReceta(Receta receta) {
        receta.setId(recetaDao.insertReceta(convertToEntity(receta)));
    }

    @Override
    public void updateReceta(Receta receta) {
        recetaDao.updateReceta(convertToEntity(receta));
    }

    @Override
    public void deleteReceta(long id) {
        recetaDao.deleteRecetaById(id);
    }

    @Override
    public List<Receta> getAllRecetas() {
        return convertToDomainList(recetaDao.getAllRecetasSync());
    }

    @Override
    public Receta getRecetaById(long id) {
        RecetaEntity entity = recetaDao.getRecetaByIdSync(id);
        return entity != null ? convertToDomain(entity) : null;
    }

    @Override
    public List<Receta> searchRecetas(String query) {
        return convertToDomainList(recetaDao.searchRecetasSync("%" + query + "%"));
    }

    @Override
    public List<Receta> getRecetasByCategory(String categoria) {
        return convertToDomainList(recetaDao.getRecetasByCategorySync(categoria));
    }

    @Override
    public List<Receta> getFavoriteRecetas() {
        return convertToDomainList(recetaDao.getFavoriteRecetasSync());
    }

    private List<Receta> convertToDomainList(List<RecetaEntity> entities) {
        List<Receta> result = new ArrayList<>();
        if (entities != null) {
            for (RecetaEntity entity : entities) {
                result.add(convertToDomain(entity));
            }
        }
        return result;
    }

    private Receta convertToDomain(RecetaEntity entity) {
        Receta domain = new Receta();
        domain.setId(entity.id);
        domain.setNombre(entity.nombre);
        domain.setDescripcion(entity.descripcion);
        domain.setCategoria(entity.categoria);
        domain.setTiempoPrepMin(entity.tiempoPrepMin);
        domain.setTiempoCoccionMin(entity.tiempoCoccionMin);
        domain.setPorciones(entity.porciones);
        domain.setDificultad(entity.dificultad);
        domain.setInstrucciones(entity.instrucciones);
        domain.setFotoUri(entity.fotoUri);
        domain.setEsFavorita(entity.esFavorita);
        domain.setEsPublica(entity.esPublica);
        domain.setUserId(entity.userId);
        domain.setCaloriasPorPorcion(entity.caloriasPorPorcion);
        domain.setProteinasPorPorcion(entity.proteinasPorPorcion);
        domain.setCarbohidratosPorPorcion(entity.carbohidratosPorPorcion);
        domain.setGrasasPorPorcion(entity.grasasPorPorcion);
        domain.setFibraPorPorcion(entity.fibraPorPorcion);
        domain.setSynced(entity.isSynced);
        domain.setSyncAction(entity.syncAction);
        domain.setServerId(entity.serverId);
        domain.setTimestamp(entity.timestamp);
        domain.setLastModified(entity.lastModified);

        // Convert ingredientes
        List<IngredienteReceta> ingredientes = new ArrayList<>();
        List<com.lupefit.app.data.local.entity.IngredienteRecetaEntity> storedIngredients = entity.getIngredientes();
        if (storedIngredients == null) return domain;
        for (com.lupefit.app.data.local.entity.IngredienteRecetaEntity ingEntity : storedIngredients) {
            if (ingEntity == null) continue;
            IngredienteReceta ing = new IngredienteReceta();
            ing.setId(ingEntity.id);
            ing.setRecetaId(ingEntity.recetaId);
            ing.setAlimentoId(ingEntity.alimentoId);
            ing.setAlimentoNombre(ingEntity.alimentoNombre);
            ing.setCantidad(ingEntity.cantidad);
            ing.setUnidad(ingEntity.unidad);
            ing.setNota(ingEntity.nota);
            ing.setOrden(ingEntity.orden);
            ingredientes.add(ing);
        }
        domain.setIngredientes(ingredientes);

        return domain;
    }

    private RecetaEntity convertToEntity(Receta domain) {
        RecetaEntity entity = new RecetaEntity();
        entity.id = domain.getId();
        entity.nombre = domain.getNombre();
        entity.descripcion = domain.getDescripcion();
        entity.categoria = domain.getCategoria();
        entity.tiempoPrepMin = domain.getTiempoPrepMin();
        entity.tiempoCoccionMin = domain.getTiempoCoccionMin();
        entity.porciones = domain.getPorciones();
        entity.dificultad = domain.getDificultad();
        entity.instrucciones = domain.getInstrucciones();
        entity.fotoUri = domain.getFotoUri();
        entity.esFavorita = domain.isEsFavorita();
        entity.esPublica = domain.isEsPublica();
        entity.userId = domain.getUserId();
        entity.caloriasPorPorcion = domain.getCaloriasPorPorcion();
        entity.proteinasPorPorcion = domain.getProteinasPorPorcion();
        entity.carbohidratosPorPorcion = domain.getCarbohidratosPorPorcion();
        entity.grasasPorPorcion = domain.getGrasasPorPorcion();
        entity.fibraPorPorcion = domain.getFibraPorPorcion();
        entity.isSynced = domain.isSynced();
        entity.syncAction = domain.getSyncAction();
        entity.serverId = domain.getServerId();
        entity.timestamp = domain.getTimestamp();
        entity.lastModified = domain.getLastModified();

        // Convert ingredientes
        List<com.lupefit.app.data.local.entity.IngredienteRecetaEntity> ingEntities = new ArrayList<>();
        for (IngredienteReceta ing : domain.getIngredientes()) {
            com.lupefit.app.data.local.entity.IngredienteRecetaEntity ingEntity = new com.lupefit.app.data.local.entity.IngredienteRecetaEntity();
            ingEntity.id = ing.getId();
            ingEntity.recetaId = ing.getRecetaId();
            ingEntity.alimentoId = ing.getAlimentoId();
            ingEntity.alimentoNombre = ing.getAlimentoNombre();
            ingEntity.cantidad = ing.getCantidad();
            ingEntity.unidad = ing.getUnidad();
            ingEntity.nota = ing.getNota();
            ingEntity.orden = ing.getOrden();
            ingEntities.add(ingEntity);
        }
        entity.setIngredientes(ingEntities);

        return entity;
    }
}