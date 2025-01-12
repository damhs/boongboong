package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private String placeType;

    @Column(length = 20)
    private String placeName;

    // DECIMAL(13,10)을 자바 BigDecimal 로 매핑
    @Column(precision = 13, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 13, scale = 10)
    private BigDecimal longitude;

    @Column(length = 255)
    private String etc;
}
