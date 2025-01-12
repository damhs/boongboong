package com.example.shoong.dto.pathlight;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathLightDTO {
    private String pathlightID;
    private String pathID;
    private String lightID;
    private Boolean isAvoided;
}