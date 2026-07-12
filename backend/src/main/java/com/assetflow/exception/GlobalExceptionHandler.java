package com.assetflow.exception;

import com.assetflow.shared.dto.ErrorResponse;
import com.assetflow.shared.dto.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusinessException(BusinessException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "BAD_REQUEST", request.getRequestURI());
    }

    @ExceptionHandler(AssetFlowException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAssetFlowException(AssetFlowException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "BAD_REQUEST", request.getRequestURI());
    }

    @ExceptionHandler(InvalidStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidStateException(InvalidStateException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "INVALID_STATE", request.getRequestURI());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "NOT_FOUND", request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "NOT_FOUND", request.getRequestURI());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "CONFLICT", request.getRequestURI());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "CONFLICT", request.getRequestURI());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "UNAUTHORIZED", request.getRequestURI());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUnauthorizedActionException(UnauthorizedActionException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "FORBIDDEN", request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return buildErrorResponse("Not authorized to access this resource", "FORBIDDEN", request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = "Validation failed: " + String.join("; ", errors.values());

        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrorCode("VALIDATION_ERROR");
        response.setPath(request.getRequestURI());
        response.setTimestamp(Instant.now());
        response.setFieldErrors(errors);

        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR", request.getRequestURI());
    }

    private ErrorResponse buildErrorResponse(String message, String errorCode, String path) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .path(path)
                .build();
    }
}
