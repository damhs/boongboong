package com.example.shoong.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Light {

    @Id
    @Column(name = "lightID", length = 36, nullable = false)
    private String lightID;

    @Column
    private String itstId;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}
