package com.example.shoong.controller;

import com.example.shoong.dto.light.LightCreateRequest;
import com.example.shoong.dto.light.LightDTO;
import com.example.shoong.service.LightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lights")
public class LightController {

    private final LightService lightService;
    public LightController(LightService lightService) {
        this.lightService = lightService;
    }

    // [POST] /lights
    @PostMapping
    public ResponseEntity<LightDTO> createLight(@Valid @RequestBody LightCreateRequest request) {
        LightDTO dto = lightService.createLight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // [GET] /lights/{lightID}
    @GetMapping("/{lightID}")
    public ResponseEntity<LightDTO> getLight(@PathVariable String lightID) {
        LightDTO dto = lightService.getLight(lightID);
        return ResponseEntity.ok(dto);
    }

    // // [PUT] /lights/{lightID}
    // @PutMapping("/{lightID}")
    // public ResponseEntity<LightDTO> updateLight(
    //         @PathVariable String lightID,
    //         @Valid @RequestBody LightUpdateRequest request
    // ) {
    //     LightDTO updated = lightService.updateLight(lightID, request);
    //     return ResponseEntity.ok(updated);
    // }

    // [DELETE] /lights/{lightID}
    @DeleteMapping("/{lightID}")
    public ResponseEntity<Void> deleteLight(@PathVariable String lightID) {
        lightService.deleteLight(lightID);
        return ResponseEntity.noContent().build();
    }
}
