package com.example.shoong.service;

import com.example.shoong.dto.favorite.FavoriteCreateRequest;
import com.example.shoong.dto.favorite.FavoriteDTO;
import com.example.shoong.dto.favorite.FavoriteUpdateRequest;
import com.example.shoong.entity.Favorite;
import com.example.shoong.entity.User;
import com.example.shoong.entity.Place;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.FavoriteRepository;
import com.example.shoong.repository.UserRepository;
import com.example.shoong.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public FavoriteService(
        FavoriteRepository favoriteRepository,
        UserRepository userRepository,
        PlaceRepository placeRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Transactional
    public FavoriteDTO createFavorite(FavoriteCreateRequest request) {
        // user, place 존재 여부 체크
        User user = userRepository.findById(request.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다. ID=" + request.getUserID()));
        Place place = placeRepository.findById(request.getPlaceID())
                .orElseThrow(() -> new ResourceNotFoundException("장소가 존재하지 않습니다. ID=" + request.getPlaceID()));

        Favorite fav = new Favorite();
        fav.setFavoriteID(UUID.randomUUID().toString());
        fav.setUser(user);
        fav.setPlace(place);
        fav.setFavoriteName(request.getFavoriteName());
        fav.setUpdatedAt(LocalDateTime.now());

        Favorite saved = favoriteRepository.save(fav);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<FavoriteDTO> getFavoritesByUser(String userID) {
        // 특정 유저 ID 기준으로 찾는다고 가정
        // 필요하다면 커스텀 쿼리를 사용할 수도 있음 (ex. findByUserUserID)
        List<Favorite> list = favoriteRepository.findAll()
                .stream()
                .filter(f -> f.getUser().getUserID().equals(userID))
                .toList();
        return list.stream().map(this::toDTO).toList();
    }

    @Transactional
    public void deleteFavorite(String favoriteID) {
        if (!favoriteRepository.existsById(favoriteID)) {
            throw new ResourceNotFoundException("해당 즐겨찾기가 존재하지 않습니다. ID=" + favoriteID);
        }
        favoriteRepository.deleteById(favoriteID);
    }

    private FavoriteDTO toDTO(Favorite fav) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setFavoriteID(fav.getFavoriteID());
        dto.setUserID(fav.getUser().getUserID());
        dto.setPlaceID(fav.getPlace().getPlaceID());
        dto.setFavoriteName(fav.getFavoriteName());
        dto.setUpdatedAt(fav.getUpdatedAt() != null ? fav.getUpdatedAt().toString() : null);
        return dto;
    }
}