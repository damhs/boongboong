package com.example.shoong.entity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.shoong.repository.SegmentRepository;

@Component
public class UserProgress {

    @Autowired
    private SegmentRepository segmentRepository;

    private List<Segment> segments;
    private int currentSegmentIndex = 0;
    private String currentPathId;

    public void loadSegmentsFromDB() {
        // DB에서 경로와 관련된 세그먼트 데이터를 불러옵니다.
        this.segments = segmentRepository.findByPathId(currentPathId); // DB 호출
    }

    public Segment getCurrentSegment() {
        if (segments == null || currentSegmentIndex >= segments.size()) return null;
        return segments.get(currentSegmentIndex);
    }

    public boolean moveToNextSegment() {
        if (segments == null || currentSegmentIndex >= segments.size() - 1) return false;
        currentSegmentIndex++;
        return true;
    }
}
