package com.example.shoong.service;

import com.example.shoong.dto.place.PlaceCreateRequest;
import com.example.shoong.dto.place.PlaceDTO;
import com.example.shoong.dto.place.PlaceUpdateRequest;
import com.example.shoong.entity.Place;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Transactional
    public PlaceDTO createPlace(PlaceCreateRequest request) {
        Place place = new Place();
        place.setPlaceID(UUID.randomUUID().toString());
        place.setPlaceType(request.getPlaceType());
        place.setPlaceName(request.getPlaceName());
        place.setLatitude(request.getLatitude());
        place.setLongitude(request.getLongitude());
        place.setEtc(request.getEtc());

        Place saved = placeRepository.save(place);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public PlaceDTO getPlace(String placeID) {
        Place place = placeRepository.findById(placeID)
                .orElseThrow(() -> new ResourceNotFoundException("장소를 찾을 수 없습니다. ID=" + placeID));
        return toDTO(place);
    }

    @Transactional
    public PlaceDTO updatePlace(String placeID, PlaceUpdateRequest request) {
        Place place = placeRepository.findById(placeID)
                .orElseThrow(() -> new ResourceNotFoundException("장소를 찾을 수 없습니다. ID=" + placeID));

        if (request.getPlaceType() != null)   place.setPlaceType(request.getPlaceType());
        if (request.getPlaceName() != null)   place.setPlaceName(request.getPlaceName());
        if (request.getLatitude() != null)    place.setLatitude(request.getLatitude());
        if (request.getLongitude() != null)   place.setLongitude(request.getLongitude());
        if (request.getEtc() != null)         place.setEtc(request.getEtc());

        return toDTO(place);
    }

    @Transactional
    public void deletePlace(String placeID) {
        if (!placeRepository.existsById(placeID)) {
            throw new ResourceNotFoundException("장소를 찾을 수 없습니다. ID=" + placeID);
        }
        placeRepository.deleteById(placeID);
    }

    private PlaceDTO toDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setPlaceID(place.getPlaceID());
        dto.setPlaceType(place.getPlaceType());
        dto.setPlaceName(place.getPlaceName());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setEtc(place.getEtc());
        return dto;
    }
}
