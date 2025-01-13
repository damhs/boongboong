package com.example.shoong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
  // Constructor, Getters, and Setters
}