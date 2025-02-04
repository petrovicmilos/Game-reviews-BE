package com.gamereviews.gamereviews.controller;

import com.gamereviews.gamereviews.model.News;
import com.gamereviews.gamereviews.repository.NewsRepository;
import com.gamereviews.gamereviews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
    // 🆕 Kreiranje vesti sa `MultipartFile`
    @PostMapping("/create")
    public ResponseEntity<News> createNews(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {

            System.out.println("✅ Ušao u try blok - Backend je primio zahtev!");
            System.out.println("Title: " + title);
            System.out.println("Content: " + content);
            if (image != null) {
                System.out.println("Primljena slika: " + image.getOriginalFilename());
            } else {
                System.out.println("❌ Slika nije primljena.");
            }
            String imagePath = image != null ? saveImage(image) : null;

            News news = new News();
            news.setTitle(title);
            news.setContent(content);
            news.setPostingDate(LocalDate.now().toString());
            news.setImage(imagePath);

            // ✅ Dodajemo podrazumevane vrednosti za likes i dislikes
            news.setLikes(0);
            news.setDislikes(0);

            News savedNews = newsRepository.save(news);
            System.out.println("✅ Uspešno sačuvana vest: " + savedNews.getId());
            return ResponseEntity.ok(savedNews);
        } catch (IOException e) {
            System.out.println("❌ Greška prilikom čuvanja slike: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        String filePath = uploadDir + fileName;
        image.transferTo(new File(filePath));
        return "/uploads/" + fileName;
    }

}
