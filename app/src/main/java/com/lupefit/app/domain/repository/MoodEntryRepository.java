package com.lupefit.app.domain.repository;

import com.lupefit.app.data.local.dao.MoodEntryDao;
import com.lupefit.app.data.local.entity.MoodEntry;
import androidx.lifecycle.LiveData;
import java.util.List;

/** Repositorio para MoodEntry - stub para compilación base. */
public class MoodEntryRepository {
    private final MoodEntryDao dao;
    public MoodEntryRepository(MoodEntryDao dao) { this.dao = dao; }
    public long insert(MoodEntry entry) { return dao.insert(entry); }
    public LiveData<List<MoodEntry>> getForDate(long userId, int dateKey) { return dao.getByUserAndDate(userId, dateKey); }
    public LiveData<Double> average(long userId, int from, int to) { return dao.getAverageMoodInRange(userId, from, to); }
}
