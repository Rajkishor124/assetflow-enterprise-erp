package com.assetflow.auth.service;

import com.assetflow.auth.entity.RefreshToken;
import com.assetflow.auth.repository.RefreshTokenRepository;
import com.assetflow.exception.UnauthorizedException;
import com.assetflow.organization.entity.User;
import com.assetflow.security.jwt.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

/**
 * Manages refresh token lifecycle: creation, rotation, revocation.
 * Refresh tokens are stored as SHA-256 hashes to prevent theft via DB compromise.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    /**
     * Create a new refresh token for the given user.
     * @return the raw (unhashed) token to send to the client.
     */
    @Transactional
    public String createRefreshToken(User user) {
        String rawToken = jwtService.generateRefreshToken();
        String hashedToken = hashToken(rawToken);

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setToken(hashedToken);
        entity.setExpiryDate(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()));
        refreshTokenRepository.save(entity);

        return rawToken;
    }

    /**
     * Validate and rotate a refresh token.
     * Deletes the old token and creates a new one (token rotation).
     * @return the new raw refresh token.
     */
    @Transactional
    public RefreshToken validateAndRotate(String rawToken) {
        String hashedToken = hashToken(rawToken);
        RefreshToken existing = refreshTokenRepository.findByToken(hashedToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (existing.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(existing);
            throw new UnauthorizedException("Refresh token has expired. Please log in again.");
        }

        // Delete old token (rotation)
        refreshTokenRepository.delete(existing);

        return existing;
    }

    /**
     * Revoke a refresh token by its raw value.
     */
    @Transactional
    public void revokeByToken(String rawToken) {
        String hashedToken = hashToken(rawToken);
        refreshTokenRepository.findByToken(hashedToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    /**
     * Revoke all refresh tokens for a user (e.g., on password change).
     */
    @Transactional
    public void revokeAllForUser(Long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    /**
     * SHA-256 hash a raw token string.
     */
    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
