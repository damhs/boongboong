package com.example.shoong.repository;

import com.example.shoong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  User findUserByUserID(String userID);
  
}
