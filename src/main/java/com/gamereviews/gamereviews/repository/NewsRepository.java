package com.gamereviews.gamereviews.repository;
import com.gamereviews.gamereviews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query(value = "SELECT * FROM news ORDER BY posting_date DESC LIMIT 3", nativeQuery = true)
    List<News> findLatestNews();

    List<News> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

}
