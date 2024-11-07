package edu.tbank.hw5.controller;

import edu.tbank.hw5.dto.auth.request.LoginRequest;
import edu.tbank.hw5.dto.auth.request.RegisterRequest;
import edu.tbank.hw5.dto.auth.request.ResetPasswordRequest;
import edu.tbank.hw5.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, @RequestHeader("Authorization") String token) {
        return authService.resetPassword(request, token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        log.info("Logging out user with token: {}", token);
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}