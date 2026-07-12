package com.assetflow.security.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "assetflow.security.jwt")
public record JwtProperties(
        @NotBlank(message = "JWT secret must be configured")
        @Size(min = 64, message = "JWT secret must be at least 64 characters long for HS512")
        String secret,

        @Min(value = 60000, message = "Access token expiration must be at least 1 minute (60000ms)")
        long accessExpirationMs,

        @Min(value = 3600000, message = "Refresh token expiration must be at least 1 hour (3600000ms)")
        long refreshExpirationMs
) {}
