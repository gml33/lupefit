package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.WaterEntry;

import java.util.List;

public interface WaterRepository {
    void insertWater(WaterEntry entry);
    void updateWater(WaterEntry entry);
    void deleteWater(long id);
    List<WaterEntry> getAllWaterEntries();
    List<WaterEntry> getWaterEntriesByDate(String fecha);
    WaterEntry getWaterEntryById(long id);
    int getTotalWaterToday(String fecha);
}