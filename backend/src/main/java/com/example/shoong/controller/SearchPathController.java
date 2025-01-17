package com.example.shoong.controller;

import java.nio.charset.Charset;
import java.util.UUID;
import java.net.URI;

import com.example.shoong.entity.Path;
import com.example.shoong.service.SegmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search-path")
public class SearchPathController {
  @Value("${naver.map-client-id}")
  private String clientId;

  @Value("${naver.map-client-secret}")
  private String clientSecret;

  private final SegmentService segmentService;

  public SearchPathController(SegmentService segmentService) {
    this.segmentService = segmentService;
  }

  @GetMapping
  public ResponseEntity<String> search(@RequestParam String start, @RequestParam String goal) throws IOException {
    URI uri = UriComponentsBuilder
        .fromUriString("https://naveropenapi.apigw.ntruss.com")
        .path("/map-direction/v1/driving")
        .queryParam("goal", goal)
        .queryParam("start", start)
        .encode(Charset.forName("UTF-8"))
        .build()
        .toUri();

    RequestEntity<Void> req = RequestEntity
        .get(uri)
        .header("x-ncp-apigw-api-key-id", clientId)
        .header("x-ncp-apigw-api-key", clientSecret)
        .header("Content-Type", "application/json; charset=UTF-8")
        .build();

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);

    String responseBody = responseEntity.getBody();

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> jsonMap = mapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
    if (jsonMap == null || !jsonMap.containsKey("route")) {
      return ResponseEntity.badRequest().body("경로 데이터를 가져오지 못했습니다.");
    }

    // 경로 데이터 추출
    Map<String, Object> routeData = (Map<String, Object>) jsonMap.get("route");
    System.out.println("routeData");
    List<Object> traoptimal = (List<Object>) routeData.get("traoptimal");
    System.out.println("traoptimal");
    Map<String, Object> route = (Map<String, Object>) traoptimal.get(0);
    System.out.println("route");
    List<Map<String, Object>> guides = (List<Map<String, Object>>) route.get("guide");
    System.out.println("guides");
    List<List<Double>> paths = (List<List<Double>>) route.get("path");
    System.out.println("paths");

    // Path 엔티티 생성
    Path path = new Path();
    path.setPathID(UUID.randomUUID().toString());
    System.out.println("pathID");
    // Segment 데이터 저장
    segmentService.saveSegments(path, guides, paths);
    System.out.println("saveSegments");
    return ResponseEntity.ok("Segment 저장 완료");
  }
}
