package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PathLight")
@Getter
@Setter
@NoArgsConstructor
public class PathLight {

    @Id
    @Column(name = "pathlightID", length = 36, nullable = false)
    private String pathlightID;

    @ManyToOne
    @JoinColumn(name = "pathID")
    private Path path;

    // DB 컬럼이 lightID2 이므로 따로 설정
    @ManyToOne
    @JoinColumn(name = "lightID")
    private Light light;

    private Boolean isAvoided;
}
