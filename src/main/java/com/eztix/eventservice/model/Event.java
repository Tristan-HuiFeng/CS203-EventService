package com.eztix.eventservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.mapping.Set;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="event")
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
    private Integer featureSequence;

    @JsonManagedReference
    @OneToMany(mappedBy="event",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Activity> activities;



}
