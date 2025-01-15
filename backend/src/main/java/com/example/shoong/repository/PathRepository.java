package com.example.shoong.repository;

import com.example.shoong.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PathRepository extends JpaRepository<Path, String> {
  List<Path> findTop10ByUser_UserIDOrderByUpdatedAtDesc(String userID);
  Optional<Path> findByUser_UserIDAndOrigin_PlaceIDAndDestination_PlaceID(String userID, String originID, String destinationID);
}
