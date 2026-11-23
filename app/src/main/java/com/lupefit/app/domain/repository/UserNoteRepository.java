package com.lupefit.app.domain.repository;

import com.lupefit.app.data.local.dao.UserNoteDao;
import com.lupefit.app.data.local.entity.UserNote;
import androidx.lifecycle.LiveData;
import java.util.List;

/** Repositorio para UserNote - stub para compilación base. */
public class UserNoteRepository {
    private final UserNoteDao dao;
    public UserNoteRepository(UserNoteDao dao) { this.dao = dao; }
    public long insert(UserNote note) { return dao.insert(note); }
    public LiveData<List<UserNote>> getForDate(long userId, int dateKey) { return dao.getByUserAndDate(userId, dateKey); }
    public LiveData<List<UserNote>> getRecent(long userId, int limit) { return dao.getRecentByUser(userId, limit); }
}
