package com.example.shoong.handler;

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

import java.util.concurrent.ConcurrentHashMap;

public class LocationWebSocketHandler extends TextWebSocketHandler {

  private final LightRepository lightRepository;

  public LocationWebSocketHandler(LightRepository lightRepository) {
    this.lightRepository = lightRepository;
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
    String payload = message.getPayload();
    Location userLocation = objectMapper.readValue(payload, Location.class);

    UserProgress progress = userProgressMap.get(session.getId());
    if (progress == null)
      return;

    // 현재 세그먼트 가져오기
    Segment currentSegment = progress.getCurrentSegment();
    if (currentSegment == null) {
      progress.loadSegmentsFromDB(); // 경로 초기화 (DB에서 세그먼트 불러오기)
      currentSegment = progress.getCurrentSegment();
    }

    // Light의 위도/경도 가져오기
    Light light = lightRepository.findByLightID(currentSegment.getLightID());
    if (light == null)
      return;

    // 유저가 현재 세그먼트를 지나갔는지 확인
    if (isCloseToLight(userLocation, light)) {
      if (progress.moveToNextSegment()) {
        Segment nextSegment = progress.getCurrentSegment();
        if (nextSegment != null) {
          session.sendMessage(new TextMessage("Next segment: " + nextSegment.getSequenceNumber()));
        } else {
          session.sendMessage(new TextMessage("You have reached your destination!"));
          userProgressMap.remove(session.getId()); // 진행 완료 시 제거
        }
      }
    }
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
