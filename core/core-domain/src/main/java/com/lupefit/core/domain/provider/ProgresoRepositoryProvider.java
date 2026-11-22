package com.lupefit.core.domain.provider;

import com.lupefit.core.domain.repository.ProgresoRepository;

public interface ProgresoRepositoryProvider {
    ProgresoRepository getProgresoRepository();
}