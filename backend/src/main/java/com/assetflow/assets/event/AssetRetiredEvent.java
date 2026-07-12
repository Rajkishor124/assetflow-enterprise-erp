package com.assetflow.assets.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetRetiredEvent {
    private final Long assetId;
}
