package com.ovidea.assetflow.auth.controller;

import com.ovidea.assetflow.auth.dto.LoginRequestDTO;
import com.ovidea.assetflow.auth.dto.RefreshTokenRequestDTO;
import com.ovidea.assetflow.auth.dto.SignupRequestDTO;
import com.ovidea.assetflow.auth.dto.TokenResponseDTO;
import com.ovidea.assetflow.auth.service.AuthService;
import com.ovidea.assetflow.common.ApiResponse;
import com.ovidea.assetflow.organization.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*") // In production, restrict this via CorsConfig
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDTO>> signup(@Valid @RequestBody SignupRequestDTO dto) {
        UserResponseDTO user = authService.signup(dto);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully. Default role is EMPLOYEE.", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> login(@Valid @RequestBody LoginRequestDTO dto) {
        TokenResponseDTO tokenResponse = authService.login(dto);
        return ResponseEntity.ok(ApiResponse.success("Login successful.", tokenResponse));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        TokenResponseDTO tokenResponse = authService.refreshToken(dto.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully.", tokenResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        authService.logout(dto.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Logout successful. Refresh token revoked."));
    }
}
