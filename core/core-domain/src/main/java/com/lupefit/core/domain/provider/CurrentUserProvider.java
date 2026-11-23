package com.lupefit.core.domain.provider;

/** Provee la identidad local activa a los módulos de funcionalidad. */
public interface CurrentUserProvider {
    long getCurrentUserId();
}
