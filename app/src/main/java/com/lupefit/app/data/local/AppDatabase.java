package com.lupefit.app.data.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lupefit.app.data.local.converter.Converters;
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
import com.lupefit.app.data.local.dao.ProgresoDao;
import com.lupefit.app.data.local.dao.RecetaDao;
import com.lupefit.app.data.local.dao.EscaneoDao;
import com.lupefit.app.data.local.entity.FoodLog;
import com.lupefit.app.data.local.entity.WorkoutLog;
import com.lupefit.app.data.local.entity.PersonalRecordEntity;
import com.lupefit.app.data.local.entity.GlucoseLog;
import com.lupefit.app.data.local.entity.MoodEntry;
import com.lupefit.app.data.local.entity.UserNote;
import com.lupefit.app.data.local.entity.WeatherSnapshot;
import com.lupefit.app.data.local.entity.Streak;
import com.lupefit.app.data.local.entity.User;
import com.lupefit.app.data.local.entity.Exercise;
import com.lupefit.app.data.local.entity.Food;
import com.lupefit.app.data.local.entity.Recipe;
import com.lupefit.app.data.local.entity.RecipeIngredient;
import com.lupefit.app.data.local.entity.WaterLog;
import com.lupefit.app.data.local.entity.ProgressPhoto;
import com.lupefit.app.data.local.entity.FoodProduct;
import com.lupefit.app.data.local.entity.PesoEntity;
import com.lupefit.app.data.local.entity.RecetaEntity;
import com.lupefit.app.data.local.entity.IngredienteRecetaEntity;
import com.lupefit.app.data.local.entity.EscaneoEntity;
import com.lupefit.core.sync.queue.SyncQueueDao;
import com.lupefit.core.sync.model.SyncQueueItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Room Database principal de LuPeFit.
 * Incluye todas las entidades para Offline-First.
 * Precarga alimentos frescos desde assets/fresh_foods.json en onCreate.
 */
@Database(
    entities = {
        FoodLog.class,
        WorkoutLog.class,
        PersonalRecordEntity.class,
        GlucoseLog.class,
        MoodEntry.class,
        UserNote.class,
        WeatherSnapshot.class,
        Streak.class,
        User.class,
        Exercise.class,
        Food.class,
        Recipe.class,
        RecipeIngredient.class,
        WaterLog.class,
        ProgressPhoto.class,
        FoodProduct.class,
        PesoEntity.class,
        RecetaEntity.class,
        IngredienteRecetaEntity.class,
        EscaneoEntity.class,
        SyncQueueItem.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";
    private static volatile AppDatabase INSTANCE;

    // Executor para tareas de escritura en background
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    // Executor para tareas de inicialización (precarga JSON)
    private static final ExecutorService INIT_EXECUTOR = Executors.newSingleThreadExecutor();

    public abstract FoodLogDao foodLogDao();
    public abstract WorkoutLogDao workoutLogDao();
    public abstract PersonalRecordDao personalRecordDao();
    public abstract GlucoseLogDao glucoseLogDao();
    public abstract MoodEntryDao moodEntryDao();
    public abstract UserNoteDao userNoteDao();
    public abstract WeatherSnapshotDao weatherSnapshotDao();
    public abstract StreakDao streakDao();
    public abstract UserDao userDao();
    public abstract ExerciseDao exerciseDao();
    public abstract FoodDao foodDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredientDao recipeIngredientDao();
    public abstract WaterLogDao waterLogDao();
    public abstract ProgressPhotoDao progressPhotoDao();
    public abstract FoodProductDao foodProductDao();
    public abstract ProgresoDao progresoDao();
    public abstract RecetaDao recetaDao();
    public abstract EscaneoDao escaneoDao();
    public abstract SyncQueueDao syncQueueDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "lupefit_database"
                    )
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // Precargar alimentos frescos desde JSON en background
                            INIT_EXECUTOR.execute(() -> populateFreshFoods(context));
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                            // Habilitar foreign keys
                            db.execSQL("PRAGMA foreign_keys = ON");
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Lee fresh_foods.json desde assets e inserta en Room.
     * Se ejecuta en background thread via INIT_EXECUTOR.
     */
    private static void populateFreshFoods(Context context) {
        try (InputStream is = context.getAssets().open("fresh_foods.json");
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<FreshFoodJson>>() {}.getType();
            List<FreshFoodJson> freshFoods = gson.fromJson(reader, listType);

            if (freshFoods != null && !freshFoods.isEmpty()) {
                AppDatabase db = getInstance(context);
                long now = System.currentTimeMillis();

                // Convertir a entidades FoodProduct
                FoodProduct[] products = new FoodProduct[freshFoods.size()];
                for (int i = 0; i < freshFoods.size(); i++) {
                    FreshFoodJson json = freshFoods.get(i);
                    FoodProduct product = new FoodProduct();
                    product.name = json.name;
                    product.brand = json.brand;
                    product.kcalPer100g = json.kcalPer100g;
                    product.proteinPer100g = json.proteinPer100g;
                    product.carbsPer100g = json.carbsPer100g;
                    product.fatPer100g = json.fatPer100g;
                    product.defaultServingGrams = json.defaultServingGrams;
                    product.isFreshFood = json.isFreshFood;
                    product.lastUpdated = now;
                    // barcode queda null para alimentos frescos
                    products[i] = product;
                }

                // Insertar en batch
                long[] ids = db.foodProductDao().insertAll(List.of(products));
                Log.d(TAG, "Precargados " + ids.length + " alimentos frescos desde JSON");

                // Verificar conteo
                int count = db.foodProductDao().countFreshFoods();
                Log.d(TAG, "Total alimentos frescos en BD: " + count);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error precargando alimentos frescos", e);
        }
    }

    /**
     * Clase interna para mapear JSON de alimentos frescos.
     */
    private static class FreshFoodJson {
        String name;
        String brand;
        double kcalPer100g;
        double proteinPer100g;
        double carbsPer100g;
        double fatPer100g;
        double defaultServingGrams;
        boolean isFreshFood;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Para tests: permite inyectar instancia mock.
     */
    @VisibleForTesting
    public static void setInstanceForTesting(AppDatabase instance) {
        INSTANCE = instance;
    }
}