package com.example.shoong.controller;

import com.example.shoong.dto.pathlight.PathLightCreateRequest;
import com.example.shoong.dto.pathlight.PathLightDTO;
import com.example.shoong.dto.pathlight.PathLightUpdateRequest;
import com.example.shoong.service.PathLightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pathlights")
public class PathLightController {

    private final PathLightService pathLightService;

    public PathLightController(PathLightService pathLightService) {
        this.pathLightService = pathLightService;
    }

    // [POST] /pathlights
    @PostMapping
    public ResponseEntity<PathLightDTO> createPathLight(
            @Valid @RequestBody PathLightCreateRequest request
    ) {
        PathLightDTO dto = pathLightService.createPathLight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // [GET] /pathlights/{pathlightID}
    @GetMapping("/{pathlightID}")
    public ResponseEntity<PathLightDTO> getPathLight(@PathVariable String pathlightID) {
        PathLightDTO dto = pathLightService.getPathLight(pathlightID);
        return ResponseEntity.ok(dto);
    }

    // [PUT] /pathlights/{pathlightID}
    @PutMapping("/{pathlightID}")
    public ResponseEntity<PathLightDTO> updatePathLight(
            @PathVariable String pathlightID,
            @Valid @RequestBody PathLightUpdateRequest request
    ) {
        PathLightDTO updated = pathLightService.updatePathLight(pathlightID, request);
        return ResponseEntity.ok(updated);
    }

    // [DELETE] /pathlights/{pathlightID}
    @DeleteMapping("/{pathlightID}")
    public ResponseEntity<Void> deletePathLight(@PathVariable String pathlightID) {
        pathLightService.deletePathLight(pathlightID);
        return ResponseEntity.noContent().build();
    }
}
