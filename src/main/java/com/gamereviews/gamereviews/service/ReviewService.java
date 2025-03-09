package com.gamereviews.gamereviews.service;

import com.gamereviews.gamereviews.dto.ReviewWithUsername;
import com.gamereviews.gamereviews.model.Game;
import com.gamereviews.gamereviews.model.Review;
import com.gamereviews.gamereviews.model.User;
import com.gamereviews.gamereviews.repository.ReviewRepository;
import com.gamereviews.gamereviews.repository.GameRepository;
import com.gamereviews.gamereviews.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review saveReview(Review review) {
        // Sačuvaj review
        Review savedReview = reviewRepository.save(review);

        // Ažuriraj prosek za igru
        updateGameAverageScores(review.getGame().getId(), review.getUser().getId(), review.getScore());

        return savedReview;
    }

    private void updateGameAverageScores(Long gameId, Long userId, int newScore) {
        // Pronađi igru
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found with id " + gameId));

        // Pronađi korisnika
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + userId));

        // Proveri da li je korisnik kritičar ili običan korisnik
        if (user.isCritic()) {
            // Ako je kritičar, ažuriraj average_critic_score
            int currentCriticScore = game.getAverageCriticScore();
            int criticReviewCount = reviewRepository.countByGameIdAndUserIsCritic(gameId, true);
            int newCriticScore = calculateNewAverage(currentCriticScore, newScore, criticReviewCount);
            game.setAverageCriticScore(newCriticScore);  // Postavi zaokruženu vrednost
        } else {
            // Ako je običan korisnik, ažuriraj average_audience_score
            int currentAudienceScore = game.getAverageAudienceScore();
            int audienceReviewCount = reviewRepository.countByGameIdAndUserIsCritic(gameId, false);
            int newAudienceScore = calculateNewAverage(currentAudienceScore, newScore, audienceReviewCount);
            game.setAverageAudienceScore(newAudienceScore);  // Postavi zaokruženu vrednost
        }

        // Sačuvaj ažuriranu igru
        gameRepository.save(game);
    }

    private int calculateNewAverage(double currentAverage, int newScore, int reviewCount) {
        // Formula za izračunavanje novog proseka:
        // Novi prosek = (Trenutni prosek * Broj recenzija + Novi score) / (Broj recenzija + 1)
        double newAverage = (currentAverage * reviewCount + newScore) / (reviewCount + 1);
        return (int) Math.round(newAverage);  // Eksplicitno kastovanje u int
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
    public int countByGameIdAndUserIsCritic(Long gameId, boolean isCritic) {
        return reviewRepository.countByGameIdAndUserIsCritic(gameId, isCritic);
    }

    public List<ReviewWithUsername> findLatestReviewsByGameId(Long gameId, int limit) {
        List<Object[]> results = reviewRepository.findTop4ByGameIdOrderByIdDesc(gameId);
        return results.stream()
                .map(result -> new ReviewWithUsername((Review) result[0], (String) result[1]))
                .collect(Collectors.toList());
    }

    public List<Review> getReviewsByGameId(Long gameId) {
        return reviewRepository.findByGameId(gameId);
    }
}