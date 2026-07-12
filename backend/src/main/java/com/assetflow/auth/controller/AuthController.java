package com.assetflow.auth.controller;

import com.assetflow.auth.dto.LoginRequestDTO;
import com.assetflow.auth.dto.RefreshTokenRequestDTO;
import com.assetflow.auth.dto.SignupRequestDTO;
import com.assetflow.auth.dto.TokenResponseDTO;
import com.assetflow.auth.service.AuthService;
import com.assetflow.shared.dto.ApiResponse;
import com.assetflow.util.ApiPaths;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.AUTH)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> signup(@Valid @RequestBody SignupRequestDTO request) {
        authService.signup(request);
        return ApiResponse.success(null, "User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        TokenResponseDTO token = authService.login(request);
        return ApiResponse.success(token, "Login successful");
    }

    @PostMapping("/refresh-token")
    public ApiResponse<TokenResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        TokenResponseDTO token = authService.refreshToken(request);
        return ApiResponse.success(token, "Token refreshed successfully");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@Valid @RequestBody RefreshTokenRequestDTO request) {
        authService.logout(request);
        return ApiResponse.success(null, "Logged out successfully");
    }
}
