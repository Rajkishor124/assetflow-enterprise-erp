package com.assetflow.auth.service;

import com.assetflow.auth.dto.LoginRequestDTO;
import com.assetflow.auth.dto.RefreshTokenRequestDTO;
import com.assetflow.auth.dto.SignupRequestDTO;
import com.assetflow.auth.dto.TokenResponseDTO;
import com.assetflow.auth.entity.RefreshToken;
import com.assetflow.exception.ConflictException;
import com.assetflow.exception.UnauthorizedException;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.repository.UserRepository;
import com.assetflow.security.entity.Role;
import com.assetflow.security.jwt.JwtService;
import com.assetflow.security.repository.RoleRepository;
import com.assetflow.security.userdetails.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String DEFAULT_ROLE = "EMPLOYEE";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public void signup(SignupRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("An account with email '" + request.getEmail() + "' already exists");
        }

        Role employeeRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default role '" + DEFAULT_ROLE + "' not found. Run Flyway migrations."));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(employeeRole);

        userRepository.save(user);
        logger.info("AUTH_EVENT: USER_REGISTERED email={}", request.getEmail());
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(userDetails);

            User user = userRepository.findById(userDetails.getUserId())
                    .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
            String rawRefreshToken = refreshTokenService.createRefreshToken(user);

            logger.info("AUTH_EVENT: USER_LOGGED_IN email={}", request.getEmail());

            return TokenResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(rawRefreshToken)
                    .expiresIn(jwtService.getAccessExpirationSeconds())
                    .build();

        } catch (BadCredentialsException e) {
            logger.warn("AUTH_EVENT: LOGIN_FAILED email={} reason=bad_credentials", request.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        } catch (DisabledException e) {
            logger.warn("AUTH_EVENT: LOGIN_FAILED email={} reason=account_disabled", request.getEmail());
            throw new UnauthorizedException("Account is deactivated. Please contact an administrator.");
        }
    }

    @Transactional
    public TokenResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        RefreshToken oldToken = refreshTokenService.validateAndRotate(request.getRefreshToken());

        User user = oldToken.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRawRefreshToken = refreshTokenService.createRefreshToken(user);

        logger.info("AUTH_EVENT: TOKEN_REFRESHED userId={}", user.getId());

        return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRawRefreshToken)
                .expiresIn(jwtService.getAccessExpirationSeconds())
                .build();
    }

    @Transactional
    public void logout(RefreshTokenRequestDTO request) {
        refreshTokenService.revokeByToken(request.getRefreshToken());
        logger.info("AUTH_EVENT: USER_LOGGED_OUT");
    }
}
