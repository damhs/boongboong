package com.example.shoong.controller;

import com.example.shoong.dto.place.PlaceCreateRequest;
import com.example.shoong.dto.place.PlaceDTO;
import com.example.shoong.dto.place.PlaceUpdateRequest;
import com.example.shoong.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    // [POST] /places
    @PostMapping
    public ResponseEntity<PlaceDTO> createPlace(@Valid @RequestBody PlaceCreateRequest request) {
        PlaceDTO dto = placeService.createPlace(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // [GET] /places/{placeID}
    @GetMapping("/{placeID}")
    public ResponseEntity<PlaceDTO> getPlace(@PathVariable String placeID) {
        PlaceDTO dto = placeService.getPlace(placeID);
        return ResponseEntity.ok(dto);
    }

    // [PUT] /places/{placeID}
    @PutMapping("/{placeID}")
    public ResponseEntity<PlaceDTO> updatePlace(
            @PathVariable String placeID,
            @Valid @RequestBody PlaceUpdateRequest request
    ) {
        PlaceDTO updated = placeService.updatePlace(placeID, request);
        return ResponseEntity.ok(updated);
    }

    // [DELETE] /places/{placeID}
    @DeleteMapping("/{placeID}")
    public ResponseEntity<Void> deletePlace(@PathVariable String placeID) {
        placeService.deletePlace(placeID);
        return ResponseEntity.noContent().build();
    }
}
