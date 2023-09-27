package com.eztix.eventservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.mapping.Set;

import java.time.OffsetDateTime;
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
    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    @NotNull
    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "feature_sequence")
    private Integer featureSequence;

    @Formula("(SELECT a.start_datetime FROM activity as a WHERE a.event_id = id ORDER BY a.start_datetime LIMIT 1)")
    private OffsetDateTime start_datetime;

    @Formula("(SELECT a.end_datetime FROM activity as a WHERE a.event_id = id ORDER BY a.end_datetime DESC LIMIT 1)")
    private OffsetDateTime end_datetime;

    @JsonManagedReference
    @OneToMany(mappedBy="event",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Activity> activities;

    @JsonManagedReference
    @OneToMany(mappedBy="event",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<AdmissionPolicy> admissionPolicies;

}
