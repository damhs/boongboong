package com.example.shoong.dto.websocket;

import com.example.shoong.entity.Location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketRequest {
  private String pathID;
  private Location location;
}
