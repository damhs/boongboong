package com.example.shoong.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @Size(min = 1, max = 20, message = "이름은 1~20자리여야 합니다.")
    private String name;

    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이어야 합니다.")
    private String password;
}