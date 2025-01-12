package com.example.shoong.controller;

import com.example.shoong.dto.favorite.FavoriteCreateRequest;
import com.example.shoong.dto.favorite.FavoriteDTO;
import com.example.shoong.dto.favorite.FavoriteUpdateRequest;
import com.example.shoong.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // [POST] /favorites
    @PostMapping
    public ResponseEntity<FavoriteDTO> createFavorite(
            @Valid @RequestBody FavoriteCreateRequest request
    ) {
        FavoriteDTO dto = favoriteService.createFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // [GET] /favorites/{userID} -> 특정 유저의 즐겨찾기 목록 조회
    @GetMapping("/{userID}")
    public ResponseEntity<List<FavoriteDTO>> getFavoritesByUser(@PathVariable String userID) {
        List<FavoriteDTO> list = favoriteService.getFavoritesByUser(userID);
        return ResponseEntity.ok(list);
    }

    // [DELETE] /favorites/{favoriteID}
    @DeleteMapping("/{favoriteID}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable String favoriteID) {
        favoriteService.deleteFavorite(favoriteID);
        return ResponseEntity.noContent().build();
    }
}

