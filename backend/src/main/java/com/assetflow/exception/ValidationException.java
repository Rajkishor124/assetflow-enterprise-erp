package com.assetflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends AssetFlowException {
    
    public ValidationException(String message) {
        super(message);
    }
}
