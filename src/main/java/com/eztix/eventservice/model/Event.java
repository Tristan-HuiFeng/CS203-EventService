package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name="Event")
@Table(name= "EVENT")
public class Event {

    @Id
    @SequenceGenerator(name = "event_sequence", sequenceName = "event_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "artist")
    private String artist;

    @NotNull
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "banner_url", columnDefinition = "TEXT")
    private String bannerURL;

    @NotNull
    @Column(name = "seat_map_url", columnDefinition = "TEXT")
    private String seatMapURL;

    @NotNull
    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "feature_sequence")
    private int featureSequence;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerURL() {
        return bannerURL;
    }

    public void setBannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    public String getSeatMapURL() {
        return seatMapURL;
    }

    public void setSeatMapURL(String seatmapURL) {
        this.seatMapURL = seatmapURL;
    }

    public Boolean getFeatured() {
        return isFeatured;
    }

    public void setFeatured(Boolean featured) {
        isFeatured = featured;
    }

    public int getFeatureSequence() {
        return featureSequence;
    }

    public void setFeatureSequence(int featureSequence) {
        this.featureSequence = featureSequence;
    }

}
