package com.gamereviews.gamereviews.dto;

import com.gamereviews.gamereviews.model.Review;

public class ReviewWithUsername {
    private Review review;
    private String username;

    public ReviewWithUsername(Review review, String username) {
        this.review = review;
        this.username = username;
    }

    // Getters
    public Review getReview() {
        return review;
    }

    public String getUsername() {
        return username;
    }
}
