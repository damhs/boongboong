package com.example.shoong.repository;

import com.example.shoong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByLoginID(String loginID);
}
