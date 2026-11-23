package com.lupefit.app.domain.engine;

import com.lupefit.app.domain.repository.StreakRepository;
import com.lupefit.app.domain.repository.FoodLogRepository;
import com.lupefit.app.domain.repository.WorkoutLogRepository;
import com.lupefit.app.domain.repository.WaterLogRepository;
import com.lupefit.app.data.local.entity.Streak;
import com.lupefit.app.domain.repository.StreakRepository;

/** Gestor de rachas (streaks) - stub para compilación base. */
public class StreakManager {
    private final StreakRepository streakRepository;
    public StreakManager(StreakRepository streakRepository,
                         FoodLogRepository foodLogRepository,
                         WorkoutLogRepository workoutLogRepository,
                         WaterLogRepository waterLogRepository) { this.streakRepository = streakRepository; }

    /** Registra el cumplimiento del objetivo para un día YYYYMMDD. */
    public void recordDailyAchievement(long userId, String type, int dateKey, String unit) {
        Streak streak = new Streak();
        streak.userId = userId;
        streak.streakType = type;
        streak.currentStreak = 1;
        streak.longestStreak = 1;
        streak.lastAchievedDate = dateKey;
        streak.targetUnit = unit == null ? "COUNT" : unit;
        streak.updatedAt = System.currentTimeMillis();
        streakRepository.upsert(streak);
    }
}
