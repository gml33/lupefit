package com.lupefit.core.domain.provider;

import com.lupefit.core.domain.repository.RoutineRepository;

public interface RoutineRepositoryProvider {
    RoutineRepository getRoutineRepository();
}
