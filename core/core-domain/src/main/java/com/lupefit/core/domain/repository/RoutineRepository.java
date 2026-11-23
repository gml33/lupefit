package com.lupefit.core.domain.repository;

import java.util.List;

public interface RoutineRepository {
    final class Routine {
        public final long id;
        public final String title;
        public final String exercises;
        public final long completedAt;

        public Routine(long id, String title, String exercises, long completedAt) {
            this.id = id;
            this.title = title;
            this.exercises = exercises;
            this.completedAt = completedAt;
        }
    }

    List<Routine> list();
    void save(String title, String exercises);
    void markCompleted(long id);
}
