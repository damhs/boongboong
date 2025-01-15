package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Path")  // DB 테이블이 "Path" 이므로 지정
@Getter
@Setter
@NoArgsConstructor
public class Path {
    
    @Id
    @Column(name = "pathID", length = 36, nullable = false)
    private String pathID;

    @ManyToOne
    @JoinColumn(name = "userID") // FK
    private User user;

    @ManyToOne
    @JoinColumn(name = "originID") // FK
    private Place origin;

    @ManyToOne
    @JoinColumn(name = "destinationID") // FK
    private Place destination;

    @Column(name = "totalDistance")
    private Integer totalDistance;

    @Column(name = "totalTime")
    private LocalTime totalTime;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}