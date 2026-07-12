package com.assetflow.assets.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetCreatedEvent {
    private final Long assetId;
    private final String assetTag;
}
