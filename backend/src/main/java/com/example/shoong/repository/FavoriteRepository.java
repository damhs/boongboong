package com.example.shoong.repository;

import com.example.shoong.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, String> {
}
