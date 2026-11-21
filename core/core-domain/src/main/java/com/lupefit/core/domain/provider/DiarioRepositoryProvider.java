package com.lupefit.core.domain.provider;

import com.lupefit.core.domain.repository.DiarioRepository;

public interface DiarioRepositoryProvider {
    DiarioRepository getDiarioRepository();
}