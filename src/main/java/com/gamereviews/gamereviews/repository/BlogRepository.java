package com.gamereviews.gamereviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamereviews.gamereviews.model.Blog;
import java.util.List;
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    // Dodatne metode za pretragu blogova

    List<Blog> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}

