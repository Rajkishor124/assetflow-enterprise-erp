package com.assetflow.assets.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetMaintenanceRequestedEvent {
    private final Long assetId;
}
