package com.example.shoong.service;

import com.example.shoong.dto.segment.SegmentCreateRequest;
import com.example.shoong.dto.segment.SegmentDTO;
import com.example.shoong.dto.segment.SegmentUpdateRequest;
import com.example.shoong.entity.Path;
import com.example.shoong.entity.Segment;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.PathRepository;
import com.example.shoong.repository.SegmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SegmentService {

    private final SegmentRepository segmentRepository;
    private final PathRepository pathRepository;

    public SegmentService(SegmentRepository segmentRepository, PathRepository pathRepository) {
        this.segmentRepository = segmentRepository;
        this.pathRepository = pathRepository;
    }

    @Transactional
    public SegmentDTO createSegment(SegmentCreateRequest request) {
        Path path = pathRepository.findById(request.getPathID())
            .orElseThrow(() -> new ResourceNotFoundException("경로가 없습니다. ID=" + request.getPathID()));

        Segment seg = new Segment();
        seg.setSegmentID(UUID.randomUUID().toString());
        seg.setPath(path);
        seg.setSequenceNumber(request.getSequenceNumber());
        seg.setDescription(request.getDescription());
        seg.setDistance(request.getDistance());
        seg.setDuration(request.getDuration());

        Segment saved = segmentRepository.save(seg);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public SegmentDTO getSegment(String segmentID) {
        Segment seg = segmentRepository.findById(segmentID)
            .orElseThrow(() -> new ResourceNotFoundException("세그먼트를 찾을 수 없습니다. ID=" + segmentID));
        return toDTO(seg);
    }

    @Transactional
    public SegmentDTO updateSegment(String segmentID, SegmentUpdateRequest request) {
        Segment seg = segmentRepository.findById(segmentID)
            .orElseThrow(() -> new ResourceNotFoundException("세그먼트를 찾을 수 없습니다. ID=" + segmentID));

        if (request.getSequenceNumber() != null) seg.setSequenceNumber(request.getSequenceNumber());
        if (request.getDescription() != null) seg.setDescription(request.getDescription());
        if (request.getDistance() != null) seg.setDistance(request.getDistance());
        if (request.getDuration() != null) seg.setDuration(request.getDuration());

        return toDTO(seg);
    }

    @Transactional
    public void deleteSegment(String segmentID) {
        if(!segmentRepository.existsById(segmentID)) {
            throw new ResourceNotFoundException("해당 세그먼트가 없습니다. ID=" + segmentID);
        }
        segmentRepository.deleteById(segmentID);
    }

    private SegmentDTO toDTO(Segment seg) {
        SegmentDTO dto = new SegmentDTO();
        dto.setSegmentID(seg.getSegmentID());
        dto.setPathID(seg.getPath().getPathID());
        dto.setSequenceNumber(seg.getSequenceNumber());
        dto.setDescription(seg.getDescription());
        dto.setDistance(seg.getDistance());
        dto.setDuration(seg.getDuration() == null ? null : seg.getDuration().toString());
        return dto;
    }
}
