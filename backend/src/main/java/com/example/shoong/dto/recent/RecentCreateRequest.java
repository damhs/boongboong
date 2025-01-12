package com.example.shoong.dto.recent;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentCreateRequest {
    @NotBlank
    private String userID;
    @NotBlank
    private String placeID;
}