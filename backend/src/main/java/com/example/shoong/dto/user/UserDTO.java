package com.example.shoong.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
  private String userID;
  private String name;
  private String loginID;
  // password는 민감정보이므로 응답에서 제외하거나, 필요에 따라 노출 방식을 결정
  private String updatedAt; // 2023-10-11T12:34:56 형태로 변환할 수 있음
}
