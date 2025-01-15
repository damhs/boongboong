package com.example.shoong.service;

import com.example.shoong.dto.place.PlaceDTO;
import com.example.shoong.dto.place.PlaceUpdateRequest;
import com.example.shoong.dto.recent.RecentCreateRequest;
import com.example.shoong.dto.recent.RecentDTO;
import com.example.shoong.entity.Place;
import com.example.shoong.entity.Recent;
import com.example.shoong.entity.User;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.PlaceRepository;
import com.example.shoong.repository.RecentRepository;
import com.example.shoong.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecentService {

    private final RecentRepository recentRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public RecentService(
        RecentRepository recentRepository,
        UserRepository userRepository,
        PlaceRepository placeRepository
    ) {
        this.recentRepository = recentRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        }

        @Transactional(readOnly = true)
        public List<RecentDTO> getRecents() {
        List<Recent> recents = recentRepository.findAll();
        return recents.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        }

        @Transactional
        public RecentDTO createRecent(RecentCreateRequest request) {
        User user = userRepository.findById(request.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("유저가 없습니다. ID=" + request.getUserID()));
        Place place = placeRepository.findById(request.getPlaceID())
                .orElseThrow(() -> new ResourceNotFoundException("장소가 없습니다. ID=" + request.getPlaceID()));

        Recent recent = new Recent();
        recent.setRecentID(UUID.randomUUID().toString());
        recent.setUser(user);
        recent.setPlace(place);
        recent.setUpdatedAt(LocalDateTime.now());

        Recent saved = recentRepository.save(recent);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<RecentDTO> getRecentsByUser(String userID) {
        // 간단 구현
        List<Recent> list = recentRepository.findAll()
                .stream()
                .filter(r -> r.getUser().getUserID().equals(userID))
                .toList();
        return list.stream().map(this::toDTO).collect(Collectors.toList());
        }

        @Transactional
        public RecentDTO updateRecent(String recentID, RecentCreateRequest request) {
        Recent recent = recentRepository.findById(recentID)
            .orElseThrow(() -> new ResourceNotFoundException("최근 검색 기록을 찾을 수 없습니다. ID=" + recentID));

        User user = userRepository.findById(request.getUserID())
            .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다. ID=" + request.getUserID()));
        Place place = placeRepository.findById(request.getPlaceID())
            .orElseThrow(() -> new ResourceNotFoundException("장소를 찾을 수 없습니다. ID=" + request.getPlaceID()));

        recent.setUser(user);
        recent.setPlace(place);
        recent.setUpdatedAt(LocalDateTime.now());

        Recent updated = recentRepository.save(recent);
        return toDTO(updated);
        }

        @Transactional
        public void deleteRecent(String recentID) {
        if(!recentRepository.existsById(recentID)) {
            throw new ResourceNotFoundException("해당 최근 검색 기록이 없습니다. ID=" + recentID);
        }
        recentRepository.deleteById(recentID);
    }

    private RecentDTO toDTO(Recent recent) {
        RecentDTO dto = new RecentDTO();
        dto.setRecentID(recent.getRecentID());
        dto.setUserID(recent.getUser().getUserID());
        dto.setPlaceID(recent.getPlace().getPlaceID());
        dto.setUpdatedAt(recent.getUpdatedAt().toString());
        return dto;
    }
}
