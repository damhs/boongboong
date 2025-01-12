package com.example.shoong.dto.place;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlaceDTO {
    private String placeID;
    private String placeType;
    private String placeName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String etc;
}