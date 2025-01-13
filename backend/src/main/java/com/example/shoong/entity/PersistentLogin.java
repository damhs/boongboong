package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Timestamp;

@Entity
@Table(name = "PersistentLogin")  // DB 테이블이 "User" 이므로 지정
@Getter
@Setter
@NoArgsConstructor
public class PersistentLogin {
    
    @Id
    @Column(length = 64)
    private String series;

    @Column(name = "username", length = 36, nullable = false) // id를 username으로 사용
    private String username;

    @Column(length = 64)
    private String token;

    @Column(name = "last_used")
    private Timestamp lastUsed;
    
    // 필요하다면 생성자, toString, 빌더 등을 추가
}