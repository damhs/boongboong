package com.example.shoong.dto.place;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlaceUpdateRequest {
    // 수정 시엔 모두 선택 입력 가능
    private String placeType;
    private String placeName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String etc;
}