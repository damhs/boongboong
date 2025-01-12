package com.example.shoong.dto.light;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightDTO {
    private String lightID;
    private String placeID;
    private String status;
    private String remainTime; // "HH:mm:ss"
}