package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * OpenAPI 스펙:
 *  placeID, placeType, placeName, latitude, longitude, etc
 */
@Entity
@Table(name = "Place")
@Getter
@Setter
@NoArgsConstructor
public class Place {

    @Id
    @Column(name = "placeID", length = 36, nullable = false)
    private String placeID;

    @Column(length = 20)
    private String placeType; // SPOT, INTERSECTION 등

    @Column(length = 50)
    private String placeName;

    @Column(precision = 13, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 13, scale = 10)
    private BigDecimal longitude;

    @Column(length = 255)
    private String etc; // 기타 정보
}
