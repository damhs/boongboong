package com.example.shoong.repository;

import com.example.shoong.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token); // 토큰으로 검색

    void deleteByUserID(String userID); // 특정 사용자의 토큰 삭제
}
