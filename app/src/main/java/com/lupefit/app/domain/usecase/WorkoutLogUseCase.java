package com.lupefit.app.domain.usecase;

import com.lupefit.app.domain.repository.WorkoutLogRepository;
import com.lupefit.app.domain.engine.MacroCalculatorEngine;
import com.lupefit.app.domain.engine.SyncEngine;
import com.lupefit.app.domain.engine.HealthConnectManager;

/** Caso de uso para WorkoutLog - stub para compilación base. */
public class WorkoutLogUseCase {
    public WorkoutLogUseCase(WorkoutLogRepository repository,
                             MacroCalculatorEngine macroCalculatorEngine,
                             SyncEngine syncEngine,
                             HealthConnectManager healthConnectManager) {}
}