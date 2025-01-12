package com.example.shoong.dto.path;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class PathUpdateRequest {
    private Integer totalDistance;
    private LocalTime totalTime;
}