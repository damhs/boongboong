package com.example.shoong.repository;

import com.example.shoong.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment, String> {

    // Path ID로 Segment를 검색
    List<Segment> findByPath_PathID(String pathId);

    // Path ID로 Segment를 검색하고 sequence_number로 정렬
    List<Segment> findByPath_PathIDOrderBySequenceNumber(String pathId);

    // Light ID로 Segment를 검색
    List<Segment> findByLight_LightID(String lightId);

    // Path ID와 Sequence Number로 Segment 검색
    Segment findByPath_PathIDAndSequenceNumber(String pathId, int sequence_number);
}
