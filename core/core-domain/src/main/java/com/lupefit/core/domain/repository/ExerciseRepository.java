package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.Exercise;

import java.util.List;

public interface ExerciseRepository {
    void searchByName(String query, int limit, OnExerciseSearchListener listener);
    void getByCategory(String category, OnExerciseListListener listener);
    void getAllOfficial(OnExerciseListListener listener);
    void getCustomByUser(long userId, OnExerciseListListener listener);
    void getById(long id, OnExerciseListener listener);
    void insert(Exercise exercise);
    void update(Exercise exercise);
    void delete(Exercise exercise);

    interface OnExerciseSearchListener {
        void onResult(List<Exercise> exercises, String error);
    }

    interface OnExerciseListListener {
        void onResult(List<Exercise> exercises);
    }

    interface OnExerciseListener {
        void onResult(Exercise exercise);
    }
}