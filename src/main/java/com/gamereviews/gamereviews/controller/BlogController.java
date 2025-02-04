package com.gamereviews.gamereviews.controller;

import com.gamereviews.gamereviews.model.Blog;
import com.gamereviews.gamereviews.repository.BlogRepository;
import com.gamereviews.gamereviews.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Blog> createBlog(
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

            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setContent(content);
            blog.setPostingDate(LocalDate.now().toString()); // ✅ Ovo dodaje datum
            blog.setImage(imagePath);

            Blog savedBlog = blogRepository.save(blog);
            System.out.println("✅ Uspešno sačuvan blog: " + savedBlog.getId());
            return ResponseEntity.ok(savedBlog);
        } catch (IOException e) {
            System.out.println("❌ Greška prilikom čuvanja slike: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @ModelAttribute Blog blogDetails, @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        return blogRepository.findById(id).map(blog -> {
            blog.setTitle(blogDetails.getTitle());
            blog.setContent(blogDetails.getContent());
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = "assets/" + imageFile.getOriginalFilename();
                blog.setImage(imagePath);
            }
            return ResponseEntity.ok(blogRepository.save(blog));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        return blogRepository.findById(id).map(blog -> {
            blogRepository.delete(blog);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Blog> likeBlog(@PathVariable Long id) {
        return blogRepository.findById(id).map(blog -> {
            blog.setLikes(blog.getLikes() + 1);
            return ResponseEntity.ok(blogRepository.save(blog));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Blog> dislikeBlog(@PathVariable Long id) {
        return blogRepository.findById(id).map(blog -> {
            blog.setDislikes(blog.getDislikes() + 1);
            return ResponseEntity.ok(blogRepository.save(blog));
        }).orElse(ResponseEntity.notFound().build());
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
