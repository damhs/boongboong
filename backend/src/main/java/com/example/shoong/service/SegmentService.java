package com.example.shoong.service;

import com.example.shoong.dto.segment.SegmentCreateRequest;
import com.example.shoong.dto.segment.SegmentDTO;
import com.example.shoong.dto.segment.SegmentUpdateRequest;
import com.example.shoong.entity.Light;
import com.example.shoong.entity.Path;
import com.example.shoong.entity.Segment;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.PathRepository;
import com.example.shoong.repository.SegmentRepository;
import com.example.shoong.repository.LightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.UUID;
import java.util.List;
import java.util.Map;

@Service
public class SegmentService {

  private final SegmentRepository segmentRepository;
  private final PathRepository pathRepository;
  private final LightRepository lightRepository;

  public SegmentService(SegmentRepository segmentRepository, PathRepository pathRepository,
      LightRepository lightRepository) {
    this.segmentRepository = segmentRepository;
    this.pathRepository = pathRepository;
    this.lightRepository = lightRepository;
  }

  @Transactional
  public void saveSegments(Path path, List<Object> guides, List<List<Double>> paths) {
    List<Light> lights = lightRepository.findAll(); // 모든 Light 데이터를 가져옴
    for (int i = 0; i < guides.size(); i++) {
      @SuppressWarnings("unchecked")
      Map<String, Object> step = (Map<String, Object>) guides.get(i);

      // 방위 계산을 위한 좌표 가져오기
      String direction = null;
      if (i < paths.size() - 1) {
        List<Double> currentPoint = paths.get(i);
        List<Double> nextPoint = paths.get(i + 1);

        double angle = calculateDirection(
            currentPoint.get(1), currentPoint.get(0), // 현재 지점 (lat1, lon1)
            nextPoint.get(1), nextPoint.get(0) // 다음 지점 (lat2, lon2)
        );
        direction = getDirectionFromAngle(angle);
      }

      List<Double> currentPoint = paths.get(i);
      double currentLat = currentPoint.get(0);
      double currentLon = currentPoint.get(1);

      // 가장 가까운 Light 찾기
      Light closestLight = findClosestLight(lights, currentLat, currentLon);

      // Segment 생성 및 저장
      Segment segment = new Segment();
      segment.setSegmentID(UUID.randomUUID().toString());
      segment.setPath(path);
      segment.setSequenceNumber(i + 1);
      segment.setDescription((String) step.get("instructions"));
      segment.setDistance((Integer) step.get("distance"));
      segment.setDuration(LocalTime.ofSecondOfDay(((Number) step.get("duration")).longValue()));
      segment.setDirection(direction);

      segmentRepository.save(segment);
    }
  }

  private Light findClosestLight(List<Light> lights, double currentLat, double currentLon) {
    Light closestLight = null;
    double minDistance = Double.MAX_VALUE;

    for (Light light : lights) {
      double distance = calculateDistance(currentLat, currentLon, light.getLatitude(), light.getLongitude());
      if (distance < minDistance) {
        minDistance = distance;
        closestLight = light;
      }
    }
    return closestLight;
  }

  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    // Haversine formula
    final int R = 6371; // 지구 반지름 (km)
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c; // 두 지점 간 거리 반환 (km)
  }

  private Double calculateDirection(double lat1, double lon1, double lat2, double lon2) {
    double deltaLon = Math.toRadians(lon2 - lon1);
    double y = Math.sin(deltaLon) * Math.cos(Math.toRadians(lat2));
    double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) -
        Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(deltaLon);
    double theta = Math.atan2(y, x);
    double direction = Math.toDegrees(theta);

    // 0~360도로 변환
    return (direction + 360) % 360;
  }

  private String getDirectionFromAngle(double angle) {
    if (angle >= 337.5 || angle < 22.5) {
      return "nt"; // North
    } else if (angle >= 22.5 && angle < 67.5) {
      return "ne"; // North-East
    } else if (angle >= 67.5 && angle < 112.5) {
      return "et"; // East
    } else if (angle >= 112.5 && angle < 157.5) {
      return "se"; // South-East
    } else if (angle >= 157.5 && angle < 202.5) {
      return "st"; // South
    } else if (angle >= 202.5 && angle < 247.5) {
      return "sw"; // South-West
    } else if (angle >= 247.5 && angle < 292.5) {
      return "wt"; // West
    } else {
      return "nw"; // North-West
    }
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

    if (request.getSequenceNumber() != null)
      seg.setSequenceNumber(request.getSequenceNumber());
    if (request.getDescription() != null)
      seg.setDescription(request.getDescription());
    if (request.getDistance() != null)
      seg.setDistance(request.getDistance());
    if (request.getDuration() != null)
      seg.setDuration(request.getDuration());

    return toDTO(seg);
  }

  @Transactional
  public void deleteSegment(String segmentID) {
    if (!segmentRepository.existsById(segmentID)) {
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
