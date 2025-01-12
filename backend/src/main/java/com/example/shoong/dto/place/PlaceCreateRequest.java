package com.example.shoong.dto.place;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlaceCreateRequest {

    @NotBlank(message = "placeType은 필수입니다.")
    private String placeType;

    @NotBlank(message = "placeName은 필수입니다.")
    private String placeName;

    // 위도/경도는 null 가능하다고 가정
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String etc;
}