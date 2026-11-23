package com.lupefit.app.domain.usecase;

import com.lupefit.app.domain.repository.UserNoteRepository;
import com.lupefit.app.domain.engine.SyncEngine;

/** Caso de uso para UserNote - stub para compilación base. */
public class UserNoteUseCase {
    private final UserNoteRepository repository;
    public UserNoteUseCase(UserNoteRepository repository, SyncEngine syncEngine) { this.repository = repository; }
    public long save(com.lupefit.app.data.local.entity.UserNote note) { return repository.insert(note); }
}
