package com.gamereviews.gamereviews.repository;

import com.gamereviews.gamereviews.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
