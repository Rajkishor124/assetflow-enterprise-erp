package com.assetflow.shared.service;

import com.assetflow.shared.repository.BaseRepository;

public abstract class BaseCommandService<T, ID, R extends BaseRepository<T, ID>> {

    protected abstract R getRepository();
    
    protected abstract String getResourceName();

    // Command services often need to fetch before update/delete
    // We can inject the corresponding QueryService or provide basic fetches here
}
