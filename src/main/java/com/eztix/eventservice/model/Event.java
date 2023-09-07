package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="Event")
@Table(name= "EVENT")
public class Event {

    @Id
    @SequenceGenerator(name = "event_sequence", sequenceName = "event_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_sequence")
    @Schema(hidden = true)
    private Long id;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();

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
    private Integer featureSequence;


}
