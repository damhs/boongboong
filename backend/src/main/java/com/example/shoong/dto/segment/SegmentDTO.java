package com.example.shoong.dto.segment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SegmentDTO {
    private String segmentID;
    private String pathID;
    private Integer sequenceNumber;
    private String description;
    private Integer distance;
    private String duration; // "HH:mm:ss"
}