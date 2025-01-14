package com.example.shoong.service;

import com.example.shoong.dto.light.LightCreateRequest;
import com.example.shoong.dto.light.LightDTO;
import com.example.shoong.dto.light.LightUpdateRequest;
import com.example.shoong.entity.Light;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.LightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LightService {

    private final LightRepository lightRepository;

    public LightService(LightRepository lightRepository) {
        this.lightRepository = lightRepository;
    }

    @Transactional
    public LightDTO createLight(LightCreateRequest request) {
        
        Light light = new Light();
        light.setLightID(UUID.randomUUID().toString());
        

        Light saved = lightRepository.save(light);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public LightDTO getLight(String lightID) {
        Light light = lightRepository.findById(lightID)
            .orElseThrow(() -> new ResourceNotFoundException("신호등이 없습니다. ID=" + lightID));
        return toDTO(light);
    }

    // @Transactional
    // public LightDTO updateLight(String lightID, LightUpdateRequest request) {
    //     Light light = lightRepository.findById(lightID)
    //         .orElseThrow(() -> new ResourceNotFoundException("신호등이 없습니다. ID=" + lightID));

    //     if (request.getStatus() != null) {
    //         light.setStatus(request.getStatus());
    //     }
    //     if (request.getRemainTime() != null) {
    //         light.setRemainTime(request.getRemainTime());
    //     }
    //     return toDTO(light);
    // }

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
        return dto;
    }
}
