package com.example.shoong.controller;

import com.example.shoong.dto.LoginRequest;
import com.example.shoong.dto.RefreshRequest;
import com.example.shoong.dto.TokenResponse;
import com.example.shoong.service.JwtUtil;
import com.example.shoong.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLoginID(),
                        loginRequest.getPassword()
                )
        );

        // Access Token 생성
        String accessToken = jwtUtil.generateAccessToken(authentication.getName());

        // Refresh Token 생성 및 저장
        String refreshToken = refreshTokenService.createRefreshToken(authentication.getName());

        return new TokenResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshRequest refreshRequest) {
        // Refresh Token 검증 및 사용자 이름 반환
        if (!refreshTokenService.validateRefreshToken(refreshRequest.getRefreshToken())) {
            throw new RuntimeException("Invalid or expired Refresh Token");
        }

        // 새로운 Access Token 생성
        String username = jwtUtil.validateToken(refreshRequest.getRefreshToken());
        String newAccessToken = jwtUtil.generateAccessToken(username);

        return new TokenResponse(newAccessToken, refreshRequest.getRefreshToken());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody String userId) {
        // Refresh Token 삭제
        refreshTokenService.deleteRefreshToken(userId);
    }
}
