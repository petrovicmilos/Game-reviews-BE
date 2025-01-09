package com.gamereviews.gamereviews.repository;
import com.gamereviews.gamereviews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // Dodatne metode za pretragu vesti
}
