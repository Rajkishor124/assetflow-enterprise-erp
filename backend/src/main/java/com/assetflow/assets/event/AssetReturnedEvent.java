package com.assetflow.assets.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetReturnedEvent {
    private final Long assetId;
}
