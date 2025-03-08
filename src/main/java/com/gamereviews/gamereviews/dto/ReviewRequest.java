package com.gamereviews.gamereviews.dto;  // Paket za DTO-ove

public class ReviewRequest {
    private Long gameId;  // ID igre koja se recenzira
    private Long userId;  // ID korisnika koji piše recenziju
    private String content;  // Tekst recenzije
    private int score;  // Ocena (npr. od 0 do 100)

    // Getters i Setters
    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}