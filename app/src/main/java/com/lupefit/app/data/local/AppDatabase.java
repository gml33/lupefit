package com.lupefit.app.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

/**
 * Room Database principal de LuPeFit.
 * Incluye todas las entidades para Offline-First.
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
        ProgressPhoto.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

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
                            // Poblar ejercicios base si es primera vez
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}