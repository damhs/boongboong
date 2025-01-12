package com.example.shoong.service;

import com.example.shoong.dto.light.LightCreateRequest;
import com.example.shoong.dto.light.LightDTO;
import com.example.shoong.dto.light.LightUpdateRequest;
import com.example.shoong.entity.Light;
import com.example.shoong.entity.Place;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.LightRepository;
import com.example.shoong.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LightService {

    private final LightRepository lightRepository;
    private final PlaceRepository placeRepository;

    public LightService(LightRepository lightRepository, PlaceRepository placeRepository) {
        this.lightRepository = lightRepository;
        this.placeRepository = placeRepository;
    }

    @Transactional
    public LightDTO createLight(LightCreateRequest request) {
        Place place = placeRepository.findById(request.getPlaceID())
            .orElseThrow(() -> new ResourceNotFoundException("장소를 찾을 수 없습니다. ID=" + request.getPlaceID()));

        Light light = new Light();
        light.setLightID(UUID.randomUUID().toString());
        light.setPlace(place);
        light.setStatus(request.getStatus());
        light.setRemainTime(request.getRemainTime());

        Light saved = lightRepository.save(light);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public LightDTO getLight(String lightID) {
        Light light = lightRepository.findById(lightID)
            .orElseThrow(() -> new ResourceNotFoundException("신호등이 없습니다. ID=" + lightID));
        return toDTO(light);
    }

    @Transactional
    public LightDTO updateLight(String lightID, LightUpdateRequest request) {
        Light light = lightRepository.findById(lightID)
            .orElseThrow(() -> new ResourceNotFoundException("신호등이 없습니다. ID=" + lightID));

        if (request.getStatus() != null) {
            light.setStatus(request.getStatus());
        }
        if (request.getRemainTime() != null) {
            light.setRemainTime(request.getRemainTime());
        }
        return toDTO(light);
    }

    @Transactional
    public void deleteLight(String lightID) {
        if(!lightRepository.existsById(lightID)) {
            throw new ResourceNotFoundException("해당 신호등이 없습니다. ID=" + lightID);
        }
        lightRepository.deleteById(lightID);
    }

    private LightDTO toDTO(Light light) {
        LightDTO dto = new LightDTO();
        dto.setLightID(light.getLightID());
        dto.setPlaceID(light.getPlace().getPlaceID());
        dto.setStatus(light.getStatus());
        dto.setRemainTime(light.getRemainTime() == null ? null : light.getRemainTime().toString());
        return dto;
    }
}
