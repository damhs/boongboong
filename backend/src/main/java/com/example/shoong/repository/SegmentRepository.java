package com.example.shoong.repository;

import com.example.shoong.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment, String> {
  List<Segment> findByPathId(String pathId);
  List<Segment> findByPathIdOrderBySequenceNumber(String pathId);
  List<Segment> findByLightID(String lightID);
}

