package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")  // DB 테이블이 "User" 이므로 지정
@Getter
@Setter
@NoArgsConstructor
public class User {
    
    @Id
    @Column(name = "userID", length = 36, nullable = false)
    private String userID;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String id; // 로그인 아이디

    @Column(length = 100)
    private String password;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    
    // 필요하다면 생성자, toString, 빌더 등을 추가
}