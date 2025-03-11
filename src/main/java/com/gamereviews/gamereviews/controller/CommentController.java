package com.gamereviews.gamereviews.controller;

import com.gamereviews.gamereviews.model.Comment;
import com.gamereviews.gamereviews.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    // Dobavi sve komentare za određeni blog
    @GetMapping("/by-blog/{blogId}")
    public List<Comment> getCommentsByBlogId(@PathVariable Long blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    @GetMapping("/by-news/{newsId}")
    public List<Comment> getCommentsByNewsId(@PathVariable Long newsId) {
        return commentRepository.findByNewsId(newsId);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }


}