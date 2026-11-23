package com.lupefit.app.domain.usecase;

import com.lupefit.app.domain.repository.MoodEntryRepository;
import com.lupefit.app.domain.engine.SyncEngine;

/** Caso de uso para MoodEntry - stub para compilación base. */
public class MoodEntryUseCase {
    private final MoodEntryRepository repository;
    public MoodEntryUseCase(MoodEntryRepository repository, SyncEngine syncEngine) { this.repository = repository; }
    public long save(com.lupefit.app.data.local.entity.MoodEntry entry) { return repository.insert(entry); }
}
