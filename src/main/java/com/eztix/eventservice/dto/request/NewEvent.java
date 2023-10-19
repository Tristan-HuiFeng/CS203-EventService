package com.eztix.eventservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class NewEvent {
    @NotNull
    private String name;

    @NotNull
    private String category;

    @NotNull
    private String artist;

    @NotNull
    private String description;

    @NotNull
    private String bannerURL;

    @NotNull
    private String seatMapURL;

    @NotNull
    private String location;

    @NotNull
    private Boolean isFeatured;

    private Integer featureSequence;

    private OffsetDateTime start_datetime;

    private OffsetDateTime end_datetime;

    @NotNull
    private List<NewActivity> activities;

    @NotNull
    private List<NewAdmissionPolicy> admissionPolicies;

}

