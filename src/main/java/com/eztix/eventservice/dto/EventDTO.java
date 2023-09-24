package com.eztix.eventservice.dto;


public interface EventDTO {

    Long getId();

    String getName();
    String getCategory();
    String getArtist();

    String getDescription();

    String getBannerURL();

    String getSetMapURL();

    Boolean getIsFeatured();

    Integer getFeatureSequence();

}
