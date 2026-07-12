package com.assetflow.shared.service;

import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.shared.enums.RecordStatus;
import com.assetflow.shared.repository.BaseRepository;

import java.util.Optional;

public abstract class BaseQueryService<T, ID, R extends BaseRepository<T, ID>> {

    protected abstract R getRepository();
    
    protected abstract String getResourceName();

    public T findActiveEntityById(ID id) {
        return getRepository().findByIdAndStatus(id, RecordStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(getResourceName(), "id", id));
    }

    public T requireExists(Optional<T> optional, ID id) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(getResourceName(), "id", id));
    }
}
