package com.example.shoong.dto.path;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathDTO {
    private String pathID;
    private String userID;
    private String originID;
    private String destinationID;
    private Integer totalDistance;
    private String totalTime;  // "HH:mm:ss"
    private String updatedAt;
}