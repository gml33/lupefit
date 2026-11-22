package com.lupefit.core.domain.provider;

import com.lupefit.core.domain.repository.WaterRepository;

public interface WaterRepositoryProvider {
    WaterRepository getWaterRepository();
}