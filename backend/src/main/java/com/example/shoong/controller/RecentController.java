package com.example.shoong.controller;

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

    // [DELETE] /recents/{recentID}
    @DeleteMapping("/{recentID}")
    public ResponseEntity<Void> deleteRecent(@PathVariable String recentID) {
        recentService.deleteRecent(recentID);
        return ResponseEntity.noContent().build();
    }
}
