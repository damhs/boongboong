package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Favorite")
@Getter
@Setter
@NoArgsConstructor
public class Favorite {

    @Id
    @Column(name = "favoriteID", length = 36, nullable = false)
    private String favoriteID;

    @ManyToOne
    @JoinColumn(name = "userID")   // FK -> User
    private User user;

    @ManyToOne
    @JoinColumn(name = "placeID")  // FK -> Place
    private Place place;

    @Column(length = 50)
    private String favoriteName;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
