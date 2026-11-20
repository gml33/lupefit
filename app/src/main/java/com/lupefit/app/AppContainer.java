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

    // Repositories
    private volatile FoodLogRepository foodLogRepository;
    private volatile WorkoutLogRepository workoutLogRepository;
    private volatile PersonalRecordRepository personalRecordRepository;
    private volatile GlucoseLogRepository glucoseLogRepository;
    private volatile MoodEntryRepository moodEntryRepository;
    private volatile UserNoteRepository userNoteRepository;
    private volatile WeatherSnapshotRepository weatherSnapshotRepository;
    private volatile StreakRepository streakRepository;
    private volatile UserRepository userRepository;
    private volatile ExerciseRepository exerciseRepository;
    private volatile FoodRepository foodRepository;
    private volatile RecipeRepository recipeRepository;
    private volatile WaterLogRepository waterLogRepository;
    private volatile ProgressPhotoRepository progressPhotoRepository;

    // Engines
    private volatile MacroCalculatorEngine macroCalculatorEngine;
    private volatile StreakManager streakManager;
    private volatile HealthConnectManager healthConnectManager;
    private volatile SyncEngine syncEngine;
    private volatile MacroCyclerEngine macroCyclerEngine;

    // UseCases
    private volatile FoodLogUseCase foodLogUseCase;
    private volatile WorkoutLogUseCase workoutLogUseCase;
    private volatile PersonalRecordUseCase personalRecordUseCase;
    private volatile GlucoseLogUseCase glucoseLogUseCase;
    private volatile MoodEntryUseCase moodEntryUseCase;
    private volatile UserNoteUseCase userNoteUseCase;
    private volatile UserProfileUseCase userProfileUseCase;

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

        // Repositories
        foodLogRepository = new FoodLogRepository(foodLogDao);
        workoutLogRepository = new WorkoutLogRepository(workoutLogDao);
        personalRecordRepository = new PersonalRecordRepository(personalRecordDao);
        glucoseLogRepository = new GlucoseLogRepository(glucoseLogDao);
        moodEntryRepository = new MoodEntryRepository(moodEntryDao);
        userNoteRepository = new UserNoteRepository(userNoteDao);
        weatherSnapshotRepository = new WeatherSnapshotRepository(weatherSnapshotDao);
        streakRepository = new StreakRepository(streakDao);
        userRepository = new UserRepository(userDao);
        exerciseRepository = new ExerciseRepository(exerciseDao);
        foodRepository = new FoodRepository(foodDao);
        recipeRepository = new RecipeRepository(recipeDao, recipeIngredientDao);
        waterLogRepository = new WaterLogRepository(waterLogDao);
        progressPhotoRepository = new ProgressPhotoRepository(progressPhotoDao);

        // Engines
        macroCalculatorEngine = new MacroCalculatorEngine();
        streakManager = new StreakManager(streakRepository, foodLogRepository, workoutLogRepository, waterLogRepository);
        healthConnectManager = new HealthConnectManager(context);
        syncEngine = new SyncEngine(context, foodLogRepository, workoutLogRepository, personalRecordRepository,
                glucoseLogRepository, moodEntryRepository, userNoteRepository, weatherSnapshotRepository,
                streakRepository, waterLogRepository, progressPhotoRepository, recipeRepository);
        macroCyclerEngine = new MacroCyclerEngine(macroCalculatorEngine, workoutLogRepository, healthConnectManager);

        // UseCases
        foodLogUseCase = new FoodLogUseCase(foodLogRepository, macroCalculatorEngine, syncEngine);
        workoutLogUseCase = new WorkoutLogUseCase(workoutLogRepository, macroCalculatorEngine, syncEngine, healthConnectManager);
        personalRecordUseCase = new PersonalRecordUseCase(personalRecordRepository, exerciseRepository);
        glucoseLogUseCase = new GlucoseLogUseCase(glucoseLogRepository, syncEngine);
        moodEntryUseCase = new MoodEntryUseCase(moodEntryRepository, syncEngine);
        userNoteUseCase = new UserNoteUseCase(userNoteRepository, syncEngine);
        userProfileUseCase = new UserProfileUseCase(userRepository, macroCalculatorEngine);
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

    public FoodLogRepository getFoodLogRepository() { return foodLogRepository; }
    public WorkoutLogRepository getWorkoutLogRepository() { return workoutLogRepository; }
    public PersonalRecordRepository getPersonalRecordRepository() { return personalRecordRepository; }
    public GlucoseLogRepository getGlucoseLogRepository() { return glucoseLogRepository; }
    public MoodEntryRepository getMoodEntryRepository() { return moodEntryRepository; }
    public UserNoteRepository getUserNoteRepository() { return userNoteRepository; }
    public WeatherSnapshotRepository getWeatherSnapshotRepository() { return weatherSnapshotRepository; }
    public StreakRepository getStreakRepository() { return streakRepository; }
    public UserRepository getUserRepository() { return userRepository; }
    public ExerciseRepository getExerciseRepository() { return exerciseRepository; }
    public FoodRepository getFoodRepository() { return foodRepository; }
    public RecipeRepository getRecipeRepository() { return recipeRepository; }
    public WaterLogRepository getWaterLogRepository() { return waterLogRepository; }
    public ProgressPhotoRepository getProgressPhotoRepository() { return progressPhotoRepository; }

    public MacroCalculatorEngine getMacroCalculatorEngine() { return macroCalculatorEngine; }
    public StreakManager getStreakManager() { return streakManager; }
    public HealthConnectManager getHealthConnectManager() { return healthConnectManager; }
    public SyncEngine getSyncEngine() { return syncEngine; }
    public MacroCyclerEngine getMacroCyclerEngine() { return macroCyclerEngine; }

    public FoodLogUseCase getFoodLogUseCase() { return foodLogUseCase; }
    public WorkoutLogUseCase getWorkoutLogUseCase() { return workoutLogUseCase; }
    public PersonalRecordUseCase getPersonalRecordUseCase() { return personalRecordUseCase; }
    public GlucoseLogUseCase getGlucoseLogUseCase() { return glucoseLogUseCase; }
    public MoodEntryUseCase getMoodEntryUseCase() { return moodEntryUseCase; }
    public UserNoteUseCase getUserNoteUseCase() { return userNoteUseCase; }
    public UserProfileUseCase getUserProfileUseCase() { return userProfileUseCase; }
}