package com.lupefit.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lupefit.app.data.local.entity.RoutineEntity;

import java.util.List;

@Dao
public interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY id DESC")
    List<RoutineEntity> list();

    @Insert
    void insert(RoutineEntity routine);

    @Query("UPDATE routines SET completedAt = :timestamp WHERE id = :id")
    void markCompleted(long id, long timestamp);
}
