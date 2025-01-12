package com.example.shoong.controller;

import com.example.shoong.dto.segment.SegmentCreateRequest;
import com.example.shoong.dto.segment.SegmentDTO;
import com.example.shoong.dto.segment.SegmentUpdateRequest;
import com.example.shoong.service.SegmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segments")
public class SegmentController {

    private final SegmentService segmentService;
    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    // [POST] /segments
    @PostMapping
    public ResponseEntity<SegmentDTO> createSegment(@Valid @RequestBody SegmentCreateRequest request) {
        SegmentDTO dto = segmentService.createSegment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // [GET] /segments/{segmentID}
    @GetMapping("/{segmentID}")
    public ResponseEntity<SegmentDTO> getSegment(@PathVariable String segmentID) {
        SegmentDTO dto = segmentService.getSegment(segmentID);
        return ResponseEntity.ok(dto);
    }

    // [PUT] /segments/{segmentID}
    @PutMapping("/{segmentID}")
    public ResponseEntity<SegmentDTO> updateSegment(
            @PathVariable String segmentID,
            @Valid @RequestBody SegmentUpdateRequest request
    ) {
        SegmentDTO updated = segmentService.updateSegment(segmentID, request);
        return ResponseEntity.ok(updated);
    }

    // [DELETE] /segments/{segmentID}
    @DeleteMapping("/{segmentID}")
    public ResponseEntity<Void> deleteSegment(@PathVariable String segmentID) {
        segmentService.deleteSegment(segmentID);
        return ResponseEntity.noContent().build();
    }
}
