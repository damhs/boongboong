package com.example.shoong.dto.pathlight;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathLightCreateRequest {
    @NotBlank
    private String pathID;
    @NotBlank
    private String lightID;
    private Boolean isAvoided;
}