package com.assetflow.exception;

public abstract class AssetFlowException extends RuntimeException {
    public AssetFlowException(String message) {
        super(message);
    }

    public AssetFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
