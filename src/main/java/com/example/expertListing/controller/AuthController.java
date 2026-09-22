package com.example.expertListing.controller;

import com.example.expertListing.dto.*;
import com.example.expertListing.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse data = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success("User registered and logged in successfully.", data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse data = authService.authenticateUser(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", data));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.example.expertListing.security.UserPrincipal currentUser) {
        UserInfoResponse data = authService.getCurrentUser(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User information retrieved successfully", data));
    }
}
