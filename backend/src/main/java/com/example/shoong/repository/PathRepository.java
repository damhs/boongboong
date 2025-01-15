package com.example.shoong.repository;

import com.example.shoong.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PathRepository extends JpaRepository<Path, String> {
  List<Path> findTop10ByUser_UserIDOrderByUpdatedAtDesc(String userID);
}
