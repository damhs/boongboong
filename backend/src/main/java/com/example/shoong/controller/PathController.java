package com.example.shoong.controller;

import com.example.shoong.dto.path.PathCreateRequest;
import com.example.shoong.dto.path.PathDTO;
import com.example.shoong.dto.path.PathUpdateRequest;
import com.example.shoong.dto.place.PlaceDTO;
import com.example.shoong.service.PathService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/paths")
public class PathController {

    private final PathService pathService;
    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    // [GET] /paths
    public ResponseEntity<List<PathDTO>> getPaths() {
        List<PathDTO> paths = pathService.getPaths();
        return ResponseEntity.ok(paths);
    }

    // [POST] /paths
    @PostMapping
    public ResponseEntity<PathDTO> createPath(@Valid @RequestBody PathCreateRequest request) {
        PathDTO dto = pathService.createOrUpdatePath(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // [GET] /paths/{pathID}
    @GetMapping("/{pathID}")
    public ResponseEntity<PathDTO> getPath(@PathVariable String pathID) {
        PathDTO dto = pathService.getPath(pathID);
        return ResponseEntity.ok(dto);
    }

    // [GET] /paths/{userID}/recents
    @GetMapping("/{userID}/recents")
    public ResponseEntity<List<PathDTO>> getRecentPaths(@PathVariable String userID) {
        List<PathDTO> dtoList = pathService.getRecentPaths(userID);
        return ResponseEntity.ok(dtoList);
    }

    // [PUT] /paths/{pathID}
    @PutMapping("/{pathID}")
    public ResponseEntity<PathDTO> updatePath(
            @PathVariable String pathID,
            @Valid @RequestBody PathUpdateRequest request
    ) {
        PathDTO updated = pathService.updatePath(pathID, request);
        return ResponseEntity.ok(updated);
    }

    // [DELETE] /paths/{pathID}
    @DeleteMapping("/{pathID}")
    public ResponseEntity<Void> deletePath(@PathVariable String pathID) {
        pathService.deletePath(pathID);
        return ResponseEntity.noContent().build();
    }
}
