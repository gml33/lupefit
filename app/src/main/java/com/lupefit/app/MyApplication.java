package com.lupefit.app;

import android.app.Application;
import android.content.Context;

import com.lupefit.app.data.local.AppDatabase;
import com.lupefit.app.data.local.dao.FoodLogDao;
import com.lupefit.app.data.local.dao.WorkoutLogDao;
import com.lupefit.app.data.local.dao.PersonalRecordDao;
import com.lupefit.app.data.local.dao.GlucoseLogDao;
import com.lupefit.app.data.local.dao.MoodEntryDao;
import com.lupefit.app.data.local.dao.UserNoteDao;
import com.lupefit.app.data.local.dao.WeatherSnapshotDao;
import com.lupefit.app.data.local.dao.StreakDao;
import com.lupefit.app.data.local.dao.UserDao;
import com.lupefit.app.data.local.dao.ExerciseDao;
import com.lupefit.app.data.local.dao.FoodDao;
import com.lupefit.app.data.local.dao.RecipeDao;
import com.lupefit.app.data.local.dao.RecipeIngredientDao;
import com.lupefit.app.data.local.dao.WaterLogDao;
import com.lupefit.app.data.local.dao.ProgressPhotoDao;
import com.lupefit.app.domain.repository.FoodLogRepository;
import com.lupefit.app.domain.repository.WorkoutLogRepository;
import com.lupefit.app.domain.repository.PersonalRecordRepository;
import com.lupefit.app.domain.repository.GlucoseLogRepository;
import com.lupefit.app.domain.repository.MoodEntryRepository;
import com.lupefit.app.domain.repository.UserNoteRepository;
import com.lupefit.app.domain.repository.WeatherSnapshotRepository;
import com.lupefit.app.domain.repository.StreakRepository;
import com.lupefit.app.domain.repository.UserRepository;
import com.lupefit.app.domain.repository.ExerciseRepository;
import com.lupefit.app.domain.repository.FoodRepository;
import com.lupefit.app.domain.repository.RecipeRepository;
import com.lupefit.app.domain.repository.WaterLogRepository;
import com.lupefit.app.domain.repository.ProgressPhotoRepository;
import com.lupefit.app.domain.engine.MacroCalculatorEngine;
import com.lupefit.app.domain.engine.StreakManager;
import com.lupefit.app.domain.engine.HealthConnectManager;
import com.lupefit.app.domain.engine.SyncEngine;
import com.lupefit.app.domain.engine.MacroCyclerEngine;
import com.lupefit.app.domain.usecase.FoodLogUseCase;
import com.lupefit.app.domain.usecase.WorkoutLogUseCase;
import com.lupefit.app.domain.usecase.PersonalRecordUseCase;
import com.lupefit.app.domain.usecase.GlucoseLogUseCase;
import com.lupefit.app.domain.usecase.MoodEntryUseCase;
import com.lupefit.app.domain.usecase.UserNoteUseCase;
import com.lupefit.app.domain.usecase.UserProfileUseCase;

import com.lupefit.core.sync.worker.SyncWorkerFactory;
import com.lupefit.core.sync.worker.SyncWorkerFactoryProvider;
import com.lupefit.core.sync.queue.SyncQueueRepository;
import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.provider.ExerciseRepositoryProvider;
import com.lupefit.core.domain.provider.FoodCatalogRepositoryProvider;
import com.lupefit.core.domain.provider.ProgresoRepositoryProvider;
import com.lupefit.core.domain.provider.RecetaRepositoryProvider;

/**
 * Application principal - Inicializa el contenedor de dependencias manual (AppContainer).
 * No usa Dagger/Hilt/Kapt - Inyección 100% manual en Java 17+.
 * Implementa SyncWorkerFactoryProvider para proveer dependencias al SyncWorker.
 */
public class MyApplication extends Application implements SyncWorkerFactoryProvider, DiarioRepositoryProvider, ExerciseRepositoryProvider, FoodCatalogRepositoryProvider, ProgresoRepositoryProvider, RecetaRepositoryProvider {

    private static MyApplication instance;
    private AppContainer container;
    private SyncWorkerFactory syncWorkerFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        container = new AppContainer(this);

        // Inicializar factory para SyncWorker
        syncWorkerFactory = new SyncWorkerFactory() {
            @Override
            public SyncQueueRepository createSyncQueueRepository() {
                return container.getSyncQueueRepository();
            }

            @Override
            public com.lupefit.core.network.SyncEngine createSyncEngine() {
                return container.getNetworkSyncEngine();
            }
        };
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static AppContainer getContainer(Context context) {
        return getInstance().container;
    }

    public AppContainer getContainer() {
        return container;
    }

    @Override
    public SyncWorkerFactory getSyncWorkerFactory() {
        return syncWorkerFactory;
    }

    @Override
    public com.lupefit.core.domain.repository.DiarioRepository getDiarioRepository() {
        return container.getDiarioRepository();
    }

    @Override
    public com.lupefit.core.domain.repository.FoodCatalogRepository getFoodCatalogRepository() {
        return container.getFoodCatalogRepository();
    }

    @Override
    public com.lupefit.core.domain.repository.ExerciseRepository getExerciseRepository() {
        return container.getExerciseRepository();
    }

    @Override
    public com.lupefit.core.domain.repository.ProgresoRepository getProgresoRepository() {
        return container.getProgresoRepository();
    }

    @Override
    public com.lupefit.core.domain.repository.RecetaRepository getRecetaRepository() {
        return container.getRecetaRepository();
    }
}