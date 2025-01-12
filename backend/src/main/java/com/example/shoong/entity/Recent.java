package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Recent")
@Getter
@Setter
@NoArgsConstructor
public class Recent {

    @Id
    @Column(name = "recentID", length = 36, nullable = false)
    private String recentID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "placeID")
    private Place place;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
