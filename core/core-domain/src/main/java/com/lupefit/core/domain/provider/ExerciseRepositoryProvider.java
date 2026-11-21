package com.lupefit.core.domain.provider;

import com.lupefit.core.domain.repository.ExerciseRepository;

public interface ExerciseRepositoryProvider {
    ExerciseRepository getExerciseRepository();
}