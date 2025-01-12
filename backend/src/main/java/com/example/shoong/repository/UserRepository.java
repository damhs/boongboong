package com.example.shoong.repository;

import com.example.shoong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserRepository extends JpaRepository<User, String> {
  List<User> findUserById(String id);
}
