package com.example.shoong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.shoong.handler.LocationWebSocketHandler; // Add this import statement
import com.example.shoong.repository.LightRepository; // Add this import statement

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final LightRepository lightRepository;

  public WebSocketConfig(LightRepository lightRepository) {
    this.lightRepository = lightRepository;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new LocationWebSocketHandler(lightRepository), "/ws/location")
        .setAllowedOrigins("*"); // CORS 설정
  }
}
