package com.example.shoong.dto.light;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LightUpdateRequest {
    private String status;
    private LocalTime remainTime;
}