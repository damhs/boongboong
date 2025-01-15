package com.example.shoong.service;

import com.example.shoong.entity.RefreshToken;
import com.example.shoong.entity.User;
import com.example.shoong.repository.RefreshTokenRepository;
import com.example.shoong.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }

  // Refresh Token 생성 및 저장
  public String createRefreshToken(String loginID) {
    // 사용자 조회
    User user = userRepository.findByLoginID(loginID)
        .orElseThrow(() -> new IllegalArgumentException("User with loginID " + loginID + " not found"));

    // 새 토큰 생성
    String token = UUID.randomUUID().toString();
    LocalDateTime expiresAt = LocalDateTime.now().plusDays(7); // Token valid for 7 days

    // Save token to DB
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(token);
    refreshToken.setUser(user);
    refreshToken.setExpiresAt(expiresAt);


    refreshTokenRepository.save(refreshToken);
    return token;
  }

  // Refresh Token 검증
  public boolean validateRefreshToken(String token) {
    return refreshTokenRepository.findByToken(token)
                .map(refreshToken -> refreshToken.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElse(false);
  }

  // Refresh Token 삭제
  public void deleteRefreshToken(String loginID) {
    // 사용자 조회
    User user = userRepository.findByLoginID(loginID)
        .orElseThrow(() -> new IllegalArgumentException("User with loginID " + loginID + " not found"));

    refreshTokenRepository.deleteByUser(user);
  }
}
