package com.gamereviews.gamereviews.repository;

import com.gamereviews.gamereviews.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByTitleContaining(String title);
}

