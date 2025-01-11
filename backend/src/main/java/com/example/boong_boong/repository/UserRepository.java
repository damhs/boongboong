package com.example.boong_boong.repository;

import com.example.boong_boong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
