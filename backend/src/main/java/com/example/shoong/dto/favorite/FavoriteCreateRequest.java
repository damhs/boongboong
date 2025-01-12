package com.example.shoong.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteCreateRequest {
    @NotBlank
    private String userID;
    @NotBlank
    private String placeID;
    private String favoriteName;
}