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

    @ManyToOne
    @JoinColumn(name = "lightID")
    private Light light;

    private Boolean isAvoided;
}
