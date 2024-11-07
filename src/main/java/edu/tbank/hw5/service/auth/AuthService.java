package edu.tbank.hw5.service.auth;

import edu.tbank.hw5.dto.auth.request.LoginRequest;
import edu.tbank.hw5.dto.auth.request.RegisterRequest;
import edu.tbank.hw5.dto.auth.request.ResetPasswordRequest;
import edu.tbank.hw5.dto.auth.response.JwtResponse;
import edu.tbank.hw5.entity.Role;
import edu.tbank.hw5.entity.Token;
import edu.tbank.hw5.entity.User;
import edu.tbank.hw5.repository.jpa.TokenRepository;
import edu.tbank.hw5.repository.jpa.UserRepository;
import edu.tbank.hw5.service.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getUsername(), false);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public String authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean rememberMe = loginRequest.isRememberMe();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtProvider.generateToken(userDetails, rememberMe);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Token token = new Token();
        token.setToken(jwt);
        token.setExpiryDate(rememberMe ?
                Instant.now().plus(jwtProvider.getJwtExpirationDays(), ChronoUnit.DAYS) :
                Instant.now().plus(jwtProvider.getJwtExpirationMinutes(), ChronoUnit.MINUTES)
        );
        token.setUser(user);
        token.setActive(true);

        tokenRepository.save(token);

        return jwt;
    }

    public void logout(String token) {
        token = token.replace("Bearer ", "");
        if (!validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        Token storedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        tokenRepository.delete(storedToken);
    }

    public boolean validateToken(String token) {
        token = token.replace("Bearer ", "");
        Token storedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        boolean isValid = storedToken.isActive() && storedToken.getExpiryDate().isAfter(Instant.now());
        if (!isValid) {
            expireToken(token);
        }
        return isValid;
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest request, String token) {
        token = token.replace("Bearer ", "");
        if (!validateToken(token)) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        expireToken(token);

        return ResponseEntity.ok("Password reset successfully");
    }

    public void expireToken(String token) {
        token = token.replace("Bearer ", "");
        Token storedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        tokenRepository.delete(storedToken);
    }
}