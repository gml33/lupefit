package com.lupefit.app.data.local.mapper;

import com.lupefit.app.data.local.dao.FoodLogDao;
import com.lupefit.app.data.local.entity.FoodLog;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;

public class DiarioMapper {

    public static DiarioEntry toDomain(FoodLog entity) {
        if (entity == null) return null;

        DiarioEntry domain = new DiarioEntry();
        domain.setId(String.valueOf(entity.id));
        domain.setFoodName(entity.foodName);
        domain.setFoodId(entity.foodId);
        domain.setFoodProductId(entity.foodId); // Using foodId as fallback
        domain.setCantidad(entity.quantityGrams);
        domain.setCalorias(entity.calories);
        domain.setProteinas(entity.proteinGrams);
        domain.setCarbohidratos(entity.carbsGrams);
        domain.setGrasas(entity.fatGrams);
        domain.setFibra(entity.fiberGrams);
        domain.setAzucar(entity.sugarGrams);
        domain.setSodio(entity.sodiumMg);
        domain.setTipoComida(entity.mealType);
        domain.setTimestamp(entity.timestamp);
        domain.setDateKey(entity.dateKey);
        domain.setEstimatedCostSnapshot(entity.estimatedCostSnapshot);
        domain.setNotes(entity.notes);
        domain.setBarcode(entity.barcode);
        domain.setFromVoiceInput(entity.fromVoiceInput);
        domain.setFromAiFoodScan(entity.fromAIFoodScan);
        domain.setSourceType(entity.sourceType);
        domain.setSynced(entity.isSynced);
        domain.setSyncAction(entity.syncAction);
        domain.setServerId(entity.serverId);
        domain.setLastModified(entity.lastModified);
        return domain;
    }

    public static FoodLog toEntity(DiarioEntry domain) {
        if (domain == null) return null;

        FoodLog entity = new FoodLog();
        if (domain.getId() != null && !domain.getId().isEmpty()) {
            try {
                entity.id = Long.parseLong(domain.getId());
            } catch (NumberFormatException e) {
                entity.id = 0; // Auto-generate
            }
        }
        entity.foodName = domain.getFoodName();
        entity.foodId = domain.getFoodId();
        entity.quantityGrams = domain.getCantidad();
        entity.calories = domain.getCalorias();
        entity.proteinGrams = domain.getProteinas();
        entity.carbsGrams = domain.getCarbohidratos();
        entity.fatGrams = domain.getGrasas();
        entity.fiberGrams = domain.getFibra();
        entity.sugarGrams = domain.getAzucar();
        entity.sodiumMg = domain.getSodio();
        entity.mealType = domain.getTipoComida();
        entity.timestamp = domain.getTimestamp();
        entity.dateKey = domain.getDateKey();
        entity.estimatedCostSnapshot = domain.getEstimatedCostSnapshot();
        entity.notes = domain.getNotes();
        entity.barcode = domain.getBarcode();
        entity.fromVoiceInput = domain.isFromVoiceInput();
        entity.fromAIFoodScan = domain.isFromAiFoodScan();
        entity.sourceType = domain.getSourceType();
        entity.isSynced = domain.isSynced();
        entity.syncAction = domain.getSyncAction();
        entity.serverId = domain.getServerId();
        entity.lastModified = domain.getLastModified();
        return entity;
    }

    public static MacroTotals toMacroTotals(FoodLogDao.MealMacroSummary summary) {
        if (summary == null) return new MacroTotals(0, 0, 0, 0);
        return new MacroTotals(summary.totalCal, summary.totalProt, summary.totalCarb, summary.totalFat);
    }
}