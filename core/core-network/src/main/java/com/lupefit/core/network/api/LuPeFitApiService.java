package com.lupefit.core.network.api;

import com.lupefit.core.network.dto.auth.*;
import com.lupefit.core.network.dto.common.*;
import com.lupefit.core.network.dto.food.*;
import com.lupefit.core.network.dto.workout.*;
import com.lupefit.core.network.dto.progress.*;
import com.lupefit.core.network.dto.recipe.*;
import com.lupefit.core.network.dto.water.*;
import com.lupefit.core.network.dto.exercise.*;
import com.lupefit.core.network.dto.pr.*;
import com.lupefit.core.network.dto.glucose.*;
import com.lupefit.core.network.dto.mood.*;
import com.lupefit.core.network.dto.note.*;
import com.lupefit.core.network.dto.sync.*;
import com.lupefit.core.network.dto.user.*;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * LuPeFit API Service Interface
 * Define todos los endpoints REST del backend FastAPI
 */
public interface LuPeFitApiService {

    // ==================== AUTH ====================

    @POST("/api/v1/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("/api/v1/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/api/v1/auth/refresh")
    Call<TokenResponse> refreshToken(@Header("Authorization") String refreshToken);

    @POST("/api/v1/auth/logout")
    Call<Void> logout(@Header("Authorization") String accessToken);

    @GET("/api/v1/auth/me")
    Call<UserProfile> getCurrentUser(@Header("Authorization") String accessToken);

    // ==================== SYNC ====================

    @POST("/api/v1/sync/batch")
    Call<SyncBatchResponse> syncBatch(
        @Header("Authorization") String accessToken,
        @Body SyncBatchRequest request
    );

    @GET("/api/v1/sync/status")
    Call<SyncStatus> getSyncStatus(@Header("Authorization") String accessToken);

    // ==================== FOOD LOGS ====================

    @GET("/api/v1/food-logs")
    Call<PaginatedResponse<FoodLogResponse>> getFoodLogs(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("meal_type") String mealType,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/food-logs")
    Call<FoodLogResponse> createFoodLog(
        @Header("Authorization") String accessToken,
        @Body FoodLogRequest request
    );

    @PUT("/api/v1/food-logs/{id}")
    Call<FoodLogResponse> updateFoodLog(
        @Header("Authorization") String accessToken,
        @Path("id") String id,
        @Body FoodLogRequest request
    );

    @DELETE("/api/v1/food-logs/{id}")
    Call<Void> deleteFoodLog(
        @Header("Authorization") String accessToken,
        @Path("id") String id
    );

    // ==================== WORKOUT LOGS ====================

    @GET("/api/v1/workout-logs")
    Call<PaginatedResponse<WorkoutLogResponse>> getWorkoutLogs(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/workout-logs")
    Call<WorkoutLogResponse> createWorkoutLog(
        @Header("Authorization") String accessToken,
        @Body WorkoutLogRequest request
    );

    @PUT("/api/v1/workout-logs/{id}")
    Call<WorkoutLogResponse> updateWorkoutLog(
        @Header("Authorization") String accessToken,
        @Path("id") String id,
        @Body WorkoutLogRequest request
    );

    @DELETE("/api/v1/workout-logs/{id}")
    Call<Void> deleteWorkoutLog(
        @Header("Authorization") String accessToken,
        @Path("id") String id
    );

    // ==================== PROGRESS ====================

    @GET("/api/v1/progress/weight")
    Call<PaginatedResponse<WeightEntryResponse>> getWeightEntries(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/progress/weight")
    Call<WeightEntryResponse> createWeightEntry(
        @Header("Authorization") String accessToken,
        @Body WeightEntryRequest request
    );

    @PUT("/api/v1/progress/weight/{id}")
    Call<WeightEntryResponse> updateWeightEntry(
        @Header("Authorization") String accessToken,
        @Path("id") String id,
        @Body WeightEntryRequest request
    );

    @DELETE("/api/v1/progress/weight/{id}")
    Call<Void> deleteWeightEntry(
        @Header("Authorization") String accessToken,
        @Path("id") String id
    );

    @GET("/api/v1/progress/measurements")
    Call<PaginatedResponse<MeasurementResponse>> getMeasurements(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/progress/measurements")
    Call<MeasurementResponse> createMeasurement(
        @Header("Authorization") String accessToken,
        @Body MeasurementRequest request
    );

    @GET("/api/v1/progress/photos")
    Call<PaginatedResponse<ProgressPhotoResponse>> getProgressPhotos(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/progress/photos")
    Call<ProgressPhotoResponse> createProgressPhoto(
        @Header("Authorization") String accessToken,
        @Body ProgressPhotoRequest request
    );

    // ==================== RECIPES ====================

    @GET("/api/v1/recipes")
    Call<PaginatedResponse<RecipeResponse>> getRecipes(
        @Header("Authorization") String accessToken,
        @Query("is_public") Boolean isPublic,
        @Query("collection_id") String collectionId,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/recipes")
    Call<RecipeResponse> createRecipe(
        @Header("Authorization") String accessToken,
        @Body RecipeRequest request
    );

    @PUT("/api/v1/recipes/{id}")
    Call<RecipeResponse> updateRecipe(
        @Header("Authorization") String accessToken,
        @Path("id") String id,
        @Body RecipeRequest request
    );

    @DELETE("/api/v1/recipes/{id}")
    Call<Void> deleteRecipe(
        @Header("Authorization") String accessToken,
        @Path("id") String id
    );

    // ==================== FOODS (Search) ====================

    @GET("/api/v1/foods/search")
    Call<PaginatedResponse<FoodSearchResponse>> searchFoods(
        @Header("Authorization") String accessToken,
        @Query("q") String query,
        @Query("category") String category,
        @Query("page") int page,
        @Query("size") int size
    );

    @GET("/api/v1/foods/{id}")
    Call<FoodDetailResponse> getFoodDetail(
        @Header("Authorization") String accessToken,
        @Path("id") String id
    );

    // ==================== WATER ====================

    @GET("/api/v1/water")
    Call<PaginatedResponse<WaterLogResponse>> getWaterLogs(
        @Header("Authorization") String accessToken,
        @Query("date") String date,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/water")
    Call<WaterLogResponse> createWaterLog(
        @Header("Authorization") String accessToken,
        @Body WaterLogRequest request
    );

    // ==================== EXERCISES ====================

    @GET("/api/v1/exercises")
    Call<PaginatedResponse<ExerciseResponse>> getExercises(
        @Header("Authorization") String accessToken,
        @Query("category") String category,
        @Query("is_custom") Boolean isCustom,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/exercises")
    Call<ExerciseResponse> createExercise(
        @Header("Authorization") String accessToken,
        @Body ExerciseRequest request
    );

    // ==================== PERSONAL RECORDS ====================

    @GET("/api/v1/personal-records")
    Call<PaginatedResponse<PersonalRecordResponse>> getPersonalRecords(
        @Header("Authorization") String accessToken,
        @Query("exercise_id") String exerciseId,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/personal-records")
    Call<PersonalRecordResponse> createPersonalRecord(
        @Header("Authorization") String accessToken,
        @Body PersonalRecordRequest request
    );

    // ==================== GLUCOSE ====================

    @GET("/api/v1/glucose")
    Call<PaginatedResponse<GlucoseLogResponse>> getGlucoseLogs(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/glucose")
    Call<GlucoseLogResponse> createGlucoseLog(
        @Header("Authorization") String accessToken,
        @Body GlucoseLogRequest request
    );

    // ==================== MOOD ====================

    @GET("/api/v1/mood")
    Call<PaginatedResponse<MoodEntryResponse>> getMoodEntries(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/mood")
    Call<MoodEntryResponse> createMoodEntry(
        @Header("Authorization") String accessToken,
        @Body MoodEntryRequest request
    );

    // ==================== USER NOTES ====================

    @GET("/api/v1/notes")
    Call<PaginatedResponse<UserNoteResponse>> getUserNotes(
        @Header("Authorization") String accessToken,
        @Query("date_from") String dateFrom,
        @Query("date_to") String dateTo,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("/api/v1/notes")
    Call<UserNoteResponse> createUserNote(
        @Header("Authorization") String accessToken,
        @Body UserNoteRequest request
    );

    // ==================== SETTINGS ====================

    @GET("/api/v1/settings")
    Call<UserSettingsResponse> getSettings(@Header("Authorization") String accessToken);

    @PUT("/api/v1/settings")
    Call<UserSettingsResponse> updateSettings(
        @Header("Authorization") String accessToken,
        @Body UserSettingsRequest request
    );
}