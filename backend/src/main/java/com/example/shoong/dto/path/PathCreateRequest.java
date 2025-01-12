package com.example.shoong.dto.path;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class PathCreateRequest {
    @NotBlank
    private String userID;
    @NotBlank
    private String originID;
    @NotBlank
    private String destinationID;
    private Integer totalDistance;
    private LocalTime totalTime;
}