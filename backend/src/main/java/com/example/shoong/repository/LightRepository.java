package com.example.shoong.repository;

import com.example.shoong.entity.Light;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightRepository extends JpaRepository<Light, String> {
  Light findByLightID(String lightID);
}
