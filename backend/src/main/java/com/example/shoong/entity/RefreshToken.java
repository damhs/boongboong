package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenID;

    @Column(nullable = false, unique = true)
    private String token; // Refresh Token 값

    @Column(nullable = false)
    private String userID; // 사용자 ID와 연결

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 만료 시간
}
