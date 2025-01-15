package com.example.shoong.service;

import com.example.shoong.dto.light.LightCreateRequest;
import com.example.shoong.dto.light.LightDTO;
import com.example.shoong.dto.light.LightUpdateRequest;
import com.example.shoong.entity.Light;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.LightRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class LightService {

  @Value("${seoul.traffic-api-key}")
  private String apikey;
  private final LightRepository lightRepository;

  private final RestTemplate restTemplate = new RestTemplate();

  public LightService(LightRepository lightRepository) {
    this.lightRepository = lightRepository;
  }

  public Light getLightById(String lightId) {
    return lightRepository.findById(lightId).orElseThrow(() -> new RuntimeException("Light 정보를 찾을 수 없습니다."));
  }
  
  /**
   * 신호등 남은 초록불 시간을 가져옵니다.
   */
  public int getRemainingGreenLightTime(String itstId, String direction) {
    String url = "http://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xSignalPhaseTimingInformation/1.0"
        + "?apikey=" + apikey
        + "&itstId=" + itstId;

    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      throw new RuntimeException("신호등 API 호출 실패: " + responseEntity.getStatusCode());
    }

    try {
      // JSON 데이터 파싱
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode root = objectMapper.readTree(responseEntity.getBody());

      // 응답 데이터에서 첫 번째 신호등 데이터 가져오기
      JsonNode dataNode = root.path(0); // 첫 번째 데이터 노드
      if (dataNode.isMissingNode()) {
        throw new RuntimeException("신호등 데이터가 없습니다.");
      }

      // 방향별 남은 시간 가져오기
      return parseRemainingTime(dataNode, direction);

    } catch (Exception e) {
      throw new RuntimeException("JSON 응답 처리 중 오류 발생: " + e.getMessage(), e);
    }
  }

  /**
   * 방향에 따른 남은 시간을 파싱합니다.
   */
  private int parseRemainingTime(JsonNode dataNode, String direction) {
    // 방향별 키 값 매핑
    String directionKey = switch (direction.toLowerCase()) {
      case "nt" -> "ntStsgRmdrCs";
      case "et" -> "etStsgRmdrCs";
      case "st" -> "stStsgRmdrCs";
      case "wt" -> "wtStsgRmdrCs";
      case "nw" -> "nwStsgRmdrCs";
      case "ne" -> "neStsgRmdrCs";
      case "sw" -> "swStsgRmdrCs";
      case "se" -> "seStsgRmdrCs";
      default -> throw new IllegalArgumentException("Invalid direction: " + direction);
    };

    // JSON에서 방향별 남은 시간 추출
    JsonNode timeNode = dataNode.path(directionKey);

    if (timeNode.isMissingNode() || timeNode.isNull()) {
      // 기본값 처리 (예: 0)
      return 0;
    }

    return timeNode.asInt();
  }

  @Transactional
  public LightDTO createLight(LightCreateRequest request) {

    Light light = new Light();
    light.setLightID(UUID.randomUUID().toString());

    Light saved = lightRepository.save(light);
    return toDTO(saved);
  }

  @Transactional(readOnly = true)
  public LightDTO getLight(String lightID) {
    Light light = lightRepository.findById(lightID)
        .orElseThrow(() -> new ResourceNotFoundException("신호등이 없습니다. ID=" + lightID));
    return toDTO(light);
  }

  // @Transactional
  // public LightDTO updateLight(String lightID, LightUpdateRequest request) {
  // Light light = lightRepository.findById(lightID)
  // .orElseThrow(() -> new ResourceNotFoundException("신호등이 없습니다. ID=" +
  // lightID));

  // if (request.getStatus() != null) {
  // light.setStatus(request.getStatus());
  // }
  // if (request.getRemainTime() != null) {
  // light.setRemainTime(request.getRemainTime());
  // }
  // return toDTO(light);
  // }

  @Transactional
  public void deleteLight(String lightID) {
    if (!lightRepository.existsById(lightID)) {
      throw new ResourceNotFoundException("해당 신호등이 없습니다. ID=" + lightID);
    }
    lightRepository.deleteById(lightID);
  }

  private LightDTO toDTO(Light light) {
    LightDTO dto = new LightDTO();
    dto.setLightID(light.getLightID());
    return dto;
  }
}
