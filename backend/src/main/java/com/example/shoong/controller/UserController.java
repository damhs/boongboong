package com.example.shoong.controller;

import com.example.shoong.dto.user.UserCreateRequest;
import com.example.shoong.dto.user.UserDTO;
import com.example.shoong.dto.user.UserUpdateRequest;
import com.example.shoong.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * [POST] /users
     * 유저 생성
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        // @Valid -> UserCreateRequest의 @NotBlank, @Size 등 검증
        UserDTO saved = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * [GET] /users/{userID}
     * 특정 유저 조회
     */
    @GetMapping("/{userID}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String userID) {
        UserDTO dto = userService.getUser(userID);
        return ResponseEntity.ok(dto);
    }

    /**
     * [PUT] /users/{userID}
     * 특정 유저 수정
     */
    @PutMapping("/{userID}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String userID,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        // @Valid -> UserUpdateRequest의 @Size 등 검증
        UserDTO updated = userService.updateUser(userID, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * [DELETE] /users/{userID}
     * 특정 유저 삭제
     */
    @DeleteMapping("/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userID) {
        userService.deleteUser(userID);
        return ResponseEntity.noContent().build();
    }
}
