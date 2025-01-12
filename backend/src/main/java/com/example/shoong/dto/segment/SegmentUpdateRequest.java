package com.example.shoong.dto.segment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class SegmentUpdateRequest {
    private Integer sequenceNumber;
    private String description;
    private Integer distance;
    private LocalTime duration;
}
