package com.gamereviews.gamereviews.controller;

import com.gamereviews.gamereviews.dto.ReviewRequest;
import com.gamereviews.gamereviews.dto.ReviewWithUsername;
import com.gamereviews.gamereviews.model.Review;
import com.gamereviews.gamereviews.model.Game;
import com.gamereviews.gamereviews.model.User;
import com.gamereviews.gamereviews.repository.GameRepository;
import com.gamereviews.gamereviews.repository.UserRepository;
import com.gamereviews.gamereviews.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import com.gamereviews.gamereviews.repository.ReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody com.gamereviews.gamereviews.dto.ReviewRequest reviewRequest) {
        // Fetch the Game entity
        Optional<Game> gameOpt = gameRepository.findById(reviewRequest.getGameId());
        if (gameOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Game not found
        }
        Game game = gameOpt.get();

        // Fetch the User entity
        Optional<User> userOpt = userRepository.findById(reviewRequest.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // User not found
        }
        User user = userOpt.get();

        // Create a new Review entity
        Review review = new Review();
        review.setGame(game);
        review.setUser(user);
        review.setContent(reviewRequest.getContent());
        review.setScore(reviewRequest.getScore());

        // Save the Review entity
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody ReviewRequest reviewRequest) {
        // Fetch the existing Review entity
        Optional<Review> existingReviewOpt = Optional.ofNullable(reviewService.getReviewById(id));
        if (existingReviewOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // Review not found
        }
        Review existingReview = existingReviewOpt.get();

        // Fetch the Game entity
        Optional<Game> gameOpt = gameRepository.findById(reviewRequest.getGameId());
        if (gameOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Game not found
        }
        Game game = gameOpt.get();

        // Fetch the User entity
        Optional<User> userOpt = userRepository.findById(reviewRequest.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // User not found
        }
        User user = userOpt.get();

        // Update the Review entity
        existingReview.setGame(game);
        existingReview.setUser(user);
        existingReview.setContent(reviewRequest.getContent());
        existingReview.setScore(reviewRequest.getScore());

        // Save the updated Review entity
        Review updatedReview = reviewService.saveReview(existingReview);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/by-game-and-user")
    public ResponseEntity<Review> getReviewByGameAndUser(
            @RequestParam Long gameId,
            @RequestParam Long userId) {
        Optional<Review> review = reviewRepository.findByGameIdAndUserId(gameId, userId);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/latest-reviews")
    public ResponseEntity<List<ReviewWithUsername>> getLatestReviews(
            @RequestParam Long gameId,
            @RequestParam(defaultValue = "4") int limit) {
        List<ReviewWithUsername> latestReviews = reviewService.findLatestReviewsByGameId(gameId, limit);
        return ResponseEntity.ok(latestReviews);
    }
}