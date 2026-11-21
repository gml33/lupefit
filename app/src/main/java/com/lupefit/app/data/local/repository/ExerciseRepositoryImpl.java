package com.lupefit.app.data.local.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.lupefit.app.data.local.AppDatabase;
import com.lupefit.app.data.local.dao.ExerciseDao;
import com.lupefit.app.data.local.entity.Exercise;
import com.lupefit.core.domain.repository.ExerciseRepository;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRepositoryImpl implements ExerciseRepository {

    private final ExerciseDao exerciseDao;

    public ExerciseRepositoryImpl(Context context) {
        this.exerciseDao = AppDatabase.getInstance(context).exerciseDao();
    }

    @Override
    public void searchByName(String query, int limit, OnExerciseSearchListener listener) {
        if (query == null || query.trim().isEmpty()) {
            listener.onResult(new ArrayList<>(), null);
            return;
        }
        String searchQuery = "%" + query.trim() + "%";
        // Use sync call for simplicity in search
        List<com.lupefit.app.data.local.entity.Exercise> results = exerciseDao.searchByNameSync(searchQuery, limit);
        List<com.lupefit.core.domain.model.Exercise> domainResults = convertToDomainList(results);
        listener.onResult(domainResults, null);
    }

    @Override
    public void getByCategory(String category, OnExerciseListListener listener) {
        LiveData<List<com.lupefit.app.data.local.entity.Exercise>> liveData = exerciseDao.getByCategory(category);
        listener.onResult(convertToDomainList(liveData.getValue()));
    }

    @Override
    public void getAllOfficial(OnExerciseListListener listener) {
        LiveData<List<com.lupefit.app.data.local.entity.Exercise>> liveData = exerciseDao.getOfficialExercises();
        listener.onResult(convertToDomainList(liveData.getValue()));
    }

    @Override
    public void getCustomByUser(long userId, OnExerciseListListener listener) {
        LiveData<List<com.lupefit.app.data.local.entity.Exercise>> liveData = exerciseDao.getCustomByUser(userId);
        listener.onResult(convertToDomainList(liveData.getValue()));
    }

    @Override
    public void getById(long id, OnExerciseListener listener) {
        LiveData<com.lupefit.app.data.local.entity.Exercise> liveData = exerciseDao.getById(id);
        com.lupefit.app.data.local.entity.Exercise exercise = liveData.getValue();
        if (exercise != null) {
            listener.onResult(convertToDomain(exercise));
        } else {
            listener.onResult(null);
        }
    }

    @Override
    public void insert(com.lupefit.core.domain.model.Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            com.lupefit.app.data.local.entity.Exercise entity = convertToEntity(exercise);
            long id = exerciseDao.insert(entity);
            entity.id = id;
        });
    }

    @Override
    public void update(com.lupefit.core.domain.model.Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            com.lupefit.app.data.local.entity.Exercise entity = convertToEntity(exercise);
            exerciseDao.update(entity);
        });
    }

    @Override
    public void delete(com.lupefit.core.domain.model.Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDao.deleteById(exercise.getId());
        });
    }

    private List<com.lupefit.core.domain.model.Exercise> convertToDomainList(List<com.lupefit.app.data.local.entity.Exercise> entities) {
        List<com.lupefit.core.domain.model.Exercise> result = new ArrayList<>();
        if (entities != null) {
            for (com.lupefit.app.data.local.entity.Exercise entity : entities) {
                result.add(convertToDomain(entity));
            }
        }
        return result;
    }

    private com.lupefit.core.domain.model.Exercise convertToDomain(com.lupefit.app.data.local.entity.Exercise entity) {
        com.lupefit.core.domain.model.Exercise domain = new com.lupefit.core.domain.model.Exercise();
        domain.setId(entity.id);
        domain.setName(entity.name);
        domain.setCategory(entity.category);
        domain.setMovementPattern(entity.movementPattern);
        domain.setEquipment(entity.equipment);
        domain.setBodyweight(entity.isBodyweight);
        domain.setCustom(entity.isCustom);
        domain.setCreatedByUserId(entity.createdByUserId);
        domain.setDefaultIncrementKg(entity.defaultIncrementKg);
        domain.setDescription(entity.description);
        domain.setVideoUrl(entity.videoUrl);
        domain.setImageUrl(entity.imageUrl);
        domain.setServerId(entity.serverId);
        domain.setSynced(entity.isSynced);
        domain.setSyncAction(entity.syncAction);
        domain.setLastModified(entity.lastModified);
        return domain;
    }

    private com.lupefit.app.data.local.entity.Exercise convertToEntity(com.lupefit.core.domain.model.Exercise domain) {
        com.lupefit.app.data.local.entity.Exercise entity = new com.lupefit.app.data.local.entity.Exercise();
        entity.id = domain.getId();
        entity.name = domain.getName();
        entity.category = domain.getCategory();
        entity.movementPattern = domain.getMovementPattern();
        entity.equipment = domain.getEquipment();
        entity.isBodyweight = domain.isBodyweight();
        entity.isCustom = domain.isCustom();
        entity.createdByUserId = domain.getCreatedByUserId();
        entity.defaultIncrementKg = domain.getDefaultIncrementKg();
        entity.description = domain.getDescription();
        entity.videoUrl = domain.getVideoUrl();
        entity.imageUrl = domain.getImageUrl();
        entity.serverId = domain.getServerId();
        entity.isSynced = domain.isSynced();
        entity.syncAction = domain.getSyncAction();
        entity.lastModified = domain.getLastModified();
        return entity;
    }
}