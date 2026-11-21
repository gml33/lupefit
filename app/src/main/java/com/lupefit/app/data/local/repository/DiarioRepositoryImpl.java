package com.lupefit.app.data.local.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.lupefit.app.data.local.AppDatabase;
import com.lupefit.app.data.local.dao.FoodLogDao;
import com.lupefit.app.data.local.entity.FoodLog;
import com.lupefit.app.data.local.mapper.DiarioMapper;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;
import com.lupefit.core.domain.repository.DiarioRepository;

import java.util.ArrayList;
import java.util.List;

public class DiarioRepositoryImpl implements DiarioRepository {

    private final FoodLogDao foodLogDao;
    private final long currentUserId;

    public DiarioRepositoryImpl(Context context, long userId) {
        this.foodLogDao = AppDatabase.getInstance(context).foodLogDao();
        this.currentUserId = userId;
    }

    @Override
    public LiveData<List<DiarioEntry>> getEntriesForDate(int dateKey) {
        return Transformations.map(foodLogDao.getByUserAndDate(currentUserId, dateKey), entities -> {
            List<DiarioEntry> result = new ArrayList<>();
            for (FoodLog entity : entities) {
                result.add(DiarioMapper.toDomain(entity));
            }
            return result;
        });
    }

    @Override
    public LiveData<List<DiarioEntry>> getEntriesForDateRange(int dateFrom, int dateTo) {
        return Transformations.map(foodLogDao.getByUserAndDateRange(currentUserId, dateFrom, dateTo), entities -> {
            List<DiarioEntry> result = new ArrayList<>();
            for (FoodLog entity : entities) {
                result.add(DiarioMapper.toDomain(entity));
            }
            return result;
        });
    }

    @Override
    public LiveData<MacroTotals> getDailyTotals(int dateKey) {
        return Transformations.map(foodLogDao.getMacrosByMealType(currentUserId, dateKey), summaries -> {
            int totalCal = 0;
            double totalProt = 0, totalCarb = 0, totalFat = 0;
            for (FoodLogDao.MealMacroSummary s : summaries) {
                totalCal += s.totalCal;
                totalProt += s.totalProt;
                totalCarb += s.totalCarb;
                totalFat += s.totalFat;
            }
            return new MacroTotals(totalCal, totalProt, totalCarb, totalFat);
        });
    }

    @Override
    public void insert(DiarioEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            FoodLog entity = DiarioMapper.toEntity(entry);
            entity.userId = currentUserId;
            foodLogDao.insert(entity);
        });
    }

    @Override
    public void update(DiarioEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            FoodLog entity = DiarioMapper.toEntity(entry);
            entity.userId = currentUserId;
            foodLogDao.update(entity);
        });
    }

    @Override
    public void delete(DiarioEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                long id = Long.parseLong(entry.getId());
                foodLogDao.deleteById(id);
            } catch (NumberFormatException ignored) {}
        });
    }

    @Override
    public void deleteAllForDate(int dateKey) {
        // Not directly supported by DAO, would need a custom query
    }

    @Override
    public DiarioEntry getById(String id) {
        return null;
    }

    @Override
    public LiveData<List<DiarioEntry>> getUnsyncedEntries() {
        return Transformations.map(foodLogDao.getUnsyncedByUser(currentUserId), entities -> {
            List<DiarioEntry> result = new ArrayList<>();
            for (FoodLog entity : entities) {
                result.add(DiarioMapper.toDomain(entity));
            }
            return result;
        });
    }

    @Override
    public void markAsSynced(String id, String serverId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                long entityId = Long.parseLong(id);
                foodLogDao.markSynced(entityId, serverId, System.currentTimeMillis());
            } catch (NumberFormatException ignored) {}
        });
    }

    @Override
    public void markForSync(String id, String action) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                long entityId = Long.parseLong(id);
                foodLogDao.markDirty(entityId, System.currentTimeMillis());
            } catch (NumberFormatException ignored) {}
        });
    }

    @Override
    public LiveData<List<DiarioEntry>> getEntriesByMealType(int dateKey, String mealType) {
        return getEntriesForDate(dateKey);
    }
}