package com.example.shoong.dto.light;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LightCreateRequest {
    @NotBlank
    private String placeID;
    private String status; // RED, GREEN
    private LocalTime remainTime;
}