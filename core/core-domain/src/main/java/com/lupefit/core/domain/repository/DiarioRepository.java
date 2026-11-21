package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface DiarioRepository {

    // Get all entries for a specific date
    LiveData<List<DiarioEntry>> getEntriesForDate(int dateKey);

    // Get entries for a date range
    LiveData<List<DiarioEntry>> getEntriesForDateRange(int dateFrom, int dateTo);

    // Get daily macro totals
    LiveData<MacroTotals> getDailyTotals(int dateKey);

    // Insert a new entry
    void insert(DiarioEntry entry);

    // Update an existing entry
    void update(DiarioEntry entry);

    // Delete an entry
    void delete(DiarioEntry entry);

    // Delete all entries for a date
    void deleteAllForDate(int dateKey);

    // Get entry by ID
    DiarioEntry getById(String id);

    // Get unsynced entries for sync
    LiveData<List<DiarioEntry>> getUnsyncedEntries();

    // Mark entry as synced
    void markAsSynced(String id, String serverId);

    // Mark entry for sync
    void markForSync(String id, String action);

    // Get entries by meal type for a date
    LiveData<List<DiarioEntry>> getEntriesByMealType(int dateKey, String mealType);
}