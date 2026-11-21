package com.lupefit.app;

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
import com.lupefit.app.data.local.dao.FoodProductDao;
import com.lupefit.app.data.local.repository.DiarioRepositoryImpl;
import com.lupefit.app.data.local.repository.ExerciseRepositoryImpl;
import com.lupefit.app.data.local.repository.FoodCatalogRepositoryImpl;
import com.lupefit.app.data.local.preferences.UserPreferencesManager;
import com.lupefit.app.domain.engine.MacroCalculatorEngine;
import com.lupefit.app.domain.engine.StreakManager;
import com.lupefit.app.domain.engine.HealthConnectManager;
import com.lupefit.app.domain.engine.SyncEngine;
import com.lupefit.app.domain.engine.MacroCyclerEngine;
import com.lupefit.app.domain.repository.FoodLogRepository;
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
import com.lupefit.core.domain.repository.DiarioRepository;
import com.lupefit.core.domain.repository.FoodCatalogRepository;
import com.lupefit.core.network.api.LuPeFitApiService;
import com.lupefit.core.network.NetworkModule;
import com.lupefit.core.network.interceptor.AuthInterceptor;

/**
 * Contenedor de Inyección de Dependencias Manual (Pure Java).
 * Crea y gestiona el ciclo de vida de todas las dependencias de la app.
 * No usa Dagger/Hilt/Kapt - 100% manual, cero reflection en runtime.
 */
public final class AppContainer {

    private final Context context;

    // Database
    private volatile AppDatabase database;

    // DAOs
    private volatile FoodLogDao foodLogDao;
    private volatile WorkoutLogDao workoutLogDao;
    private volatile PersonalRecordDao personalRecordDao;
    private volatile GlucoseLogDao glucoseLogDao;
    private volatile MoodEntryDao moodEntryDao;
    private volatile UserNoteDao userNoteDao;
    private volatile WeatherSnapshotDao weatherSnapshotDao;
    private volatile StreakDao streakDao;
    private volatile UserDao userDao;
    private volatile ExerciseDao exerciseDao;
    private volatile FoodDao foodDao;
    private volatile RecipeDao recipeDao;
    private volatile RecipeIngredientDao recipeIngredientDao;
    private volatile WaterLogDao waterLogDao;
    private volatile ProgressPhotoDao progressPhotoDao;
    private volatile FoodProductDao foodProductDao;

    // Repositories (solo los implementados para feature-diario)
    private volatile DiarioRepository diarioRepository;
    private volatile FoodLogRepository foodLogRepository;
    private volatile SyncQueueRepository syncQueueRepository;
    private volatile FoodCatalogRepository foodCatalogRepository;
    private volatile com.lupefit.core.domain.repository.ExerciseRepository exerciseRepository;

    // Preferences
    private volatile UserPreferencesManager userPreferencesManager;

    // Engines
    private volatile MacroCalculatorEngine macroCalculatorEngine;
    private volatile StreakManager streakManager;
    private volatile HealthConnectManager healthConnectManager;
    private volatile SyncEngine syncEngine; // App domain engine
    private volatile com.lupefit.core.network.SyncEngine networkSyncEngine; // Core-network engine
    private volatile MacroCyclerEngine macroCyclerEngine;

    // UseCases
    private volatile FoodLogUseCase foodLogUseCase;
    // private volatile WorkoutLogUseCase workoutLogUseCase;
    // private volatile PersonalRecordUseCase personalRecordUseCase;
    // private volatile GlucoseLogUseCase glucoseLogUseCase;
    // private volatile MoodEntryUseCase moodEntryUseCase;
    // private volatile UserNoteUseCase userNoteUseCase;
    // private volatile UserProfileUseCase userProfileUseCase;

    public AppContainer(Context context) {
        this.context = context.getApplicationContext();
        initialize();
    }

    private void initialize() {
        // Database (Room)
        database = AppDatabase.getInstance(context);

        // DAOs
        foodLogDao = database.foodLogDao();
        workoutLogDao = database.workoutLogDao();
        personalRecordDao = database.personalRecordDao();
        glucoseLogDao = database.glucoseLogDao();
        moodEntryDao = database.moodEntryDao();
        userNoteDao = database.userNoteDao();
        weatherSnapshotDao = database.weatherSnapshotDao();
        streakDao = database.streakDao();
        userDao = database.userDao();
        exerciseDao = database.exerciseDao();
        foodDao = database.foodDao();
        recipeDao = database.recipeDao();
        recipeIngredientDao = database.recipeIngredientDao();
        waterLogDao = database.waterLogDao();
        progressPhotoDao = database.progressPhotoDao();
        foodProductDao = database.foodProductDao();

        // Repositories (solo los implementados para feature-diario)
        diarioRepository = new DiarioRepositoryImpl(context, getCurrentUserId());
        foodLogRepository = new FoodLogRepository(foodLogDao);
        syncQueueRepository = new SyncQueueRepository(database.syncQueueDao());
        foodCatalogRepository = new FoodCatalogRepositoryImpl(context, foodProductDao);
        exerciseRepository = new ExerciseRepositoryImpl(context);

        // Preferences
        userPreferencesManager = UserPreferencesManager.getInstance(context);

        // Engines
        macroCalculatorEngine = new MacroCalculatorEngine();
        // TODO: streakManager necesita streakRepository, workoutLogRepository, waterLogRepository
        // streakManager = new StreakManager(streakRepository, foodLogRepository, workoutLogRepository, waterLogRepository);
        healthConnectManager = new HealthConnectManager(context);

        // App domain engine (for local sync logic)
        syncEngine = new SyncEngine(context, foodLogRepository, null, null,
                null, null, null, null,
                null, null, null, null);

        // Core-network SyncEngine (for network sync)
        okhttp3.OkHttpClient httpClient = NetworkModule.createOkHttpClient(new AuthInterceptor());
        com.squareup.moshi.Moshi moshi = NetworkModule.createMoshi();
        retrofit2.Retrofit retrofit = NetworkModule.createRetrofit(httpClient, moshi);
        LuPeFitApiService apiService = NetworkModule.createApiService(retrofit);
        AuthInterceptor authInterceptor = new AuthInterceptor();
        networkSyncEngine = new com.lupefit.core.network.SyncEngine(
            com.lupefit.core.network.NetworkModule.createApiService(
                com.lupefit.core.network.NetworkModule.createRetrofit(
                    com.lupefit.core.network.NetworkModule.createOkHttpClient(new AuthInterceptor()),
                    com.lupefit.core.network.NetworkModule.createMoshi()
                )
            ),
            new AuthInterceptor()
        );

        macroCyclerEngine = new MacroCyclerEngine(macroCalculatorEngine, null, healthConnectManager);

        // UseCases (solo los necesarios para feature-diario)
        foodLogUseCase = new FoodLogUseCase(foodLogRepository, macroCalculatorEngine, syncEngine);
        // workoutLogUseCase = new WorkoutLogUseCase(workoutLogRepository, macroCalculatorEngine, syncEngine, healthConnectManager);
        // personalRecordUseCase = new PersonalRecordUseCase(personalRecordRepository, exerciseRepository);
        // glucoseLogUseCase = new GlucoseLogUseCase(glucoseLogRepository, syncEngine);
        // moodEntryUseCase = new MoodEntryUseCase(moodEntryRepository, syncEngine);
        // userNoteUseCase = new UserNoteUseCase(userNoteRepository, syncEngine);
        // userProfileUseCase = new UserProfileUseCase(userRepository, macroCalculatorEngine);
    }

    private long getCurrentUserId() {
        // TODO: Get actual logged-in user ID from UserRepository or preferences
        // For now, return a default user ID (1)
        return 1L;
    }

    // ==================== GETTERS ====================

    public AppDatabase getDatabase() { return database; }

    public FoodLogDao getFoodLogDao() { return foodLogDao; }
    public WorkoutLogDao getWorkoutLogDao() { return workoutLogDao; }
    public PersonalRecordDao getPersonalRecordDao() { return personalRecordDao; }
    public GlucoseLogDao getGlucoseLogDao() { return glucoseLogDao; }
    public MoodEntryDao getMoodEntryDao() { return moodEntryDao; }
    public UserNoteDao getUserNoteDao() { return userNoteDao; }
    public WeatherSnapshotDao getWeatherSnapshotDao() { return weatherSnapshotDao; }
    public StreakDao getStreakDao() { return streakDao; }
    public UserDao getUserDao() { return userDao; }
    public ExerciseDao getExerciseDao() { return exerciseDao; }
    public FoodDao getFoodDao() { return foodDao; }
    public RecipeDao getRecipeDao() { return recipeDao; }
    public RecipeIngredientDao getRecipeIngredientDao() { return recipeIngredientDao; }
    public WaterLogDao getWaterLogDao() { return waterLogDao; }
    public ProgressPhotoDao getProgressPhotoDao() { return progressPhotoDao; }
    public FoodProductDao getFoodProductDao() { return foodProductDao; }

    public DiarioRepository getDiarioRepository() { return diarioRepository; }
    public FoodLogRepository getFoodLogRepository() { return foodLogRepository; }
    public SyncQueueRepository getSyncQueueRepository() { return syncQueueRepository; }
    public FoodCatalogRepository getFoodCatalogRepository() { return foodCatalogRepository; }
    public com.lupefit.core.domain.repository.ExerciseRepository getExerciseRepository() { return exerciseRepository; }

    public UserPreferencesManager getUserPreferencesManager() { return userPreferencesManager; }

    public MacroCalculatorEngine getMacroCalculatorEngine() { return macroCalculatorEngine; }
    public HealthConnectManager getHealthConnectManager() { return healthConnectManager; }
    public SyncEngine getSyncEngine() { return syncEngine; }
    public com.lupefit.core.network.SyncEngine getNetworkSyncEngine() { return networkSyncEngine; }
    public MacroCyclerEngine getMacroCyclerEngine() { return macroCyclerEngine; }

    public FoodLogUseCase getFoodLogUseCase() { return foodLogUseCase; }
    // public WorkoutLogUseCase getWorkoutLogUseCase() { return workoutLogUseCase; }
    // public PersonalRecordUseCase getPersonalRecordUseCase() { return personalRecordUseCase; }
    // public GlucoseLogUseCase getGlucoseLogUseCase() { return glucoseLogUseCase; }
    // public MoodEntryUseCase getMoodEntryUseCase() { return moodEntryUseCase; }
    // public UserNoteUseCase getUserNoteUseCase() { return userNoteUseCase; }
    // public UserProfileUseCase getUserProfileUseCase() { return userProfileUseCase; }
}