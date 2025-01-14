package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Light")
@Getter
@Setter
@NoArgsConstructor
public class Light {

    @Id
    @Column(name = "lightID", length = 5, nullable = false)
    private String lightID;

    @ManyToOne
    @JoinColumn(name = "placeID")
    private Place place;
}
