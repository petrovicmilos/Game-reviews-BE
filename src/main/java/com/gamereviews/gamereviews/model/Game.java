package com.gamereviews.gamereviews.model;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "release_date", nullable = false)
    private String releaseDate;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "average_critic_score", nullable = false)
    private double averageCriticScore;

    @Column(name = "average_audience_score", nullable = false)
    private double averageAudienceScore;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getAverageCriticScore() {
        return averageCriticScore;
    }

    public void setAverageCriticScore(double averageCriticScore) {
        this.averageCriticScore = averageCriticScore;
    }

    public double getAverageAudienceScore() {
        return averageAudienceScore;
    }

    public void setAverageAudienceScore(double averageAudienceScore) {
        this.averageAudienceScore = averageAudienceScore;
    }
}