package com.example.shoong.controller;

import com.example.shoong.dto.path.PathDTO;
import com.example.shoong.dto.place.PlaceDTO;
import com.example.shoong.dto.place.PlaceUpdateRequest;
import com.example.shoong.dto.recent.RecentCreateRequest;
import com.example.shoong.dto.recent.RecentDTO;
import com.example.shoong.service.RecentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recents")
public class RecentController {

    private final RecentService recentService;
    public RecentController(RecentService recentService) {
        this.recentService = recentService;
    }

    // [GET] /recents
    public ResponseEntity<List<RecentDTO>> getRecents() {
        List<RecentDTO> recents = recentService.getRecents();
        return ResponseEntity.ok(recents);
    }

    // [POST] /recents
    @PostMapping
    public ResponseEntity<RecentDTO> createRecent(@Valid @RequestBody RecentCreateRequest request) {
        RecentDTO dto = recentService.createRecent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // [GET] /recents/{userID}
    @GetMapping("/{userID}")
    public ResponseEntity<List<RecentDTO>> getRecentsByUser(@PathVariable String userID) {
        List<RecentDTO> list = recentService.getRecentsByUser(userID);
        return ResponseEntity.ok(list);
    }

    // [PUT] /recents/{recentID}
    @PutMapping("/{recentID}")
    public ResponseEntity<RecentDTO> updateRecent(
            @PathVariable String recentID,
            @Valid @RequestBody RecentCreateRequest request
    ) {
        RecentDTO updated = recentService.updateRecent(recentID, request);
        return ResponseEntity.ok(updated);
    }

    // [DELETE] /recents/{recentID}
    @DeleteMapping("/{recentID}")
    public ResponseEntity<Void> deleteRecent(@PathVariable String recentID) {
        recentService.deleteRecent(recentID);
        return ResponseEntity.noContent().build();
    }
}
