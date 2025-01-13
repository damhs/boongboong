package com.example.shoong.controller;

import com.example.shoong.dto.user.UserCreateRequest;
import com.example.shoong.dto.user.UserDTO;
import com.example.shoong.dto.user.UserUpdateRequest;
import com.example.shoong.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@RestController
@RequestMapping("/api/users")
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
   * [GET] /users
   * 모든 유저 조회
   */
  @GetMapping
  public ResponseEntity<Iterable<UserDTO>> getUsers() {
    Iterable<UserDTO> users = userService.getUsers();
    return ResponseEntity.ok(users);
  }

  /**
   * [POST] /users/login
   * 로그인 요청
   */
  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(@Valid @RequestBody UserCreateRequest request) {
    // id와 password를 이용해 로그인 처리
    UserDTO user = userService.login(request.getId(), request.getPassword());
    return ResponseEntity.ok(user); // 성공 응답
  }

  @GetMapping("/login")
  public String loginForm(
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "logout", required = false) String logout,
      Model model) {

    if (error != null) {
      model.addAttribute("errorMsg", "아이디 또는 비밀번호가 잘못되었습니다.");
    }
    if (logout != null) {
      model.addAttribute("logoutMsg", "로그아웃 되었습니다.");
    }
    return "login"; // login.html (Thymeleaf) or login.jsp
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
      @Valid @RequestBody UserUpdateRequest request) {
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
