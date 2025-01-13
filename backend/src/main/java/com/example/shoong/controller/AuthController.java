package com.example.shoong.controller;

import com.example.shoong.service.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.shoong.dto.LoginRequest;
import com.example.shoong.dto.RefreshRequest;
import com.example.shoong.dto.TokenResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String accessToken = jwtUtil.generateAccessToken(authentication.getName());
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());
        return new TokenResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshRequest refreshRequest) {
        String username = jwtUtil.validateToken(refreshRequest.getRefreshToken());
        if (username == null) {
            throw new RuntimeException("Invalid Refresh Token");
        }
        String newAccessToken = jwtUtil.generateAccessToken(username);
        return new TokenResponse(newAccessToken, refreshRequest.getRefreshToken());
    }
}





