package com.gamereviews.gamereviews.repository;

import com.gamereviews.gamereviews.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT COUNT(r) FROM Review r WHERE r.game.id = :gameId AND r.user.isCritic = :isCritic")
    int countByGameIdAndUserIsCritic(Long gameId, boolean isCritic);

    Optional<Review> findByGameIdAndUserId(Long gameId, Long userId);

    @Query("SELECT r, u.username FROM Review r JOIN r.user u WHERE r.game.id = :gameId ORDER BY r.id DESC")
    List<Object[]> findTop4ByGameIdOrderByIdDesc(@Param("gameId") Long gameId);

}
