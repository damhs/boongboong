package com.example.shoong.dto.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketResponse {
  private int remainingTime;
  private int recommendedSpeed;
}
