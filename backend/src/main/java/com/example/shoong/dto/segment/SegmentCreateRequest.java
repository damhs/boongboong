package com.example.shoong.dto.segment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class SegmentCreateRequest {
    @NotBlank
    private String pathID;
    private Integer sequenceNumber;
    private String description;
    private Integer distance;
    private LocalTime duration;
}