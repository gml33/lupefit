package com.lupefit.core.domain.provider;

import com.lupefit.core.domain.repository.RecetaRepository;

public interface RecetaRepositoryProvider {
    RecetaRepository getRecetaRepository();
}