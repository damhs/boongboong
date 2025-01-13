package com.example.shoong.service;

import com.example.shoong.entity.User;
import com.example.shoong.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String loginID) throws UsernameNotFoundException {
    System.out.println("Received loginID: " + loginID);
    User user = userRepository.findByLoginID(loginID)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginID));

    // 스프링 시큐리티가 제공하는 UserDetails
    // 구현체(org.springframework.security.core.userdetails.User)
    // 또는 직접 CustomUserDetails를 만들어 반환할 수도 있음
    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getLoginID()) // 시큐리티가 인식하는 username
        .password(user.getPassword()) // BCrypt 해시된 password
        .roles("USER") // 권한 설정(필요하면 DB에서 가져와 세팅)
        .build();
  }
}
