package com.lupefit.app.data.local.repository;

import com.lupefit.app.data.local.dao.RoutineDao;
import com.lupefit.app.data.local.entity.RoutineEntity;
import com.lupefit.core.domain.repository.RoutineRepository;

import java.util.ArrayList;
import java.util.List;

public class RoutineRepositoryImpl implements RoutineRepository {
    private final RoutineDao dao;

    public RoutineRepositoryImpl(RoutineDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Routine> list() {
        List<Routine> result = new ArrayList<>();
        for (RoutineEntity entity : dao.list()) {
            result.add(new Routine(entity.id, entity.title, entity.exercises, entity.completedAt));
        }
        return result;
    }

    @Override
    public void save(String title, String exercises) {
        RoutineEntity routine = new RoutineEntity();
        routine.title = title;
        routine.exercises = exercises;
        dao.insert(routine);
    }

    @Override
    public void markCompleted(long id) {
        dao.markCompleted(id, System.currentTimeMillis());
    }
}
