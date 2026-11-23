package com.lupefit.app.domain.repository;

import com.lupefit.app.data.local.dao.StreakDao;
import com.lupefit.app.data.local.entity.Streak;
import androidx.lifecycle.LiveData;
import java.util.List;

/** Repositorio para Streak - stub para compilación base. */
public class StreakRepository {
    private final StreakDao dao;
    public StreakRepository(StreakDao dao) { this.dao = dao; }
    public LiveData<List<Streak>> getActive(long userId) { return dao.getActiveStreaksByUser(userId); }
    public void upsert(Streak streak) { dao.insert(streak); }
}
