package com.eztix.eventservice.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Activity {

    @Id
    @SequenceGenerator(name = "activity_sequence", sequenceName = "activity_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_sequence")
    @Schema(hidden = true)
    private long activityId;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @NotNull
    @Column(name = "Activity_Name")
    private String activityName;

    @NotNull
    @Column(name = "Start_Date_and_Time")
    private LocalDateTime startDateTime;

    @NotNull
    @Column(name = "End_Date_Time")
    private LocalDateTime endDateTime;

    @NotNull
    @Column(name = "Location")
    private String location;
}
