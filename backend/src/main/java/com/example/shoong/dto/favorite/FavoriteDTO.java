package com.example.shoong.dto.favorite;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteDTO {
    private String favoriteID;
    private String userID;
    private String placeID;
    private String favoriteName;
    private String updatedAt;
}