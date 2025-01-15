package com.example.shoong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.shoong.handler.LocationWebSocketHandler; // Add this import statement
import com.example.shoong.service.LightService; // Add this import statement
import com.example.shoong.repository.SegmentRepository; // Add this import statement

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final LightService lightService;

  private final SegmentRepository segmentRepository;

  public WebSocketConfig(LightService lightService, SegmentRepository segmentRepository) {
    this.lightService = lightService;
    this.segmentRepository = segmentRepository;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new LocationWebSocketHandler(segmentRepository, lightService), "/ws/location")
        .setAllowedOrigins("*"); // CORS 설정
  }
}
