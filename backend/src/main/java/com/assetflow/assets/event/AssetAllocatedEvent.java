package com.assetflow.assets.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetAllocatedEvent {
    private final Long assetId;
    private final Long userId;
    private final Long departmentId;
}
