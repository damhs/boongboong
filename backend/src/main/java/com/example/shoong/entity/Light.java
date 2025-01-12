package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "Light")
@Getter
@Setter
@NoArgsConstructor
public class Light {

    @Id
    @Column(name = "lightID", length = 36, nullable = false)
    private String lightID;

    @ManyToOne
    @JoinColumn(name = "placeID")
    private Place place;

    @Column(length = 20)
    private String status;  // RED, GREEN 등

    private LocalTime remainTime;
}
