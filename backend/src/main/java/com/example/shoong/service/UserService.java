package com.example.shoong.service;

import com.example.shoong.dto.user.UserCreateRequest;
import com.example.shoong.dto.user.UserDTO;
import com.example.shoong.dto.user.UserUpdateRequest;
import com.example.shoong.entity.User;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.UserRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * 유저 생성 로직
   */
  @Transactional
  public UserDTO createUser(UserCreateRequest request) {
    // 엔티티 생성
    User user = new User();
    user.setUserID(UuidCreator.getTimeOrdered().toString()); // PK 생성
    // user.setName(request.getName());
    user.setId(request.getId());
    user.setPassword(request.getPassword());
    user.setUpdatedAt(LocalDateTime.now());

    // 저장
    User saved = userRepository.save(user);

    // 엔티티 -> DTO 변환
    return toUserDTO(saved);
  }

  /**
   * 로그인 로직
   */
  @Transactional(readOnly = true)
  public UserDTO login(String id, String password) {
    // id로 유저 조회
    User user = userRepository.findUserById(id)
        .stream()
        .filter(u -> u.getPassword().equals(password)) // 패스워드 일치 확인
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없거나 비밀번호가 일치하지 않습니다."));

    // 성공 시 DTO로 변환하여 반환
    return toUserDTO(user);
  }

  /**
   * 유저 단건 조회 (Response DTO로 변환)
   */
  @Transactional(readOnly = true)
  public UserDTO getUser(String userID) {
    User user = userRepository.findById(userID)
        .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다. ID=" + userID));
    return toUserDTO(user);
  }

  /**
   * 유저 수정
   */
  @Transactional
  public UserDTO updateUser(String userID, UserUpdateRequest request) {
    User user = userRepository.findById(userID)
        .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다. ID=" + userID));

    if (request.getName() != null) {
      user.setName(request.getName());
    }
    if (request.getPassword() != null) {
      user.setPassword(request.getPassword());
    }
    user.setUpdatedAt(LocalDateTime.now());

    return toUserDTO(user); // JPA 변경 감지로 자동 업데이트됨
  }

  /**
   * 유저 삭제
   */
  @Transactional
  public void deleteUser(String userID) {
    if (!userRepository.existsById(userID)) {
      throw new ResourceNotFoundException("유저를 찾을 수 없습니다. ID=" + userID);
    }
    userRepository.deleteById(userID);
  }

  /**
   * 엔티티 -> DTO 변환 (응답용)
   */
  private UserDTO toUserDTO(User user) {
    UserDTO dto = new UserDTO();
    dto.setUserID(user.getUserID());
    dto.setName(user.getName());
    dto.setId(user.getId());
    // LocalDateTime -> 문자열 변환(간단 예시)
    dto.setUpdatedAt((user.getUpdatedAt() != null) ? user.getUpdatedAt().toString() : null);
    return dto;
  }
}
