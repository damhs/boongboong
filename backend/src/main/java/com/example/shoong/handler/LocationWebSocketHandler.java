package com.example.shoong.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.shoong.entity.Light;
import com.example.shoong.entity.Location;
import com.example.shoong.entity.Segment;
import com.example.shoong.entity.UserProgress;
import com.example.shoong.repository.LightRepository;
import com.example.shoong.repository.SegmentRepository;
import com.example.shoong.service.LightService;
import com.example.shoong.dto.websocket.WebSocketRequest;
import com.example.shoong.dto.websocket.WebSocketResponse;

import java.util.concurrent.ConcurrentHashMap;

public class LocationWebSocketHandler extends TextWebSocketHandler {

  @Autowired
  private SegmentRepository segmentRepository;

  @Autowired
  private LightService lightService;

  public LocationWebSocketHandler(SegmentRepository segmentRepository, LightService lightService) {
    this.segmentRepository = segmentRepository;
    this.lightService = lightService;
  }

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ConcurrentHashMap<String, UserProgress> userProgressMap = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // 유저 진행 상태 초기화
    userProgressMap.put(session.getId(), new UserProgress());
    System.out.println("새로운 유저 연결: " + session.getId());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    // Parse incoming message
    WebSocketRequest request = objectMapper.readValue(message.getPayload(), WebSocketRequest.class);

    String pathID = request.getPathID();
    Location userLocation = request.getLocation();

    // Get current segment
    Segment currentSegment = segmentRepository.findByPath_PathIDAndSequenceNumber(pathID, 0);

    // Get light information
    Light light = lightService.getLightById(currentSegment.getLightID());

    // Get remaining green light time
    int remainingTime = lightService.getRemainingGreenLightTime(light.getItstId(), currentSegment.getDirection());

    // Calculate recommended speed
    double distanceToLight = calculateDistance(userLocation.getLatitude(), userLocation.getLongitude(),
        light.getLatitude(), light.getLongitude());
    int recommendedSpeed = calculateRecommendedSpeed(distanceToLight, remainingTime);

    // Send data to client
    WebSocketResponse response = new WebSocketResponse();
    response.setRemainingTime(remainingTime);
    response.setRecommendedSpeed(recommendedSpeed);
    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
  }

  private int calculateRecommendedSpeed(double distance, int time) {
    return (int) ((distance / time) * 3600); // Convert m/s to km/h
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    userProgressMap.remove(session.getId());
    System.out.println("유저 연결 종료: " + session.getId());
  }

  private boolean isCloseToLight(Location userLocation, Light light) {
    double distance = calculateDistance(
        userLocation.getLatitude(), userLocation.getLongitude(),
        light.getLatitude(), light.getLongitude());
    return distance < 10; // 10m 이내
  }

  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371; // 지구 반지름 (km)
    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c * 1000; // 거리 (m)
  }
}
