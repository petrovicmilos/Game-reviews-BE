package com.gamereviews.gamereviews.controller;

import com.gamereviews.gamereviews.model.News;
import com.gamereviews.gamereviews.repository.NewsRepository;
import com.gamereviews.gamereviews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }

    @PostMapping
    public News createNews(@RequestBody News news) {
        return newsService.saveNews(news);
    }

    @PutMapping("/{id}")
    public News updateNews(@PathVariable Long id, @RequestBody News news) {
        news.setId(id);
        return newsService.saveNews(news);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<News>> getLatestNews() {
        try {
            List<News> latestNews = newsRepository.findLatestNews();
            return ResponseEntity.ok(latestNews);
        } catch (Exception e) {
            e.printStackTrace(); // ✅ Ispiši grešku u konzolu
            return ResponseEntity.status(500).build();
        }
    }
}
