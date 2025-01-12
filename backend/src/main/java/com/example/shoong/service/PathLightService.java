package com.example.shoong.service;

import com.example.shoong.dto.pathlight.PathLightCreateRequest;
import com.example.shoong.dto.pathlight.PathLightDTO;
import com.example.shoong.dto.pathlight.PathLightUpdateRequest;
import com.example.shoong.entity.Light;
import com.example.shoong.entity.Path;
import com.example.shoong.entity.PathLight;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.LightRepository;
import com.example.shoong.repository.PathLightRepository;
import com.example.shoong.repository.PathRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PathLightService {

    private final PathLightRepository pathLightRepository;
    private final PathRepository pathRepository;
    private final LightRepository lightRepository;

    public PathLightService(
        PathLightRepository pathLightRepository,
        PathRepository pathRepository,
        LightRepository lightRepository
    ) {
        this.pathLightRepository = pathLightRepository;
        this.pathRepository = pathRepository;
        this.lightRepository = lightRepository;
    }

    @Transactional
    public PathLightDTO createPathLight(PathLightCreateRequest request) {
        Path path = pathRepository.findById(request.getPathID())
            .orElseThrow(() -> new ResourceNotFoundException("경로가 없습니다. pathID=" + request.getPathID()));
        Light light = lightRepository.findById(request.getLightID())
            .orElseThrow(() -> new ResourceNotFoundException("신호등이 없습니다. lightID=" + request.getLightID()));

        PathLight pl = new PathLight();
        pl.setPathlightID(UUID.randomUUID().toString());
        pl.setPath(path);
        pl.setLight(light);
        pl.setIsAvoided(request.getIsAvoided());

        PathLight saved = pathLightRepository.save(pl);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public PathLightDTO getPathLight(String pathlightID) {
        PathLight pl = pathLightRepository.findById(pathlightID)
            .orElseThrow(() -> new ResourceNotFoundException("경로-신호등 매핑이 없습니다. ID=" + pathlightID));
        return toDto(pl);
    }

    @Transactional
    public PathLightDTO updatePathLight(String pathlightID, PathLightUpdateRequest request) {
        PathLight pl = pathLightRepository.findById(pathlightID)
            .orElseThrow(() -> new ResourceNotFoundException("경로-신호등 매핑이 없습니다. ID=" + pathlightID));

        if (request.getIsAvoided() != null) {
            pl.setIsAvoided(request.getIsAvoided());
        }
        return toDto(pl);
    }

    @Transactional
    public void deletePathLight(String pathlightID) {
        if (!pathLightRepository.existsById(pathlightID)) {
            throw new ResourceNotFoundException("해당 매핑이 없습니다. ID=" + pathlightID);
        }
        pathLightRepository.deleteById(pathlightID);
    }

    private PathLightDTO toDto(PathLight pl) {
        PathLightDTO dto = new PathLightDTO();
        dto.setPathlightID(pl.getPathlightID());
        dto.setPathID(pl.getPath().getPathID());
        dto.setLightID(pl.getLight().getLightID());
        dto.setIsAvoided(pl.getIsAvoided());
        return dto;
    }
}
