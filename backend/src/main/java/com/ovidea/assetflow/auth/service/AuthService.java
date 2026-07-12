package com.ovidea.assetflow.auth.service;

import com.ovidea.assetflow.auth.dto.LoginRequestDTO;
import com.ovidea.assetflow.auth.dto.SignupRequestDTO;
import com.ovidea.assetflow.auth.dto.TokenResponseDTO;
import com.ovidea.assetflow.auth.entity.RefreshToken;
import com.ovidea.assetflow.auth.repository.RefreshTokenRepository;
import com.ovidea.assetflow.exception.EmailAlreadyExistsException;
import com.ovidea.assetflow.organization.dto.UserResponseDTO;
import com.ovidea.assetflow.organization.entity.Role;
import com.ovidea.assetflow.organization.entity.User;
import com.ovidea.assetflow.organization.repository.RoleRepository;
import com.ovidea.assetflow.organization.repository.UserRepository;
import com.ovidea.assetflow.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponseDTO signup(SignupRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + dto.getEmail());
        }

        // Default role is EMPLOYEE for signup per architecture
        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Default EMPLOYEE role not found in database. Seed data first."));

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role(employeeRole)
                .build();

        User savedUser = userRepository.save(user);
        return mapToUserResponseDTO(savedUser);
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User details not found for email: " + dto.getEmail()));

        // Generate Refresh Token
        String rawRefreshToken = jwtTokenProvider.generateRawRefreshToken();
        String tokenHash = jwtTokenProvider.hashToken(rawRefreshToken);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(rawRefreshToken)
                .user(mapToUserResponseDTO(user))
                .build();
    }

    @Transactional
    public TokenResponseDTO refreshToken(String rawRefreshToken) {
        String tokenHash = jwtTokenProvider.hashToken(rawRefreshToken);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token has expired");
        }

        // Revoke the old refresh token (Token Rotation)
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // Generate new Access and Refresh tokens
        User user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.generateTokenFromUsername(user.getEmail());
        String newRawRefreshToken = jwtTokenProvider.generateRawRefreshToken();
        String newTokenHash = jwtTokenProvider.hashToken(newRawRefreshToken);

        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .user(user)
                .tokenHash(newTokenHash)
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenRepository.save(newRefreshTokenEntity);

        return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRawRefreshToken)
                .user(mapToUserResponseDTO(user))
                .build();
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        String tokenHash = jwtTokenProvider.hashToken(rawRefreshToken);
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByTokenHash(tokenHash);
        if (refreshTokenOpt.isPresent()) {
            RefreshToken refreshToken = refreshTokenOpt.get();
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        }
    }

    // Helper method to map User entity to UserResponseDTO
    private UserResponseDTO mapToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .deptId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .status(user.getStatus().name())
                .build();
    }
}
